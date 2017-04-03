package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.util.Log;

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.EverdayRecommendEntitiy;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.ActivityListService;
import com.aibinong.yueaiapi.services.SysService;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import rx.Subscriber;

public class EveryDayRecommendPresenter extends PresenterBase {
    public interface IEverydayPresenter {
        void requestSuccess(ArrayList<UserEntity> userEntities);

        void requestFailed(Throwable e);
    }

    private IEverydayPresenter mIEverydayPresenter;

    public EveryDayRecommendPresenter(IEverydayPresenter iEverydayPresenter) {
        this.mIEverydayPresenter = iEverydayPresenter;
    }

    public void everydayRecommend() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(SysService.class)
                        .everydayrecommend(null)
                        .compose(ApiHelper.<JsonRetEntity<EverdayRecommendEntitiy>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<EverdayRecommendEntitiy>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                com.fatalsignal.util.Log.e(e.getMessage());
                                mIEverydayPresenter.requestFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<EverdayRecommendEntitiy> arrayListJsonRetEntity) {
                                mIEverydayPresenter.requestSuccess((ArrayList<UserEntity>) arrayListJsonRetEntity.getData().list);
                            }
                        })
        );
    }
}
