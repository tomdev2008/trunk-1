package com.aibinong.yueaiapi.api.interceptor;

import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.api.cache.AbnCacheStrategy;
import com.fatalsignal.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.CipherSuite;
import okhttp3.FormBody;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpStream;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static okhttp3.internal.Util.closeQuietly;
import static okhttp3.internal.Util.discard;

/**
 * Created by yourfriendyang on 16/7/8.
 */
public class CacheInterceptor implements Interceptor {
    public static final String EXPIRE_TIME_KEY = "expireTime";
    private int APPVERSION = 1;//如果改变,会清除所有缓存,这里考虑下是不是需要改变
    private static final int ENTRY_METADATA = 0;
    private static final int ENTRY_BODY = 1;
    private int ENTRY_COUNT = 2;//每个key可以存多少value
    private long maxSize;
    private File fileDir;
    private DiskLruCache mDiskLruCache;
    private int writeSuccessCount;
    private int writeAbortCount;
    private static CacheInterceptor mInstance;
    private static Gson gson = new GsonBuilder().create();

    public static CacheInterceptor getInstance() {
        if (mInstance == null) {
            mInstance = new CacheInterceptor();
        }
        return mInstance;
    }

    public void init(long maxSize, File cacheDir) {
        this.maxSize = maxSize;
        this.fileDir = cacheDir;
        mDiskLruCache = DiskLruCache.create(FileSystem.SYSTEM, fileDir, APPVERSION, ENTRY_COUNT, maxSize);
    }

    private CacheInterceptor() {
    }

