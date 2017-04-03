package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/28.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.tantan.ui.activity.SplashActivity;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;

public class FollowPresenter {
    private static FollowPresenter _smFollowPresenter = new FollowPresenter();

    public static FollowPresenter getInstance() {
        return _smFollowPresenter;
    }

    public void follow(final UserEntity userEntity) {
        if (UserUtil.isLoginValid(true) == null) {
            //未登录
            EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_ASK_FOR_LOGIN, userEntity));
        } else {
            ApiHelper.getInstance()
                    .follow(userEntity.id)
                    .subscribe(
                            new Subscriber<JsonRetEntity<String>>() {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_FOLLOW_START, userEntity));
                                }

                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_FOLLOW_FAILED, userEntity));
                                }

                                @Override
                                public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                    //通知关注界面刷新
//                                    GlobalLocalBroadCastManager.getInstance()
//                                            .onFllowActivityChange(1);
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_FOLLOW_SUCCESS, userEntity));
                                }
                            }
                    );
        }
    }

    public void unfollow(final UserEntity userEntity) {
        if (UserUtil.isLoginValid(true) == null) {
            //未登录
            EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_ASK_FOR_LOGIN, userEntity));
        } else {
            ApiHelper.getInstance()
                    .unfollow(userEntity.id)
                    .subscribe(
                            new Subscriber<JsonRetEntity<String>>() {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_UNFOLLOW_START, userEntity));
                                }

                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_UNFOLLOW_FAILED, userEntity));
                                }

                                @Override
                                public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                    //通知关注界面刷新
//                                    GlobalLocalBroadCastManager.getInstance()
//                                            .onFllowActivityChange(0);
                                    EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_UNFOLLOW_SUCCESS, userEntity));
                                }
                            }
                    );
        }
    }
}
