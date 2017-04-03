package com.aibinong.yueaiapi.services;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/29.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.pojo.CertStatusEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface CertService {
    @FormUrlEncoded
    @POST("cert/status.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<CertStatusEntity>>
    status(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );

    @FormUrlEncoded
    @POST("cert/cert.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    cert(
            @Field("type") int type,
            @Field("account") String account
    );
}
