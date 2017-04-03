package com.aibinong.yueaiapi.api.interceptor;

import com.fatalsignal.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public class LoggerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //print request
        StringBuilder sb = new StringBuilder(chain.request().url().toString());
        if (chain.request().body() instanceof FormBody) {
            FormBody formBody = (FormBody) chain.request().body();
            //把原来参数加上
            for (int i = 0; i < formBody.size(); i++) {
                if (i > 0) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append(formBody.name(i)).append("=").append(URLEncoder.encode(formBody.value(i), "UTF-8"));
            }
        }
        long ts_start = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());

        String responseStr = "";
        //print response
        BufferedSource bfs = response.body().source();
        bfs.request(Long.MAX_VALUE);
        Buffer buffer = bfs.buffer().clone();
        if (buffer.size() > 0.5 * 1024 * 1024) {
            responseStr = " response body is too long ";
        } else {
            responseStr = buffer.readString(Charset.defaultCharset());
        }
        buffer.close();
        String logStr ="\nduration=" + (System.currentTimeMillis() - ts_start) + "ms,thread=" + Thread.currentThread().getId()+ "\nrequest:\n" + sb.toString() + "\nresponse:\n" + responseStr;
        Log.i(getClass().getSimpleName(), logStr);
        return response;
    }
}
