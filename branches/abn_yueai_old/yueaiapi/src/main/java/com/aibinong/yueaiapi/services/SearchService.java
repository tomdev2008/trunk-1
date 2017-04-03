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
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.MainUserListEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface SearchService {
    @FormUrlEncoded
    @POST("/search/list.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<MainUserListEntity>>
    list(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("age") String age,
            @Field("distance") int distance
    );

    public static final int likeSource_main = 0, likeSource_shake = 1, likeSource_likedlist = 2;

    @FormUrlEncoded
    @POST("/search/like.html")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    like(
            @Field("id") String uuid,
            @Field("source") int source
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

    @FormUrlEncoded
    @POST("search/gift_record_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<GiftEntity>>>
    gift_record_list(
            @Field("id") String id
    );
}
