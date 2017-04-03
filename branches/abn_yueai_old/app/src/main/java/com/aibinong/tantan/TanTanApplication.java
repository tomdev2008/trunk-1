package com.aibinong.tantan;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/19.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.util.CommonResultHandler;
import com.aibinong.tantan.util.LiuLianLocationUtil;
import com.aibinong.tantan.util.message.EMChatHelper;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.MetaDataUtil;
import com.igexin.sdk.PushManager;
import com.tk.mediapicker.utils.MediaUtils;

public class TanTanApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.setLog(BuildConfig.DEBUG);
        final String processName = DeviceUtils.getProcessName(
                getApplicationContext(), android.os.Process.myPid());

        Constant.init(getApplicationContext());


        GlideBuilder glideBuilder = new GlideBuilder(getApplicationContext())
                .setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        Glide.setup(glideBuilder);
        MediaUtils.init(getApplicationContext(), Constant.cachePath);
        ApiHelper.getInstance().init(getApplicationContext(), CommonResultHandler.getInstance(), BuildConfig.HOST_ROOT, Constant.cachePath, BuildConfig.CLIENT_ID, BuildConfig.DEBUG);
        if (processName.equals(getPackageName())) {

            GlobalLocalBroadCastManager.getInstance().setLocalBroadcastManager(
                    LocalBroadcastManager.getInstance(getApplicationContext()));
            LiuLianLocationUtil.getInstance().init(getApplicationContext());
            SqlBriteUtil.getInstance().init(getApplicationContext());

            EMChatHelper.getInstance().init(getApplicationContext());

            // 个推初始化
            PushManager.getInstance().initialize(getApplicationContext());
            PushManager.getInstance().bindAlias(getApplicationContext(), MetaDataUtil.getString(getApplicationContext(), "UMENG_CHANNEL"));


        }

    }
}
