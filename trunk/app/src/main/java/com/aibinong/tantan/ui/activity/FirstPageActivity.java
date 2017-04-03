//package com.aibinong.tantan.ui.activity;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/11/2.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.aibinong.tantan.R;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class FirstPageActivity extends ActivityBase {
//
//
//    @Bind(R.id.iv_firstpage_bg)
//    ImageView mIvFirstpageBg;
//    @Bind(R.id.iv_firstpage_bg2)
//    ImageView mIvFirstpageBg2;
//    @Bind(R.id.btn_firstpage_login)
//    Button mBtnFirstpageLogin;
//    @Bind(R.id.btn_firstpage_register)
//    TextView mBtnFirstpageRegister;
//    int[] images = new int[]{R.mipmap.abn_yueai_firstpage_bg1, R.mipmap.abn_yueai_firstpage_bg2, R.mipmap.abn_yueai_firstpage_bg3};
//    ImageView[] mImageViews = new ImageView[2];
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.abn_yueai_activity_firstpage);
//        ButterKnife.bind(this);
//
//        mImageViews[0] = mIvFirstpageBg;
//        mImageViews[1] = mIvFirstpageBg2;
//
//        requireTransStatusBar();
//        setupView(savedInstanceState);
//
//    }
//
//    @Override
//    protected void setupView(@Nullable Bundle savedInstanceState) {
//        mBtnFirstpageLogin.setOnClickListener(this);
//        mBtnFirstpageRegister.setOnClickListener(this);
//
//        mIvFirstpageBg2.setImageResource(R.mipmap.abn_yueai_firstpage_bgtemp);
////        startAnimation();
//    }
//
//    private void startAnimation() {
//        //第一张图慢慢变淡,到全透明的时候，换成下一张张图，并且恢复不透明，第二张图换成下下张图
//        int imagePos = 0, imagePos0 = 1;
//        Object tag = mImageViews[1].getTag(R.id.common_tag);
//        if (tag != null && tag instanceof Integer) {
//            imagePos = ((Integer) tag).intValue();
//        }
//        mImageViews[1].setImageResource(images[(imagePos) % 3]);
//        mImageViews[1].setTag(R.id.common_tag, imagePos % 3 + 1);
//        Object tag0 = mImageViews[0].getTag(R.id.common_tag);
//        if (tag0 != null && tag0 instanceof Integer) {
//            imagePos0 = ((Integer) tag0).intValue();
//        }
//        mImageViews[0].setImageResource(images[(imagePos0) % 3]);
//        mImageViews[0].setTag(R.id.common_tag, imagePos0 % 3 + 1);
//
//        mImageViews[1].animate().alpha(0.0f).setDuration(2000).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                mImageViews[1].setAlpha(1.0f);
//                startAnimation();
//            }
//        }).start();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == mBtnFirstpageLogin) {
//            RegisterActivity.launchIntent(this, true);
//        } else if (view == mBtnFirstpageRegister) {
//            //注册
//            RegisterActivity.launchIntent(this, true);
//        } else {
//            super.onClick(view);
//        }
//    }
//}
