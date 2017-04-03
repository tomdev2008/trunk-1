package com.aibinong.tantan.presenter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/20.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.ProfileService;

import java.util.ArrayList;

import rx.Subscriber;

public class ChatActivityPresenter extends PresenterBase {
    public interface IChatActivity {
        void onGetUserSuc(UserEntity userEntity);

        void onGetUserFailed(ResponseResult e);
    }

    private IChatActivity mIChatActivity;

    public ChatActivityPresenter(IChatActivity iChatActivity) {
        mIChatActivity = iChatActivity;
    }

    public void getUserInfo(String uuid) {
        addToCycle(
                ApiHelper.getInstance()
                        .create(ProfileService.class)
                        .users(uuid)
                        .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIChatActivity.onGetUserFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                        if (arrayListJsonRetEntity.getData().size() > 0) {
                                            mIChatActivity.onGetUserSuc(arrayListJsonRetEntity.getData().get(0));
                                        } else {
                                            throw new ResponseResult(-1, "获取用户资料失败");
                                        }
                                    }
                                }
                        )
        );
    }
}
