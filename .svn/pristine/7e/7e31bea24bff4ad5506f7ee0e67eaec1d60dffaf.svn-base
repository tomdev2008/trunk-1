package com.aibinong.tantan.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.tantan.ui.activity.IapppayH5PayActivity;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.AliPayResult;
import com.aibinong.yueaiapi.pojo.CommodityEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.OrderResultEntitiy;
import com.aibinong.yueaiapi.pojo.PayOrderInfo;
import com.aibinong.yueaiapi.pojo.PayResultEntity;
import com.aibinong.yueaiapi.pojo.PayTypeEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.order.PlaceService;
import com.alipay.sdk.app.PayTask;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/7/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|
public class CommonPayPresenter extends PresenterBase {
    public interface CommonPayInteraction {
        void onCreateOrderFailed(Throwable e);

        void onCreateOrderSuccess(PayOrderInfo orderInfo, String payType);

        void onQueryResultSuccess(PayResultEntity payResultEntity, String payType);

        void onQueryResultFailed(Throwable e);
        void onOrderResultSuccess(OrderResultEntitiy orderResultEntitiy);
        void onOrderResultFailed(Throwable e);
        void onPayFailed(String msg);

        void onQueryPayResult(PayOrderInfo orderInfo, String payType, boolean canCancel);

        void onCancelQueryPayResult();

        void onMaybePaySuccess(PayOrderInfo orderInfo, String payType);
    }

    public static final String WX_APP_ID = "";
    public static final String Iapppay_Appid = "";
    public static final int PAY_RESULT_SUCCESS = 0, PAY_RESULT_FAILED = -1,
            PAY_RESULT_CANCEL = -2, PAY_RESULT_MAYBESUCCESS = 3;

    private Activity mActivity;
    private CommonPayInteraction mCommonPayInteraction;
    private IPayResultCallback iPayResultCallback;
    private IWXAPI msgApi;
    private BroadcastReceiver wxPayRetReceiver;

    public PayOrderInfo mCurrentOrderInfo;
    public String mCurrentPayType;
    private List<PayTypeEntity> mPayTypes;

    public CommonPayPresenter(Activity mActivity, CommonPayInteraction mCommonPayInteraction) {
        this.mActivity = mActivity;
        this.mCommonPayInteraction = mCommonPayInteraction;
    }

    public void reset() {
        mCurrentOrderInfo = null;
        mCurrentPayType = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("mCurrentOrderInfo", mCurrentOrderInfo);
        outState.putString("mCurrentPayType", mCurrentPayType);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentOrderInfo = (PayOrderInfo) savedInstanceState.getSerializable("mCurrentOrderInfo");
        mCurrentPayType = savedInstanceState.getString("mCurrentPayType");
    }

    @Override
    public void onDestoryView() {
        super.onDestoryView();
        if (wxPayRetReceiver != null) {
            GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(wxPayRetReceiver);
        }

        IpaynowPlugin.getInstance().init(mActivity.getApplicationContext());
        Log.i(this.getClass().getSimpleName() + " CommonPayPresenter Impl:", "onDestoryView");
    }

    public void setPayTypes(ArrayList<PayTypeEntity> payTypes) {
        mPayTypes = payTypes;
        initPayCallBack();
    }

