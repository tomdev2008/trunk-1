/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aibinong.yueaiapi.api.cache;


import android.util.Log;

import com.aibinong.yueaiapi.api.interceptor.CacheInterceptor;

import okhttp3.Request;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Given a request and cached response, this figures out whether to use the network, the cache, or
 * both.
 * <p/>
 * <p>Selecting a cache strategy may add conditions to the request (like the "If-Modified-Since"
 * header for conditional GETs) or warnings to the cached response (if the cached data is
 * potentially stale).
 */
public final class AbnCacheStrategy {
    /**
     * The request to send on the network, or null if this call doesn't use the network.
     */
    public final Request networkRequest;

    /**
     * The cached response to return or validate; or null if this call doesn't use a cache.
     */
    public final Response cacheResponse;

    private AbnCacheStrategy(Request networkRequest, Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }

    /**
     * Returns true if {@code response} can be stored to later serve another request.
     */
    public static boolean isCacheable(Response response, Request request) {
        // Always go to network for uncacheable response codes (RFC 7231 section 6.1),
        // This implementation doesn't support caching partial content.
        switch (response.code()) {
            case HTTP_OK:
                break;
            default:
                // All other codes cannot be cached.
                return false;
        }

        int exprieTime = CacheInterceptor.parseExpireTime(response.body().source());
        if (exprieTime <= 0) {
            return false;
        }
        //login doesn't support cache
        if (request.url().toString().contains("login_by_mobile")) {
            Log.i("-->isCacheable ", "login_by_mobile not support cache ");
            return false;
        }
        return true;
    }

    public static class Factory {
        final long nowMillis;
        final Request request;
        final Response cacheResponse;
        private int expireTime;

        /**
         * Extension header set by OkHttp specifying the timestamp when the cached HTTP request was
         * first initiated.
         */
        private long sentRequestMillis;

        /**
         * Extension header set by OkHttp specifying the timestamp when the cached HTTP response was
         * first received.
         */
        private long receivedResponseMillis;


        public Factory(long nowMillis, Request request, Response cacheResponse) {
            this.nowMillis = nowMillis;
            this.request = request;
            this.cacheResponse = cacheResponse;

            if (cacheResponse != null) {
                this.sentRequestMillis = cacheResponse.sentRequestAtMillis();
                this.receivedResponseMillis = cacheResponse.receivedResponseAtMillis();
                expireTime = CacheInterceptor.parseExpireTime(cacheResponse.body().source());
            }
        }

        /**
         * Returns a strategy to use assuming the request can use the network.
         */
        public AbnCacheStrategy getCandidate() {
            if (cacheResponse == null) {
                return new AbnCacheStrategy(request, null);
            }
            // If this response shouldn't have been stored, it should never be used
            // as a response source. This check should be redundant as long as the
            // persistence store is well-behaved and the rules are constant.
            if (!isCacheable(cacheResponse, request)) {
                return new AbnCacheStrategy(request, null);
            }
            if ((expireTime * 1000 + receivedResponseMillis) < System.currentTimeMillis()) {
                return new AbnCacheStrategy(request, null);
            }
            Log.i("cache expireTime left ", (expireTime * 1000 + receivedResponseMillis - System.currentTimeMillis()) + " ms");
            return new AbnCacheStrategy(null, cacheResponse);
        }
    }
}
