package com.aibinong.tantan.presenter;


import android.util.Log;

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.VersionEntity;
import com.aibinong.yueaiapi.services.ActivityListService;
import com.aibinong.yueaiapi.services.VersionService;

import rx.Subscriber;

/**
 * Created by hubin on 2017/3/3.
 */

public class VersionUpdatePresenter extends PresenterBase {
    private final static String TAG = "VersionUpdatePresenter";

    public interface IVerson {
        void onReuireStatusFailed(ResponseResult e);

        void onRequireStatusSuccess(VersionEntity versionEntity);
    }

    private IVerson mIVerson;


    public VersionUpdatePresenter(IVerson mIRequireList) {
        this.mIVerson = mIRequireList;
    }

    public void getVersion() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(VersionService.class)
               . get_version(null)
                        .compose(ApiHelper.<JsonRetEntity<VersionEntity>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<VersionEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIVerson.onReuireStatusFailed(ResponseResult.fromThrowable(e));
                                Log.e(TAG, "onError: " + e.getMessage());
                            }

                            @Override
                            public void onNext(JsonRetEntity<VersionEntity> arrayListJsonRetEntity) {
                                mIVerson.onRequireStatusSuccess(arrayListJsonRetEntity.getData());

                            }
                        })
        );


    }
}