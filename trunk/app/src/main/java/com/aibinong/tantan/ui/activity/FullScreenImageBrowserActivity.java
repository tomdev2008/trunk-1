package com.aibinong.tantan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.widget.MyViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fatalsignal.util.DeviceUtils;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import uk.co.senab.photoview.PhotoView;


public class FullScreenImageBrowserActivity extends ActivityBase {


    public static final String INTENT_EXTRA_KEY_IMAGEABLES = "INTENT_EXTRA_KEY_IMAGEABLES";
    public static final String INTENT_EXTRA_KEY_IMAGEBLUR = "INTENT_EXTRA_KEY_IMAGEBLUR";
    public static final String INTENT_EXTRA_KEY_IMAGES_SELECTEDPOS = "INTENT_EXTRA_KEY_IMAGES_SELECTEDPOS";
    @Bind(R.id.viewpager_fullscreen_imagebrowser)
    MyViewPager mViewpagerFullscreenImagebrowser;
    @Bind(R.id.viewpager_fullscreen_indicator)
    CirclePageIndicator mViewpagerFullscreenIndicator;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    private int Pageridnex;
    private boolean mIsBlur;
    private ArrayList<String> mImageUriList;
    private ExitActivityTransition exitTransition;

    public static Intent launchIntent(Context context, String url, boolean blur, boolean autoStart) {
        Intent intent = new Intent(context, FullScreenImageBrowserActivity.class);
        ArrayList<String> urlList = new ArrayList<>(1);
        urlList.add(url);
        intent.putExtra(INTENT_EXTRA_KEY_IMAGEABLES, urlList);
        intent.putExtra(INTENT_EXTRA_KEY_IMAGEBLUR, blur);
        if (autoStart) {
            context.startActivity(intent);
        }
        return intent;
    }

    public static Intent launchIntent(Context context, ArrayList<String> urls, int pos, boolean blur) {
        Intent intent = new Intent(context, FullScreenImageBrowserActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_IMAGEABLES, urls);
        intent.putExtra(INTENT_EXTRA_KEY_IMAGEBLUR, blur);
        intent.putExtra(INTENT_EXTRA_KEY_IMAGES_SELECTEDPOS, pos);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_fullscreen_imgview);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, null, true);
        requireTransStatusBar();

        if (getIntent() != null) {
            mImageUriList = (ArrayList<String>) getIntent().getSerializableExtra(INTENT_EXTRA_KEY_IMAGEABLES);
            Pageridnex = getIntent().getIntExtra(INTENT_EXTRA_KEY_IMAGES_SELECTEDPOS, 0);
            mIsBlur = getIntent().getBooleanExtra(INTENT_EXTRA_KEY_IMAGEBLUR
                    , false);
        }
        setupView(savedInstanceState);

        exitTransition = ActivityTransition.with(getIntent()).to(mViewpagerFullscreenImagebrowser).duration(400).start(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        SamplePagerAdapter mSamplePagerAdapter = new SamplePagerAdapter(this, mImageUriList, mIsBlur);
        mViewpagerFullscreenImagebrowser.setAdapter(mSamplePagerAdapter);
        mViewpagerFullscreenImagebrowser.setCurrentItem(Pageridnex);

        mViewpagerFullscreenIndicator.setViewPager(mViewpagerFullscreenImagebrowser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            FrameLayout.LayoutParams vlp = (FrameLayout.LayoutParams) mToolbar
                    .getLayoutParams();
            vlp.topMargin += DeviceUtils.getStatusBarHeight(this);
            mToolbar.setLayoutParams(vlp);
        }
        if (mImageUriList == null || mImageUriList.size() <= 1) {
            mViewpagerFullscreenIndicator.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        {
            super.onClick(v);
        }
    }


    static class SamplePagerAdapter extends PagerAdapter {
        private ArrayList<String> mImageUriList;
        private Activity mActivity;

        private HashMap<Integer, View> views = new HashMap<Integer, View>();
        private boolean mIsBlur;

        public SamplePagerAdapter(Activity activity,
                                  ArrayList<String> params, boolean isBlur) {
            mActivity = activity;
            mImageUriList = params;
            mIsBlur = isBlur;
        }

        @Override
        public int getCount() {
            if (mImageUriList == null) {
                return 0;
            }
            return mImageUriList.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mParams.get(position).getChinese();
//        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            View view = views.get(position);
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(
                        R.layout.abn_yueai_item_fullscreen_img, null);
            }

            PhotoView mPhotoview_item_fullscreen_image;
            TextView mTv_item_fullscreen_title;
            mPhotoview_item_fullscreen_image = (PhotoView) view
                    .findViewById(R.id.photoview_item_fullscreen_image);


            String imageUrl = mImageUriList.get(position);
            if (mIsBlur) {
                Glide.with(mActivity).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).bitmapTransform(new BlurTransformation(mActivity, 25)).into(mPhotoview_item_fullscreen_image);
            } else {
                Glide.with(mActivity).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mPhotoview_item_fullscreen_image);
            }


            container.addView(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
