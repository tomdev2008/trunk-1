package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/30.                                                                |
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.CertPresenter;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.CertStatusEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VipCertActivity extends ActivityBase implements CertPresenter.IRequireCert {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.btn_vip_cert_applyfor)
    Button mBtnVipCertApplyfor;
    @Bind(R.id.empty_vip_cert)
    EmptyView mEmptyVipCert;
    @Bind(R.id.scroll_vip_cert)
    ScrollView mScrollVipCert;


    private CertPresenter mCertPresenter;
    private CertStatusEntity mCertStatusEntity;


    public static Intent launchIntent(Context context) {
        Intent intent = new Intent(context, VipCertActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_vipcert);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mCertPresenter = new CertPresenter(this);
        mEmptyVipCert.startLoading();
        mCertPresenter.certStatus();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {


        mEmptyVipCert.attatchWithView(mScrollVipCert, new EmptyView.CallBack() {
            @Override
            public void onRetryClick(EmptyView.LoadingState state) {

            }

            @Override
            public boolean needHideContentView() {
                return true;
            }
        });

        mBtnVipCertApplyfor.setOnClickListener(this);
    }

    public void bindData() {

        mBtnVipCertApplyfor.setText("申请成为尊贵VIP");
        mBtnVipCertApplyfor.setEnabled(true);
        //手机号
        if (mCertStatusEntity != null) {
            if (mCertStatusEntity.vip != null) {
                //通过认证或者认证中
                if (mCertStatusEntity.vip.status == CertStatusEntity.STATUS_CERTED || mCertStatusEntity.vip.status == CertStatusEntity.STATUS_CERTING) {
                    mBtnVipCertApplyfor.setEnabled(false);
                    if (mCertStatusEntity.vip.status == CertStatusEntity.STATUS_CERTED) {
                        mBtnVipCertApplyfor.setText("已通过认证");
                    } else {
                        mBtnVipCertApplyfor.setText("认证中");
                    }
                    mBtnVipCertApplyfor.setEnabled(false);
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnVipCertApplyfor) {

            DialogUtil.showIndeternimateDialog(this, null);
            mCertPresenter.cert(0, null);
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
        mEmptyVipCert.loadingFailed(e.getMessage());
    }

    @Override
    public void onCertStatusSuccess(CertStatusEntity certStatusEntities) {
        mEmptyVipCert.loadingComplete();
        mCertStatusEntity = certStatusEntities;
        bindData();
    }
}
