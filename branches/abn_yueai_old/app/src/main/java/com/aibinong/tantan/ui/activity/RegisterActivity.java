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
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends ActivityBase implements RegisterPresenter.IRegPresenter {
    public static final String INTENT_ACTION_LOGIN = "com.aibinong.yueai.activity.login";
    public static final String INTENT_ACTION_REGISTER = "com.aibinong.yueai.activity.register";
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
    private boolean mForLogin;//是登陆还是注册

    public static Intent startLoginActivity(Context context, boolean autoStart) {
        Intent intent = new Intent(RegisterActivity.INTENT_ACTION_LOGIN);
        intent.setPackage(context.getPackageName());
        if (autoStart) {
            context.startActivity(intent);
        }
        return intent;
    }

    public static Intent startRegisterActivity(Context context) {
        Intent intent = new Intent(RegisterActivity.INTENT_ACTION_REGISTER);
        intent.setPackage(context.getPackageName());
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_register);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        mRegisterPresenter = new RegisterPresenter(this);
        setupView(savedInstanceState);
        mForLogin = INTENT_ACTION_LOGIN.equals(getIntent().getAction());
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
        if (mForLogin) {
            mBtnRegisterNext.setText(R.string.abn_yueai_login);
            mLlRegisterTips.setVisibility(View.VISIBLE);
            mLlRegisterClause.setVisibility(View.GONE);
            setTitle(R.string.abn_yueai_login);
        } else {
            setTitle(R.string.abn_yueai_register);
            mBtnRegisterNext.setText(R.string.abn_yueai_register_next);
            mLlRegisterTips.setVisibility(View.GONE);
            mLlRegisterClause.setVisibility(View.VISIBLE);
        }
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
            DialogUtil.showIndeternimateDialog(this, null).setCancelable(false);
            mRegisterPresenter.sendVerifyCode(mCurrentPhone);
        } else if (view == mTvCompleteinfoClause) {
            //点击服务协议
            CommonWebActivity.launchIntent(this, Constant._sUrl_agreement);
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
            if (mForLogin) {
                mRegisterPresenter.loginByPhone(mCurrentPhone, mCurrentVerifyCode);
            } else {
                mRegisterPresenter.verifyCodeValidation(mCurrentPhone, mCurrentVerifyCode);
            }
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
    public void onVerifyCodeFailed(Throwable e) {
        DialogUtil.showDialog(this, e.getMessage(), true);
    }

    @Override
    public void onVerifyCodeSuccess(LoginRetEntity ret) {
        DialogUtil.hideDialog(this);
        if (ret.user != null) {
            MobclickAgent.onProfileSignIn(ret.user.id);
        }
        //验证成功，进入到完善资料页
        Intent intent = new Intent(this, CompleteInfoActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_MOBILE, mCurrentPhone);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_VERIFY_CODE, mCurrentVerifyCode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(Throwable e) {
        DialogUtil.showDialog(this, e.getMessage(), true);
    }

    @Override
    public void onLoginSuccess(LoginRetEntity retEntity) {
        if (retEntity.user != null) {
            MobclickAgent.onProfileSignIn(retEntity.user.id);
        }
        //判断是否登陆，没登录就去登录首页，否则看是否需要完善资料
        UserEntity userEntity = UserUtil.getSavedUserInfo();
        if (userEntity != null && userEntity.status == 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            startActivity(FirstPageActivity.class);
            startActivity(CompleteInfoActivity.class);
        }
        finish();
    }
}
