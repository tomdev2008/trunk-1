package com.aibinong.yueaiapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author yourfriendyang
 *         支付宝或者微信支付获取来的订单，手动支付的话会带有order_id
 */
public class PayOrderInfo implements Serializable {
    //支付方式 0-榴莲币 1-支付宝 2-微信 3-现在支付(支付宝) 4-现在支付(微信) 5-爱贝支付 6-威富通支付 7-现在支付(QQ钱包)
    public static final String
            STR_BUY_TYPE_LIULIANBILL = "0",
            STR_BUY_TYPE_ALI = "1",
            STR_BUY_TYPE_WEIXIN = "2",
            STR_BUY_TYPE_NOWPAY_ALIPAY = "3",
            STR_BUY_TYPE_NOWPAY_wx = "4",
            STR_BUY_TYPE_NOWPAY_wx_2 = "994",
            STR_BUY_TYPE_NOWPAY_wx_3 = "995",
            STR_BUY_TYPE_NOWPAY_wx_4 = "996",
            STR_BUY_TYPE_IAPPPAY_wx = "5",
            STR_BUY_TYPE_WFT = "6",
            STR_BUY_TYPE_WFT_2 = "997",
            STR_BUY_TYPE_WFT_3 = "998",
            STR_BUY_TYPE_WFT_4 = "999",
            STR_BUY_TYPE_NOWPAY_QQ = "7",
            STR_BUY_TYPE__LFT_ALI = "9",
            STR_BUY_TYPE_LFT_WX = "10"
                    ;
    /**
     * timestamp : 1468489844
     * sign : 4FA015D604D6AD91B0D638EA3CEC354C
     * noncestr : 3L4v12DP80M4q8G5087vw7Y13dXLPQ9P
     * partnerid : 1344205101
     * prepayid : wx201607141750445292400fb00333574148
     * package : Sign=WXPay
     * appid : wxf7eb5cf44daaa88a
     * orderId : 201607141750443028
     */

    public String timestamp;
    public String sign;
    public String noncestr;
    public String partnerid;
    public String prepayid;
    @SerializedName("package")
    public String packageX;
    public String appid;
    public String orderId;


    public String payServicesString;
    @SerializedName(value = "ali_pay",alternate = {"aliPay"})
    private String ali_pay;
    public String ipppay_appid;
    public String transid;//爱贝支付使用
    public String web_pay;//爱贝网页支付用
    public String nowPay;
    public String token;



    public boolean mayPaySuccess;//是否已经成功支付过了

    public String getAli_pay() {
        return ali_pay;
    }

    public void setAli_pay(String ali_pay) {
        this.ali_pay = ali_pay;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }


    public String getPackageX() {
        return packageX;
    }

    public String getAppid() {
        return appid;
    }


}
