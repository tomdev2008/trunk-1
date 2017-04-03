package com.aibinong.yueaiapi.services;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/5.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.presenter.message.ChatRecordEntiy;
import com.aibinong.yueaiapi.annotation.AutoShowLogin;
import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.GreetEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ChatService {
    @FormUrlEncoded
    @POST("chat/gift_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<GiftEntity>>>
    gift_list(
            @Field(ParamsHelper.PARAMS_STUB) String stub
    );

    /*
    接口描述：赠送礼物
    接口地址：/chat /give_gift.html
    接口参数：
    字段
    类型
    非空
    描述
    id
    String
    Y
    赠送目标的用户ID
    commodity
    String
    Y
    礼物信息，JSON格式：{"id":1,"count":1}
    
    * */
    @FormUrlEncoded
    @POST("chat/gift.html")
    @DataKey("result")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    give_gift(
            @Field("id") String id,
            @Field("commodity") String commodity
    );
    /*
       接口描述：发送聊天记录到服务器
       接口地址：/chat/message.html
       接口参数：
       * */
    @FormUrlEncoded
    @POST("chat/message.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    send_chat_record(
            @Field("chatRecordEntiy") String chatRecordEntiy
    );


    @FormUrlEncoded
    @POST("chat/chat.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<String>>
    free_chat(
            @Field("id") String id
    );
    @FormUrlEncoded
    @POST("chat/report.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<String>>
    report(
            @Field("id") String id,
            @Field("reportId") int reportId
    );

    @FormUrlEncoded
    @POST("chat/greet.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    Observable<JsonRetEntity<GreetEntity>>
    greet(
            @Field("id") String id,
            @Field("type") int type
    );
    //收到的礼物列表
    @FormUrlEncoded
    @POST("chat/gift_record_list.html")
    @DataKey("list")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<ArrayList<GiftEntity>>>
    gift_record_list(
            @Field("id") String id
    );
}
