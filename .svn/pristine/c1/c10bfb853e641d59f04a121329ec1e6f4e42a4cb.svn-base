package com.aibinong.yueaiapi.services.user;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/7.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ProfileService {
    @FormUrlEncoded
    @POST("/user/profile/get_user.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<UserEntity>>
    get_user(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );

    @FormUrlEncoded
    @POST("/user/profile/get_fans.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    get_fans(
            @Field("toPage") int toPage
    );

    @FormUrlEncoded
    @POST("/user/profile/get_users.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    get_users(
            @Field("ids") String ids
    );

    @FormUrlEncoded
    @POST("/user/profile/update_user.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<LoginRetEntity>>
    update_user(
            @Field("pictureList") String pictureList,
            @Field("nickname") String nickname,
            @Field("birthdate") String birthdate,
            @Field("occupation") String occupation,
            @Field("tags") String tags,
            @Field("declaration") String declaration
    );

    @FormUrlEncoded
    @POST("/user/profile/feedback/submit.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>> feedback_submit
            (
                    @Field("content") String content
            );

    @FormUrlEncoded
    @POST("/user/profile/update_privacy.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    update_privacy(
            @Field("atomization") String atomization,
            @Field("messageLevel") String messageLevel
    );

    @FormUrlEncoded
    @POST("/user/profile/logout.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    logout(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );
}
