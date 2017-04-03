package com.aibinong.tantan.broadcast;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.PairSuccessActivity;
import com.aibinong.tantan.ui.activity.RegisterActivity;
import com.aibinong.tantan.ui.activity.SelectPayActivity;
import com.aibinong.tantan.ui.activity.WhoLikeMeActivity;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.StringUtils;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by yourfriendyang on 2015/11/18.
 */
public class CommonPushMessageHandler {
    public static boolean handleCommonEvent(final Activity context, final PushMessage message) {
        return handleCommonEvent(context, message, true);
    }

    public static boolean handleCommonEvent(final Activity context, final PushMessage message, boolean autoContinue) {
        if (message == null) {
            return false;
        }
        //如果是需要登录的，跳到登录界面去
        if (message.needLogin == 1) {
            if (autoContinue) {
                boolean isLogin = UserUtil.ensureLoginAuth(context, new Runnable() {
                    @Override
                    public void run() {
                        handleCommonEvent(context, message, false);
                    }
                }, true);
                return true;
            } else {

                if (StringUtils.isEmpty(UserUtil.isLoginValid(true))) {
                    RegisterActivity.startLoginActivity(context, true);
                    return true;
                }
            }
        }


        //普通的intent跳转
        Intent intent;
        if (message.data == null) {
            intent = createIntent(context, message.messageType, null, null);
        } else {
            String wrapperedUrl = message.data.url;
            if (!StringUtils.isEmpty(wrapperedUrl)) {
                HashMap<String, String> extraParams = new HashMap<>();
                if (message.isBuyVipEvent) {
                    extraParams.put("targetId", message.data.targetId);
                }
                wrapperedUrl = CommonUtils.getCommonPageUrl(wrapperedUrl, extraParams);
            }
            intent = createIntent(context, message.messageType, message.data.targetId, wrapperedUrl, message);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Intent createIntent(Context context, int messageType, String targetId, String url) {
        return createIntent(context, messageType, targetId, url, null);
    }

    public static Intent createIntent(Context context, int messageType, String targetId, String url, PushMessage message) {
        // 没有人接收，那就显示通知栏通知去把
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式

        if (messageType == PushMessage.MessageType_Pay) {
            intent = new Intent(context, SelectPayActivity.class);
            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_PUSHMESSAGE, message);
        } else if (messageType == PushMessage.MessageType_WhoLikeMe) {
            //有人喜欢了你
            intent = new Intent(context, WhoLikeMeActivity.class);
        } else if (messageType == PushMessage.MessageType_PairSuccess) {
            //配对成功
            if (message != null && message.data != null && message.data.user != null) {
                intent = new Intent(context, PairSuccessActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, message.data.user);
            }
        } else if (messageType == PushMessage.MessageType_BeenLiked) {
            intent = new Intent(context, WhoLikeMeActivity.class);
        } else {


            if (url != null) {
                intent = new Intent(context, CommonWebActivity.class);
                intent.putExtra(IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA,
                        url);
            }
        }
        return intent;
    }


    public static boolean handleMessage(Context context, PushMessage message) {

        if (null != message && null != message.data) {
            Intent loginIntent = createIntent(context, message.messageType, message.data.targetId, message.data.url, message);
            if (null == loginIntent) {
                return false;
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, loginIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            PendingIntent pendingIntentFull = PendingIntent.getActivity(
//                    context, 0, loginIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // 弹出一条通知栏消息
            NotificationManager nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Notification.Builder builder = new Notification.Builder(
                        context);
                builder.setSmallIcon(R.mipmap.abn_yueai_ic_default_avatar);
                // builder.setColor(context.getResources().getColor(
                // android.R.color.transparent));
                builder.setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(), R.mipmap.ic_launcher));
                if (!StringUtils.isEmpty(message.data.title)) {
                    builder.setTicker(message.data.title);
                }
                if (!StringUtils.isEmpty(message.data.message)) {
                    builder.setContentText(message.data.message);
                }
                builder.setContentTitle(message.data.title);
                builder.setDefaults(Notification.DEFAULT_ALL);
//                builder.setWhen(System.currentTimeMillis());
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    builder.setUsesChronometer(true);
//                builder.setFullScreenIntent(pendingIntent, true);
//                }
                Notification notification = builder.getNotification();
                // notification.flags|=Notification.FLAG_AUTO_CANCEL;
                nManager.notify(message.messageType + (new Random(System.currentTimeMillis()).nextInt()), notification);
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.mipmap.abn_yueai_ic_default_avatar);
                builder.setColor(context.getResources().getColor(
                        android.R.color.transparent));
                builder.setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(), R.mipmap.ic_launcher));
                if (!StringUtils.isEmpty(message.data.title)) {
                    builder.setTicker(message.data.title);
                }
                if (!StringUtils.isEmpty(message.data.message)) {
                    builder.setContentText(message.data.message);
                }
                builder.setContentTitle(message.data.title);
                builder.setDefaults(Notification.DEFAULT_ALL);
//                builder.setWhen(System.currentTimeMillis());
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);

                //test
//                builder.setUsesChronometer(true);
//                builder.setFullScreenIntent(pendingIntent, true);
                //test end

                Notification notification = builder.build();
                // notification.flags|=Notification.FLAG_AUTO_CANCEL;
                nManager.notify(message.messageType + (new Random(System.currentTimeMillis()).nextInt()), notification);
            }
            return true;
        }

        return false;
    }
}
