package com.aibinong.yueaiapi.services.user;


import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("user/login/login_by_mobile.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<LoginRetEntity>>
    login_by_mobile(
            @Field("mobile") String mobile,
            @Field("verifyCode") String verifyCode
    );

    @FormUrlEncoded
    @POST("user/login/verify_code_validation.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<LoginRetEntity>>
    verify_code_validation(
            @Field("mobile") String mobile,
            @Field("verifyCode") String verifyCode
    );

    public static final String registerBithFm = "yyyy-MM-dd";
    public static final int registerSexMale = 1, registerSexFemale = 0;

    @FormUrlEncoded
    @POST("user/login/register.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<LoginRetEntity>>
    register(
            @Field("pictureList") String pictureList,
            @Field("nickname") String nickname,
            @Field("birthdate") String birthdate,
            @Field("sex") int sex,
            @Field("occupation") String occupation,
            @Field("tags") String tags
    );


    public static final String OPENTYPE_WX = "wechat", OPENTYPE_QQ = "qq";

    @FormUrlEncoded
    @POST("user/login/login_by_open.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<LoginRetEntity>>
    login_by_open(
            @Field("openId") String openId,
            @Field("openType") String openType,
            @Field("nickName") String nickName,
            @Field("portrait") String portrait
    );

    @FormUrlEncoded
    @POST("user/login/bind_mobile.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    bind_mobile(
            @Field("mobile") String mobile,
            @Field("verifyCode") String verifyCode
    );

    @FormUrlEncoded
    @POST("user/login/bind_push_id.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    bind_push_id(
            @Field("pushId") String pushId
    );

    @FormUrlEncoded
    @POST("user/login/user_active.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    user_active(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude
    );
}
