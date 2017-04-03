package com.aibinong.yueaiapi.services;


import com.aibinong.yueaiapi.annotation.AutoShowLogin;
import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.aibinong.yueaiapi.pojo.UpdateInfo;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;
import java.util.List;

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
public interface ActivityListService {
    @FormUrlEncoded
    @POST("user/profile/activity_list.html")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ActivityListEntity>>
    list(
            @Field("toPage") int toPage
    );



}