    private void initPayCallBack() {

        /**
         * 爱贝支付结果回调
         */
        iPayResultCallback = new IPayResultCallback() {

            @Override
            public void onPayResult(int resultCode, String signvalue, String resultInfo) {
                // 只有成功才过去
                if (mCurrentOrderInfo == null) {
                    return;
                }
                switch (resultCode) {
                    case IAppPay.PAY_SUCCESS:
                        startQueryResult(0);
                        break;
                    default:
                        mCurrentOrderInfo.mayPaySuccess = false;
                        mCommonPayInteraction.onPayFailed("支付失败 " + resultInfo);
                        break;
                }
            }
        };
        // 微信支付结果接收
        msgApi = WXAPIFactory.createWXAPI(mActivity.getApplicationContext(),
                "");// TODO: 16/11/23 微信appid
        wxPayRetReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // 只有成功才过去
                if (mCurrentOrderInfo == null) {
                    return;
                }
                int payResult = intent.getIntExtra(
                        IntentExtraKey.PAY_RESULT_PAYRESULT,
                        PAY_RESULT_FAILED);
                if (payResult != PAY_RESULT_FAILED
                        && payResult != PAY_RESULT_CANCEL) {
                    startQueryResult(0);
                } else {
                    mCurrentOrderInfo.mayPaySuccess = false;
                    mCommonPayInteraction.onPayFailed("支付失败");
                }
            }
        };
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().registerReceiver(wxPayRetReceiver, new IntentFilter(
                BroadCastConst.WXPayResultBroadIntentAction));
        //现在支付
        IpaynowPlugin.getInstance().init(mActivity);
        IpaynowPlugin.getInstance().setCallResultReceiver(new MyIpayNowReceiver(this));

    }

    public void checkMaybePaySuccess() {
        if (mCurrentOrderInfo != null && mCurrentOrderInfo.mayPaySuccess) {
            //已经支付成功
            startQueryResult(0);
//            mCommonPayInteraction.onMaybePaySuccess(mCurrentOrderInfo, mCurrentPayType);
        }
    }

    public boolean onPlaceOrderSuccess(PayOrderInfo data, final String payType) {
        mCurrentOrderInfo = data;
        mCurrentPayType = payType;
        //支付成功
        if (PayOrderInfo.STR_BUY_TYPE_WEIXIN.equals(payType)) {
            //微信
            if (!msgApi.isWXAppInstalled()) {
                Toast.makeText(mActivity, "请先安装微信",
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            if (mCurrentOrderInfo != null) {
                msgApi.registerApp(WX_APP_ID);// TODO: 16/11/23
                PayReq payReq = new PayReq();
                payReq.appId = mCurrentOrderInfo.getAppid();
                payReq.partnerId = mCurrentOrderInfo.partnerid;
                payReq.prepayId = mCurrentOrderInfo.prepayid;
                payReq.nonceStr = mCurrentOrderInfo.noncestr;
                payReq.timeStamp = mCurrentOrderInfo.timestamp;
                payReq.packageValue = mCurrentOrderInfo.getPackageX();
                payReq.sign = mCurrentOrderInfo.getSign();
                msgApi.sendReq(payReq);

                mCurrentOrderInfo.mayPaySuccess = true;
            }

        } else if (PayOrderInfo.STR_BUY_TYPE_ALI.equals(payType) || PayOrderInfo.STR_BUY_TYPE__LFT_ALI.equals(payType)) {
            //支付宝
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象

                    String payStr = new String(Base64.decode(
                            mCurrentOrderInfo.getAli_pay() == null ? "" : mCurrentOrderInfo.getAli_pay(), Base64.DEFAULT));
//                    String payStr = new String(Base64.decode("X2lucHV0X2NoYXJzZXQ9InV0Zi04IiZib2R5PSLllYbmiLfmnI3liqHotLkt57Sr6JaH5bqXIiZpdF9iX3BheT0iMzBtIiZub3RpZnlfdXJsPSJodHRwOi8vZnJvbnQudGVzdC5hbGl1bGlhbi5jb20vdjEvb3JkZXIvcGF5LzEwMDRfMl9hbGlfbm90aWZ5X3VybC5odG1sIiZvdXRfdHJhZGVfbm89IjE2MDMwMTE4MjQwMjAwMDAwMSImcGFydG5lcj0iMjA4ODkxMTgzMDYwODk0MyImcGF5bWVudF90eXBlPSIxIiZzZWxsZXJfaWQ9IjIwODg5MTE4MzA2MDg5NDMiJnNlcnZpY2U9Im1vYmlsZS5zZWN1cml0eXBheS5wYXkiJnNob3dfdXJsPSJtLmFsaXBheS5jb20iJnN1YmplY3Q9IuWVhuaIt+acjeWKoei0uS3ntKvoloflupciJnRvdGFsX2ZlZT0iMS4wIiZzaWduPSJmZlp6R281aUoxQ2lvMzlCR0VKTW5CSlJSdlIlMkJQV3BlQUVmTjN0eWU0VGFDJTJGak01YkhnTEF3JTJGY2ZXTkNtMFYwSjFKb1lkSFNsNnNVM1dDcm11eU1NS3dMNzlaM1JGQmd2bjI1b2NGMFlpcjdoVXFyRTE5eGhJdTQzZDhuTlhpVCUyQmV2M1ZGVFpadmw2Y3VMNEVsdDM0VUdVTHU5MUl0UmtuTmM2dkFVbWJhVSUzRCImc2lnbl90eXBlPSJSU0Ei", Base64.DEFAULT));
                    PayTask alipay = new PayTask(
                            mActivity);
                    // 调用支付接口，获取支付结果
                    final String mPayResult = alipay.pay(payStr);
                    // 直接跳到支付结果界面去
                    new Handler(mActivity
                            .getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            // 支付宝支付
                            AliPayResult payResult = new AliPayResult(
                                    mPayResult);
                            boolean isPaySuccess = false;
                            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                            String resultInfo = payResult
                                    .getResult();
                            String resultStatus = payResult
                                    .getResultStatus();
                            // 判断resultStatus
                            // 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                            if (TextUtils.equals(
                                    resultStatus, "9000")) {
                                // 成功
                                isPaySuccess = true;
                            } else {
                                // 判断resultStatus
                                // 为非“9000”则代表可能支付失败
                                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                if (TextUtils.equals(
                                        resultStatus,
                                        "8000")) {
                                    isPaySuccess = true;
                                } else {
                                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                    isPaySuccess = false;
                                }
                            }

                            if (isPaySuccess) {
                                startQueryResult(0);
                            } else {
                                mCommonPayInteraction.onPayFailed(payResult.getMemo());
                            }

                        }
                    });
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else if (PayOrderInfo.STR_BUY_TYPE_IAPPPAY_wx.equals(payType)) {
            //爱贝支付 微信
            String ipppay_appid = Iapppay_Appid;
            if (!StringUtils.isEmpty(mCurrentOrderInfo.ipppay_appid)) {
                ipppay_appid = mCurrentOrderInfo.ipppay_appid;
            }

            IAppPay.startPay(mActivity, "transid=" + mCurrentOrderInfo.transid + "&appid=" + ipppay_appid, iPayResultCallback, IAppPay.PAY_METHOD_WECHATPAY);

            mCurrentOrderInfo.mayPaySuccess = true;
        } else if (PayOrderInfo.STR_BUY_TYPE_NOWPAY_wx.equals(payType) ||
                PayOrderInfo.STR_BUY_TYPE_NOWPAY_ALIPAY.equals(payType) ||
                PayOrderInfo.STR_BUY_TYPE_NOWPAY_QQ.equals(payType) ||
                PayOrderInfo.STR_BUY_TYPE_NOWPAY_wx_2.equals(payType) ||
                PayOrderInfo.STR_BUY_TYPE_NOWPAY_wx_3.equals(payType) ||
                PayOrderInfo.STR_BUY_TYPE_NOWPAY_wx_4.equals(payType)
                ) {
            //现在支付
            IpaynowPlugin.getInstance().pay(data.nowPay);

            mCurrentOrderInfo.mayPaySuccess = true;
        } else if (
                PayOrderInfo.STR_BUY_TYPE_WFT.equals(payType) ||
                        PayOrderInfo.STR_BUY_TYPE_WFT_2.equals(payType) ||
                        PayOrderInfo.STR_BUY_TYPE_WFT_3.equals(payType) ||
                        PayOrderInfo.STR_BUY_TYPE_WFT_4.equals(payType)
                ) {
            RequestMsg msg = new RequestMsg();
            msg.setTokenId(data.token);
            // 微信wap支付
            if (!StringUtils.isEmpty(data.payServicesString)) {
                msg.setTradeType(data.payServicesString);
            } else {
                msg.setTradeType(MainApplication.PAY_WX_WAP);
            }
            msg.setOutTradeNo(data.orderId);
            PayPlugin.unifiedH5Pay(mActivity, msg);

            mCurrentOrderInfo.mayPaySuccess = true;
//            mCurrentOrderInfo.mayPaySuccess = true;
//            mCommonPayInteraction.onMaybePaySuccess(mCurrentOrderInfo, mCurrentPayType);
        } else if (PayOrderInfo.STR_BUY_TYPE_LFT_WX.equals(payType)) {
            RequestMsg msg = new RequestMsg();
            msg.setTokenId(data.token);
            // 微信wap支付
            if (!StringUtils.isEmpty(data.payServicesString)) {
                msg.setTradeType(data.payServicesString);
            } else {
                msg.setTradeType(MainApplication.PAY_WX_WAP);
            }
            msg.setOutTradeNo(data.orderId);
            PayPlugin.unifiedH5Pay(mActivity, msg);

            mCurrentOrderInfo.mayPaySuccess = true;
//            mCurrentOrderInfo.mayPaySuccess = true;
//            mCommonPayInteraction.onMaybePaySuccess(mCurrentOrderInfo, mCurrentPayType);
        } else if (PayOrderInfo.STR_BUY_TYPE_LIULIANBILL.equals(payType)) {
            mCurrentOrderInfo.mayPaySuccess = true;
            startQueryResult(0);
        } else if (!StringUtils.isEmpty(mCurrentOrderInfo.web_pay)) {
            mCurrentOrderInfo.mayPaySuccess = true;
            Intent webIntent = new Intent(mActivity, IapppayH5PayActivity.class);
            webIntent.putExtra(IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA, mCurrentOrderInfo.web_pay);
            mActivity.startActivity(webIntent);
//            mCommonPayInteraction.onMaybePaySuccess(mCurrentOrderInfo, mCurrentPayType);
        }
        return true;
    }

    private Subscription mQueryIntervalSubcribtion;

    public void startQueryResult(final int initialDelay) {
        if (mCurrentOrderInfo == null) {
            return;
        }
        if (mQueryIntervalSubcribtion != null) {
            mQueryIntervalSubcribtion.unsubscribe();
        }
        mCurrentOrderInfo.mayPaySuccess = true;
        mQueryIntervalSubcribtion = Observable.interval(initialDelay, 3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mCurrentOrderInfo != null && mCurrentOrderInfo.mayPaySuccess) {
                            mQueryIntervalSubcribtion.unsubscribe();
                            mCommonPayInteraction.onQueryPayResult(mCurrentOrderInfo, mCurrentPayType, initialDelay > 0);
                        } else {
                            cancelQueryResult();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    }
                });

        addToCycle(mQueryIntervalSubcribtion);
    }

    public void cancelQueryResult() {
        if (mQueryIntervalSubcribtion != null) {
            mQueryIntervalSubcribtion.unsubscribe();
        }
        if (mCurrentOrderInfo != null) {
            mCurrentOrderInfo.mayPaySuccess = false;
        }
        mCommonPayInteraction.onCancelQueryPayResult();
    }

    static class MyIpayNowReceiver implements ReceivePayResult {
        private WeakReference<CommonPayPresenter> activityWeakReference;

        public MyIpayNowReceiver(CommonPayPresenter activity) {
            activityWeakReference = new WeakReference<CommonPayPresenter>(activity);
        }

        @Override
        public void onIpaynowTransResult(ResponseParams responseParams) {
            if (activityWeakReference != null) {
                CommonPayPresenter activity = activityWeakReference.get();
                if (activity != null) {
                    String respCode = responseParams.respCode;
                    if (respCode.equals("00")) {
                        activity.startQueryResult(0);
                    } else {
                        activity.mCurrentOrderInfo.mayPaySuccess = false;
                        activity.mCommonPayInteraction.onPayFailed("支付失败");
                    }
                }
            }
        }
    }


    public void createOrder(String commodityId, String orderType, final String payType, int payCount) {
        ArrayList<CommodityEntity> commodityEntities = new ArrayList<>(1);
        commodityEntities.add(new CommodityEntity(commodityId, payCount>0?payCount:1));
        addToCycle(ApiHelper.getInstance()
                .create(PlaceService.class)
                .create_order(ApiHelper.getInstance().getGson().toJson(commodityEntities), orderType, payType)
                .compose(ApiHelper.<JsonRetEntity<PayOrderInfo>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<PayOrderInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCommonPayInteraction.onCreateOrderFailed(ResponseResult.fromThrowable(e));
                    }

                    @Override
                    public void onNext(JsonRetEntity<PayOrderInfo> payOrderInfoJsonRetEntity) {
                        mCommonPayInteraction.onCreateOrderSuccess(payOrderInfoJsonRetEntity.getData(), payType);
                    }
                })
        );
    }

    public void queryPayResult(String orderId, final String payType) {
        addToCycle(ApiHelper.getInstance()
                .create(PlaceService.class)
                .pay_result(orderId)
                .compose(ApiHelper.<JsonRetEntity<PayResultEntity>>doIoObserveMain())
                .subscribe(
                        new Subscriber<JsonRetEntity<PayResultEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mCommonPayInteraction.onQueryResultFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<PayResultEntity> liuLianTradeInfoJsonRetEntity) {
                                mCommonPayInteraction.onQueryResultSuccess(liuLianTradeInfoJsonRetEntity.getData(), payType);
                            }
                        }
                )
        );
    }
    public void queryPayResultAndshow(String orderId) {
        addToCycle(ApiHelper.getInstance()
                .create(PlaceService.class)
                .order_result(orderId)
                .compose(ApiHelper.<JsonRetEntity<OrderResultEntitiy>>doIoObserveMain())
                .subscribe(
                        new Subscriber<JsonRetEntity<OrderResultEntitiy>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mCommonPayInteraction.onOrderResultFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<OrderResultEntitiy> liuLianTradeInfoJsonRetEntity) {
                                mCommonPayInteraction.onOrderResultSuccess(liuLianTradeInfoJsonRetEntity.getData());
                            }
                        }
                )
        );
    }
}
