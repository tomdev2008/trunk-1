package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/23.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.user.ProfileService;

import rx.Subscriber;

public class FeedbackPresenter extends PresenterBase {
    public interface IFeedBack {
        void onSubmitFailed(ResponseResult e);

        void onSubmitSuccess(String result);
    }

    private IFeedBack mIFeedBack;

    public FeedbackPresenter(IFeedBack iFeedBack) {
        mIFeedBack = iFeedBack;
    }

    public void submit(String content) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ProfileService.class)
                        .feedback_submit(content)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIFeedBack.onSubmitFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIFeedBack.onSubmitSuccess(stringJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }
}
