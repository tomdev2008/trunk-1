package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/24.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.user.ProfileService;

import rx.Subscriber;

public class PrivacySettingPresenter extends PresenterBase {
    public interface IPrivacySetting {
        void onSwitchBlurSuccess(boolean blur);

        void onSwitchBlurFailed(ResponseResult e);

        void onSetMsgLevelSuccess(int level);

        void onSetMsgLevelFailed(ResponseResult e);

    }

    private IPrivacySetting mIPrivacySetting;

    public PrivacySettingPresenter(IPrivacySetting iPrivacySetting) {
        mIPrivacySetting = iPrivacySetting;
    }

    public void switchBlur(final boolean blur) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ProfileService.class)
                        .update_privacy(blur ? "1" : "0", null)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIPrivacySetting.onSwitchBlurFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                mIPrivacySetting.onSwitchBlurSuccess(blur);
                            }
                        })
        );
    }

    public void setMsgLevel(final int level) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ProfileService.class)
                        .update_privacy(null, level + "")
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIPrivacySetting.onSetMsgLevelFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                mIPrivacySetting.onSetMsgLevelSuccess(level);
                            }
                        })
        );
    }
}
