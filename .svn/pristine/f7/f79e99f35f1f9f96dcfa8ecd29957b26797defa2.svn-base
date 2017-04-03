package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.presenter.DanMuPresenter;
import com.aibinong.tantan.ui.fragment.MineFragment;
import com.aibinong.tantan.ui.fragment.MsgFragment;
import com.aibinong.tantan.ui.fragment.PairFragment;
import com.aibinong.tantan.ui.fragment.ShakeFragment;
import com.aibinong.tantan.ui.widget.FuncBadgeView;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.tantan.util.message.EMChatHelper;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.fatalsignal.util.StringUtils;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

import static com.aibinong.tantan.constant.IntentExtraKey.INTENT_EXTRA_KEY_TAB;

public class MainActivity extends ActivityBase implements TabLayout.OnTabSelectedListener {
    private static final String INTENT_BUNDLE_KEY_CURRENT_FRAGMENT = "INTENT_BUNDLE_KEY_CURRENT_FRAGMENT";
    @Bind(R.id.fragment_main_content)
    FrameLayout mFragmentMainContent;
    @Bind(R.id.tablayout_main_bottom)
    TabLayout mTablayoutMainBottom;


    private PairFragment mPairFragment;
    private MsgFragment mMsgFragment;
    private ShakeFragment mShakeFragment;
    private MineFragment mMineFragment;
    private TabLayout.Tab mHomeTab, mMsgTab, mShakeTab, mMineTab;
    private Map<String, Fragment> mFragmentsMap;
    private String mCurrentFragmentTag;
    private String mTag_PairFragment;
    private String mTag_msgFragment;
    private String mTag_shakeFragment;
    private String mTag_mineFragment;
    private BroadcastReceiver mMsgReceivedReceiver;
    private DanMuPresenter mDanMuPresenter;

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
        GlobalLocalBroadCastManager.getInstance().registerMessageRecieved(mMsgReceivedReceiver);
        //如果registerMsg不为空，显示个对话框
        if (!StringUtils.isEmpty(Constant.registerMsg)) {
            DialogUtil.showDialog(this, Constant.registerMsg, true).setCancelable(false);
            Constant.registerMsg = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mMsgReceivedReceiver);
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
    }

    /*初始化底部的Tab*/
    private void setupTab() {
        //首页
        mHomeTab = genOneTab(R.string.abn_yueai_maintab_home, R.drawable.abn_yueai_selector_ic_tab_home);
        //私信
        mMsgTab = genOneTab(R.string.abn_yueai_maintab_msg, R.drawable.abn_yueai_selector_ic_tab_msg);
        //摇一摇
        mShakeTab = genOneTab(R.string.abn_yueai_maintab_shake, R.drawable.abn_yueai_selector_ic_tab_shake);
        //我的
        mMineTab = genOneTab(R.string.abn_yueai_maintab_mine, R.drawable.abn_yueai_selector_ic_tab_mine);


        mHomeTab.setTag(mPairFragment);
        mMsgTab.setTag(mMsgFragment);
        mShakeTab.setTag(mShakeFragment);
        mMineTab.setTag(mMineFragment);

        mTablayoutMainBottom.addOnTabSelectedListener(this);

        mTablayoutMainBottom.addTab(mHomeTab, false);
        mTablayoutMainBottom.addTab(mMsgTab, false);
        mTablayoutMainBottom.addTab(mShakeTab, false);
        mTablayoutMainBottom.addTab(mMineTab, false);
        /*选中当前的页面*/
        for (int i = 0; i < mTablayoutMainBottom.getTabCount(); i++) {
            TabLayout.Tab tab = mTablayoutMainBottom.getTabAt(i);
            if (tab.getTag() == mFragmentsMap.get(mCurrentFragmentTag)) {
                tab.select();
            }
        }
    }

    /*初始化各个Fragment*/
    private void setupFragment(Bundle savedInstanceState) {
        mTag_PairFragment = getString(R.string.abn_yueai_tag_fragment_pair);
        mTag_msgFragment = getString(R.string.abn_yueai_tag_fragment_msg);
        mTag_shakeFragment = getString(R.string.abn_yueai_tag_fragment_shake);
        mTag_mineFragment = getString(R.string.abn_yueai_tag_fragment_mine);
        /*activity重建的时候fragment还在，不需要每次都重新创建*/
        if (savedInstanceState == null) {
            mPairFragment = PairFragment.newInstance();
            mMsgFragment = MsgFragment.newInstance();
            mShakeFragment = ShakeFragment.newInstance();
            mMineFragment = MineFragment.newInstance();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_main_content, mPairFragment, mTag_PairFragment);
            ft.add(R.id.fragment_main_content, mMsgFragment, mTag_msgFragment);
            ft.add(R.id.fragment_main_content, mShakeFragment, mTag_shakeFragment);
            ft.add(R.id.fragment_main_content, mMineFragment, mTag_mineFragment);
            ft.commit();
        } else {
            mCurrentFragmentTag = savedInstanceState.getString(INTENT_BUNDLE_KEY_CURRENT_FRAGMENT);

            mPairFragment = (PairFragment) getFragmentManager().findFragmentByTag(mTag_PairFragment);
            mMsgFragment = (MsgFragment) getFragmentManager().findFragmentByTag(mTag_msgFragment);
            mShakeFragment = (ShakeFragment) getFragmentManager().findFragmentByTag(mTag_shakeFragment);
            mMineFragment = (MineFragment) getFragmentManager().findFragmentByTag(mTag_mineFragment);
        }
        if (mCurrentFragmentTag == null) {
            mCurrentFragmentTag = mTag_PairFragment;
        }

        mFragmentsMap = new HashMap<>(4);
        mFragmentsMap.put(mTag_PairFragment, mPairFragment);
        mFragmentsMap.put(mTag_msgFragment, mMsgFragment);
        mFragmentsMap.put(mTag_shakeFragment, mShakeFragment);
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

        mIv_bottom_tab_icon.setImageResource(icon);
        mTv_bottom_tab_title.setText(getResources().getString(title));
        return tab;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Object tag = tab.getTag();
        if (tab != null && tag instanceof Fragment) {
            Fragment fragment = (Fragment) tag;
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

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

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

}
