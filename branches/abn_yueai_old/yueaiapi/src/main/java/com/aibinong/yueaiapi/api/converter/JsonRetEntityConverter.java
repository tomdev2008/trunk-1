package com.aibinong.yueaiapi.api.converter;


import com.aibinong.yueaiapi.annotation.AutoShowLogin;
import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.ResponseHandler;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.converter.paser.IDataParser;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class JsonRetEntityConverter<T> implements Converter<ResponseBody, JsonRetEntity<T>> {
    private final Gson mGson;
    private boolean mAutoShowLogin;
    private Annotation[] mAnnotations;
    private String mDataKey;
    private Type mDataType;
    private IDataChecker mDataChecker;
    private IDataParser mDataPaser;
    private IResultHandler mIResponseHandler;

    JsonRetEntityConverter(Gson gson, Annotation[] annotations, Type dataType, IDataParser paser) {
        mDataType = dataType;
        mDataPaser = paser;
        this.mGson = gson;
        this.mAnnotations = annotations;
        if (mAnnotations != null) {
            for (Annotation annotation : annotations
                    ) {
                if (annotation instanceof DataKey) {
                    DataKey dataKeyAnnotation = (DataKey) annotation;
                    mDataKey = dataKeyAnnotation.value();
                } else if (annotation instanceof DataChecker) {
                    DataChecker checkerAnt = (DataChecker) annotation;
                    Class<? extends IDataChecker> checkerClass = checkerAnt.value();
                    if (checkerClass != null) {
                        try {
                            Method instanceMethod = checkerClass.getMethod("getInstance", new Class[0]);
                            if (instanceMethod != null) {
                                mDataChecker = (IDataChecker) instanceMethod.invoke(checkerClass, new Object[0]);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (annotation instanceof ResponseHandler) {
                    ResponseHandler handlerAnt = (ResponseHandler) annotation;
                    Class<? extends IResultHandler> handlerClass = handlerAnt.value();
                    if (handlerClass != null) {
                        try {
                            Method instanceMethod = handlerClass.getMethod("getInstance", new Class[0]);
                            if (instanceMethod != null) {
                                mIResponseHandler = (IResultHandler) instanceMethod.invoke(handlerClass, new Object[0]);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (annotation instanceof AutoShowLogin) {
                    AutoShowLogin autoShowLogin = (AutoShowLogin) annotation;
                    boolean autoShow = autoShowLogin.value();
                    if (autoShow) {
                        mAutoShowLogin = autoShow;
                    }
                }
            }
        }
        if (mIResponseHandler == null) {
            mIResponseHandler = ApiHelper.getInstance().getCommonResultHandler();
        }
    }

    @Override
    public JsonRetEntity<T> convert(ResponseBody value) throws IOException {
        if (mDataPaser != null) {
            String response = null;
            try {
                response = value.string();
                if (response.length() > 10 * 1024 * 1024) {
                    System.gc();
                    throw new ResponseResult(-1, "数据过大,无法解析");
                }
            } catch (OutOfMemoryError e) {
                System.gc();
                throw new ResponseResult(-1, "数据过大,无法解析");
            }
            value.close();
            JsonRetEntity<T> jsonRetEntity = mDataPaser.parse(mGson, response, mDataKey, mDataType, mDataChecker, mIResponseHandler, mAutoShowLogin);
            return jsonRetEntity;
        }
        throw new RuntimeException("please set DataPaser");
    }

}
