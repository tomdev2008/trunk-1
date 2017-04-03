package com.aibinong.tantan.presenter;


import android.util.Log;

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.ActivityListService;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by hubin on 2017/3/3.
 */

public class ActivityListPresenter extends PresenterBase {
    private final static String TAG = "ActivityListPresenter";
    private int mToPage;

    public interface IActivityList {
        void onReuireStatusFailed(ResponseResult e);

        void onRequireStatusSuccess(ActivityListEntity activityListEntitiy, Page page);
    }

    private IActivityList mIRequireList;


    public ActivityListPresenter(IActivityList mIRequireList) {
        this.mIRequireList = mIRequireList;
    }

    public void getActivityLists(boolean refresh) {
        if (refresh) {
            mToPage = 1;
        }
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ActivityListService.class)
                        .list(mToPage)
                        .compose(ApiHelper.<JsonRetEntity<ActivityListEntity>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<ActivityListEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIRequireList.onReuireStatusFailed(ResponseResult.fromThrowable(e));
                                Log.e(TAG, "onError: " + e.getMessage());
                            }

                            @Override
                            public void onNext(JsonRetEntity<ActivityListEntity> arrayListJsonRetEntity) {
                                mIRequireList.onRequireStatusSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                                mToPage++;

                            }
                        })
        );


    }
}