package com.aibinong.tantan.util.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/7.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aibinong.tantan.BuildConfig;
import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.pojo.chat.EaseEmojiconGroupEntity;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.tantan.ui.activity.message.ChatActivity;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.ProfileService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class EMChatHelper {
    private static EMChatHelper mEMChatHelper = new EMChatHelper();
    private ArrayList<WeakReference<Subscriber<ResponseResult>>> subscriberWeakReferenceList;
    private ArrayList<Subscriber<ResponseResult>> subscriberList;
    private EaseUI easeUI;
    private Context mContext;

    private EMChatHelper() {
        subscriberWeakReferenceList = new ArrayList<>();
        subscriberList = new ArrayList<>();

    }

    public static EMChatHelper getInstance() {
        return mEMChatHelper;
    }

    public void init(Context context) {
        mContext = context;
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
//            options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);

        easeUI = EaseUI.getInstance();
        //set options
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;// TODO: 16/11/15 推送设置
            }
        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });
        //推送通知
        registerMessageListener();

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(
                new EMConnectionListener() {
                    @Override
                    public void onConnected() {

                    }

                    @Override
                    public void onDisconnected(int error) {
                        if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号已经被移除
                            // 显示帐号在其他设备登录
                            // 登出
                            UserUtil.logout().subscribe(new Subscriber<JsonRetEntity<String>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(JsonRetEntity<String> stringJsonRetEntity) {

                                }
                            });
                            UserUtil.clearLoginInfo();
                            GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().sendBroadcast(new Intent(BroadCastConst.BROADCAST_OPEN_LOGIN));
                        } else {

                        }
                    }
                }

        );

    }

    protected void registerMessageListener() {
        EMMessageListener messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    GlobalLocalBroadCastManager.getInstance().notifyMessegeReceived(message);
                    boolean beenReceived = GlobalLocalBroadCastManager.getInstance().notifyMessegeReceivedSingle(message);
                    if (!beenReceived) {
                        //收到消息，如果有本地广播接收器接收了，就不管了，否则显示通知
                        showNotify(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    if (message.getIntAttribute(EMessageConstant.CMD_EXT_TYPE, 0) == EMessageConstant.CMD_EXT_TYPE_REVOCATION_MSG) {
                        //撤回消息
                        String targetId = null;
                        try {
                            targetId = message.getStringAttribute(EMessageConstant.KEY_CMD_EXT_targetId);
                            EMMessage msg2Revoke = EMClient.getInstance().chatManager().getMessage(targetId);
                            if (msg2Revoke != null) {
                                EMClient.getInstance().chatManager().getConversation(message.getFrom()).removeMessage(targetId);
                                //插入一条系统消息
                                EMMessage mHighLightMsg = EMMessage.createTxtSendMessage("对方撤回了一条消息", msg2Revoke.getFrom());
                                mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
                                mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
                                mHighLightMsg.setMsgTime(msg2Revoke.getMsgTime());
                                mHighLightMsg.setMsgId(msg2Revoke.getMsgId());
                                EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
                                //通知撤销消息
                                GlobalLocalBroadCastManager.getInstance().notifyMessegeReceived(message);
                                boolean beenReceived = GlobalLocalBroadCastManager.getInstance().notifyMessegeReceivedSingle(message);
                                if (!beenReceived) {
                                    //收到消息，如果有本地广播接收器接收了，就不管了，否则显示通知
                                    showNotify(message);
                                }
                            }

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    } else if (message.getIntAttribute(EMessageConstant.CMD_EXT_TYPE, 0) == EMessageConstant.CMD_EXT_TYPE_CANCEL_PAIR) {
                        //通知撤销消息
                        GlobalLocalBroadCastManager.getInstance().notifyMessegeReceived(message);
                        boolean beenReceived = GlobalLocalBroadCastManager.getInstance().notifyMessegeReceivedSingle(message);
                        /*if (!beenReceived) {
                            //收到消息，如果有本地广播接收器接收了，就不管了，否则显示通知
                            showNotify(message);
                        }*/
                    }
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private void showNotify(final EMMessage message) {
        if (!ConfigUtil.getInstance().getShowNotify()) {
            return;
        }
        /*获取发送人的信息*/
        final UserEntity mFromUserEntity = SqlBriteUtil.getInstance().getUserDb().getUsers(message.getFrom());
        if (mFromUserEntity != null) {
            showNotifyWithUserInfo(message, mFromUserEntity, true);
        } else {
            ApiHelper
                    .getInstance()
                    .create(ProfileService.class)
                    .get_users(message.getFrom())
                    .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
                    .subscribe(
                            new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                    if (arrayListJsonRetEntity.getData() != null && arrayListJsonRetEntity.getData().size() > 0) {
                                        showNotifyWithUserInfo(message, arrayListJsonRetEntity.getData().get(0), true);
                                    }
                                    for (UserEntity userEntity : arrayListJsonRetEntity.getData()) {
                                        SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
                                    }
                                }
                            }
                    );
        }


    }

    private void showNotifyWithUserInfo(final EMMessage message, final UserEntity userEntity, boolean useFullScreen) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
//        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.icon_push_small);
        builder.setDefaults(Notification.DEFAULT_ALL);

        String msgContent = mContext.getString(R.string.notify_received_msg);
        if (ConfigUtil.getInstance().getShowNotifyDetail()) {
            msgContent = EMChatMsgHelper.getMessageDigest(message, mContext);
            msgContent = EaseSmileUtils.getSmiledTextNoImg(mContext, msgContent);
        }
        builder.setTicker(msgContent);
        int unreadedCount = EMClient.getInstance().chatManager().getConversation(message.getFrom()).getUnreadMsgCount();
        if (unreadedCount > 1) {
            String fmtedContent = String.format("[%d条]%s", unreadedCount, msgContent);
            builder.setContentText(fmtedContent);
        } else {
            builder.setContentText(msgContent);
        }
        builder.setContentTitle(userEntity.nickname);
        try {
            File avatarFile = Glide.with(mContext).load(userEntity.getFirstPicture()).downloadOnly(48, 48).get();
            builder.setLargeIcon(BitmapFactory.decodeFile(avatarFile.getAbsolutePath()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Intent chatIntent = ChatActivity.launchIntent(mContext, message.getFrom(), userEntity, false);
        PendingIntent chatPi = PendingIntent.getActivity(mContext, 0, chatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(chatPi);
        builder.setPriority(Notification.PRIORITY_HIGH);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useFullScreen) {
            builder.setFullScreenIntent(chatPi, false);
            if (useFullScreen) {
                Observable
                        .create(new Observable.OnSubscribe<EMMessage>() {
                            @Override
                            public void call(Subscriber<? super EMMessage> subscriber) {
                                subscriber.onNext(message);
                                subscriber.onCompleted();
                            }
                        })
                        .delay(5, TimeUnit.SECONDS)
                        .subscribe(new Subscriber<EMMessage>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(EMMessage message) {
                                showNotifyWithUserInfo(message, userEntity, false);
                            }
                        });
            }
        }*/
        builder.setAutoCancel(true);

        notificationManager.notify(message.getFrom().hashCode(), builder.build());

    }

    public Observable loginChat() {
        return Observable.create(new Observable.OnSubscribe<ResponseResult>() {
            @Override
            public void call(final Subscriber<? super ResponseResult> subscriber) {
                loginChat(new Subscriber<ResponseResult>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(ResponseResult configEntity) {
                        subscriber.onNext(configEntity);
                    }
                }, false);
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }


    public void loginChat(Subscriber<ResponseResult> subscriber, boolean weakReference) {
        if (subscriber != null) {
            if (weakReference) {
                subscriberWeakReferenceList.add(new WeakReference<>(subscriber));
            } else {
                subscriberList.add(subscriber);
            }
        }
        UserEntity userEntity = UserUtil.getSavedUserInfo();
        if (!EMClient.getInstance().isLoggedInBefore()) {
            if (userEntity == null || StringUtils.isEmpty(userEntity.id) || StringUtils.isEmpty(userEntity.password)) {
                notifyLoginError(-1, "无法登录聊天服务器");
            } else {
                EMClient.getInstance().login(userEntity.id, userEntity.password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        notifyLoginComplete();
                    }

                    @Override
                    public void onError(int i, String s) {
                        notifyLoginError(i, s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        notifyLoginProgress(i, s);
                    }
                });
            }
        } else {
            notifyLoginComplete();
        }
    }

    private void notifyLoginComplete() {
        Log.i(getClass().getSimpleName(), "登录聊天服务器成功");
        for (WeakReference<Subscriber<ResponseResult>> subscriberRef
                : subscriberWeakReferenceList) {
            if (subscriberRef != null) {
                Subscriber<ResponseResult> outSubscriber = subscriberRef.get();
                if (outSubscriber != null) {
                    outSubscriber.onCompleted();
                }
            }
        }
        subscriberWeakReferenceList.clear();
        for (Subscriber<ResponseResult> subscriber
                : subscriberList) {
            subscriber.onCompleted();
        }
        subscriberList.clear();
    }

    private void notifyLoginProgress(int percent, String msg) {
        ResponseResult err = new ResponseResult(percent, msg);
        for (WeakReference<Subscriber<ResponseResult>> subscriberRef
                : subscriberWeakReferenceList) {
            if (subscriberRef != null) {
                Subscriber<ResponseResult> outSubscriber = subscriberRef.get();
                if (outSubscriber != null) {
                    outSubscriber.onNext(err);
                }
            }
        }
        subscriberWeakReferenceList.clear();
        for (Subscriber<ResponseResult> subscriber
                : subscriberList) {
            subscriber.onNext(err);
        }
        subscriberList.clear();
    }

    private void notifyLoginError(int code, String msg) {
        Log.i(getClass().getSimpleName(), "登录聊天服务器失败 " + msg);
        ResponseResult err = new ResponseResult(code, "无法连接到聊天服务器");
        for (WeakReference<Subscriber<ResponseResult>> subscriberRef
                : subscriberWeakReferenceList) {
            if (subscriberRef != null) {
                Subscriber<ResponseResult> outSubscriber = subscriberRef.get();
                if (outSubscriber != null) {
                    outSubscriber.onError(err);
                }
            }
        }
        subscriberWeakReferenceList.clear();
        for (Subscriber<ResponseResult> subscriber
                : subscriberList) {
            subscriber.onError(err);
        }
        subscriberList.clear();
    }

}
