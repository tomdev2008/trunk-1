package com.aibinong.yueaiapi.api.interceptor;


import android.text.TextUtils;

import com.aibinong.yueaiapi.api.ParamsHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yourfriendyang on 16/6/24.
 */
public class AddParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //第一步，统一添加通用参数
        Request request = chain.request();
        RequestBody body = request.body();
        if (body instanceof FormBody || body instanceof MultipartBody) {
            if (body instanceof FormBody) {
                FormBody formBody = (FormBody) body;
                //获取通用参数
                Map<String, String> globalParams = ParamsHelper.getInstance().getGlobalParamsMap();
                //把原来参数加上
                for (int i = 0; i < formBody.size(); i++) {
                    String name = formBody.name(i);
                    String value = formBody.value(i);
                    if (!TextUtils.isEmpty(name) && value!=null) {
                        globalParams.put(formBody.name(i), formBody.value(i));
                    }
                }
                if (globalParams.containsKey(ParamsHelper.PARAMS_STUB)) {
                    globalParams.remove(ParamsHelper.PARAMS_STUB);
                }
                EncryptUtil.encrypt(globalParams);

//                StringBuilder nativeSignSb = new StringBuilder();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                Iterator<Map.Entry<String, String>> entryIterator = globalParams.entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, String> entry = entryIterator.next();
                    if (entry.getValue() != null && entry.getKey() != null) {
                        formBodyBuilder.add(entry.getKey(), entry.getValue());
//                        nativeSignSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                }
//                if (!javaSign.equals(globalParams.get("asign"))) {
//                    Log.e(globalParams.hashCode()+"javaSign = " + javaSign + ", javaSignOriStr=" + javaSignOriStr);
//                    Log.e(globalParams.hashCode()+"nativeSign = " + globalParams.get("asign") + ",nativeSignOriStr=" + nativeSignSb.toString());
//                }
                return chain.proceed(request.newBuilder().post(formBodyBuilder.build()).build());
            } else if (body instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) body;

                MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder();
                formBodyBuilder.setType(MultipartBody.FORM);

                List<MultipartBody.Part> parts = multipartBody.parts();
                if (parts != null) {
                    for (MultipartBody.Part part : parts
                            ) {
                        formBodyBuilder.addPart(part);
                    }
                }
                //获取通用参数
                Map<String, String> globalParams = ParamsHelper.getInstance().getGlobalParamsMap();

                if (globalParams.containsKey(ParamsHelper.PARAMS_STUB)) {
                    globalParams.remove(ParamsHelper.PARAMS_STUB);
                }
                EncryptUtil.encrypt(globalParams);

                Iterator<Map.Entry<String, String>> entryIterator = globalParams.entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, String> entry = entryIterator.next();
                    if (entry.getValue() != null && entry.getKey() != null) {
                        formBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                    }
                }
                return chain.proceed(request.newBuilder().post(formBodyBuilder.build()).build());
            }
        }
        return chain.proceed(request);
    }
}
