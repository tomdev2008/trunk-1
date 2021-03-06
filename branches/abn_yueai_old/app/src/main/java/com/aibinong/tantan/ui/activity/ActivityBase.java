package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/2.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.swipbackhelper.SwipeListener;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ActivityBase extends AppCompatActivity implements View.OnClickListener, SwipeListener {
    private TextView mTitleView;
    protected Handler mDelayLoadHandler;
    private BroadcastReceiver notificationReceiver;
    private BroadcastReceiver mShowLoginReciever;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(swipeBackEnable())
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeEdgePercent(0.1f)
                .setSwipeRelateOffset(300)
                .addListener(this)
        ;
        mDelayLoadHandler = new Handler();
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //收到通知
                if (intent != null) {
                    Serializable so = intent.getSerializableExtra(IntentExtraKey.BROADCAST_GETUI_PUSHMESSAGE);
                    if (so != null && so instanceof PushMessage) {
                        PushMessage pushMessage = (PushMessage) so;
                        if (pushMessage.messageType == PushMessage.MessageType_BeenLiked) {
                            //谁喜欢了我显示顶部toast
                            if (((PushMessage) so).data != null && pushMessage.data.user != null) {
                                String avatar = pushMessage.data.user.getFirstPicture();
                                String title = pushMessage.data.title; // getString(R.string.abn_yueai_have_a_newpair);
                                String content = pushMessage.data.message; // getString(R.string.abn_yueai_fmt_xx_likeyour, pushMessage.data.user.nickname);
                                Intent contentIntent = CommonPushMessageHandler.createIntent(ActivityBase.this, pushMessage.messageType, pushMessage.data.targetId, pushMessage.data.url, pushMessage);
                                showAppMsg(avatar, 0, title, content, PendingIntent.getActivity(ActivityBase.this, 0, contentIntent, PendingIntent.FLAG_ONE_SHOT));
                                return;
                            }
                        } else if (pushMessage.messageType == PushMessage.MessageType_PairSuccess && pushMessage.data != null && pushMessage.data.user != null) {
                            //配对成功直接弹出界面
                            PairSuccessActivity.launchIntent(ActivityBase.this, pushMessage.data.user);
                            return;
                        }
                        CommonPushMessageHandler.handleMessage(context, ((PushMessage) so));
                    }
                }
            }
        };
        mShowLoginReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //跳到选择登录页面
                Intent firstIntent = new Intent(ActivityBase.this, FirstPageActivity.class);
                firstIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(firstIntent);
            }
        };
    }

    private View appMsgView;
    private Runnable removeMsgRb;
    private com.fatalsignal.view.RoundAngleImageView mRiv_view_appmsg_avatar;
    private TextView mTv_view_appmsg_title;
    private TextView mTv_view_appmsg_content;
    private ImageButton mIbtn_view_appmsg_close;
    private ImageView mIv_view_appmsg_icon;

    public void showAppMsg(String avatar, int icon, String title, String content, final PendingIntent pendingIntent) {
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        if (removeMsgRb == null) {
            removeMsgRb = new Runnable() {
                @Override
                public void run() {
                    appMsgView.removeCallbacks(removeMsgRb);
                    appMsgView.animate().translationY(-appMsgView.getHeight()).setDuration(500).setInterpolator(new AnticipateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                decorView.removeView(appMsgView);
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                }
            };
        }
        if (appMsgView == null) {
            appMsgView = getLayoutInflater().inflate(R.layout.abn_yueai_view_appmsg, decorView, false);
            mRiv_view_appmsg_avatar = (com.fatalsignal.view.RoundAngleImageView) appMsgView.findViewById(R.id.riv_view_appmsg_avatar);
            mTv_view_appmsg_title = (TextView) appMsgView.findViewById(R.id.tv_view_appmsg_title);
            mTv_view_appmsg_content = (TextView) appMsgView.findViewById(R.id.tv_view_appmsg_content);
            mIbtn_view_appmsg_close = (ImageButton) appMsgView.findViewById(R.id.ibtn_view_appmsg_close);
            mIv_view_appmsg_icon = (ImageView) appMsgView.findViewById(R.id.iv_view_appmsg_icon);

            mIbtn_view_appmsg_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMsgView.removeCallbacks(removeMsgRb);
                    decorView.removeView(appMsgView);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appMsgView.setPadding(appMsgView.getPaddingLeft(), appMsgView.getPaddingTop() + com.fatalsignal.util.DeviceUtils.getStatusBarHeight(getApplicationContext()), appMsgView.getPaddingRight(), appMsgView.getPaddingBottom());
            }
        }
        if (!StringUtils.isEmpty(avatar)) {
            Glide.with(this).load(avatar).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRiv_view_appmsg_avatar);
            mRiv_view_appmsg_avatar.setVisibility(View.VISIBLE);
            mIv_view_appmsg_icon.setVisibility(View.GONE);
        } else if (icon > 0) {
            mIv_view_appmsg_icon.setImageResource(icon);
            mIv_view_appmsg_icon.setVisibility(View.VISIBLE);
            mRiv_view_appmsg_avatar.setVisibility(View.GONE);
        } else {
            mRiv_view_appmsg_avatar.setVisibility(View.GONE);
            mIv_view_appmsg_icon.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(title)) {
            mTv_view_appmsg_title.setText(title);
            mTv_view_appmsg_title.setVisibility(View.VISIBLE);
        } else {
            mTv_view_appmsg_title.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(content)) {
            mTv_view_appmsg_content.setText(content);
            mTv_view_appmsg_content.setVisibility(View.VISIBLE);
        } else {
            mTv_view_appmsg_content.setVisibility(View.GONE);
        }

        appMsgView.removeCallbacks(removeMsgRb);
        decorView.removeView(appMsgView);


        appMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMsgView.removeCallbacks(removeMsgRb);
                appMsgView.animate().translationY(-appMsgView.getHeight()).setDuration(500).setInterpolator(new AnticipateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            decorView.removeView(appMsgView);
                        } catch (Exception e) {
                        }
                    }
                }).start();
                if (pendingIntent != null) {
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        int statusHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusHeight = com.fatalsignal.util.DeviceUtils.getStatusBarHeight(getApplicationContext());
        }
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (62 * com.fatalsignal.util.DeviceUtils.getScreenDensity(getApplicationContext())) + statusHeight);
        appMsgView.setTranslationY(-vlp.height);
        decorView.addView(appMsgView, vlp);

        appMsgView.postDelayed(removeMsgRb, 2000);

        appMsgView.animate().translationY(0).setDuration(500).setInterpolator(new BounceInterpolator()).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(notificationReceiver);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mShowLoginReciever);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().registerReceiver(notificationReceiver, new IntentFilter(BroadCastConst.BROADCAST_GETUI_NOTIFYCATION_PROXY));
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().registerReceiver(mShowLoginReciever, new IntentFilter(BroadCastConst.BROADCAST_OPEN_LOGIN));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        SwipeBackHelper.onDestroy(this);

        DialogUtil.hideDialog(this);
        Glide.get(this).clearMemory();

        Field[] mFileds = this.getClass().getDeclaredFields();
        Field[] mFields_public = this.getClass().getFields();

        handlerFields(mFileds);
        handlerFields(mFields_public);

        super.onDestroy();
    }

    protected boolean swipeBackEnable() {
        return false;
    }

    public void postDelayLoad(final Runnable runnable) {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mDelayLoadHandler.post(runnable);
            }
        });
    }

    protected void setupToolbar(Toolbar toolbar, TextView titleView, boolean asBack) {
        mTitleView = titleView;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(asBack);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.abn_yueai_ic_back);
        getSupportActionBar().setTitle(null);
        if (mTitleView != null) {
            mTitleView.setText(getTitle());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mTitleView != null) {
            mTitleView.setText(titleId);
        }
    }

    protected void requireTransStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//                getWindow()
