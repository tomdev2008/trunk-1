package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/2.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.RegisterPresenter;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.apiInterface.ISysSendVerifyCode;
import com.aibinong.yueaiapi.apiInterface.IVerifyCodeValidation;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.fatalsignal.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends ActivityUnRegisterBase implements ISysSendVerifyCode, IVerifyCodeValidation {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_register_phone)
    EditText mEditRegisterPhone;
    @Bind(R.id.edit_register_verifycode)
    EditText mEditRegisterVerifycode;
    @Bind(R.id.btn_register_sendsms)
    Button mBtnRegisterSendsms;
    @Bind(R.id.btn_register_next)
    Button mBtnRegisterNext;
    @Bind(R.id.tv_completeinfo_clause)
    TextView mTvCompleteinfoClause;
    @Bind(R.id.ll_register_clause)
    LinearLayout mLlRegisterClause;
    @Bind(R.id.ll_register_tips)
    LinearLayout mLlRegisterTips;

    private RegisterPresenter mRegisterPresenter;
    private String mCurrentPhone;
    private String mCurrentVerifyCode;
    private Timer countDowntimer;
    private TimerTask countDownTask;

    public static Intent launchIntent(Context context, boolean autoStart) {
        Intent intent = new Intent(context, RegisterActivity.class);
        if (autoStart) {
            context.startActivity(intent);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_register);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        mRegisterPresenter = new RegisterPresenter();
        setupView(savedInstanceState);
        bindData();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mBtnRegisterSendsms.setOnClickListener(this);
        mBtnRegisterNext.setOnClickListener(this);
        mTvCompleteinfoClause.setOnClickListener(this);
        mTvCompleteinfoClause.getPaint().setFlags(mTvCompleteinfoClause.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //下划线

    }

    private void bindData() {
        mBtnRegisterNext.setText(R.string.abn_yueai_login);
        mLlRegisterTips.setVisibility(View.GONE);
        mLlRegisterClause.setVisibility(View.VISIBLE);
        setTitle(R.string.abn_yueai_login);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnRegisterNext) {
            verifyCode();
        } else if (view == mBtnRegisterSendsms) {
            //发送验证码
            // 先检查手机号的有效性
            mCurrentPhone = mEditRegisterPhone.getText().toString();
            if (!DeviceUtils.isMobileNO(mCurrentPhone)) {
                // Toast.makeText(this, "手机号码无效", Toast.LENGTH_SHORT).show();
                mEditRegisterPhone.requestFocus();
                mEditRegisterPhone.setError(getString(R.string.abn_yueai_resgiter_invalid_phone));
                return;
            }

            mRegisterPresenter.sendVerifyCode(mCurrentPhone, this);
        } else if (view == mTvCompleteinfoClause) {
            //点击服务协议
            UserAgreementActivity.launchIntent(this, Constant._sUrl_agreement);
        } else {
            super.onClick(view);
        }
    }

    private void verifyCode() {
        // 验证用户输入的验证码是否正确
        // 先检查手机号的有效性
        mCurrentPhone = mEditRegisterPhone.getText().toString();
        if (!DeviceUtils.isMobileNO(mCurrentPhone)) {
            // Toast.makeText(this, "手机号码无效", Toast.LENGTH_SHORT).show();
            mEditRegisterPhone.setError(getString(R.string.abn_yueai_resgiter_invalid_phone));
            mEditRegisterPhone.requestFocus();
            return;
        }

        mCurrentVerifyCode = mEditRegisterVerifycode.getText().toString();
        if (mCurrentVerifyCode.length() <= 0) {
            mEditRegisterVerifycode.setError(getString(R.string.abn_yueai_hint_verifycode));
            mEditRegisterVerifycode.requestFocus();
            return;
        } else {
            DialogUtil.showIndeternimateDialog(this, null).setCancelable(false);
            mRegisterPresenter.verifyCodeValidation(mCurrentPhone, mCurrentVerifyCode, this);
        }
    }

    private void restartCountDownTask() {
        if (countDownTask != null) {
            countDownTask.cancel();
        }
        countDownTask = new TimerTask() {
            long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        long deltaTime = System.currentTimeMillis() - startTime;
                        long latestTime = 40L - deltaTime / 1000;
                        if (latestTime <= 0) {
                            // 倒计时完了，取消倒计时并变回重新发送状态
                            mBtnRegisterSendsms.setEnabled(true);
                            mBtnRegisterSendsms
                                    .setText(R.string.abn_yueai_resend_verifycode);
                            countDownTask.cancel();
                            return;
                        }
                        mBtnRegisterSendsms
                                .setText(getString(R.string.abn_yueai_register_countdown, latestTime));

                    }
                });

            }
        };
        if (countDowntimer == null) {
            countDowntimer = new Timer();
        }
        countDowntimer.schedule(countDownTask, 0, 10);
    }

    @Override
    protected void onDestroy() {
        if (countDownTask != null) {
            countDownTask.cancel();
            countDownTask = null;
        }
        if (countDowntimer != null) {
            countDowntimer.cancel();
            countDowntimer = null;
        }
        super.onDestroy();

    }

    @Override
    public void onSendSmsFailed(Throwable e) {
        DialogUtil.showDialog(this, e.getMessage(), true);
    }

    @Override
    public void onSendSmsSuccess(String code) {
        DialogUtil.hideDialog(this);
        // 发送完了开始倒计时
        restartCountDownTask();
        mBtnRegisterSendsms.setEnabled(false);
        mBtnRegisterNext.setEnabled(true);
        mEditRegisterVerifycode.requestFocus();
    }

    @Override
    public void onSendSmsStart() {
        DialogUtil.showIndeternimateDialog(this, null).setCancelable(false);
    }

    @Override
    public void onVerifyCodeValidationStart() {
        DialogUtil.showIndeternimateDialog(this, null).setCancelable(false);
    }

    @Override
    public void onVerifyCodeValidationFailed(Throwable e) {
        showErrDialog(e);
    }

    @Override
    public void onVerifyCodeValidationSuccess(LoginRetEntity userEntities) {
        DialogUtil.hideDialog(this);
        //验证成功，进入到完善资料页
        Intent intent = new Intent(this, CompleteInfoActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_MOBILE, mCurrentPhone);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_VERIFY_CODE, mCurrentVerifyCode);
        startActivity(intent);
//        finish();
    }

    @Override
    public void onLoginSuccess(LoginRetEntity retEntity) {
        if (retEntity.user != null) {
            MobclickAgent.onProfileSignIn(retEntity.user.id);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
