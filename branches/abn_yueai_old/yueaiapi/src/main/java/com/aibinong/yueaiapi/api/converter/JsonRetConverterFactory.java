package com.aibinong.yueaiapi.api.converter;

import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.converter.paser.IDataParser;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.fatalsignal.util.Log;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.Part;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class JsonRetConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static JsonRetConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static JsonRetConverterFactory create(Gson gson) {
        return new JsonRetConverterFactory(gson);
    }

    private final Gson gson;

    private JsonRetConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            if (ptype.getRawType().equals(JsonRetEntity.class)) {
                Type[] argTypes = ptype.getActualTypeArguments();
                Type argsType = null;
                if (argTypes != null && argTypes.length >= 1) {
                    argsType = argTypes[0];
                }
                IDataParser mDataPaser = null;
                for (Annotation annotation : annotations
                        ) {
                    if (annotation instanceof DataPaser) {
                        DataPaser paserAnt = (DataPaser) annotation;
                        Class<? extends IDataParser> paserClass = paserAnt.value();
                        if (paserClass != null) {
                            try {
                                Method instanceMethod = paserClass.getMethod("getInstance", new Class[0]);
                                if (instanceMethod != null) {
                                    mDataPaser = (IDataParser) instanceMethod.invoke(paserClass, new Object[0]);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }
                if (mDataPaser != null) {
                    return new JsonRetEntityConverter<>(gson, annotations, argsType, mDataPaser);
                }
            }
        }
        if (type.equals(String.class)) {
            return new SimpleStringConverter();
        } else {
            IDataParser mDataPaser = null;
            for (Annotation annotation : annotations
                    ) {
                if (annotation instanceof DataPaser) {
                    DataPaser paserAnt = (DataPaser) annotation;
                    Class<? extends IDataParser> paserClass = paserAnt.value();
                    if (paserClass != null) {
                        try {
                            Method instanceMethod = paserClass.getMethod("getInstance", new Class[0]);
                            if (instanceMethod != null) {
                                mDataPaser = (IDataParser) instanceMethod.invoke(paserClass, new Object[0]);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
            if (mDataPaser != null) {
                return new DirectEntityConverter<>(gson, annotations, type, mDataPaser);
            }
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        Log.i(getClass().getSimpleName(), "requestBodyConverter");
        if (!type.equals(RequestBody.class) && !type.equals(MultipartBody.Part.class)) {
            if (parameterAnnotations != null) {
                for (Annotation annotation : parameterAnnotations) {
                    if (annotation instanceof Part) {
                        Part partAn = (Part) annotation;
                        return new MultiPartBodyConverter(partAn.value());
                    }
                }
            }
        }

        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Log.i(getClass().getSimpleName(), "stringConverter");
        return super.stringConverter(type, annotations, retrofit);
    }
}
