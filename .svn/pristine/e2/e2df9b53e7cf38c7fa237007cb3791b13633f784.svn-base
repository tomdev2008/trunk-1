package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends ActivityBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_splash);
        requireTransStatusBar();
        ConfigUtil.getInstance().requireRefresh(null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //判断是否登陆，没登录就去登录首页，否则看是否需要完善资料
                if (StringUtils.isEmpty(UserUtil.isLoginValid(true))) {
                    startActivity(FirstPageActivity.class);
                } else {
                    UserEntity userEntity = UserUtil.getSavedUserInfo();
                    if (userEntity != null && userEntity.status == 1) {
                        startActivity(MainActivity.class);
                    } else {
                        startActivity(FirstPageActivity.class);
                        startActivity(CompleteInfoActivity.class);
                    }
                }
                finish();
            }
        }, 1000);
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {

    }
}
