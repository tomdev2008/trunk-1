package com.aibinong.tantan.ui.widget;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.UserEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationView extends FrameLayout {
    @Bind(R.id.tv_auth_verify_vip)
    TextView mTvAuthVerifyVip;
    @Bind(R.id.tv_auth_verify_phone)
    TextView mTvAuthVerifyPhone;
    @Bind(R.id.tv_auth_verify_wx)
    TextView mTvAuthVerifyWx;
//    @Bind(R.id.tv_auth_verify_video)
//    TextView mTvAuthVerifyVideo;

    public AuthenticationView(Context context) {
        super(context);
        init();
    }

    public AuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AuthenticationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AuthenticationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_view_authentication, this, true);
        ButterKnife.bind(this);
    }

    public void bindUserEntity(UserEntity userEntity) {
        if (userEntity != null && userEntity.cert != null) {
//            mTvAuthVerifyVip.setEnabled("1".equals(userEntity.cert.vip));
            mTvAuthVerifyPhone.setEnabled("1".equals(userEntity.cert.mobile));
            mTvAuthVerifyWx.setEnabled("1".equals(userEntity.cert.wechat));
        } else {
//            mTvAuthVerifyVip.setEnabled(false);
            mTvAuthVerifyPhone.setEnabled(false);
//            mTvAuthVerifyVideo.setEnabled(false);
            mTvAuthVerifyWx.setEnabled(false);
        }
    }
}
