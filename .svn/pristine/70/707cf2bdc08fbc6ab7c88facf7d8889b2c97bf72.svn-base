package com.aibinong.yueaiapi.api.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public class SimpleStringConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        String ret = value.string();
        value.close();
        return ret;
    }
}
