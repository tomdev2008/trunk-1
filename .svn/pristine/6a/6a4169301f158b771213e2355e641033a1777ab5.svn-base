package com.aibinong.yueaiapi.services;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.annotation.AutoShowLogin;
import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.converter.checker.AllowNullChecker;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface SearchService {
    @FormUrlEncoded
    @POST("/search/list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    list(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("sex") int sex,
            @Field("toPage") int toPage
    );

    @FormUrlEncoded
    @POST("/search/select_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    select_list(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("sex") int sex,
            @Field("toPage") int toPage
    );

    @FormUrlEncoded
    @POST("/search/follow_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    follow_list(
            @Field("toPage") int toPage
    );
    @FormUrlEncoded
    @POST("/search/fans_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    fans_list(
            @Field("toPage") int toPage
    );

    @FormUrlEncoded
    @POST("/search/browse_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    browse_list(
            @Field("toPage") int toPage
    );
    public static final int likeSource_main = 0, likeSource_shake = 1, likeSource_likedlist = 2;

    @FormUrlEncoded
    @POST("/search/follow.html")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    follow(
            @Field("id") String uuid
    );
    @FormUrlEncoded
    @POST("/search/unfollow.html")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    unfollow(
            @Field("id") String uuid
    );
    @FormUrlEncoded
    @POST("/search/browse.html")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    browse(
            @Field("id") String uuid
    );

    @FormUrlEncoded
    @POST("/search/sever.html")
    @AutoShowLogin
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    sever(
            @Field("id") String uuid
    );

    @FormUrlEncoded
    @POST("/search/shake.html")
    @DataKey("user")
    @DataChecker(AllowNullChecker.class)
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<UserEntity>>
    shake(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude
    );

    @FormUrlEncoded
    @POST("/search/matching_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<UserEntity>>>
    matching_list(
            @Field("toPage") int toPage
    );


}
