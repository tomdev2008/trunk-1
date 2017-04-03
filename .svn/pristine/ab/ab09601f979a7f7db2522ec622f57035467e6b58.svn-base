package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/2.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.SysService;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.aibinong.yueaiapi.utils.UserUtil;

import rx.Subscriber;

public class RegisterPresenter extends PresenterBase {
    public interface IRegPresenter {
        void onSendSmsFailed(Throwable e);

        void onSendSmsSuccess(String code);

        void onVerifyCodeFailed(Throwable e);

        void onVerifyCodeSuccess(LoginRetEntity ret);

        void onLoginFailed(Throwable e);

        void onLoginSuccess(LoginRetEntity retEntity);
    }

    private IRegPresenter mIRegPresenter;

    public RegisterPresenter(IRegPresenter iRegPresenter) {
        mIRegPresenter = iRegPresenter;
    }

    public void sendVerifyCode(String mobile) {
        addToCycle(ApiHelper.getInstance().create(SysService.class).send_phone_verify_code(mobile)
                .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                .subscribe(
                        new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIRegPresenter.onSendSmsFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                mIRegPresenter.onSendSmsSuccess(stringJsonRetEntity.getData());
                            }
                        }
                ));
    }

    public void verifyCodeValidation(String mobile, String verifyCode) {
        addToCycle(ApiHelper.getInstance()
                .create(LoginService.class)
                .verify_code_validation(mobile, verifyCode)
                .compose(ApiHelper.<JsonRetEntity<LoginRetEntity>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<LoginRetEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIRegPresenter.onVerifyCodeFailed(ResponseResult.fromThrowable(e));
                    }

                    @Override
                    public void onNext(JsonRetEntity<LoginRetEntity> loginRet) {
                        //这里就登陆成功了，只是还没有完善个人资料
                        LoginRetEntity loginRetEntity = loginRet.getData();
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
                            mIRegPresenter.onVerifyCodeSuccess(loginRetEntity);
                        } else {
                            throw new ResponseResult(-1, "验证失败，请重试");
                        }
                    }
                })
        );
    }

    public void loginByPhone(String mobile, String verifyCode) {
        addToCycle(ApiHelper.getInstance()
                .create(LoginService.class)
                .login_by_mobile(mobile, verifyCode)
                .compose(ApiHelper.<JsonRetEntity<LoginRetEntity>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<LoginRetEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIRegPresenter.onLoginFailed(ResponseResult.fromThrowable(e));
                    }

                    @Override
                    public void onNext(JsonRetEntity<LoginRetEntity> loginRet) {
                        //这里就登陆成功了，只是还没有完善个人资料
                        LoginRetEntity loginRetEntity = loginRet.getData();
                        UserUtil.saveAccessToken(loginRetEntity.accessToken);
                        UserUtil.saveHuanxinPwd(loginRetEntity.user.password);
                        UserUtil.saveUUID(loginRetEntity.user.id);
                        UserUtil.saveUserInfo(loginRetEntity.user);
//                        UserUtil.bindPushId();

                        SqlBriteUtil.getInstance().getUserDb().saveUser(loginRetEntity.user);

                        if (UserUtil.isLoginValid(true) != null) {
                            UserUtil.bindPushId();
                            // 通知刷新
                            GlobalLocalBroadCastManager.getInstance()
                                    .onLoginStatusChange();
                            mIRegPresenter.onLoginSuccess(loginRetEntity);
                        } else {
                            throw new ResponseResult(-1, "验证失败，请重试");
                        }

                    }
                })
        );
    }
}
