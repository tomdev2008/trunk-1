package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/31.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.activity.message.ChatActivity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.view.RoundAngleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PairSuccessActivity extends ActivityBase {
    @Bind(R.id.tv_pair_success_title)
    TextView mTvPairSuccessTitle;
    @Bind(R.id.riv_pair_success_lavatar)
    RoundAngleImageView mRivPairSuccessLavatar;
    @Bind(R.id.riv_pair_success_ravatar)
    RoundAngleImageView mRivPairSuccessRavatar;
    @Bind(R.id.btn_pair_success_chat)
    Button mBtnPairSuccessChat;
    @Bind(R.id.btn_pair_success_continue)
    Button mBtnPairSuccessContinue;
    private UserEntity mUserEntity;

    public static Intent launchIntent(Context context, UserEntity userEntity) {
        Intent intent = new Intent(context, PairSuccessActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_pairsuccess);
        ButterKnife.bind(this);
        requireTransStatusBar();
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);
        setupView(savedInstanceState);
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        Glide.with(this).load(UserUtil.getSavedUserInfoNotNull().getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivPairSuccessRavatar);
        if (mUserEntity != null) {
            Glide.with(this).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivPairSuccessLavatar);
            mTvPairSuccessTitle.setText(getString(R.string.abn_yueai_fmt_pair_success_title, mUserEntity.nickname));
        }
        mBtnPairSuccessChat.setOnClickListener(this);
        mBtnPairSuccessContinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == mBtnPairSuccessChat) {
            //聊天
            ChatActivity.launchIntent(this, null, mUserEntity, true);
            finish();
        } else if (view == mBtnPairSuccessContinue) {
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
            startActivity(intent);
        }
        super.onClick(view);
    }
}
