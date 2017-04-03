package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/30.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.PushMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class SysMessagePresenter extends PresenterBase {
    private int mRefCount = 0;
    private Subscription mSubscription;
    private static SysMessagePresenter _smSysMessagePresenter = new SysMessagePresenter();

    public static SysMessagePresenter getInstance() {
        return _smSysMessagePresenter;
    }

    public void onResume() {
        mRefCount++;
        if (mRefCount > 0) {
            startLoadSysMessage();
        }
    }

    public void onPause() {
        mRefCount--;
        if (mRefCount <= 0) {
            if (mSubscription != null) {
                mSubscription.unsubscribe();
                mSubscription = null;
            }
        }
    }

    public boolean processGeTuiMsg(PushMessage msg) {
        if (mRefCount <= 0) {
            return false;
        }
        EventBus.getDefault().post(new BaseEvent<PushMessage>(BaseEvent.ACTION_SYS_MESSAGE, msg));
        return true;
    }

    public void startLoadSysMessage() {
        if (mSubscription == null) {
            mSubscription = Observable.interval(6, TimeUnit.SECONDS).subscribe(
                    new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            startLoadSysMessage();
                        }

                        @Override
                        public void onNext(Long aLong) {
                            loadSysMessage();
                        }
                    }
            );
        }
    }

    private void loadSysMessage() {
        ApiHelper.getInstance()
                .sysMessage()
                .subscribe(
                        new Subscriber<JsonRetEntity<ArrayList<PushMessage>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(JsonRetEntity<ArrayList<PushMessage>> arrayListJsonRetEntity) {
                                if (arrayListJsonRetEntity != null && arrayListJsonRetEntity.getData() != null && arrayListJsonRetEntity.getData().size() > 0) {
                                    EventBus.getDefault().post(new BaseEvent<PushMessage>(BaseEvent.ACTION_SYS_MESSAGE, arrayListJsonRetEntity.getData().get(0)));
                                }
                            }
                        }
                );
    }
}
