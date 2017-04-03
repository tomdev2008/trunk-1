package com.aibinong.tantan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.CommonPayPresenter;
import com.aibinong.tantan.ui.adapter.CrowdfundingPayTypeAdapter;
import com.aibinong.tantan.ui.dialog.PopupCheckPayResult;
import com.aibinong.tantan.ui.fragment.message.DividerItemDecoration;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.OrderEntity;
import com.aibinong.yueaiapi.pojo.OrderResultEntitiy;
import com.aibinong.yueaiapi.pojo.PayOrderInfo;
import com.aibinong.yueaiapi.pojo.PayResultEntity;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.fatalsignal.util.StringUtils;
import com.iapppay.sdk.main.IAppPay;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yourfriendyang on 2016/2/23.
 */
public class SelectPayActivity extends ActivityBase implements CommonPayPresenter.CommonPayInteraction {
    @Bind(R.id.tv_selectpay_price)
    TextView mTvSelectpayPrice;
    @Bind(R.id.btn_selectpay_buy)
    TextView mBtnSelectpayBuy;
    @Bind(R.id.rl_selectpay_bottom)
    LinearLayout mRlSelectpayBottom;
    @Bind(R.id.tv_pay_select_none)
    TextView mTvPaySelectNone;
    @Bind(R.id.recyclerView_selectpay)
    RecyclerView mRecyclerViewSelectpay;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private CrowdfundingPayTypeAdapter mBuyScorePayTypeAdapter;
    private PushMessage mPushMessage;
    private CommonPayPresenter mCommonPayPresenter;
    protected PopupCheckPayResult mPopupCheckPayResult;
    /**
     * 自定义的支付类型 1是会员充值  2是购买礼物
     */
    private int definePayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_select_pay);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mPushMessage = (PushMessage) getIntent().getSerializableExtra(IntentExtraKey.INTENT_EXTRA_KEY_PUSHMESSAGE);
        mCommonPayPresenter = new CommonPayPresenter(this, this);
        bindData();
        IAppPay.init(this, IAppPay.PORTRAIT, "abcd");
        mCommonPayPresenter.onCreate();
        if (mPushMessage.data != null) {
            mCommonPayPresenter.setPayTypes(mPushMessage.data.payTypes);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommonPayPresenter.checkMaybePaySuccess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommonPayPresenter.onDestoryView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCommonPayPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommonPayPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //只有可取消时才消失
        if (mPopupCheckPayResult != null && mPopupCheckPayResult.mIsShowing) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mBuyScorePayTypeAdapter = new CrowdfundingPayTypeAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSelectpay.setLayoutManager(linearLayoutManager);
        mRecyclerViewSelectpay.setAdapter(mBuyScorePayTypeAdapter);
        mRecyclerViewSelectpay.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mBtnSelectpayBuy.setOnClickListener(this);
        mPopupCheckPayResult = new PopupCheckPayResult(this);
    }


    private void bindData() {
        if (mPushMessage != null && mPushMessage.data != null) {
            mBuyScorePayTypeAdapter.setPayTypes(mPushMessage.data.payTypes);
            mBuyScorePayTypeAdapter.setRmbPrice(mPushMessage.data.payMoney);
            mTvSelectpayPrice.setText(String.format("¥%.2f", mPushMessage.data.payMoney));
        }
        if (mBuyScorePayTypeAdapter.getItemCount() <= 0) {
            mTvPaySelectNone.setVisibility(View.VISIBLE);
        } else {
            mTvPaySelectNone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSelectpayBuy) {
            //确定支付
            DialogUtil.showIndeternimateDialog(this, null);
            mCommonPayPresenter.createOrder(mPushMessage.data.targetId, mPushMessage.data.type, mBuyScorePayTypeAdapter.getCurrentPayType().payType, mPushMessage.data.payCount);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("targetId", mPushMessage.data.targetId);
            map.put("type", mPushMessage.data.type);
            map.put("payType", mBuyScorePayTypeAdapter.getCurrentPayType().payType);
            map.put("payCount", mPushMessage.data.payCount + "");
            MobclickAgent.onEventValue(this, "try_pay", map, (int) (mPushMessage.data.payMoney * 100));
        } else {
            super.onClick(v);
        }
    }


    @Override
    public void onCreateOrderFailed(Throwable e) {
        showErrDialog(e);
    }

    @Override
    public void onCreateOrderSuccess(PayOrderInfo orderInfo, String payType) {
        DialogUtil.hideDialog(this);
        mCommonPayPresenter.onPlaceOrderSuccess(orderInfo, payType);
    }


    @Override
    public void onQueryResultSuccess(PayResultEntity payResultEntity, String payType) {
        if (payResultEntity.order.status == OrderEntity.ORDER_STATUS_SUCCESS) {
            //支付成功
            mCommonPayPresenter.cancelQueryResult();
            mPopupCheckPayResult.dismiss();
            mCommonPayPresenter.reset();


            mCommonPayPresenter.queryPayResultAndshow(payResultEntity.order.orderId);
            definePayType = payResultEntity.order.orderType;

            DialogUtil.showDialog(this, "支付成功", true).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });
            GlobalLocalBroadCastManager.getInstance().notifyPaySuccess();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("targetId", mPushMessage.data.targetId);
            map.put("type", mPushMessage.data.type);
            map.put("payType", mBuyScorePayTypeAdapter.getCurrentPayType().payType);
            map.put("payCount", mPushMessage.data.payCount + "");
            MobclickAgent.onEventValue(this, "pay", map, (int) (mPushMessage.data.payMoney * 100));
        } else if (payResultEntity.order.status == OrderEntity.ORDER_STATUS_FAILED) {
            mPopupCheckPayResult.setState(PopupCheckPayResult.STATE_ERROR, "支付失败");
            mCommonPayPresenter.cancelQueryResult();
        } else {
            //继续查询
            mCommonPayPresenter.startQueryResult(3);
        }
    }

    @Override
    public void onQueryResultFailed(Throwable e) {
        mPopupCheckPayResult.setState(PopupCheckPayResult.STATE_NOTPAYSUCCESS, e.getMessage() + "\n订单未成功,如果您确认已经付过款,请再次尝试查询支付结果");
        mCommonPayPresenter.cancelQueryResult();
    }

    @Override
    public void onOrderResultSuccess(OrderResultEntitiy orderResultEntitiy) {
        if (definePayType == 1) {  //是会员充值 才弹对话框
            GlobalLocalBroadCastManager.getInstance().onCanVipGiftShow(orderResultEntitiy);
        }

    }

    @Override
    public void onOrderResultFailed(Throwable e) {

    }

    @Override
    public void onPayFailed(String msg) {
        if (!mPopupCheckPayResult.mIsShowing) {
            mPopupCheckPayResult.show(mCommonPayPresenter);
        }
        mPopupCheckPayResult.setState(PopupCheckPayResult.STATE_ERROR, msg);
    }


    @Override
    public void onQueryPayResult(PayOrderInfo orderInfo, String payType, boolean canCancel) {
        if (mCommonPayPresenter.mCurrentOrderInfo != null) {
            if (!mPopupCheckPayResult.mIsShowing) {
                mPopupCheckPayResult.show(mCommonPayPresenter);
            }
            mPopupCheckPayResult.setState(PopupCheckPayResult.STATE_QUERYING, "正在查询支付结果", canCancel);
            mCommonPayPresenter.queryPayResult(orderInfo.orderId, payType);
        }
    }


    @Override
    public void onCancelQueryPayResult() {
//        mCommonPayPresenter.reset();
//        if (mPopupCheckPayResult != null) {
//            mPopupCheckPayResult.dismiss();
//        }
    }

    @Override
    public void onMaybePaySuccess(PayOrderInfo orderInfo, String payType) {
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                if (!mPopupCheckPayResult.mIsShowing) {
                    mPopupCheckPayResult.show(mCommonPayPresenter);
                }
                if (mPopupCheckPayResult.getState() != PopupCheckPayResult.STATE_QUERYING) {
                    mPopupCheckPayResult.setState(PopupCheckPayResult.STATE_NORMAL, null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }
        //微付通的回调
        String respCode = data.getExtras().getString("resultCode");
        if (!StringUtils.isEmpty(respCode) && respCode.equalsIgnoreCase("success")) {
            //标示支付成功
            mCommonPayPresenter.startQueryResult(0);
        } else { //其他状态NOPAY状态：取消支付，未支付等状态
            if (mCommonPayPresenter.mCurrentOrderInfo != null) {
                mCommonPayPresenter.mCurrentOrderInfo.mayPaySuccess = false;
            }
            onPayFailed("未支付");
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
