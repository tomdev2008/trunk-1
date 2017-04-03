package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/10.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;

public class WhoLikeMePresenter extends PresenterBase {

    public interface IWhoLikeMe {
        void onGetFunsFailed(ResponseResult e);

        void onGetFunsSuccess(ArrayList<UserEntity> userEntities, Page page);

        void onGetUserInfoSuccess(UserEntity userEntity);
    }

    private int mToPage;



    public void getUserInfo(String uuid) {
//        addToCycle(
//                ApiHelper
//                        .getInstance()
//                        .create(ProfileService.class)
//                        .get_users(uuid)
//                        .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
//                        .subscribe(
//                                new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
//                                    @Override
//                                    public void onCompleted() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                    }
//
//                                    @Override
//                                    public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
//                                        if (arrayListJsonRetEntity.getData() != null && arrayListJsonRetEntity.getData().size() > 0) {
//                                            mIWhoLikeMe.onGetUserInfoSuccess(arrayListJsonRetEntity.getData().get(0));
//                                        }
//                                        for (UserEntity userEntity : arrayListJsonRetEntity.getData()) {
//                                            SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
//                                        }
//                                    }
//                                }
//                        )
//        );
    }

    public void getFuns(boolean refresh) {
//        if (refresh) {
//            mToPage = 1;
//        }
//        addToCycle(
//                ApiHelper
//                        .getInstance()
//                        .create(ProfileService.class)
//                        .get_fans(mToPage)
//                        .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
//                        .subscribe(new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                mIWhoLikeMe.onGetFunsFailed(ResponseResult.fromThrowable(e));
//                            }
//
//                            @Override
//                            public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
//                                mIWhoLikeMe.onGetFunsSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
//                                mToPage++;
//                                for (UserEntity userEntity : arrayListJsonRetEntity.getData()) {
//                                    SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
//                                }
//                            }
//                        })
//        );
    }
}
