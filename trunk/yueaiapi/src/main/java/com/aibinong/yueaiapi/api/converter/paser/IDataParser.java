package com.aibinong.yueaiapi.api.converter.paser;

import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by yourfriendyang on 16/1/31.
 */
public interface IDataParser {
    <R> R parse(Gson gson, String jsonStr, String dataKey, Type dataType, IDataChecker mDataChecker, IResultHandler resultHandler, boolean autoShowLogin);
}
