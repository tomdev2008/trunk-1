package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/2.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.apiInterface.ISysSendVerifyCode;
import com.aibinong.yueaiapi.apiInterface.IVerifyCodeValidation;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.SysService;
import com.aibinong.yueaiapi.utils.UserUtil;

import rx.Subscriber;

public class RegisterPresenter extends PresenterBase {

    public void sendVerifyCode(String mobile, final ISysSendVerifyCode iSysSendVerifyCode) {
        addToCycle(ApiHelper.getInstance().create(SysService.class).send_phone_verify_code(mobile)
                .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                .subscribe(
                        new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                iSysSendVerifyCode.onSendSmsStart();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                iSysSendVerifyCode.onSendSmsFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                iSysSendVerifyCode.onSendSmsSuccess(stringJsonRetEntity.getData());
                            }
                        }
                ));
    }

    public void verifyCodeValidation(final String mobile, final String verifyCode, final IVerifyCodeValidation verifyCodeValidation) {
        addToCycle(ApiHelper.getInstance()
                .verifyCodeValidation(mobile, verifyCode)
                .subscribe(
                        new Subscriber<LoginRetEntity>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                verifyCodeValidation.onVerifyCodeValidationStart();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                verifyCodeValidation.onVerifyCodeValidationFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(LoginRetEntity loginRetEntity) {
                                if (loginRetEntity.status == 1) {
                                    //表示是已注册用户登录成功
                                    UserUtil.saveAccessToken(loginRetEntity.accessToken);
                                    UserUtil.saveHuanxinPwd(loginRetEntity.user.password);
                                    UserUtil.saveUUID(loginRetEntity.user.id);
                                    UserUtil.saveUserInfo(loginRetEntity.user);

                                    SqlBriteUtil.getInstance().getUserDb().saveUser(loginRetEntity.user);

                                    if (UserUtil.isLoginValid(true) != null) {
                                        UserUtil.bindPushId();
                                        // 通知刷新
                                        GlobalLocalBroadCastManager.getInstance()
                                                .onLoginStatusChange();
                                        verifyCodeValidation.onLoginSuccess(loginRetEntity);


                                    } else {
                                        throw new ResponseResult(-1, "验证失败，请重试");
                                    }
                                } else {
                                    verifyCodeValidation.onVerifyCodeValidationSuccess(loginRetEntity);
                                }
                            }

                        }
                )
        );
    }
}
