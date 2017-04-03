package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.pojo.LiuLianLocation;
import com.aibinong.tantan.util.LiuLianLocationUtil;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.apiInterface.ISearchFollow;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;

import java.util.ArrayList;

import rx.Subscriber;

public class RecommendFragPresenter extends PresenterBase {
    private int mToPage;

    public void select_list(boolean refresh, final ISearchList iSearchList) {
        if (refresh) {
            mToPage = 1;
        }
        LiuLianLocation location = LiuLianLocationUtil.getInstance().getLocationSync();
        String longitude = location.isValid() ? (location.longitude + "") : null;
        String latitude = location.isValid() ? (location.latitude + "") : null;
        int sex = ConfigUtil.getInstance().getTempSex();
        if (UserUtil.isLoginValid(false) != null) {
            sex = UserUtil.getSavedUserInfoNotNull().sex;
        }
        addToCycle(
                ApiHelper
                        .getInstance()
                        .select_list(longitude, latitude, sex, mToPage)
                        .subscribe(new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                iSearchList.onListStart();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                iSearchList.onListFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                iSearchList.onListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                                UserUtil.saveUsers(arrayListJsonRetEntity.getData());
                                if (arrayListJsonRetEntity.getData().size() > 0) {
                                    mToPage++;
                                } else {
                                    mToPage = 1;
                                }
                            }
                        })
        );
    }

    public void follow(String id, final ISearchFollow iSearchFollow) {
        addToCycle(
                ApiHelper.getInstance()
                        .follow(id)
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        iSearchFollow.onFollowStart();
                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        iSearchFollow.onFollowFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        iSearchFollow.onFollowSuccess(stringJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }

}
