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
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.ChatService;
import com.aibinong.yueaiapi.services.SearchService;

import java.util.ArrayList;

import rx.Subscriber;

public class UserDetailPresenter extends PresenterBase {
    public interface IuserDetail {
        void onGetGiftListSuccess(ArrayList<GiftEntity> giftEntities);

        void onGetGiftListFailed(ResponseResult e);

        void onReportSuccess();

        void onReportFailed(ResponseResult e);
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
                        .create(SearchService.class)
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

    public void report(QuestionEntity.OptionsEntity reson) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .report(mUserId, reson.id)
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
}
