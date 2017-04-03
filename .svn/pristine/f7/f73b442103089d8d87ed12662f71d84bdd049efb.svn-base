package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/8.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VipActivity extends ActivityBase {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ll_vip_buymonth)
    LinearLayout mLlVipBuymonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_vip);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
    }
    @Override
    protected boolean swipeBackEnable() {
        return true;
    }
    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLlVipBuymonth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLlVipBuymonth) {

        } else {
            super.onClick(view);
        }
    }
}
