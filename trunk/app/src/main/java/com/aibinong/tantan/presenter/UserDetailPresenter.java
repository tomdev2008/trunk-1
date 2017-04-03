package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/6.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ReportEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.ChatService;
import com.aibinong.yueaiapi.services.user.ProfileService;

import java.util.ArrayList;

import rx.Subscriber;

public class UserDetailPresenter extends PresenterBase {
    public interface IuserDetail {
        void onGetGiftListSuccess(ArrayList<GiftEntity> giftEntities);

        void onGetGiftListFailed(ResponseResult e);

        void onReportSuccess();

        void onReportFailed(ResponseResult e);

        void onGetUserSuc(UserEntity userEntity);

        void onGetUserFailed(ResponseResult e);
    }

    private IuserDetail mIuserDetail;
    private String mUserId;

    public UserDetailPresenter(String userId, IuserDetail iuserDetail) {
        mIuserDetail = iuserDetail;
        mUserId = userId;
    }

    public void gift_record_list() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .gift_record_list(mUserId)
                        .compose(ApiHelper.<JsonRetEntity<ArrayList<GiftEntity>>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<ArrayList<GiftEntity>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIuserDetail.onGetGiftListFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ArrayList<GiftEntity>> arrayListJsonRetEntity) {
                                        mIuserDetail.onGetGiftListSuccess(arrayListJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }

    public void report(String id,ReportEntity reson) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .report(id,Integer.parseInt(reson.id))
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIuserDetail.onReportFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIuserDetail.onReportSuccess();
                                    }
                                }
                        )
        );
    }

    public void browse(String useId) {
        ApiHelper.getInstance().browse(useId).subscribe(new Subscriber<JsonRetEntity<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {

            }
        });
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
                                        mIuserDetail.onGetUserFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                        if (arrayListJsonRetEntity.getData().size() > 0) {
                                            mIuserDetail.onGetUserSuc(arrayListJsonRetEntity.getData().get(0));
                                        } else {
                                            throw new ResponseResult(-1, "获取用户资料失败");
                                        }
                                    }
                                }
                        )
        );
    }
}
