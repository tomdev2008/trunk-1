package com.aibinong.tantan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.aibinong.tantan.constant.IntentExtraKey;
import com.hyphenate.chat.EMMessage;

public class GlobalLocalBroadCastManager {

    private LocalBroadcastManager localBroadcastManager;

    public static GlobalLocalBroadCastManager getInstance() {
        return InstanceHolder.instance;
    }

    public LocalBroadcastManager getLocalBroadcastManager() {
        return localBroadcastManager;
    }

    public void setLocalBroadcastManager(
            LocalBroadcastManager localBroadcastManager) {
        this.localBroadcastManager = localBroadcastManager;
    }

    public void onLoginStatusChange() {
        // 通知刷新
        Intent intent = new Intent(
                LocalBroadCastConst.LocalBroadIntentAction_loginStatusChange);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager()
                .sendBroadcast(intent);
    }

    public void registerLoginStatusChange(BroadcastReceiver receiver) {
        getLocalBroadcastManager().registerReceiver(
                receiver,
                new IntentFilter(
                        LocalBroadCastConst.LocalBroadIntentAction_loginStatusChange));
    }


    public void notifyBindMobile() {
        Intent intent = new Intent(
                LocalBroadCastConst.LocalBroadIntentAction_bindmobile);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager()
                .sendBroadcast(intent);
    }

    public void registerBindMobile(BroadcastReceiver receiver) {
        getLocalBroadcastManager()
                .registerReceiver(
                        receiver,
                        new IntentFilter(
                                LocalBroadCastConst.LocalBroadIntentAction_bindmobile));
    }

    public void notifyPaySuccess() {
        Intent intent = new Intent(
                LocalBroadCastConst.LocalBroadIntentAction_PaySuccess);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager()
                .sendBroadcast(intent);
    }

    public void registerPaySuccess(BroadcastReceiver receiver) {
        getLocalBroadcastManager()
                .registerReceiver(
                        receiver,
                        new IntentFilter(
                                LocalBroadCastConst.LocalBroadIntentAction_PaySuccess));
    }


    public void registerLikeOrDisLike(BroadcastReceiver receiver) {
        getLocalBroadcastManager()
                .registerReceiver(
                        receiver,
                        new IntentFilter(
                                LocalBroadCastConst.LocalBroadIntentAction_LikeOrDislike));
    }

    public boolean notifyLikeOrDisLike(String uid, boolean like) {
        Intent intent = new Intent(LocalBroadCastConst.LocalBroadIntentAction_LikeOrDislike);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_UUUID, uid);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_IS_LIKE, like);
        return getLocalBroadcastManager().sendBroadcast(intent);
    }

    public boolean notifyMessegeReceived(EMMessage message) {
        Intent intent = new Intent(LocalBroadCastConst.LocalBroadIntentAction_EMmessageReceived);
        if (message != null) {
            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_EMMESSAGE, message);
        }
        return getLocalBroadcastManager().sendBroadcast(intent);
    }

    public void registerMessageRecieved(BroadcastReceiver receiver) {
        getLocalBroadcastManager()
                .registerReceiver(
                        receiver,
                        new IntentFilter(
                                LocalBroadCastConst.LocalBroadIntentAction_EMmessageReceived));
    }

    /*接收这个广播的话，就不会显示通知栏消息*/
    public boolean notifyMessegeReceivedSingle(EMMessage message) {
        Intent intent = new Intent(LocalBroadCastConst.LocalBroadIntentAction_EMmessageReceived_single + message.getFrom());
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_EMMESSAGE, message);
        return getLocalBroadcastManager().sendBroadcast(intent);
    }

    /*接收这个广播的话，就不会显示通知栏消息*/
    public void registerMessageRecievedSingle(BroadcastReceiver receiver, String uuid) {
        getLocalBroadcastManager()
                .registerReceiver(
                        receiver,
                        new IntentFilter(
                                LocalBroadCastConst.LocalBroadIntentAction_EMmessageReceived_single + uuid));
    }

    static class InstanceHolder {
        static GlobalLocalBroadCastManager instance = new GlobalLocalBroadCastManager();
    }
}
