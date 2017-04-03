package com.aibinong.yueaiapi.api;

import android.content.Context;
import android.util.Log;

import com.aibinong.yueaiapi.api.converter.JsonRetConverterFactory;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.api.interceptor.AddParamsInterceptor;
import com.aibinong.yueaiapi.api.interceptor.CacheInterceptor;
import com.aibinong.yueaiapi.api.interceptor.LoggerInterceptor;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.LongSerializationPolicy;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class ApiHelper {
    public String HOST_ROOT;

    private static ApiHelper ourInstance = new ApiHelper();
    private Retrofit mRetrofit;
    private OkHttpClient mOkhHttpClient;
    private Gson mGson;
    private Context mContext;
    private IResultHandler mCommonResultHandler;

    public static ApiHelper getInstance() {
        return ourInstance;
    }

    private ApiHelper() {
         /*初始化Retrofit*/
        mGson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Number.class, new JsonDeserializer<Number>() {

                    @Override
                    public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        Log.i("deserialize Number", typeOfT + "");
                        try {
                            Number number = json.getAsNumber();
                            return number;
                        } catch (Exception e) {
                            return 0;
                        }
                    }
                })
                .registerTypeAdapter(Long.class, new JsonDeserializer<Long>() {
                    @Override
                    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        Log.i("deserialize Long", typeOfT + "");
                        try {
                            Long number = json.getAsLong();
                            return number;
                        } catch (Exception e) {
                            return 0L;
                        }
                    }
                })
                .registerTypeAdapter(int.class, new JsonDeserializer<Integer>() {
                    @Override
                    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            Number number = json.getAsNumber();
                            return number.intValue();
                        } catch (Exception e) {
                            return 0;
                        }
                    }
                })
                .registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
                    @Override
                    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        if (json.isJsonArray()) {
                            JsonArray array = json.getAsJsonArray();
                            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                            List list = new ArrayList<>();
                            for (int i = 0; i < array.size(); i++) {
                                JsonElement element = array.get(i);
                                Object item = context.deserialize(element, itemType);
                                list.add(item);
                            }
                            return list;
                        } else {
                            //和接口类型不符，返回空List
                            return Collections.EMPTY_LIST;
                        }
                    }
                })
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .create();
    }

    public Gson getGson() {
        return mGson;
    }

    public IResultHandler getCommonResultHandler() {
        return mCommonResultHandler;
    }

    public Context getContext() {
        return mContext;
    }

    public void init(final Context appContext, IResultHandler handler, String hostRoot, String cachePath, String clientId, boolean debug) {
        /*Stetho.initialize(Stetho.newInitializerBuilder(appContext)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(appContext))
                .build());*/
        mContext = appContext;
        HOST_ROOT = hostRoot;
        mCommonResultHandler = handler;
        /*初始化周边类*/
        ParamsHelper.getInstance().init(appContext, clientId, debug);
        ConfigUtil.getInstance().init(cachePath);
        LocalStorage.getInstance().init(appContext);
        CacheInterceptor.getInstance().init(10 * 1024 * 1024, new File(cachePath));

        /*初始化okhttpclient*/
        OkHttpClient.Builder okHBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AddParamsInterceptor())//添加通用参数
                .addInterceptor(CacheInterceptor.getInstance())//缓存控制
                ;
        if (debug) {
//            okHBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.16.2.58", 8888)));
            okHBuilder.addInterceptor(new LoggerInterceptor());//日志输出
        }

        mOkhHttpClient = okHBuilder.build();


        mRetrofit = new Retrofit.Builder()
                .baseUrl(HOST_ROOT)
                .client(mOkhHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JsonRetConverterFactory.create(mGson))
                .build();
    }

    public static <T> Observable.Transformer<T, T> doIoObserveMain() {
        Observable.Transformer<T, T> transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        ;
            }
        };
        return transformer;
    }

    public <T> T create(Class<T> service) {
        T t = mRetrofit.create(service);
        return t;
    }
}
