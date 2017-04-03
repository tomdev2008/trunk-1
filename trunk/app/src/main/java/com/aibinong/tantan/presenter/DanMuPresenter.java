package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/15.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.DanMusEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.services.BarrageService;
import com.fatalsignal.util.Log;

import rx.Subscriber;

public class DanMuPresenter extends PresenterBase {
    public interface IDanmuPresenter {
        void onDanmuLoadSuccess(DanMusEntity danMuEntities);
    }

    private IDanmuPresenter mIDanmuPresenter;

    public DanMuPresenter(IDanmuPresenter iDanmuPresenter) {
        mIDanmuPresenter = iDanmuPresenter;
    }

    public void loadDanMu() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(BarrageService.class)
                        .get_barrages(null)
                        .compose(ApiHelper.<JsonRetEntity<DanMusEntity>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<DanMusEntity>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("loadDanMu", "onError");
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<DanMusEntity> arrayListJsonRetEntity) {
                                        mIDanmuPresenter.onDanmuLoadSuccess(arrayListJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }
}
