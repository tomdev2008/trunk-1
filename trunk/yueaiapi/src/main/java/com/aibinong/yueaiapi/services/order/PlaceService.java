package com.aibinong.yueaiapi.services.order;


import com.aibinong.yueaiapi.annotation.AutoShowLogin;
import com.aibinong.yueaiapi.annotation.DataChecker;
import com.aibinong.yueaiapi.annotation.DataKey;
import com.aibinong.yueaiapi.annotation.DataPaser;
import com.aibinong.yueaiapi.api.converter.checker.ValidPayResultChecker;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.OrderResultEntitiy;
import com.aibinong.yueaiapi.pojo.PayOrderInfo;
import com.aibinong.yueaiapi.pojo.PayResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yourfriendyang on 16/7/14.
 */
public interface PlaceService {

    @FormUrlEncoded
    @POST("order/create_order.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<PayOrderInfo>>
    create_order(
            @Field("commodity") String commodityId,
            @Field("orderType") String orderType,
            @Field("payType") String payType
    );

    /**
     * 3.3 订单支付结果（√）
     * 接口描述：支付完成后，APP轮询获取订单状态 ，订单流程第三步
     * 接口地址：/order/place/pay_result.html
     * 接口参数：
     * <p/>
     * 字段	字段名称	类型	非空	描述
     * orderId	订单号	Long	是
     * {
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("order/pay_result.html")
    @DataKey("")
    @DataChecker(ValidPayResultChecker.class)
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<PayResultEntity>>
    pay_result(
            @Field("orderId") String orderId
    );


    @FormUrlEncoded
    @POST("/order/result.html")
    @DataKey("")
    @DataChecker
    @DataPaser
    @AutoShowLogin
    Observable<JsonRetEntity<OrderResultEntitiy>>
    order_result(
            @Field("orderId") String orderId
    );}
