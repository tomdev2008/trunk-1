package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.pojo.LiuLianLocation;
import com.aibinong.tantan.util.LiuLianLocationUtil;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;

import java.util.ArrayList;

import rx.Subscriber;

public class ImgPlazaPresenter extends PresenterBase {
    ISearchList mISearchList;
    private int mListToPage = 1;

    public ImgPlazaPresenter(ISearchList iSearchList) {
        mISearchList = iSearchList;
    }

    public void list(boolean refresh) {
        if (refresh) {
            mListToPage = 1;
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
                        .searchList(longitude, latitude, sex, mListToPage)
                        .subscribe(new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mISearchList.onListStart();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mISearchList.onListFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<ArrayList<UserEntity>> userEntities) {
                                mISearchList.onListSuccess(userEntities.getData(), userEntities.getPage());
                                UserUtil.saveUsers(userEntities.getData());
                                mListToPage++;
                            }
                        })
        );
    }

}
