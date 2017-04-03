package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.presenter.SysMessagePresenter;
import com.aibinong.tantan.presenter.VersionUpdatePresenter;
import com.aibinong.tantan.ui.dialog.FirstRegisterGiftSendDialog;
import com.aibinong.tantan.ui.dialog.VersionUpdateDialog;
import com.aibinong.tantan.util.update.ApkInstallReceiver;
import com.aibinong.tantan.util.update.AppInnerDownLoder;
import com.aibinong.tantan.util.update.DownLoadApk;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.VersionEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends ActivityUnRegisterBase implements VersionUpdatePresenter.IVerson {
    @Bind(R.id.iv_splash_change)
    ImageView ivSplashChange;
    @Bind(R.id.login_register)
    Button loginRegister;
//    private AnimationDrawable anim;

    private int[] mImageResIds = new int[]{R.mipmap.abn_ya_login_one, R.mipmap.abn_ya_login_two, R.mipmap.abn_ya_login_three};
    private int mCount;
    private Timer mTimer;
    private VersionUpdatePresenter mVersionUpdatePresenter;
    private AlertDialog.Builder mDialog;
    private ApkInstallReceiver mApkInstallReceiver;

    public static Intent launchIntent(Context context, boolean autoStart) {
        Intent intent = new Intent(context, SplashActivity.class);
        if (autoStart) {
            context.startActivity(intent);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_splash);
        ButterKnife.bind(this);
        requireTransStatusBar();
        setupView(savedInstanceState);

        ConfigUtil.getInstance().requireRefresh(null);
        if (ConfigUtil.getInstance().isNeedGuide()) {
            GuideActivity.launchIntent(SplashActivity.this);
        }
        mApkInstallReceiver = new ApkInstallReceiver();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mApkInstallReceiver, filter);

        //在自己的应用初始Activity中加入如下两行代码
//        RefWatcher refWatcher = TanTanApplication.getRefWatcher(this);
//        refWatcher.watch(this);
        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (ConfigUtil.getInstance().isNeedGuide()) {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTimer == null) {
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Glide.with(SplashActivity.this).load(mImageResIds[mCount]).into(ivSplashChange);
                        ivSplashChange.setImageResource(mImageResIds[mCount]);
                        mCount++;
                        if (mCount == 3) {
                            mCount = 0;
                        }
                    }
                });
            }
        }, 0, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mTimer) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;           //(可选)
        }
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        loginRegister.setOnClickListener(this);
        mVersionUpdatePresenter = new VersionUpdatePresenter(this);
        mVersionUpdatePresenter.getVersion();

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        //设置splash动画
//        ivSplashChange.setBackgroundResource(R.drawable.abn_ya_splash_frame_anim);
//        anim = (AnimationDrawable) ivSplashChange.getBackground();
//        if (anim!=null && !anim.isRunning()){
//            anim.start();
//        }
//
//    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SysMessagePresenter.getInstance().onPause();
        if (mApkInstallReceiver != null) {
            unregisterReceiver(mApkInstallReceiver);
            mApkInstallReceiver = null;
        }
//        if (anim != null &&anim.isRunning()){
//            anim.stop();
//        }

    }

    /**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(SplashActivity.this, downUrl, appName);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                 AppInnerDownLoder.downLoadApk(SplashActivity.this,downUrl,appName);
//                DownLoadApk.download(SplashActivity.this, downUrl, updateinfo, appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onReuireStatusFailed(ResponseResult e) {

    }

    @Override
    public void onRequireStatusSuccess(VersionEntity versionEntity) {
        //先判断本地版本和服务器最新版本是否相同
        String versionname = DeviceUtils.getVersionName(this);
        if (!versionEntity.version.equals(versionname)) {  //本地版本和服务器版本不同 需要提示用户版本更新
//           VersionUpdateDialog mdialog = VersionUpdateDialog.newInstance(versionEntity);
//           mdialog.setCancelable(false);
//           mdialog.show(getFragmentManager(), null);
            if (versionEntity.status.equals(0 + "")) {  //非强制更新
                normalUpdate(this, "约爱", "http://ont9j0dy7.bkt.clouddn.com/app-YA001-debug_201704031327_1.5.apk", "版本升级了哟");
            } else {  //强制更新
                forceUpdate(this, "约爱", "http://ont9j0dy7.bkt.clouddn.com/app-YA001-debug_201704031327_1.5.apk", "版本升级了哟");
            }
        } else {
            if (UserUtil.getLoginState()) {
                //进入主界面前需要调一下message接口
                SysMessagePresenter.getInstance().onResume();  //判断是否登录失效
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }
}
