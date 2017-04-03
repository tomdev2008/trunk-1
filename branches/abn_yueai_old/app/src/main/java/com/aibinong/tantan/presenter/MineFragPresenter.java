package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/7.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.ProfileService;
import com.aibinong.yueaiapi.utils.UserUtil;

import rx.Subscriber;

public class MineFragPresenter extends PresenterBase {
    public interface IMineFrag {
        void onGetMyInfoFailed(ResponseResult e);

        void onGetMyInfoSuccess(UserEntity userEntity);
    }

    private IMineFrag mIMineFrag;
    private BroadcastReceiver mPaySuccessReceiver;

    public MineFragPresenter(IMineFrag iMineFrag) {
        mIMineFrag = iMineFrag;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPaySuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getMyInfo();
            }
        };
        GlobalLocalBroadCastManager.getInstance().registerPaySuccess(mPaySuccessReceiver);
    }

    @Override
    public void onDestoryView() {
        super.onDestoryView();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mPaySuccessReceiver);
    }

    public void getMyInfo() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ProfileService.class)
                        .get_user(null)
                        .compose(ApiHelper.<JsonRetEntity<UserEntity>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<UserEntity>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIMineFrag.onGetMyInfoFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<UserEntity> userEntityJsonRetEntity) {
                                        UserEntity userEntity = userEntityJsonRetEntity.getData();
                                        if (userEntity != null) {
                                            UserUtil.saveUserInfo(userEntity);
                                        }
                                        mIMineFrag.onGetMyInfoSuccess(userEntity);
                                    }
                                }
                        )
        );
    }
}
