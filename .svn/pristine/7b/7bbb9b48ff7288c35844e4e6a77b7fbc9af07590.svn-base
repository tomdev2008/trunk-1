package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/16.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.util.List;

import rx.Subscriber;

public class SelectQuestionPresenter extends PresenterBase {
    public interface ISelectQuestion {
        void onQuestionLoaded(List<QuestionEntity> questionEntities);

        void onQuestionLoadFailed(ResponseResult e);
    }

    private ISelectQuestion mISelectQuestion;

    public SelectQuestionPresenter(ISelectQuestion iSelectQuestion) {
        mISelectQuestion = iSelectQuestion;
    }

    public void loadQuestion() {
        addToCycle(
                ConfigUtil.getInstance().getOrRequireConfig().subscribe(
                        new Subscriber<ConfigEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mISelectQuestion.onQuestionLoadFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(ConfigEntity configEntity) {
                                mISelectQuestion.onQuestionLoaded(configEntity.questions);
                            }
                        }
                )
        );
    }
}
