package com.aibinong.yueaiapi.pojo;

import java.io.Serializable;

/**
 * Created by yourfriendyang on 16/7/15.
 */
public class OrderEntity implements Serializable {

    /**
     * orderId : 2016071123123310000121
     * userId : 10000121
     * amount : 100
     * payAmount : 100
     * payType : 1
     * thirdOrderId : wxFSFSD1010FSD12111
     * buyTime : 123123123131131
     * orderType : 1
     * status : 1 支付状态 0-待支付 1-支付成功 2-支付失败
     */
    public static final int ORDER_TYPE_BUY = 1, ORDER_TYPE_RECHARTE = 2;
    public static final int ORDER_STATUS_NOTPAY = 0, ORDER_STATUS_SUCCESS = 1, ORDER_STATUS_FAILED = 2;
    public String orderId;
    public String userId;
    public int amount;//订单总额
    public int payAmount;//支付金额
    public int payType;
    public String thirdOrderId;
    public long buyTime;
    public String linkUrl;
    public int orderType;// 订单类型1-支付订单 2-充值订单
    public int status;//支付状态 0-待支付 1-支付成功 2-支付失败
}
