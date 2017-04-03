package com.aibinong.tantan.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.CommonPayPresenter;
import com.aibinong.tantan.ui.widget.LoadingFooter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yourfriendyang on 16/7/16.
 * yourfriendyang@163.com
 */
public class PopupCheckPayResult implements View.OnClickListener {
    @Bind(R.id.abn_cwd_tv_pop_checkpayresult_requery)
    TextView mAbnCwdTvPopCheckpayresultRequery;
    @Bind(R.id.abn_cwd_lf_pop_checkpayresult_status)
    LoadingFooter mAbnCwdLfPopCheckpayresultStatus;
    @Bind(R.id.abn_cwd_tv_pop_checkpayresult_tips)
    TextView mAbnCwdTvPopCheckpayresultTips;
    @Bind(R.id.abn_cwd_tv_pop_checkpayresult_change)
    TextView mAbnCwdTvPopCheckpayresultChange;
    @Bind(R.id.abn_cwd_tv_pop_checkpayresult_paysuccess)
    TextView mAbnCwdTvPopCheckpayresultPaysuccess;
    @Bind(R.id.abn_cwd_ll_pop_checkpayresult_select)
    LinearLayout mAbnCwdLlPopCheckpayresultSelect;
    @Bind(R.id.abn_cwd_tv_pop_checkpayresult_cancel)
    TextView mAbnCwdTvPopCheckpayresultCancel;
    private View mContentView;

    private Activity mActivity;
    private PopupWindow mPopupWindow;
    public boolean mIsShowing;
    private CommonPayPresenter mCommonPayPresenter;

    public PopupCheckPayResult(Activity activity) {
        mActivity = activity;
        bindView();
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
//        mPopupWindow.setHeight((int) (DeviceUtils.getScreenHeight(mActivity) * 0.6f));
//        mPopupWindow.setWidth((int) (DeviceUtils.getScreenWidth(mActivity) * 0.8f));
//        mPopupWindow.setHeight((int) (mPopupWindow.getWidth() * 3 / 4.0f));
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                mActivity.getWindow().setAttributes(lp);
                mIsShowing = false;
            }
        });

    }

    private void bindView() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.abn_yueai_popupwindow_querypayresult, null);
        ButterKnife.bind(this, mContentView);
        mAbnCwdLfPopCheckpayresultStatus.setState(LoadingFooter.State.Normal, null);
        mAbnCwdTvPopCheckpayresultCancel.setOnClickListener(this);
        mAbnCwdTvPopCheckpayresultChange.setOnClickListener(this);
        mAbnCwdTvPopCheckpayresultPaysuccess.setOnClickListener(this);
        mAbnCwdTvPopCheckpayresultRequery.setOnClickListener(this);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public void show(CommonPayPresenter presenter) {
        mCommonPayPresenter = presenter;
        if (mActivity.getWindow() != null && mActivity.getWindow().getDecorView() != null && mActivity.getWindow().getDecorView().getWindowToken() != null) {
            mPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//            lp.alpha = 0.4f;
//            mActivity.getWindow().setAttributes(lp);
            mIsShowing = true;
        }
    }

    public static final int STATE_NORMAL = 0, STATE_QUERYING = 1, STATE_ERROR = 2, STATE_NOTPAYSUCCESS = 3;
    private int mState;

    public int getState() {
        return mState;
    }

    public void setState(int state, String msg) {
        setState(state, msg, false);
    }

    public void setState(int state, String msg, boolean canCancel) {
        mState = state;
        if (state == STATE_NORMAL) {
            mAbnCwdLfPopCheckpayresultStatus.setState(LoadingFooter.State.Normal, msg);
            mAbnCwdTvPopCheckpayresultTips.setVisibility(View.VISIBLE);
            mAbnCwdLlPopCheckpayresultSelect.setVisibility(View.VISIBLE);
            mAbnCwdTvPopCheckpayresultCancel.setVisibility(View.GONE);
            mAbnCwdTvPopCheckpayresultRequery.setVisibility(View.GONE);
        } else if (state == STATE_QUERYING) {
            mAbnCwdLfPopCheckpayresultStatus.setState(LoadingFooter.State.Loading, msg);
            mAbnCwdTvPopCheckpayresultTips.setVisibility(View.INVISIBLE);
            mAbnCwdLlPopCheckpayresultSelect.setVisibility(View.GONE);
            if (canCancel) {
                mAbnCwdTvPopCheckpayresultCancel.setVisibility(View.VISIBLE);// TODO: 16/9/1 是否允许取消
            } else {
                mAbnCwdTvPopCheckpayresultCancel.setVisibility(View.GONE);// TODO: 16/9/1 是否允许取消
            }
            mAbnCwdTvPopCheckpayresultCancel.setText("取消");
            mAbnCwdTvPopCheckpayresultRequery.setVisibility(View.GONE);
        } else if (state == STATE_ERROR) {
            mAbnCwdLfPopCheckpayresultStatus.setState(LoadingFooter.State.NetWorkError, msg);
            mAbnCwdTvPopCheckpayresultTips.setVisibility(View.INVISIBLE);
            mAbnCwdLlPopCheckpayresultSelect.setVisibility(View.GONE);
            mAbnCwdTvPopCheckpayresultCancel.setVisibility(View.VISIBLE);
            mAbnCwdTvPopCheckpayresultCancel.setText("确定");
            mAbnCwdTvPopCheckpayresultRequery.setVisibility(View.GONE);
        } else if (state == STATE_NOTPAYSUCCESS) {
            mAbnCwdLfPopCheckpayresultStatus.setState(LoadingFooter.State.NetWorkError, msg);
            mAbnCwdTvPopCheckpayresultTips.setVisibility(View.GONE);
            mAbnCwdLlPopCheckpayresultSelect.setVisibility(View.GONE);
            mAbnCwdTvPopCheckpayresultCancel.setVisibility(View.VISIBLE);
            mAbnCwdTvPopCheckpayresultRequery.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        mAbnCwdTvPopCheckpayresultCancel.setOnClickListener(this);
        mAbnCwdTvPopCheckpayresultChange.setOnClickListener(this);
        mAbnCwdTvPopCheckpayresultPaysuccess.setOnClickListener(this);
        if (v == mAbnCwdTvPopCheckpayresultCancel) {
            mCommonPayPresenter.cancelQueryResult();
            dismiss();
            if (mState == STATE_QUERYING) {
                Toast.makeText(PopupCheckPayResult.this.mActivity, "如果您已经付款,请勿担心,取消查询并不影响购买结果", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mAbnCwdTvPopCheckpayresultChange) {
            mCommonPayPresenter.cancelQueryResult();
            dismiss();
        } else if (v == mAbnCwdTvPopCheckpayresultPaysuccess) {
            mCommonPayPresenter.startQueryResult(0);
        } else if (v == mAbnCwdTvPopCheckpayresultRequery) {
            mCommonPayPresenter.startQueryResult(0);
        }
    }

}
