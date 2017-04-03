package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.broadcast.LocalBroadCastConst;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.presenter.EveryDayRecommendPresenter;
import com.aibinong.tantan.ui.adapter.ViewPagerFragmentAdapter;
import com.aibinong.tantan.ui.dialog.FirstRegisterGiftSendDialog;
import com.aibinong.tantan.ui.dialog.SelectToSayHiDialog;
import com.aibinong.tantan.ui.fragment.ImgPlazaFragment;
import com.aibinong.tantan.ui.fragment.MineFragment;
import com.aibinong.tantan.ui.fragment.MsgFragment;
import com.aibinong.tantan.ui.fragment.RecommendFragment;
import com.aibinong.tantan.ui.widget.FuncBadgeView;
import com.aibinong.tantan.ui.widget.NoScrollViewPager;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.tantan.util.message.EMChatHelper;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.LocalStorage;
import com.aibinong.yueaiapi.utils.LocalStorageKey;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.hyphenate.chat.EMClient;
import com.jaydenxiao.guider.HighLightGuideView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

import static com.aibinong.tantan.constant.IntentExtraKey.INTENT_EXTRA_KEY_TAB;

public class MainActivity extends ActivityBase implements EveryDayRecommendPresenter.IEverydayPresenter, View.OnClickListener, TabLayout.OnTabSelectedListener {
    private static final String INTENT_BUNDLE_KEY_CURRENT_FRAGMENT = "INTENT_BUNDLE_KEY_CURRENT_FRAGMENT";
    @Bind(R.id.fragment_main_content)
    FrameLayout mFragmentMainContent;
    @Bind(R.id.tablayout_main_bottom)
    TabLayout mTablayoutMainBottom;
    private ImgPlazaFragment mImgPlazaFragment;
    private MsgFragment mMsgFragment;
    private RecommendFragment mRecommendFragment;
    //    private ShakeFragment mShakeFragment;
    private MineFragment mMineFragment;
    private TabLayout.Tab mHomeTab, mMsgTab, mRecommendTab, mMineTab;
    private Map<String, Fragment> mFragmentsMap;
    private ArrayList<Fragment> fragments;
    private String mCurrentFragmentTag;
    private String mTag_plazaFragment;
    private String mTag_msgFragment;
    private String mTag_recommendFragment;
    private String mTag_mineFragment;
    private BroadcastReceiver mMsgReceivedReceiver;
    private GiftDialogCanShowDialogReceiver mGiftDialogCanShowDialogReceiver;
    private LocalBroadcastManager localBroadcastManager;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireTransStatusBar();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupView(savedInstanceState);
        //登录聊天服务器
        EMChatHelper.getInstance().loginChat().subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
        if (ConfigUtil.getInstance().isNeedGuide()) {
            GuideActivity.launchIntent(this);
        }
        mMsgReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshBadge();
            }
        };


        mGiftDialogCanShowDialogReceiver = new GiftDialogCanShowDialogReceiver();
        IntentFilter filter = new IntentFilter(LocalBroadCastConst.LocalBroadIntentAction_onCanGiftShow);
        filter.addAction(LocalBroadCastConst.LocalBroadIntentAction_MsgRead);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mGiftDialogCanShowDialogReceiver, filter);

        GlobalLocalBroadCastManager.getInstance().registerMessageRecieved(mMsgReceivedReceiver);

        //如果registerMsg不为空，显示个对话框
        if (!StringUtils.isEmpty(Constant.registerMsg)) {
            DialogUtil.showDialog(this, Constant.registerMsg, true).setCancelable(false);
            Constant.registerMsg = null;
        }


        EveryDayRecommendPresenter everyDayRecommendPresenter = new EveryDayRecommendPresenter(this);
        everyDayRecommendPresenter.everydayRecommend();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mMsgReceivedReceiver);

        localBroadcastManager.unregisterReceiver(mGiftDialogCanShowDialogReceiver);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tab = intent.getIntExtra(INTENT_EXTRA_KEY_TAB, 0);
        if (mTablayoutMainBottom != null && tab >= 0 && tab < mTablayoutMainBottom.getTabCount()) {
            mTablayoutMainBottom.getTabAt(tab).select();
        }

    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        setupFragment(savedInstanceState);
        setupTab();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshBadge();
        //用户进来过 此时用户再次进入应用应该直接进入 主界面 定义一个常量记录下
        UserUtil.saveLoginState(true);
    }

    /*初始化底部的Tab*/
    private void setupTab() {
        //首页
        mHomeTab = genOneTab(R.string.abn_yueai_maintab_home, R.drawable.abn_yueai_selector_ic_tab_home);
        mHomeTab.getCustomView().findViewById(R.id.iv_bottom_tab_icon).setSelected(true);
//        //引导页是否展示过
//        if (!LocalStorage.getInstance().getBoolean(
//                LocalStorageKey.KEY_STORAGE_HOME_GUIDE_SHOW, false)) {
//            HighLightGuideView.builder(this).
//                    addHighLightGuidView(mHomeTab.getCustomView().findViewById(R.id.iv_bottom_tab_icon), R.mipmap.guangchang)
//                    .setHighLightStyle(HighLightGuideView.VIEWSTYLE_OVAL).
//                    show();
//            UserUtil.saveHomeGudieShow(true);
//        }

        //私信
        mMsgTab = genOneTab(R.string.abn_yueai_maintab_msg, R.drawable.abn_yueai_selector_ic_tab_msg);
        mMsgTab.getCustomView().findViewById(R.id.iv_bottom_tab_icon).setSelected(false);
        //精选
        mRecommendTab = genOneTab(R.string.abn_yueai_maintab_recommend, R.drawable.abn_yueai_selector_ic_tab_shake);
        mRecommendTab.getCustomView().findViewById(R.id.iv_bottom_tab_icon).setSelected(false);
        //我的
        mMineTab = genOneTab(R.string.abn_yueai_maintab_mine, R.drawable.abn_yueai_selector_ic_tab_mine);
        mMineTab.getCustomView().findViewById(R.id.iv_bottom_tab_icon).setSelected(false);

        mHomeTab.setTag(mImgPlazaFragment);
        mMsgTab.setTag(mMsgFragment);
        mRecommendTab.setTag(mRecommendFragment);
        mMineTab.setTag(mMineFragment);


        mTablayoutMainBottom.addTab(mHomeTab, false);
        mTablayoutMainBottom.addTab(mMsgTab, false);
        mTablayoutMainBottom.addTab(mRecommendTab, false);
        mTablayoutMainBottom.addTab(mMineTab, false);
        mTablayoutMainBottom.addOnTabSelectedListener(this);
//        mTablayoutMainBottom.getTabAt(0).select();

//        选中当前的页面
        for (int i = 0; i < mTablayoutMainBottom.getTabCount(); i++) {
            TabLayout.Tab tab = mTablayoutMainBottom.getTabAt(i);
            if (tab.getTag() == mFragmentsMap.get(mCurrentFragmentTag)) {
                tab.select();
            }
        }
    }

    /*初始化各个Fragment*/
    private void setupFragment(Bundle savedInstanceState) {
        mTag_plazaFragment = getString(R.string.abn_yueai_tag_fragment_plaza);
        mTag_msgFragment = getString(R.string.abn_yueai_tag_fragment_msg);
        mTag_recommendFragment = getString(R.string.abn_yueai_tag_fragment_recommend);
        mTag_mineFragment = getString(R.string.abn_yueai_tag_fragment_mine);
        /*activity重建的时候fragment还在，不需要每次都重新创建*/
        if (savedInstanceState == null) {
            mImgPlazaFragment = ImgPlazaFragment.newInstance();
            mMsgFragment = MsgFragment.newInstance();
            mRecommendFragment = RecommendFragment.newInstance();
            mMineFragment = MineFragment.newInstance();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_main_content, mImgPlazaFragment, mTag_plazaFragment);
            ft.add(R.id.fragment_main_content, mMsgFragment, mTag_msgFragment);
            ft.add(R.id.fragment_main_content, mRecommendFragment, mTag_recommendFragment);
//            ft.add(R.id.fragment_main_content, mShakeFragment, mTag_shakeFragment);
            ft.add(R.id.fragment_main_content, mMineFragment, mTag_mineFragment);
            ft.commit();
        } else {
            mCurrentFragmentTag = savedInstanceState.getString(INTENT_BUNDLE_KEY_CURRENT_FRAGMENT);
            mImgPlazaFragment = (ImgPlazaFragment) getFragmentManager().findFragmentByTag(mTag_plazaFragment);
            mMsgFragment = (MsgFragment) getFragmentManager().findFragmentByTag(mTag_msgFragment);
            mRecommendFragment = (RecommendFragment) getFragmentManager().findFragmentByTag(mTag_recommendFragment);
            mMineFragment = (MineFragment) getFragmentManager().findFragmentByTag(mTag_mineFragment);
        }
        if (mCurrentFragmentTag == null) {
            mCurrentFragmentTag = mTag_plazaFragment;
        }

        mFragmentsMap = new HashMap<>(4);

        mFragmentsMap.put(mTag_plazaFragment, mImgPlazaFragment);
        mFragmentsMap.put(mTag_msgFragment, mMsgFragment);
        mFragmentsMap.put(mTag_recommendFragment, mRecommendFragment);
        mFragmentsMap.put(mTag_mineFragment, mMineFragment);
    }

    private TabLayout.Tab genOneTab(@StringRes int title, @DrawableRes int icon) {
        TabLayout.Tab tab = mTablayoutMainBottom.newTab();
        View tabView = LayoutInflater.from(mTablayoutMainBottom.getContext()).inflate(R.layout.widget_main_bottom_tab, mTablayoutMainBottom, false);
        tab.setCustomView(tabView);

        LinearLayout mLl_bottom_tab_center;
        ImageView mIv_bottom_tab_icon;
        TextView mTv_bottom_tab_title;
        FuncBadgeView mFbv_bottom_tab_badge;

        mIv_bottom_tab_icon = (ImageView) tabView.findViewById(R.id.iv_bottom_tab_icon);
        mTv_bottom_tab_title = (TextView) tabView.findViewById(R.id.tv_bottom_tab_title);
        mFbv_bottom_tab_badge = (FuncBadgeView) tabView.findViewById(R.id.fbv_bottom_tab_badge);


        mTv_bottom_tab_title.setText(getResources().getString(title));
        mIv_bottom_tab_icon.setImageResource(icon);
//        if (selected) {
//            mTv_bottom_tab_title.setTextColor(getResources().getColor(R.color.abn_yueai_color_red_primary));
//        } else {
//            mTv_bottom_tab_title.setTextColor(getResources().getColor(R.color.color_common_translucent_black_20));
//        }

        return tab;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab != null) {
            View customView = tab.getCustomView();
            ((TextView) customView.findViewById(R.id.tv_bottom_tab_title)).setTextColor(getResources().
                    getColor(R.color.abn_yueai_color_red_primary));
            customView.findViewById(R.id.iv_bottom_tab_icon).setSelected(true);
            Object tag = tab.getTag();
            if (tab != null && tag instanceof Fragment) {
                Fragment fragment = (Fragment) tag;

//            if (fragment == mMsgFragment && UserUtil.isLoginValid(true) == null) {
//                //未登录
//            /*选中当前的页面*/
//                for (int i = 0; i < mTablayoutMainBottom.getTabCount(); i++) {
//                    TabLayout.Tab lastTab = mTablayoutMainBottom.getTabAt(i);
//
//                    if (lastTab.getTag() == mFragmentsMap.get(mCurrentFragmentTag)) {
//                        lastTab.select();
//                    }
//                }
//                askForLogin();
//                return;
//            }

//
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(fragment);
                Iterator<Map.Entry<String, Fragment>> iterator = mFragmentsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Fragment> entry = iterator.next();
                    if (entry.getValue() != null && entry.getValue() != fragment) {
                        ft.hide(entry.getValue());

                    } else {
                        mCurrentFragmentTag = entry.getKey();

                    }
                }
                ft.commit();

            }
        }


    }

    //
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab != null) {
            View customView = tab.getCustomView();
            ((TextView) customView.findViewById(R.id.tv_bottom_tab_title)).setTextColor(getResources().
                    getColor(R.color.color_common_translucent_black_20));
            customView.findViewById(R.id.iv_bottom_tab_icon).setSelected(false);
        }

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INTENT_BUNDLE_KEY_CURRENT_FRAGMENT, mCurrentFragmentTag);
    }

    public void refreshBadge() {
        //未读消息数量
        int msgCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (mMsgTab != null) {
            FuncBadgeView mFbv_bottom_tab_badge = (FuncBadgeView) mMsgTab.getCustomView().findViewById(R.id.fbv_bottom_tab_badge);
            mFbv_bottom_tab_badge.setBadge(msgCount);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveRecommends(LoginRetEntity loginRetEntity) {
        if (loginRetEntity != null) {
            if (loginRetEntity.recommends != null && loginRetEntity.recommends.size() > 0) {
                SelectToSayHiDialog dialog = SelectToSayHiDialog.newInstance(loginRetEntity.recommends, null);
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), null);
                EventBus.getDefault().removeStickyEvent(loginRetEntity);
            }
        }
    }

    @Override
    public void requestSuccess(ArrayList<UserEntity> userEntities) {
        //彈出每日推荐的对话框
        if (userEntities != null && userEntities.size() > 0) {

            SelectToSayHiDialog dialog = SelectToSayHiDialog.newInstance(userEntities,
                    getResources().getString(R.string.abn_yueai_everyday_recomend_title));
            dialog.setCancelable(false);
            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public void requestFailed(Throwable e) {

    }

    class GiftDialogCanShowDialogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(LocalBroadCastConst.LocalBroadIntentAction_onCanGiftShow)){
                if (Constant.newnovicePacks != null) {
                    FirstRegisterGiftSendDialog mdialog = FirstRegisterGiftSendDialog.newInstance(Constant.newnovicePacks);
                    mdialog.setCancelable(false);
                    mdialog.show(getFragmentManager(), null);
                }
            }else if (action.equals(LocalBroadCastConst.LocalBroadIntentAction_MsgRead)){
                refreshBadge();
            }


        }
    }
}
