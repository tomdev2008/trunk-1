package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.TagsEntity;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import rx.Subscriber;

public class CompleteInfoPresenter extends PresenterBase {
    public interface ICompleteInfoPresenter {
        void onRegisterFailed(Throwable e);

        void onREgisterSuccess(LoginRetEntity ret);

        void onGetTagFailed(Throwable e);

        void onGetTagSuccess(TagsEntity tags);
    }

    private ICompleteInfoPresenter mICompleteInfoPresenter;

    public CompleteInfoPresenter(ICompleteInfoPresenter iCompleteInfoPresenter) {
        this.mICompleteInfoPresenter = iCompleteInfoPresenter;
    }

    public void getTags() {
        addToCycle(
                ConfigUtil
                        .getInstance()
                        .getOrRequireConfig()
                        .subscribe(new Subscriber<ConfigEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mICompleteInfoPresenter.onGetTagFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(ConfigEntity configEntity) {
                                mICompleteInfoPresenter.onGetTagSuccess(configEntity.tags);
                            }
                        })
        );
    }

    public void register(ArrayList<String> portrait, String nickname, String birthdate, int sex, String occupation, ArrayList<String> tagsArr, String mobile) {
        StringBuilder pictureBuilder = new StringBuilder();
        for (String url : portrait) {
            pictureBuilder.append(url).append(',');
        }
        pictureBuilder.deleteCharAt(pictureBuilder.length() - 1);

        StringBuilder tagsArrBuilder = new StringBuilder();
        for (String tag : tagsArr) {
            tagsArrBuilder.append(tag).append(',');
        }
        tagsArrBuilder.deleteCharAt(tagsArrBuilder.length() - 1);

        addToCycle(ApiHelper.getInstance()
                .create(LoginService.class)
                .register(pictureBuilder.toString(), nickname, birthdate, sex, occupation, tagsArrBuilder.toString(), mobile)
                .compose(ApiHelper.<JsonRetEntity<LoginRetEntity>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<LoginRetEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mICompleteInfoPresenter.onRegisterFailed(ResponseResult.fromThrowable(e));

                    }

                    @Override
                    public void onNext(JsonRetEntity<LoginRetEntity> loginRetEntityJsonRetEntity) {
                        //这里就登陆成功了，只是还没有完善个人资料
                        LoginRetEntity loginRetEntity = loginRetEntityJsonRetEntity.getData();
                        UserUtil.saveUserInfo(loginRetEntity.user);
                        Log.e("============hehe"+loginRetEntity.user.toString());
                        //表示是已注册用户登录成功
                        UserUtil.saveAccessToken(loginRetEntity.accessToken);
                        UserUtil.saveHuanxinPwd(loginRetEntity.user.password);
                        UserUtil.saveUUID(loginRetEntity.user.id);

                        SqlBriteUtil.getInstance().getUserDb().saveUser(loginRetEntity.user);

                        if (UserUtil.isLoginValid(true) != null) {
                            UserUtil.bindPushId();
                            // 通知刷新
                            GlobalLocalBroadCastManager.getInstance()
                                    .onLoginStatusChange();
                            mICompleteInfoPresenter.onREgisterSuccess(loginRetEntity);
                            EventBus.getDefault().postSticky(loginRetEntityJsonRetEntity.getData());
                        } else {
                            throw new ResponseResult(-1, "验证失败，请重试");
                        }
                    }
                })
        );
    }
}
