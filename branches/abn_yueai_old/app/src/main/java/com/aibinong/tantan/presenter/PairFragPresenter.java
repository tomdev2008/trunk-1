package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.pojo.LiuLianLocation;
import com.aibinong.tantan.util.LiuLianLocationUtil;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.MainUserListEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class PairFragPresenter extends PresenterBase {
    public interface IPairFragPresenter {
        void onListStart();

        void onListFailed(Throwable e);

        void onListSuccess(ArrayList<UserEntity> userEntities);

        void onLikeFailed(Throwable e);

        void onLikeSuccess();

        void onGetMembersSuccess(List<MemberEntity> members);

    }

    private IPairFragPresenter mIPairFragPresenter;

    public PairFragPresenter(IPairFragPresenter iPairFragPresenter) {
        this.mIPairFragPresenter = iPairFragPresenter;
    }

    public void list() {
        mIPairFragPresenter.onListStart();

        LiuLianLocation location = LiuLianLocationUtil.getInstance().getLocationSync();
        addToCycle(
                ApiHelper.getInstance()
                        .create(SearchService.class)
                        .list(
                                location.isValid() ? (location.longitude + "") : null,
                                location.isValid() ? (location.latitude + "") : null,
                                String.format("%d-%d", ConfigUtil.getInstance().getPairMinAge(), ConfigUtil.getInstance().getPairMaxAge()),
                                ConfigUtil.getInstance().getPairMaxDistance()
                        )
                        .compose(ApiHelper.<JsonRetEntity<MainUserListEntity>>doIoObserveMain())
                        .debounce(1000, TimeUnit.MILLISECONDS)
                        .map(new Func1<JsonRetEntity<MainUserListEntity>, ArrayList<UserEntity>>() {
                            @Override
                            public ArrayList<UserEntity> call(JsonRetEntity<MainUserListEntity> arrayListJsonRetEntity) {
                                int times = arrayListJsonRetEntity.getData().times;
                                ConfigUtil.getInstance().saveLikeTimesRemain(times);
                                return arrayListJsonRetEntity.getData().list;
                            }
                        })
                        .subscribe(new Subscriber<ArrayList<UserEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIPairFragPresenter.onListFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(ArrayList<UserEntity> userEntities) {
                                mIPairFragPresenter.onListSuccess(userEntities);
                                saveUsers(userEntities);
                            }
                        })
        );
    }

    private void saveUsers(final ArrayList<UserEntity> userEntities) {
        if (userEntities == null || userEntities.size() <= 0) {
            return;
        }
        addToCycle(
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        for (UserEntity userEntity : userEntities) {
                            SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
                        }
                    }
                }).compose(ApiHelper.doIoObserveMain()).subscribe(
                        new Subscriber<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        }
                )
        );

    }

    public void like(UserEntity userEntity) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(SearchService.class)
                        .like(userEntity.id, SearchService.likeSource_main)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIPairFragPresenter.onListFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIPairFragPresenter.onLikeSuccess();
                                    }
                                }
                        )
        );
    }

    public void getMembers() {
        addToCycle(
                ConfigUtil.getInstance().getOrRequireConfig().map(new Func1<ConfigEntity, List<MemberEntity>>() {
                    @Override
                    public List<MemberEntity> call(ConfigEntity configEntity) {
                        return configEntity.members;
                    }
                })
                        .subscribe(
                                new Subscriber<List<MemberEntity>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<MemberEntity> memberEntities) {
                                        mIPairFragPresenter.onGetMembersSuccess(memberEntities);
                                    }
                                }
                        )
        );
    }

    public void userActive() {
        LiuLianLocation location = LiuLianLocationUtil.getInstance().getLocationSync();
        addToCycle(
                ApiHelper.getInstance()
                        .create(LoginService.class)
                        .user_active(
                                location.isValid() ? (location.longitude + "") : null,
                                location.isValid() ? (location.latitude + "") : null
                        )
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .debounce(1000, TimeUnit.MILLISECONDS)
                        .subscribe(new Subscriber<JsonRetEntity<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(JsonRetEntity<String> stringJsonRetEntity) {

                            }
                        })
        );
    }
}
