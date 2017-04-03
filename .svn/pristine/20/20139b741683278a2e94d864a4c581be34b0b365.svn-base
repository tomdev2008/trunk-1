package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/29.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.CertPresenter;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.CertStatusEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RequireCertActivity extends ActivityBase implements CertPresenter.IRequireCert {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private com.aibinong.tantan.ui.widget.EmptyView mEmpty_require_cert;
    private LinearLayout mLl_require_cert_content;
    private ImageView mIv_require_cert_icon_mobile;
    private com.aibinong.tantan.ui.widget.AIEditText mEdit_require_cert_mobile;
    private Button mBtn_require_cert_mobile;
    private ImageView m_require_cert_icon_wx;
    private com.aibinong.tantan.ui.widget.AIEditText mEdit_require_cert_wx;
    private Button mBtn_require_cert_wx;

    private CertPresenter mCertPresenter;
    private CertStatusEntity mCertStatusEntity;

    public static Intent launchIntent(Context context) {
        Intent intent = new Intent(context, RequireCertActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_require_cert);
        ButterKnife.bind(this);
        requireTransStatusBar();
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        setupView(savedInstanceState);
        mCertPresenter = new CertPresenter(this);
        mEmpty_require_cert.startLoading();
        mCertPresenter.certStatus();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mEmpty_require_cert = (com.aibinong.tantan.ui.widget.EmptyView) findViewById(R.id.empty_require_cert);
        mLl_require_cert_content = (LinearLayout) findViewById(R.id.ll_require_cert_content);
        mIv_require_cert_icon_mobile = (ImageView) findViewById(R.id.iv_require_cert_icon_mobile);
        mEdit_require_cert_mobile = (com.aibinong.tantan.ui.widget.AIEditText) findViewById(R.id.edit_require_cert_mobile);
        mBtn_require_cert_mobile = (Button) findViewById(R.id.btn_require_cert_mobile);
        m_require_cert_icon_wx = (ImageView) findViewById(R.id._require_cert_icon_wx);
        mEdit_require_cert_wx = (com.aibinong.tantan.ui.widget.AIEditText) findViewById(R.id.edit_require_cert_wx);
        mBtn_require_cert_wx = (Button) findViewById(R.id.btn_require_cert_wx);

        mEmpty_require_cert.attatchWithView(mLl_require_cert_content, new EmptyView.CallBack() {
            @Override
            public void onRetryClick(EmptyView.LoadingState state) {

            }

            @Override
            public boolean needHideContentView() {
                return true;
            }
        });

        mBtn_require_cert_mobile.setOnClickListener(this);
        mBtn_require_cert_wx.setOnClickListener(this);
    }

    public void bindData() {

        mBtn_require_cert_mobile.setText("申请认证");
        mBtn_require_cert_wx.setText("申请认证");
        mEdit_require_cert_mobile.setEnabled(true);
        mEdit_require_cert_wx.setEnabled(true);
        mBtn_require_cert_mobile.setEnabled(true);
        mBtn_require_cert_wx.setEnabled(true);
        //手机号
        if (mCertStatusEntity != null) {
            if (mCertStatusEntity.mobile != null) {
                //通过认证或者认证中
                if (mCertStatusEntity.mobile.status == CertStatusEntity.STATUS_CERTED || mCertStatusEntity.mobile.status == CertStatusEntity.STATUS_CERTING) {
                    mEdit_require_cert_mobile.setEnabled(false);
                    mEdit_require_cert_mobile.setText(mCertStatusEntity.mobile.mobile);
                    if (mCertStatusEntity.mobile.status == CertStatusEntity.STATUS_CERTED) {
                        mBtn_require_cert_mobile.setText("已通过");
                    } else {
                        mBtn_require_cert_mobile.setText("认证中");
                    }
                    mBtn_require_cert_mobile.setEnabled(false);
                }
            }

            if (mCertStatusEntity.wechat != null) {
                //通过认证或者认证中
                if (mCertStatusEntity.wechat.status == CertStatusEntity.STATUS_CERTED || mCertStatusEntity.wechat.status == CertStatusEntity.STATUS_CERTING) {
                    mEdit_require_cert_wx.setEnabled(false);
                    mEdit_require_cert_wx.setText(mCertStatusEntity.wechat.wechat);
                    if (mCertStatusEntity.wechat.status == CertStatusEntity.STATUS_CERTED) {
                        mBtn_require_cert_wx.setText("已通过");
                    } else {
                        mBtn_require_cert_wx.setText("认证中");
                    }
                    mBtn_require_cert_wx.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBtn_require_cert_mobile) {
            String mobile = mEdit_require_cert_mobile.getText().toString();
            if (mobile.length() < 11) {
                mEdit_require_cert_mobile.setError("请输入有效的手机号码");
                mEdit_require_cert_mobile.requestFocus();
                return;
            }
            DialogUtil.showIndeternimateDialog(this, null);
            mCertPresenter.cert(1, mobile);
        } else if (view == mBtn_require_cert_wx) {
            String weixin = mEdit_require_cert_wx.getText().toString();
            if (weixin.length() < 1) {
                mEdit_require_cert_wx.setError("请输入有效的微信号码");
                mEdit_require_cert_wx.requestFocus();
                return;
            }
            DialogUtil.showIndeternimateDialog(this, null);
            mCertPresenter.cert(2, weixin);
        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onCertFailed(ResponseResult e) {
        showErrDialog(e);
    }

    @Override
    public void onCertSuccess(int type, String account) {
        DialogUtil.showDialog(this, "认证申请提交成功", true);
        mCertPresenter.certStatus();
    }

    @Override
    public void onCertStatusFailed(ResponseResult e) {
        mEmpty_require_cert.loadingFailed(e.getMessage());
    }

    @Override
    public void onCertStatusSuccess(CertStatusEntity certStatusEntities) {
        mEmpty_require_cert.loadingComplete();
        mCertStatusEntity = certStatusEntities;
        bindData();
    }
}
