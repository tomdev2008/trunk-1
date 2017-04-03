package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.ui.activity.FansListActivity;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;

public class FansListPresenter extends PresenterBase {
    private int mToPage;
    private int mListType;

    public FansListPresenter(int listType) {
        mListType = listType;
    }

    public void funs_list(final ISearchList iSearchList, boolean refresh) {
        if (refresh) {
            mToPage = 1;
        }
        Subscription subscription = null;
        if (mListType == FansListActivity.LIST_TYPE_FANS_LIST) {
            subscription = ApiHelper
                    .getInstance()
                    .fans_list(mToPage)
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
                            // TODO: 16/12/26 加数据
//                            arrayListJsonRetEntity.setData(SqlBriteUtil.getInstance().getUserDb().getAllUsers());
                            iSearchList.onListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                            UserUtil.saveUsers(arrayListJsonRetEntity.getData());
                            mToPage++;
                        }
                    });
        } else {
            subscription = ApiHelper
                    .getInstance()
                    .browse_list(mToPage)
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
                            // TODO: 16/12/26 加数据
//                            arrayListJsonRetEntity.setData(SqlBriteUtil.getInstance().getUserDb().getAllUsers());
                            iSearchList.onListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                            UserUtil.saveUsers(arrayListJsonRetEntity.getData());
                            mToPage++;
                        }
                    });
        }
        addToCycle(subscription);
    }

}
