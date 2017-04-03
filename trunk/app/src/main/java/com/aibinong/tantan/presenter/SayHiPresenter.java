package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/24.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.GreetEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscriber;

public class SayHiPresenter extends PresenterBase {
    //    private ISayHiPresenter mISayHiPresenter;
    private static SayHiPresenter _smSayHiPresenter = new SayHiPresenter();

    public static SayHiPresenter getInstance() {
        return _smSayHiPresenter;
    }

    private SayHiPresenter() {
//        mISayHiPresenter = iSayHiPresenter;
    }

    public void sayHiBatch(final ArrayList<UserEntity> userEntities) {
        //判断是否已经登录
        if (UserUtil.isLoginValid(true) != null) {
            StringBuilder sb = new StringBuilder();
            for (UserEntity userEntity : userEntities) {
                EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_SAYHI_START, userEntity));
                sb.append(userEntity.id).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            addToCycle(
                    ApiHelper
                            .getInstance()
                            .greet(sb.toString(), 1)
                            .subscribe(
                                    new Subscriber<JsonRetEntity<GreetEntity>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            for (UserEntity userEntity : userEntities) {
                                                onSayHiFailed(userEntity);
                                            }

                                        }

                                        @Override
                                        public void onNext(JsonRetEntity<GreetEntity> stringJsonRetEntity) {
                                            for (UserEntity userEntity : userEntities) {
                                                //其实就是给这个人发一条消息
                                                EMMessage emMessage = EMMessage.createTxtSendMessage(stringJsonRetEntity.getData().content, userEntity.id);
                                                emMessage.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SAY_HI);
                                                emMessage.setAttribute(EMessageConstant.KEY_EXT_id, stringJsonRetEntity.getData().id);
                                                //发送消息
                                                SayHiEMCallBack callBack = new SayHiEMCallBack(SayHiPresenter.this, userEntity);
                                                emMessage.setMessageStatusCallback(callBack);
                                                EMClient.getInstance().chatManager().sendMessage(emMessage);
                                            }

                                        }
                                    }
                            )
            );


        } else {
            //未登录
            EventBus.getDefault().post(new BaseEvent<ArrayList<UserEntity>>(BaseEvent.ACTION_ASK_FOR_LOGIN, userEntities));
        }

    }

    public void sayHi(final UserEntity userEntity) {
        //判断是否已经登录
        if (UserUtil.isLoginValid(true) != null) {
            //不能给vip

//            if (userEntity.isVIP()&& !UserUtil.hasSayHiToVipPer()) {
//                EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_ASK_FOR_BUY_VIP, userEntity));
//                return;
//            }

            EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_SAYHI_START, userEntity));
            addToCycle(
                    ApiHelper
                            .getInstance()
                            .greet(userEntity.id, 0)
                            .subscribe(
                                    new Subscriber<JsonRetEntity<GreetEntity>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            onSayHiFailed(userEntity);
                                        }

                                        @Override
                                        public void onNext(JsonRetEntity<GreetEntity> stringJsonRetEntity) {
                                            //其实就是给这个人发一条消息
                                            EMMessage emMessage = EMMessage.createTxtSendMessage(stringJsonRetEntity.getData().content, userEntity.id);
                                            emMessage.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SAY_HI);
                                            emMessage.setAttribute(EMessageConstant.KEY_EXT_id, stringJsonRetEntity.getData().id);
                                            //发送消息
                                            SayHiEMCallBack callBack = new SayHiEMCallBack(SayHiPresenter.this, userEntity);
                                            emMessage.setMessageStatusCallback(callBack);
                                            EMClient.getInstance().chatManager().sendMessage(emMessage);
                                        }
                                    }
                            )
            );


        } else {
            //未登录
            EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_ASK_FOR_LOGIN, userEntity));
        }


    }

    private void onSayHiSended(UserEntity userEntity) {
        //标记为已打招呼
//        SqlBriteUtil.getInstance().getUserDb().markSaiedHi(userEntity.id);
        SqlBriteUtil.getInstance().getUserDb().markNewSaiedHi(userEntity.id);
        EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_SAYHI_SUCCESS, userEntity));
    }
    public void onSayNoHiSended(UserEntity userEntity) {
        //标记为未打招呼
        SqlBriteUtil.getInstance().getUserDb().markNewNoSaiedHi(userEntity.id);
//        SqlBriteUtil.getInstance().getSayUserDb().markNoSaiedHi(userEntity.id);
    }
    private void onSayHiFailed(UserEntity userEntity) {
        EventBus.getDefault().post(new BaseEvent<UserEntity>(BaseEvent.ACTION_SAYHI_FAILED, userEntity));
    }

    static class SayHiEMCallBack implements EMCallBack {
        private WeakReference<SayHiPresenter> mIChatMsgListWeakReference;
        private WeakReference<UserEntity> mUserEntityWeakReference;

        public SayHiEMCallBack(SayHiPresenter presenter, UserEntity userEntity) {
            mIChatMsgListWeakReference = new WeakReference<SayHiPresenter>(presenter);
            mUserEntityWeakReference = new WeakReference<UserEntity>(userEntity);
        }

        @Override
        public void onSuccess() {
            UserEntity userEntity = mUserEntityWeakReference.get();
            SayHiPresenter presenter = mIChatMsgListWeakReference.get();
            if (userEntity != null && presenter != null) {
                presenter.onSayHiSended(userEntity);
            }


        }

        @Override
        public void onError(int i, String s) {
            UserEntity userEntity = mUserEntityWeakReference.get();
            SayHiPresenter presenter = mIChatMsgListWeakReference.get();
            if (userEntity != null && presenter != null) {
                presenter.onSayHiFailed(userEntity);
            }
        }

        @Override
        public void onProgress(int i, String s) {

        }
    }
}
