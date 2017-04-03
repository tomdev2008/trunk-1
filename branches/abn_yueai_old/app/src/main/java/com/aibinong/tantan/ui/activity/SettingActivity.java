package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/8.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.dialog.SelectItemIOSDialog;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.FileSizeUtil;
import com.hyphenate.chat.EMClient;
import com.kyleduo.switchbutton.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.apptik.widget.MultiSlider;
import rx.Subscriber;

import static com.aibinong.yueaiapi.utils.UserUtil.clearLoginInfo;

public class SettingActivity extends ActivityBase implements MultiSlider.OnThumbValueChangeListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_setting_agerange)
    TextView mTvSettingAgerange;
    @Bind(R.id.slider_setting_age)
    MultiSlider mSliderSettingAge;
    @Bind(R.id.tv_setting_distance)
    TextView mTvSettingDistance;
    @Bind(R.id.slider_setting_distance)
    MultiSlider mSliderSettingDistance;
    @Bind(R.id.ll_setting_privacy_notify)
    LinearLayout mLlSettingPrivacyNotify;
    @Bind(R.id.tv_setting_cache)
    TextView mTvSettingCache;
    @Bind(R.id.ll_setting_cache)
    LinearLayout mLlSettingCache;
    @Bind(R.id.ll_setting_feedback)
    LinearLayout mLlSettingFeedback;
    @Bind(R.id.btn_setting_logout)
    Button mBtnSettingLogout;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.switch_setting_notify_privacy)
    SwitchButton mSwitchSettingNotifyPrivacy;
    @Bind(R.id.tv_setting_cache_size)
    TextView mTvSettingCacheSize;
    @Bind(R.id.switch_setting_notify_detail)
    SwitchButton mSwitchSettingNotifyDetail;
    @Bind(R.id.ll_setting_privacy_notifydetail)
    LinearLayout mLlSettingPrivacyNotifydetail;
    @Bind(R.id.tv_setting_versionname)
    TextView mTvSettingVersionname;
    private int mMinAge, mMaxAge;
    private int mMaxDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_setting);
        ButterKnife.bind(this);
        requireTransStatusBar();
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        setupView(savedInstanceState);
        bindData();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLlSettingPrivacyNotify.setOnClickListener(this);
        mLlSettingCache.setOnClickListener(this);
        mLlSettingFeedback.setOnClickListener(this);
        mBtnSettingLogout.setOnClickListener(this);

        mSliderSettingAge.setMin(ConfigUtil.MIN_AGE);
        mSliderSettingAge.setMax(ConfigUtil.MAX_AGE);

        mSliderSettingDistance.setMin(ConfigUtil.MIN_DISTANCE);
        mSliderSettingDistance.setMax(ConfigUtil.MAX_DISTANCE);

        mMinAge = ConfigUtil.getInstance().getPairMinAge();
        mMaxAge = ConfigUtil.getInstance().getPairMaxAge();
        mMaxDistance = ConfigUtil.getInstance().getPairMaxDistance();

        mSliderSettingDistance.getThumb(0).setValue(mMaxDistance);
        mSliderSettingAge.getThumb(0).setValue(mMinAge);
        mSliderSettingAge.getThumb(1).setValue(mMaxAge);

        mSliderSettingAge.setOnThumbValueChangeListener(this);
        mSliderSettingDistance.setOnThumbValueChangeListener(this);

        mSwitchSettingNotifyPrivacy.setOnCheckedChangeListener(this);
    }


    private void bindData() {
        mTvSettingVersionname.setText(String.format("版本 %s", DeviceUtils.getVersionName(this)));

        if (UserUtil.isLoginValid(true) != null) {
            mBtnSettingLogout.setVisibility(View.VISIBLE);
        } else {
            mBtnSettingLogout.setVisibility(View.GONE);
        }
        if (mMaxAge < ConfigUtil.MAX_AGE) {
            mTvSettingAgerange.setText(String.format("%d-%d", mMinAge, mMaxAge));
        } else {
            mTvSettingAgerange.setText(String.format("%d-%d+", mMinAge, ConfigUtil.MAX_AGE));
        }
        if (mMaxDistance < ConfigUtil.MAX_DISTANCE) {
            mTvSettingDistance.setText(String.format("%dkm", mMaxDistance));
        } else {
            mTvSettingDistance.setText(String.format("%dkm+", ConfigUtil.MAX_DISTANCE));
        }

        mLlSettingPrivacyNotify.setOnClickListener(this);
        mLlSettingPrivacyNotifydetail.setOnClickListener(this);
        refreshSwitchStatus();
        refreshCacheSize();
    }

    private void refreshSwitchStatus() {
        mSwitchSettingNotifyPrivacy.setOnCheckedChangeListener(null);
        mSwitchSettingNotifyDetail.setOnCheckedChangeListener(null);
        if (ConfigUtil.getInstance().getShowNotify()) {
            mSwitchSettingNotifyPrivacy.setChecked(true);
            mLlSettingPrivacyNotifydetail.setVisibility(View.VISIBLE);
            if (ConfigUtil.getInstance().getShowNotifyDetail()) {
                mSwitchSettingNotifyDetail.setChecked(true);
            } else {
                mSwitchSettingNotifyDetail.setChecked(false);
            }
        } else {
            mSwitchSettingNotifyPrivacy.setChecked(false);
            mLlSettingPrivacyNotifydetail.setVisibility(View.GONE);
        }
        mSwitchSettingNotifyPrivacy.setOnCheckedChangeListener(this);
        mSwitchSettingNotifyDetail.setOnCheckedChangeListener(this);

    }

    private void refreshCacheSize() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                File glideCacheDir = Glide.getPhotoCacheDir(getApplicationContext());
                String cacheSize = "(0kb)";
                if (glideCacheDir != null) {
                    cacheSize = "(" + FileSizeUtil.getAutoFileOrFilesSize(glideCacheDir.getAbsolutePath()) + ")";
                }

                final String finalCacheSize = cacheSize;
                runOnUiThread(new Runnable() {
                    public void run() {
                        mTvSettingCacheSize.setText(finalCacheSize);
                    }
                });
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        if (view == mBtnSettingLogout) {
            //询问是否退出
            ArrayList<String> itemList = new ArrayList<>(1);
            itemList.add("退出登录");
            SelectItemIOSDialog selectItemIOSDialog = SelectItemIOSDialog.newInstance(itemList, "确定要退出吗？退出后你将在上次登录的位置对其他用户可见");
            selectItemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
                @Override
                public void onSelectItem(int position) {
                    //退出登录
                    DialogUtil.showIndeternimateDialog(SettingActivity.this, null);
                    UserUtil.logout().subscribe(new Subscriber<JsonRetEntity<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            DialogUtil.hideDialog(SettingActivity.this);
                            showErrToast(e);
                        }

                        @Override
                        public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                            DialogUtil.hideDialog(SettingActivity.this);
                            clearLoginInfo();
                            EMClient.getInstance().logout(false);
                            Intent intent = new Intent(SettingActivity.this, FirstPageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            MobclickAgent.onProfileSignOff();
                        }
                    });

                    bindData();
                }

                @Override
                public void onSelectNone() {

                }
            }, getFragmentManager(), null);


        } else if (view == mLlSettingFeedback) {
            //意见反馈
            FeedBackActivity.launchIntent(this);
        } else if (view == mLlSettingCache) {
            //清除缓存
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                    refreshCacheSize();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "清除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();

        } else if (view == mLlSettingPrivacyNotify) {
            mSwitchSettingNotifyPrivacy.performClick();
        } else if (view == mLlSettingPrivacyNotifydetail) {
            mSwitchSettingNotifyDetail.performClick();
        }
        super.onClick(view);
    }

    @Override
    public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
        if (multiSlider == mSliderSettingAge) {
            //年龄
            if (thumbIndex == 0) {
                //最小年龄
                mMinAge = value;
                ConfigUtil.getInstance().setPairMinAge(mMinAge);
            } else {
                mMaxAge = value;
                ConfigUtil.getInstance().setPairMaxAge(mMaxAge);
            }
        } else if (multiSlider == mSliderSettingDistance) {
            //距离
            mMaxDistance = value;
            ConfigUtil.getInstance().setPairMaxDistance(mMaxDistance);
        }
        bindData();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mSwitchSettingNotifyPrivacy) {
            //是否接收通知
            ConfigUtil.getInstance().setShowNotify(isChecked);
            refreshSwitchStatus();
        } else {
            ConfigUtil.getInstance().setShowNotifyDetail(isChecked);
        }
    }
}
