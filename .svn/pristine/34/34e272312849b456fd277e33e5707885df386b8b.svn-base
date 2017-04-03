package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/24.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.PrivacySettingPresenter;
import com.aibinong.tantan.ui.dialog.SelectItemIOSDialog;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrivacySettingActivity extends ActivityBase implements PrivacySettingPresenter.IPrivacySetting {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_privacy_setting_blurstatus)
    TextView mTvPrivacySettingBlurstatus;
    @Bind(R.id.ll_privacy_setting_blur)
    LinearLayout mLlPrivacySettingBlur;
    @Bind(R.id.tv_privacy_setting_receivelimit_level)
    TextView mTvPrivacySettingReceivelimitLevel;
    @Bind(R.id.ll_privacy_setting_receivelimit)
    LinearLayout mLlPrivacySettingReceivelimit;
    private PrivacySettingPresenter mPrivacySettingPresenter;

    public static Intent launchIntent(Context context) {
        Intent intent = new Intent(context, PrivacySettingActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_privacy_setting);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mPrivacySettingPresenter = new PrivacySettingPresenter(this);
        bindData();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLlPrivacySettingBlur.setOnClickListener(this);
        mLlPrivacySettingReceivelimit.setOnClickListener(this);
    }

    private void bindData() {
        UserEntity userEntity = UserUtil.getSavedUserInfoNotNull();
//        if (userEntity.atomization == 1) {
//            mTvPrivacySettingBlurstatus.setText(R.string.abn_yueai_on);
//        } else {
//            mTvPrivacySettingBlurstatus.setText(R.string.abn_yueai_off);
//        }
//        ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
//        if (configEntity != null && configEntity.members != null) {
//            for (MemberEntity memberEntity : configEntity.members) {
//                if (memberEntity.level == userEntity.messageLevel) {
//                    mTvPrivacySettingReceivelimitLevel.setText(memberEntity.name);
//                    break;
//                }
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLlPrivacySettingBlur) {
            //照片模糊
            ArrayList<String> items = new ArrayList<>(2);
            items.add("开启照片模糊");
            items.add("关闭照片模糊");
            SelectItemIOSDialog itemIOSDialog = SelectItemIOSDialog.newInstance(items);
            itemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
                @Override
                public void onSelectItem(int position) {
                    if (position == 0) {
                        //开
                        mPrivacySettingPresenter.switchBlur(true);
                    } else if (position == 1) {
                        //关
                        mPrivacySettingPresenter.switchBlur(false);
                    }
                }

                @Override
                public void onSelectNone() {

                }
            }, getFragmentManager(), null);
        } else if (view == mLlPrivacySettingReceivelimit) {
            //接收限制
            if (ConfigUtil.getInstance().getConfig() != null && ConfigUtil.getInstance().getConfig().members != null && ConfigUtil.getInstance().getConfig().members.size() > 0) {
                final ArrayList<MemberEntity> memberEntities = ConfigUtil.getInstance().getConfig().members;
                ArrayList<String> items = new ArrayList<>(2);
                for (int i = 0; i < memberEntities.size(); i++) {
                    items.add(memberEntities.get(i).name);
                }

                SelectItemIOSDialog itemIOSDialog = SelectItemIOSDialog.newInstance(items);
                itemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
                    @Override
                    public void onSelectItem(int position) {
                        MemberEntity memberEntity = memberEntities.get(position);
                        mPrivacySettingPresenter.setMsgLevel(memberEntity.level);
                    }

                    @Override
                    public void onSelectNone() {

                    }
                }, getFragmentManager(), null);
            }

        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onSwitchBlurSuccess(boolean blur) {
        UserEntity userEntity = UserUtil.getSavedUserInfoNotNull();
//        userEntity.atomization = blur ? 1 : 0;
        UserUtil.saveUserInfo(userEntity);
        bindData();
    }

    @Override
    public void onSwitchBlurFailed(ResponseResult e) {
        showErrToast(e);
    }

    @Override
    public void onSetMsgLevelSuccess(int level) {
        UserEntity userEntity = UserUtil.getSavedUserInfoNotNull();
//        userEntity.messageLevel = level;
        UserUtil.saveUserInfo(userEntity);
        bindData();
    }

    @Override
    public void onSetMsgLevelFailed(ResponseResult e) {
        showErrToast(e);
    }
}

