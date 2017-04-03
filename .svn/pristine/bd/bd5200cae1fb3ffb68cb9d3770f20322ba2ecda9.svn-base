package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/29.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.CertStatusEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;

import rx.Subscriber;

public class CertPresenter extends PresenterBase {
    public interface IRequireCert {
        void onCertFailed(ResponseResult e);

        void onCertSuccess(int type, String account);

        void onCertStatusFailed(ResponseResult e);

        void onCertStatusSuccess(CertStatusEntity certStatusEntities);
    }

    private IRequireCert mIRequireCert;

    public CertPresenter(IRequireCert iRequireCert) {
        mIRequireCert = iRequireCert;
    }

    public void certStatus() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .certStatus()
                        .subscribe(
                                new Subscriber<JsonRetEntity<CertStatusEntity>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIRequireCert.onCertStatusFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<CertStatusEntity> arrayListJsonRetEntity) {
                                        mIRequireCert.onCertStatusSuccess(arrayListJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }

    public void cert(final int type, final String account) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .cert(account, type)
                        .subscribe(new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIRequireCert.onCertFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                mIRequireCert.onCertSuccess(type, account);
                            }
                        })
        );
    }
}
