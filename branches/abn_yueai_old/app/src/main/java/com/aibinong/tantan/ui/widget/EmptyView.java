package com.aibinong.tantan.ui.widget;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/17.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;

public class EmptyView extends FrameLayout implements View.OnClickListener {

    private LinearLayout mLl_common_empty_failed;
    private ImageView mIv_common_empty_icon;
    private TextView mTv_common_empty_info;
    private Button mBtn_common_empty_retry;
    private LinearLayout mLl_common_empty_loading;
    private com.wang.avi.AVLoadingIndicatorView mAvindicator_common_empty_progress;
    private LinearLayout mLl_common_empty_loading_text;
    private TextView mTv_common_empty_loading;
    private pl.tajchert.sample.DotsTextView mTv_common_empty_loadingdots;
    private boolean mNeedRetryBtn;
    // End Of Content View Elements

    private void bindViews() {

        mLl_common_empty_failed = (LinearLayout) findViewById(R.id.ll_common_empty_failed);
        mIv_common_empty_icon = (ImageView) findViewById(R.id.iv_common_empty_icon);
        mTv_common_empty_info = (TextView) findViewById(R.id.tv_common_empty_info);
        mBtn_common_empty_retry = (Button) findViewById(R.id.btn_common_empty_retry);
        mLl_common_empty_loading = (LinearLayout) findViewById(R.id.ll_common_empty_loading);
        mAvindicator_common_empty_progress = (com.wang.avi.AVLoadingIndicatorView) findViewById(R.id.avindicator_common_empty_progress);
        mLl_common_empty_loading_text = (LinearLayout) findViewById(R.id.ll_common_empty_loading_text);
        mTv_common_empty_loading = (TextView) findViewById(R.id.tv_common_empty_loading);
        mTv_common_empty_loadingdots = (pl.tajchert.sample.DotsTextView) findViewById(R.id.tv_common_empty_loadingdots);
    }

    private CallBack mCallBack;
    private LoadingState mLoadingState = LoadingState.EMPTYDATA;
    private String mErrText, mBtnText, mEmptyText, mLoadingText, mEmptyBtnText;

    public EmptyView(Context context) {
        super(context);
        initView(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_common_empty_view, this, true);
        bindViews();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
            mErrText = a.getString(R.styleable.EmptyView_errorText);
            mEmptyText = a.getString(R.styleable.EmptyView_emptyText);
            mLoadingText = a.getString(R.styleable.EmptyView_loadingText);
            mBtnText = a.getString(R.styleable.EmptyView_retryBtnText);
            mEmptyBtnText = a.getString(R.styleable.EmptyView_emptyBtnText);
            mNeedRetryBtn = a.getBoolean(R.styleable.EmptyView_needRetryBtn, false);
        }
        if (mErrText == null) {
            mErrText = getResources().getString(R.string.abn_yueai_loaderror);
        }
        if (mEmptyText == null) {
            mEmptyText = getResources().getString(R.string.abn_yueai_emptydata);
        }
        if (mLoadingText == null) {
            mLoadingText = getResources().getString(R.string.abn_yueai_loading);
        }
        if (mBtnText == null) {
            mBtnText = getResources().getString(R.string.abn_yueai_loading_click_retry);
        }
        if (mEmptyBtnText == null) {
            mEmptyBtnText = getResources().getString(R.string.abn_yueai_loading_emptybtn);
        }
        if (mNeedRetryBtn) {
            mBtn_common_empty_retry.setVisibility(VISIBLE);
        } else {
            mBtn_common_empty_retry.setVisibility(GONE);
        }
        mBtn_common_empty_retry.setText(mBtnText);
        mTv_common_empty_loading.setText(mLoadingText);

        mBtn_common_empty_retry.setOnClickListener(this);

        loadingComplete();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void startLoading() {
        mLoadingState = LoadingState.LOADING;

        //加载中
        mLl_common_empty_loading_text.setVisibility(VISIBLE);
        mLl_common_empty_failed.setVisibility(GONE);

        mAvindicator_common_empty_progress.smoothToShow();
    }

    public void loadingFailed(String info) {
        if (info != null) {
            mErrText = info;
        }
        mLoadingState = LoadingState.LOADFAILED;
        //加载失败
        mAvindicator_common_empty_progress.smoothToHide();
        mLl_common_empty_loading_text.setVisibility(GONE);

        mLl_common_empty_failed.setVisibility(VISIBLE);

        mTv_common_empty_info.setText(mErrText);
        mBtn_common_empty_retry.setText(mBtnText);
    }

    public void loadingComplete() {
        mLoadingState = LoadingState.LOADCOMPLETE;

        mAvindicator_common_empty_progress.smoothToHide();
        mLl_common_empty_loading_text.setVisibility(GONE);

        mLl_common_empty_failed.setVisibility(GONE);
        mBtn_common_empty_retry.setText(mEmptyBtnText);
    }

    public void emptyData() {
        mLoadingState = LoadingState.EMPTYDATA;

        mAvindicator_common_empty_progress.smoothToHide();
        mLl_common_empty_loading_text.setVisibility(GONE);

        mLl_common_empty_failed.setVisibility(VISIBLE);
        mTv_common_empty_info.setText(mEmptyText);
    }

    @Override
    public void onClick(View view) {
        if (mLoadingState == LoadingState.LOADFAILED || mLoadingState == LoadingState.EMPTYDATA) {
            if (mCallBack != null) {
                mCallBack.onRetryClick(mLoadingState);
            }
        }
    }

    public enum LoadingState {
        LOADING,
        LOADFAILED,
        LOADCOMPLETE,
        EMPTYDATA
    }

    public interface CallBack {
        void onRetryClick(LoadingState state);
    }
}
