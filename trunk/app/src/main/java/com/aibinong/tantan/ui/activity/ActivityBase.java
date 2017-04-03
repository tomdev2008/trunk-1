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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.tantan.presenter.MineFragPresenter;
import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.tantan.presenter.SysMessagePresenter;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.tantan.ui.dialog.CommonCardDialog;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.swipbackhelper.SwipeListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public abstract class ActivityBase extends AppCompatActivity implements View.OnClickListener, SwipeListener, MineFragPresenter.IMineFrag {
    private TextView mTitleView;
    protected Handler mDelayLoadHandler;
    //    private BroadcastReceiver notificationReceiver;
    private BroadcastReceiver mShowLoginReciever;
    private MineFragPresenter mMineFragPresenter;

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
//        notificationReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //收到通知
//                if (intent != null) {
//                    Serializable so = intent.getSerializableExtra(IntentExtraKey.BROADCAST_GETUI_PUSHMESSAGE);
//                    if (so != null && so instanceof PushMessage) {
//                        PushMessage pushMessage = (PushMessage) so;
//                        if (pushMessage.messageType == PushMessage.MessageType_BeenLiked) {
//                            //谁喜欢了我显示顶部toast
//                            if (((PushMessage) so).data != null && pushMessage.data.user != null) {
//                                String avatar = pushMessage.data.user.getFirstPicture();
//                                String title = pushMessage.data.title; // getString(R.string.abn_yueai_have_a_newpair);
//                                String content = pushMessage.data.message; // getString(R.string.abn_yueai_fmt_xx_likeyour, pushMessage.data.user.nickname);
//                                Intent contentIntent = CommonPushMessageHandler.createIntent(ActivityBase.this, pushMessage.messageType, pushMessage.data.targetId, pushMessage.data.url, pushMessage);
//                                showAppMsg(avatar, 0, title, content, PendingIntent.getActivity(ActivityBase.this, 0, contentIntent, PendingIntent.FLAG_ONE_SHOT));
//                                return;
//                            }
//                        } else if (pushMessage.messageType == PushMessage.MessageType_PairSuccess && pushMessage.data != null && pushMessage.data.user != null) {
//                            //配对成功直接弹出界面
//                            PairSuccessActivity.launchIntent(ActivityBase.this, pushMessage.data.user);
//                            return;
//                        }
//                        CommonPushMessageHandler.handleMessage(context, ((PushMessage) so));
//                    }
//                }
//            }
//        };
        mMineFragPresenter = new MineFragPresenter(this);

        mShowLoginReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //跳到选择登录页面
                Intent firstIntent = new Intent(ActivityBase.this, SplashActivity.class);
//                firstIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(firstIntent);
            }
        };

    }

    private View appMsgView;
    private Runnable removeMsgRb;

    public com.fatalsignal.view.RoundAngleImageView mRiv_view_appmsg_avatar;
    public TextView mTv_view_appmsg_title;
    public LinearLayout mLl_view_appmsg_content;

    public void showAppMsg(String avatar, String content, final PendingIntent pendingIntent) {
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        if (removeMsgRb == null) {
            removeMsgRb = new Runnable() {
                @Override
                public void run() {
                    appMsgView.removeCallbacks(removeMsgRb);
                    appMsgView.animate().translationY(-appMsgView.getHeight()).setDuration(300).setInterpolator(new FastOutLinearInInterpolator()).withEndAction(new Runnable() {
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
            mLl_view_appmsg_content = (LinearLayout) appMsgView.findViewById(R.id.ll_view_appmsg_content);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                appMsgView.setPadding(appMsgView.getPaddingLeft(), appMsgView.getPaddingTop() + com.fatalsignal.util.DeviceUtils.getStatusBarHeight(getApplicationContext()), appMsgView.getPaddingRight(), appMsgView.getPaddingBottom());
                FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) mLl_view_appmsg_content.getLayoutParams();
                flp.topMargin = flp.topMargin + com.fatalsignal.util.DeviceUtils.getStatusBarHeight(getApplicationContext());
                mLl_view_appmsg_content.setLayoutParams(flp);
            }
        }
        if (!StringUtils.isEmpty(avatar)) {
            Glide.with(this).load(avatar).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRiv_view_appmsg_avatar);
            mRiv_view_appmsg_avatar.setVisibility(View.VISIBLE);

        } else {
            mRiv_view_appmsg_avatar.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(content)) {
            mTv_view_appmsg_title.setText(content);
            mTv_view_appmsg_title.setVisibility(View.VISIBLE);
        } else {
            mTv_view_appmsg_title.setVisibility(View.GONE);
        }


        appMsgView.removeCallbacks(removeMsgRb);
        decorView.removeView(appMsgView);


        appMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMsgView.removeCallbacks(removeMsgRb);
                appMsgView.animate().translationY(-appMsgView.getHeight()).setDuration(400).setInterpolator(new FastOutLinearInInterpolator()).withEndAction(new Runnable() {
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
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (90 * com.fatalsignal.util.DeviceUtils.getScreenDensity(getApplicationContext())) + statusHeight);
        appMsgView.setTranslationY(-vlp.height);
        decorView.addView(appMsgView, vlp);

        appMsgView.postDelayed(removeMsgRb, 4000);

        appMsgView.animate().translationY(0).setDuration(400).setInterpolator(new FastOutSlowInInterpolator()).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(notificationReceiver);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mShowLoginReciever);
        MobclickAgent.onPause(this);
        EventBus.getDefault().unregister(this);
        SysMessagePresenter.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().registerReceiver(notificationReceiver, new IntentFilter(BroadCastConst.BROADCAST_GETUI_NOTIFYCATION_PROXY));
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().registerReceiver(mShowLoginReciever, new IntentFilter(BroadCastConst.BROADCAST_OPEN_LOGIN));
        MobclickAgent.onResume(this);
        EventBus.getDefault().register(this);
        SysMessagePresenter.getInstance().onResume();
        mMineFragPresenter.getMyInfo();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        SwipeBackHelper.onDestroy(this);
        mMineFragPresenter.onDestoryView();
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

    private Toast mCommonToast;

    protected void showToast(String msg) {
        if (mCommonToast == null) {
            mCommonToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            //设置吐司显示位置为屏幕正中心
            mCommonToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mCommonToast.setText(msg);
        }
        mCommonToast.show();
    }

    private PublishSubject<String> mToastSub;

    public void showToastDebounce(String msg) {
        if (mToastSub == null) {
            mToastSub = PublishSubject.create();
            mToastSub.debounce(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(String o) {
                    showToast(o);
                }
            });
        }
        mToastSub.onNext(msg);
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

    public void askForLogin() {

        CommonCardDialog askForLoginDialog = CommonCardDialog.newInstance("您还未登录", getString(R.string.abn_yueai_need_login), null, "登录/注册");
        askForLoginDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == Dialog.BUTTON_NEGATIVE) {
                    //取消
                    //前往登录界面
                    UserUtil.saveLoginState(false);
                    SplashActivity.launchIntent(ActivityBase.this, true);
                }
            }
        });