    public static int parseExpireTime(BufferedSource bfs) {
        try {
            bfs.request(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = bfs.buffer().clone();
        int expireTime = 0;
        JsonReader reader = gson.newJsonReader(new InputStreamReader(buffer.inputStream()));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                if (EXPIRE_TIME_KEY.equals(key)) {
                    expireTime = reader.nextInt();
                    break;
                } else {
                    reader.skipValue();
                }

            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

            if (buffer != null) {
                buffer.close();
            }
        }
        return expireTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response cacheCandidate = get(chain.request());
        long now = System.currentTimeMillis();
        AbnCacheStrategy strategy = null;
        try {
            strategy = new AbnCacheStrategy.Factory(now, chain.request(), cacheCandidate).getCandidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request networkRequest = strategy.networkRequest;
        Response cacheResponse = strategy.cacheResponse;
        if (cacheCandidate != null && cacheResponse == null) {
            closeQuietly(cacheCandidate.body()); // The cache candidate wasn't applicable. Close it.
            //delete cache
            remove(chain.request());
        }
        // If we don't need the network, we're done.
        if (networkRequest == null) {
            Log.i("---> intercept", "use cache");
            return cacheResponse.newBuilder()
                    .cacheResponse(stripBody(cacheResponse))
                    .build();
        }
        Response networkResponse;
        try {
            networkResponse = chain.proceed(networkRequest);
        } finally {
            // If we're crashing on I/O or otherwise, don't leak the cache body.
            if (cacheCandidate != null) {
                closeQuietly(cacheCandidate.body());
            }
        }
        Response response = networkResponse.newBuilder()
                .cacheResponse(stripBody(cacheResponse))
                .networkResponse(stripNetworkResponse(stripBody(networkResponse)))
                .build();

        if (HttpHeaders.hasBody(response)) {
            CacheRequest cacheRequest = maybeCache(response, networkResponse.request());
            response = cacheWritingResponse(cacheRequest, response);
        }
        return response;
    }

    private CacheRequest maybeCache(Response userResponse, Request networkRequest) throws IOException {
        // Should we cache this response for this request?
        if (!AbnCacheStrategy.isCacheable(userResponse, networkRequest)) {
//            if (HttpMethod.invalidatesCache(networkRequest.method())) {
            try {
                remove(networkRequest);
            } catch (IOException ignored) {
                // The cache cannot be written.
            }
//            }
            return null;
        }

        // Offer this request to the cache.
        return put(userResponse);
    }

    private static Response stripBody(Response response) {
        return response != null && response.body() != null
                ? response.newBuilder().body(null).build()
                : response;
    }

    private static Response stripNetworkResponse(Response response) {
        return response != null && response.networkResponse() != null
                ? response.newBuilder().networkResponse(null).priorResponse(null).build()
                : response;
    }

    /**
     * Returns a new source that writes bytes to {@code cacheRequest} as they are read by the source
     * consumer. This is careful to discard bytes left over when the stream is closed; otherwise we
     * may never exhaust the source stream and therefore not complete the cached response.
     */
    private Response cacheWritingResponse(final CacheRequest cacheRequest, Response response)
            throws IOException {
        // Some apps return a null body; for compatibility we treat that like a null cache request.
        if (cacheRequest == null) return response;
        Sink cacheBodyUnbuffered = cacheRequest.body();
        if (cacheBodyUnbuffered == null) return response;

        final BufferedSource source = response.body().source();
        final BufferedSink cacheBody = Okio.buffer(cacheBodyUnbuffered);

        Source cacheWritingSource = new Source() {
            boolean cacheRequestClosed;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead;
                try {
                    bytesRead = source.read(sink, byteCount);
                } catch (IOException e) {
                    if (!cacheRequestClosed) {
                        cacheRequestClosed = true;
                        cacheRequest.abort(); // Failed to write a complete cache response.
                    }
                    throw e;
                }

                if (bytesRead == -1) {
                    if (!cacheRequestClosed) {
                        cacheRequestClosed = true;
                        cacheBody.close(); // The cache response is complete!
                    }
                    return -1;
                }

                sink.copyTo(cacheBody.buffer(), sink.size() - bytesRead, bytesRead);
                cacheBody.emitCompleteSegments();
                return bytesRead;
            }

            @Override
            public Timeout timeout() {
                return source.timeout();
            }

            @Override
            public void close() throws IOException {
                if (!cacheRequestClosed
                        && !discard(this, HttpStream.DISCARD_STREAM_TIMEOUT_MILLIS, MILLISECONDS)) {
                    cacheRequestClosed = true;
                    cacheRequest.abort();
                }
                source.close();
            }
        };

        return response.newBuilder()
                .body(new RealResponseBody(response.headers(), Okio.buffer(cacheWritingSource)))
                .build();
    }

    private static String urlToKey(Request request) {
        //把requestbody去除通用参数以后,也作为key的一部分
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();

            Map<String, String> paramsMap = new HashMap<>(formBody.size());
            //把原来参数加上
            for (int i = 0; i < formBody.size(); i++) {
                paramsMap.put(formBody.name(i), formBody.value(i));
            }
            //删除通用参数
            ParamsHelper.getInstance().removeCommonParams(paramsMap);
            return Util.md5Hex(request.url().toString() + paramsMap.toString());
        }

        return Util.md5Hex(request.url().toString());
    }

    Response get(Request request) {
        String key = urlToKey(request);
        DiskLruCache.Snapshot snapshot;
        Entry entry;
        try {
            snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            // Give up because the cache cannot be read.
            return null;
        }

        try {
            entry = new Entry(snapshot.getSource(ENTRY_METADATA));
        } catch (IOException e) {
            closeQuietly(snapshot);
            return null;
        }

        Response response = entry.response(snapshot);

        if (!entry.matches(request, response)) {
            closeQuietly(response.body());
            return null;
        }

        return response;
    }

