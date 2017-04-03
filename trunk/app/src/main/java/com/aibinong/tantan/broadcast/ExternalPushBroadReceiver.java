package com.aibinong.tantan.broadcast;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.aibinong.tantan.presenter.SysMessagePresenter;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.fatalsignal.util.Log;

/**
 * @author 杨升迁(yourfriendyang@163.com)
 *         <p/>
 *         上午10:57:32 2015年6月5日 推送广播接收器 并不是直接接收个推的广播，而是从个推广播接收器里面转发出来的广播
 *         这样的话方便控制广播接收顺序 如果你又更好的方法，可以告诉我
 */
public class ExternalPushBroadReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {


        String currentExtraString = intent
                .getStringExtra(BroadCastConst.GeTuiPlayLoadKey);
        Log.e("ExternalPushBroadReceiver onReceive", currentExtraString);
        try {

            PushMessage mushMsg = ApiHelper.getInstance().getGson().fromJson(currentExtraString, PushMessage.class);
            if (mushMsg == null) {
                return;
            }
            // 根据不同类型发送不同广播
            boolean sendRet = SysMessagePresenter.getInstance().processGeTuiMsg(mushMsg);
            if (!sendRet) {
                CommonPushMessageHandler.handleMessage(context, mushMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}