//        askForLoginDialog.setCancelable(false);
        askForLoginDialog.show(getFragmentManager(), null);
    }

    public void askForBuyVip() {

        CommonCardDialog askForLoginDialog = CommonCardDialog.newInstance("想给尊贵VIP打招呼？", "对方是尊贵VIP会员，开通钻石会员即可与对方无限畅聊", "升级钻石会员", "什么是尊贵VIP");
        askForLoginDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == Dialog.BUTTON_NEGATIVE) {
                    //vip认证
                    VipCertActivity.launchIntent(ActivityBase.this);
                } else {
                    //钻石
                    CommonWebActivity.launchIntent(ActivityBase.this, CommonUtils.getCommonPageUrl(Constant._sUrl_vip_buy, null));
                }
            }
        });
//        askForLoginDialog.setCancelable(false);
        askForLoginDialog.show(getFragmentManager(), null);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCommonEvent(Object event) {
        if (event instanceof BaseEvent) {
            BaseEvent baseEvent = (BaseEvent) event;
            if (BaseEvent.ACTION_SYS_MESSAGE.equals(baseEvent.action)) {
                EventBus.getDefault().cancelEventDelivery(event);

                if (((BaseEvent) event).data != null && ((BaseEvent) event).data instanceof PushMessage) {
                    PushMessage pushMessage = (PushMessage) ((BaseEvent) event).data;
                    String avatar = null;
                    PendingIntent pendingIntent = null;
                    if (pushMessage.data != null) {
                        if (pushMessage.data.user != null) {
                            avatar = pushMessage.data.user.getFirstPicture();
                        }
                        Intent contentIntent = CommonPushMessageHandler.createIntent(this, pushMessage.messageType,
                                pushMessage.data.targetId, pushMessage.data.url, pushMessage);
                        pendingIntent = PendingIntent.getActivity(ActivityBase.this, 0, contentIntent, PendingIntent.FLAG_ONE_SHOT);
                    }
                    if (
                            UserUtil.getSavedUserInfoNotNull().sex == 1) {  //男性能收到推送 女性不能
                        showAppMsg(avatar, pushMessage.data.message, pendingIntent);
                    }
                }

                return;
            }
            if (BaseEvent.ACTION_ASK_FOR_LOGIN.equals(baseEvent.action)) {
                //是否登录
                EventBus.getDefault().cancelEventDelivery(event);
                UserUtil.saveLoginState(false);
                SplashActivity.launchIntent(this, true);
                Log.e("登录失效了");
//                askForLogin();
            }
            if (BaseEvent.ACTION_ASK_FOR_BUY_VIP.equals(baseEvent.action)) {
                EventBus.getDefault().cancelEventDelivery(event);
                askForBuyVip();
            }

            if (baseEvent.action.equals(BaseEvent.ACTION_FOLLOW_START) || baseEvent.action.equals(BaseEvent.ACTION_UNFOLLOW_START)) {

            } else if (baseEvent.action.equals(BaseEvent.ACTION_FOLLOW_FAILED)) {
                showToastDebounce("关注失败，请重试");
            } else if (baseEvent.action.equals(BaseEvent.ACTION_UNFOLLOW_FAILED)) {
                showToastDebounce("取消关注失败，请重试");
            } else if (baseEvent.action.equals(BaseEvent.ACTION_FOLLOW_SUCCESS)) {
                showToastDebounce("关注成功");

                //通知关注界面刷新
                GlobalLocalBroadCastManager.getInstance()
                        .onFllowActivityChange(1);
            } else if (baseEvent.action.equals(BaseEvent.ACTION_UNFOLLOW_SUCCESS)) {
                showToastDebounce("取消关注成功");

                //通知关注界面刷新
                GlobalLocalBroadCastManager.getInstance()
                        .onFllowActivityChange(0);
            } else if (BaseEvent.ACTION_SAYHI_FAILED.equals(baseEvent.action)) {
                showToastDebounce("打招呼失败，请重试");
            } else if (BaseEvent.ACTION_SAYHI_SUCCESS.equals(baseEvent.action)) {
                showToastDebounce("打招呼成功");

                //通知私信界面刷新
                GlobalLocalBroadCastManager.getInstance()
                        .onPersonMessageActivityChange();
            } else if (BaseEvent.ACTION_SAYHI_START.equals(baseEvent.action)) {

            }
        }
    }

    @Override
    public void onGetMyInfoFailed(ResponseResult e) {
        if (e.getMessage().contains("登录失效，请重新登录")){
            SplashActivity.launchIntent(this,true);
            showToast(getResources().getString(R.string.abn_yueai_notice_no_token));
            UserUtil.saveLoginState(false);
            finish();
        }
    }

    @Override
    public void onGetMyInfoSuccess(UserEntity userEntity) {

    }
}