    private CacheRequest put(Response response) throws IOException {
        String requestMethod = response.request().method();
        if (response.code() != 200) {
            return null;
        }

//        if (HttpMethod.invalidatesCache(response.request().method())) {
//            try {
//                remove(response.request());
//            } catch (IOException ignored) {
//                // The cache cannot be written.
//            }
//            return null;
//        }
        if (false && !requestMethod.equals("GET")) {
            // Don't cache non-GET responses. We're technically allowed to cache
            // HEAD requests and some POST requests, but the complexity of doing
            // so is high and the benefit is low.
            return null;
        }

        if (HttpHeaders.hasVaryAll(response)) {
            return null;
        }

        Entry entry = new Entry(response);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(urlToKey(response.request()));
            if (editor == null) {
                return null;
            }
            entry.writeTo(editor);
            return new CacheRequestImpl(editor);
        } catch (IOException e) {
            abortQuietly(editor);
            return null;
        }
    }

    private void remove(Request request) throws IOException {
        mDiskLruCache.remove(urlToKey(request));
    }

    private void update(Response cached, Response network) {
        Entry entry = new Entry(network);
        DiskLruCache.Snapshot snapshot = ((CacheResponseBody) cached.body()).snapshot;
        DiskLruCache.Editor editor = null;
        try {
            editor = snapshot.edit(); // Returns null if snapshot is not current.
            if (editor != null) {
                entry.writeTo(editor);
                editor.commit();
            }
        } catch (IOException e) {
            abortQuietly(editor);
        }
    }

    private void abortQuietly(DiskLruCache.Editor editor) {
        // Give up because the cache cannot be written.
        try {
            if (editor != null) {
                editor.abort();
            }
        } catch (IOException ignored) {
        }
    }

    private static int readInt(BufferedSource source) throws IOException {
        try {
            long result = source.readDecimalLong();
            String line = source.readUtf8LineStrict();
            if (result < 0 || result > Integer.MAX_VALUE || !line.isEmpty()) {
                throw new IOException("expected an int but was \"" + result + line + "\"");
            }
            return (int) result;
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }


    private static final class Entry {
        /**
         * Synthetic response header: the local time when the request was sent.
         */
        private static final String SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";

        /**
         * Synthetic response header: the local time when the response was received.
         */
        private static final String RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";

        private final String url;
        private final Headers varyHeaders;
        private final String requestMethod;
        private final Protocol protocol;
        private final int code;
        private final String message;
        private final Headers responseHeaders;
        private final Handshake handshake;
        private final long sentRequestMillis;
        private final long receivedResponseMillis;
        private final MediaType requestType;
        private final RequestBody requestBody;
//        private final int expireTime;

        /**
         * Reads an entry from an input stream. A typical entry looks like this:
         * <pre>{@code
         *   http://google.com/foo
         *   GET
         *   2
         *   Accept-Language: fr-CA
         *   Accept-Charset: UTF-8
         *   HTTP/1.1 200 OK
         *   3
         *   Content-Type: image/png
         *   Content-Length: 100
         *   Cache-Control: max-age=600
         * }</pre>
         * <p/>
         * <p>A typical HTTPS file looks like this:
         * <pre>{@code
         *   https://google.com/foo
         *   GET
         *   2
         *   Accept-Language: fr-CA
         *   Accept-Charset: UTF-8
         *   HTTP/1.1 200 OK
         *   3
         *   Content-Type: image/png
         *   Content-Length: 100
         *   Cache-Control: max-age=600
         *
         *   AES_256_WITH_MD5
         *   2
         *   base64-encoded peerCertificate[0]
         *   base64-encoded peerCertificate[1]
         *   -1
         *   TLSv1.2
         * }</pre>
         * The file is newline separated. The first two lines are the URL and the request method. Next
         * is the number of HTTP Vary request header lines, followed by those lines.
         * <p/>
         * <p>Next is the response status line, followed by the number of HTTP response header lines,
         * followed by those lines.
         * <p/>
         * <p>HTTPS responses also contain SSL session information. This begins with a blank line, and
         * then a line containing the cipher suite. Next is the length of the peer certificate chain.
         * These certificates are base64-encoded and appear each on their own line. The next line
         * contains the length of the local certificate chain. These certificates are also
         * base64-encoded and appear each on their own line. A length of -1 is used to encode a null
         * array. The last line is optional. If present, it contains the TLS version.
         */
        public Entry(Source in) throws IOException {
            try {
                BufferedSource source = Okio.buffer(in);
                url = source.readUtf8LineStrict();
                requestMethod = source.readUtf8LineStrict();
                HeaderBuilder varyHeadersBuilder = new HeaderBuilder();
                int varyRequestHeaderLineCount = readInt(source);
                for (int i = 0; i < varyRequestHeaderLineCount; i++) {
                    varyHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                varyHeaders = varyHeadersBuilder.build();

                StatusLine statusLine = StatusLine.parse(source.readUtf8LineStrict());
                protocol = statusLine.protocol;
                code = statusLine.code;
                message = statusLine.message;
                HeaderBuilder responseHeadersBuilder = new HeaderBuilder();
                int responseHeaderLineCount = readInt(source);
                for (int i = 0; i < responseHeaderLineCount; i++) {
                    responseHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                String sendRequestMillisString = responseHeadersBuilder.get(SENT_MILLIS);
                String receivedResponseMillisString = responseHeadersBuilder.get(RECEIVED_MILLIS);
                responseHeadersBuilder.removeAll(SENT_MILLIS);
                responseHeadersBuilder.removeAll(RECEIVED_MILLIS);
                sentRequestMillis = sendRequestMillisString != null
                        ? Long.parseLong(sendRequestMillisString)
                        : 0L;
                receivedResponseMillis = receivedResponseMillisString != null
                        ? Long.parseLong(receivedResponseMillisString)
                        : 0L;
                responseHeaders = responseHeadersBuilder.build();

                if (isHttps()) {
                    String blank = source.readUtf8LineStrict();
                    if (blank.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + blank + "\"");
                    }
                    String cipherSuiteString = source.readUtf8LineStrict();
                    CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
                    List<Certificate> peerCertificates = readCertificateList(source);
                    List<Certificate> localCertificates = readCertificateList(source);
                    TlsVersion tlsVersion = !source.exhausted()
                            ? TlsVersion.forJavaName(source.readUtf8LineStrict())
                            : null;
                    handshake = Handshake.get(tlsVersion, cipherSuite, peerCertificates, localCertificates);
                } else {
                    handshake = null;
                }
                //read requestBody
                String mediaTypeStr = source.readUtf8LineStrict();
                if (mediaTypeStr != null) {
                    requestType = MediaType.parse(mediaTypeStr);
                    String requestBodyStr = source.readUtf8LineStrict();
                    if (requestBodyStr != null) {
                        requestBody = RequestBody.create(requestType, requestBodyStr);
                    } else {
                        requestBody = null;
                    }
                } else {
                    requestType = null;
                    requestBody = null;
                }
                /*
                //read expireTime
                expireTime = source.readInt();*/
            } finally {
                in.close();
            }
        }

        public Entry(Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = HttpHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
            if (response.request().body() != null) {
                //清理掉通用参数
                if (response.request().body() instanceof FormBody) {
                    FormBody formBody = (FormBody) response.request().body();

                    Map<String, String> paramsMap = new HashMap<>(formBody.size());
                    //把原来参数加上
                    for (int i = 0; i < formBody.size(); i++) {
                        paramsMap.put(formBody.name(i), formBody.value(i));
                    }
                    //删除通用参数
                    ParamsHelper.getInstance().removeCommonParams(paramsMap);

                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    Iterator<Map.Entry<String, String>> entryIterator = paramsMap.entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Map.Entry<String, String> entry = entryIterator.next();
                        formBodyBuilder.add(entry.getKey(), entry.getValue());
                    }
                    requestBody = formBodyBuilder.build();
                } else {
                    requestBody = response.request().body();
                }

                this.requestType = requestBody.contentType();
            } else {
                this.requestBody = null;
                this.requestType = null;
            }
            /*
            expireTime = parseExpireTime(response.body().source());*/
            responseHeaders = response.headers();
//            responseHeaders = oriHeaders.newBuilder().add(EXPIRE_TIME_KEY, expireTime + "").build();
        }

        public void writeTo(DiskLruCache.Editor editor) throws IOException {
            BufferedSink sink = Okio.buffer(editor.newSink(ENTRY_METADATA));

            sink.writeUtf8(url)
                    .writeByte('\n');
            sink.writeUtf8(requestMethod)
                    .writeByte('\n');
            sink.writeDecimalLong(varyHeaders.size())
                    .writeByte('\n');
            for (int i = 0, size = varyHeaders.size(); i < size; i++) {
                sink.writeUtf8(varyHeaders.name(i))
                        .writeUtf8(": ")
                        .writeUtf8(varyHeaders.value(i))
                        .writeByte('\n');
            }

            sink.writeUtf8(new StatusLine(protocol, code, message).toString())
                    .writeByte('\n');
            sink.writeDecimalLong(responseHeaders.size() + 2)
                    .writeByte('\n');
            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                sink.writeUtf8(responseHeaders.name(i))
                        .writeUtf8(": ")
                        .writeUtf8(responseHeaders.value(i))
                        .writeByte('\n');
            }
            sink.writeUtf8(SENT_MILLIS)
                    .writeUtf8(": ")
                    .writeDecimalLong(sentRequestMillis)
                    .writeByte('\n');
            sink.writeUtf8(RECEIVED_MILLIS)
                    .writeUtf8(": ")
                    .writeDecimalLong(receivedResponseMillis)
                    .writeByte('\n');

            if (isHttps()) {
                sink.writeByte('\n');
                sink.writeUtf8(handshake.cipherSuite().javaName())
                        .writeByte('\n');
                writeCertList(sink, handshake.peerCertificates());
                writeCertList(sink, handshake.localCertificates());
                // The handshake’s TLS version is null on HttpsURLConnection and on older cached responses.
                if (handshake.tlsVersion() != null) {
                    sink.writeUtf8(handshake.tlsVersion().javaName())
                            .writeByte('\n');
                }
            }
            //write requestBody
            if (requestType != null) {
                sink.writeUtf8(requestType.toString()).writeByte('\n');
                if (requestBody != null) {
                    requestBody.writeTo(sink);
                    sink.writeByte('\n');
                }
            }
            /*
            //write expireTime
            sink.writeDecimalLong(expireTime).writeByte('\n');*/
            sink.close();
        }

        private boolean isHttps() {
            return url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource source) throws IOException {
            int length = readInt(source);
            if (length == -1)
                return Collections.emptyList(); // OkHttp v1.2 used -1 to indicate null.

            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                List<Certificate> result = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    String line = source.readUtf8LineStrict();
                    Buffer bytes = new Buffer();
                    bytes.write(ByteString.decodeBase64(line));
                    result.add(certificateFactory.generateCertificate(bytes.inputStream()));
                }
                return result;
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        }

        private void writeCertList(BufferedSink sink, List<Certificate> certificates)
                throws IOException {
            try {
                sink.writeDecimalLong(certificates.size())
                        .writeByte('\n');
                for (int i = 0, size = certificates.size(); i < size; i++) {
                    byte[] bytes = certificates.get(i).getEncoded();
                    String line = ByteString.of(bytes).base64();
                    sink.writeUtf8(line)
                            .writeByte('\n');
                }
            } catch (CertificateEncodingException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean matches(Request request, Response response) {
            return url.equals(request.url().toString())
                    && requestMethod.equals(request.method())
                    && HttpHeaders.varyMatches(response, varyHeaders, request);
        }

        public Response response(DiskLruCache.Snapshot snapshot) {
            String contentType = responseHeaders.get("Content-Type");
            String contentLength = responseHeaders.get("Content-Length");
            Request cacheRequest = new Request.Builder()
                    .url(url)
                    .method(requestMethod, requestBody)
                    .headers(varyHeaders)
                    .build();
            return new Response.Builder()
                    .request(cacheRequest)
                    .protocol(protocol)
                    .code(code)
                    .message(message)
                    .headers(responseHeaders)
                    .body(new CacheResponseBody(snapshot, contentType, contentLength))
                    .handshake(handshake)
                    .sentRequestAtMillis(sentRequestMillis)
                    .receivedResponseAtMillis(receivedResponseMillis)
                    .build();
        }
    }

    private static class CacheResponseBody extends ResponseBody {
        private final DiskLruCache.Snapshot snapshot;
        private final BufferedSource bodySource;
        private final String contentType;
        private final String contentLength;

        public CacheResponseBody(final DiskLruCache.Snapshot snapshot,
                                 String contentType, String contentLength) {
            this.snapshot = snapshot;
            this.contentType = contentType;
            this.contentLength = contentLength;

            Source source = snapshot.getSource(ENTRY_BODY);
            bodySource = Okio.buffer(new ForwardingSource(source) {
                @Override
                public void close() throws IOException {
                    snapshot.close();
                    super.close();
                }
            });
        }

        @Override
        public MediaType contentType() {
            return contentType != null ? MediaType.parse(contentType) : null;
        }

        @Override
        public long contentLength() {
            try {
                return contentLength != null ? Long.parseLong(contentLength) : -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override
        public BufferedSource source() {
            return bodySource;
        }
    }


    public static final class HeaderBuilder {
        private final List<String> namesAndValues = new ArrayList<>(20);


        HeaderBuilder addLenient(String line) {
            int index = line.indexOf(":", 1);
            if (index != -1) {
                return addLenient(line.substring(0, index), line.substring(index + 1));
            } else if (line.startsWith(":")) {
                // Work around empty header names and header names that start with a
                // colon (created by old broken SPDY versions of the response cache).
                return addLenient("", line.substring(1)); // Empty header name.
            } else {
                return addLenient("", line); // No header name.
            }
        }

        public HeaderBuilder add(String line) {
            int index = line.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + line);
            }
            return add(line.substring(0, index).trim(), line.substring(index + 1));
        }

        public HeaderBuilder add(String name, String value) {
            checkNameAndValue(name, value);
            return addLenient(name, value);
        }

        HeaderBuilder addLenient(String name, String value) {
            namesAndValues.add(name);
            namesAndValues.add(value.trim());
            return this;
        }

        public HeaderBuilder removeAll(String name) {
            for (int i = 0; i < namesAndValues.size(); i += 2) {
                if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                    namesAndValues.remove(i); // name
                    namesAndValues.remove(i); // value
                    i -= 2;
                }
            }
            return this;
        }

        public HeaderBuilder set(String name, String value) {
            checkNameAndValue(name, value);
            removeAll(name);
            addLenient(name, value);
            return this;
        }

        private void checkNameAndValue(String name, String value) {
            if (name == null) throw new NullPointerException("name == null");
            if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
            for (int i = 0, length = name.length(); i < length; i++) {
                char c = name.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    throw new IllegalArgumentException(Util.format(
                            "Unexpected char %#04x at %d in header name: %s", (int) c, i, name));
                }
            }
            if (value == null) throw new NullPointerException("value == null");
            for (int i = 0, length = value.length(); i < length; i++) {
                char c = value.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    throw new IllegalArgumentException(Util.format(
                            "Unexpected char %#04x at %d in %s value: %s", (int) c, i, name, value));
                }
            }
        }

        public String get(String name) {
            for (int i = namesAndValues.size() - 2; i >= 0; i -= 2) {
                if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                    return namesAndValues.get(i + 1);
                }
            }
            return null;
        }

        public Headers build() {
            Headers.Builder builder = new Headers.Builder();
            if (namesAndValues != null) {
                for (int i = 0; i < namesAndValues.size(); i += 2) {
                    builder.add(namesAndValues.get(i), namesAndValues.get(i + 1));
                }
            }
            return builder.build();
        }
    }

    private final class CacheRequestImpl implements CacheRequest {
        private final DiskLruCache.Editor editor;
        private Sink cacheOut;
        private boolean done;
        private Sink body;

        public CacheRequestImpl(final DiskLruCache.Editor editor) throws IOException {
            this.editor = editor;
            this.cacheOut = editor.newSink(ENTRY_BODY);
            this.body = new ForwardingSink(cacheOut) {
                @Override
                public void close() throws IOException {
                    synchronized (CacheInterceptor.this) {
                        if (done) {
                            return;
                        }
                        done = true;
                        writeSuccessCount++;
                    }
                    super.close();
                    editor.commit();
                }
            };
        }

        @Override
        public void abort() {
            synchronized (CacheInterceptor.this) {
                if (done) {
                    return;
                }
                done = true;
                writeAbortCount++;
            }
            closeQuietly(cacheOut);
            try {
                editor.abort();
            } catch (IOException ignored) {
            }
        }

        @Override
        public Sink body() {
            return body;
        }
    }
}