//                        .setStatusBarColor(
//                                getResources().getColor(
//                                        R.color.color_common_textwhite));
//                getWindow()
//                        .setNavigationBarColor(
//                                getResources().getColor(
//                                        R.color.color_common_textwhite));
//
//            } else
            {
                // 4.4的话半透明状态栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().addFlags(
//                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    public void startActivity(Class<?> intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }

    protected abstract void setupView(@Nullable Bundle savedInstanceState);


    private void handlerFields(Field[] fields) {
        if (fields != null && fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Object object = null;
                try {
                    object = field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //取消所有的下拉刷新
               /* if (PtrFrameLayout.class.isAssignableFrom(field.getType())) {
                    try {
                        Method method = PtrFrameLayout.class.getMethod("refreshComplete");
                        field.setAccessible(true);
                        Object obj = field.get(this);
                        method.invoke(obj);
                        Log.d(field.getName() + "", "invoke PtrFrameLayout.refreshComplete()");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else */
                if (object != null && PresenterBase.class.isInstance(object)) {
                    //PresenterBase.class.isAssignableFrom(field.getType())
                    try {
                        Method method = object.getClass().getMethod("onDestoryView");
                        method.invoke(object);
                        Log.d(field.getName() + "", "invoke PresenterBase.onDestoryView()");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected Dialog showErrDialog(Throwable e) {
        return DialogUtil.showDialog(this, e.getMessage(), true);
    }

    protected void showErrToast(Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScroll(float percent, int px) {

    }

    @Override
    public void onEdgeTouch() {

    }

    @Override
    public void onScrollToClose() {

    }


    InputMethodManager inputManager;

    public void hideKeyboard() {
        if (inputManager == null) {
            inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
