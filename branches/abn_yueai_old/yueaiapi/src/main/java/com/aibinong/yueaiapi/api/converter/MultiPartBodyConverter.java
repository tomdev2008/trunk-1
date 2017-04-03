package com.aibinong.yueaiapi.api.converter;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by yourfriendyang on 16/7/14.
 */
public class MultiPartBodyConverter<T> implements Converter<T, RequestBody> {
    private String mFormName;

    public MultiPartBodyConverter(String mFormName) {
        this.mFormName = mFormName;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        if (value.getClass().equals(File.class)) {
            File file = (File) value;
            return RequestBody.create(MultipartBody.FORM, file);
        } else {
            return RequestBody.create(MultipartBody.FORM, value.toString());
        }
    }
}
