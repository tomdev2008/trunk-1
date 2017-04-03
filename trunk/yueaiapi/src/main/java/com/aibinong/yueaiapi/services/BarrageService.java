package com.aibinong.yueaiapi.services;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/15.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.api.converter.paser.DanmuDataParser;
import com.aibinong.yueaiapi.pojo.DanMusEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface BarrageService {
    @FormUrlEncoded
    @POST("/barrage/get_barrages.html")
    @DataKey("list")
    @DataChecker
    @DataPaser(DanmuDataParser.class)
    Observable<JsonRetEntity<DanMusEntity>>
    get_barrages(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );
}
