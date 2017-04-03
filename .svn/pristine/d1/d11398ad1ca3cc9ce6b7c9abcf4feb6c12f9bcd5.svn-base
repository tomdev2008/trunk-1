package com.aibinong.yueaiapi.api.converter;

import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.converter.paser.IDataParser;
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
public class DirectEntityConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;
    private Annotation[] mAnnotations;
    private String mDataKey;
    private Type mDataType;
    private IDataChecker mDataChecker;
    private IDataParser mDataPaser;

    DirectEntityConverter(Gson gson, Annotation[] annotations, Type dataType, IDataParser paser) {
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
                }
            }
        }
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        if (mDataPaser != null) {
            String response = value.string();
            value.close();
            return mDataPaser.parse(mGson, response, mDataKey, mDataType, mDataChecker, null,false);
        }
        throw new RuntimeException("please set DataPaser");
    }
}
