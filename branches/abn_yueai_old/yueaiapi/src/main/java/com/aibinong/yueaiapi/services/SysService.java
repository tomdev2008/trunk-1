package com.aibinong.yueaiapi.services;


import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.UpdateInfo;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public interface SysService {
    @FormUrlEncoded
    @POST("sys/config.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<ConfigEntity>> config(@Field(ParamsHelper.PARAMS_STUB) String stub);


    @FormUrlEncoded
    @POST("sys/app_download.html")
    @DataChecker
    @DataKey("download")
    @DataPaser
    Observable<JsonRetEntity<UpdateInfo>> app_download(@Field(ParamsHelper.PARAMS_STUB) String stub);

    public static final int UPLOAD_TYPE_AVATAR = 1, UPLOAD_TYPE_SHARE = 2;
    public static final String upload_file_key = "attachment";

    @Multipart
    @POST("sys/upload_file.html")
    @DataKey("url")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>> upload_file(@Part MultipartBody.Part file);

    //
    @FormUrlEncoded
    @POST("sys/send_phone_verify_code.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>> send_phone_verify_code(@Field("mobile") String mobile);

/*    @FormUrlEncoded
    @POST("/sys/tags.html")
    @DataKey("tags")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<ArrayList<String>>>
    tags(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );*/
}
