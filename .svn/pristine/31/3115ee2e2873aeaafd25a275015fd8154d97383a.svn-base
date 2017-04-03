package com.aibinong.tantan.util;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;

import java.lang.ref.WeakReference;

/**
 * Created by yourfriendyang on 16/1/22.
 */
public class AliulianAndroidJSInterface {
    private WeakReference<Activity> activityWeakReference;

    public AliulianAndroidJSInterface(Activity ac) {
        activityWeakReference = new WeakReference<Activity>(ac);
    }

    @JavascriptInterface
    public void commonOperate(String jsonStr) {
        Log.d("commonOperate jsonStr=", jsonStr.toString());
        Activity activity = activityWeakReference.get();
        if (activity == null) {
            return;
        }
//            Toast.makeText(activity, jsonStr, Toast.LENGTH_SHORT).show();
        if (StringUtils.isEmpty(jsonStr)) {
            return;
        }
        try {
            PushMessage PushMessage = ApiHelper.getInstance().getGson().fromJson(jsonStr, PushMessage.class);
            if (PushMessage != null) {
                /*if (PushMessage.messageType == PushMessage.MessageType_ClosePage) {
                    activity.finish();
                    return;
                }*/// TODO: 16/11/21 关闭页面
                CommonPushMessageHandler.handleCommonEvent(activity, PushMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
