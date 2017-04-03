package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/19.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.UserDetailPresenter;
import com.aibinong.tantan.ui.adapter.PersonDetailGalleryAdapter;
import com.aibinong.tantan.ui.adapter.UserDetailGiftAdapter;
import com.aibinong.tantan.ui.dialog.SelectItemIOSDialog;
import com.aibinong.tantan.ui.widget.BlurTipsView;
import com.aibinong.tantan.ui.widget.CircleNoPageIndicator;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.lankton.flowlayout.FlowLayout;
import rx.Subscriber;

public class UserDetailActivity extends ActivityBase implements UserDetailPresenter.IuserDetail {
    public static final String INTENT_EXTRA_KEY_IMAGE_WH_RATIO = "INTENT_EXTRA_KEY_IMAGE_WH_RATIO";
    @Bind(R.id.vpg_userdetail_images)
    ConvenientBanner mVpgUserdetailImages;
    @Bind(R.id.cpi_userdetail_imgindicator)
    CircleNoPageIndicator mCpiUserdetailImgindicator;
    @Bind(R.id.tv_person_detail_age)
    TextView mTvPersonDetailAge;
    @Bind(R.id.tv_person_detail_constellation)
    TextView mTvPersonDetailConstellation;
    @Bind(R.id.tv_userdetail_name)
    TextView mTvUserdetailName;
    @Bind(R.id.iv_userdetail_sexsymbol)
    ImageView mIvUserdetailSexsymbol;
    @Bind(R.id.iv_userdetail_levelsymbol)
    ImageView mIvUserdetailLevelsymbol;
    @Bind(R.id.ll_userdetail_name)
    LinearLayout mLlUserdetailName;
    @Bind(R.id.tv_userdetail_location)
    TextView mTvUserdetailLocation;
    @Bind(R.id.tv_userdetail_activeTime)
    TextView mTvUserdetailActiveTime;
    @Bind(R.id.ll_userdetail_location_activetime)
    LinearLayout mLlUserdetailLocationActivetime;
    @Bind(R.id.tv_person_detail_sameinterest_count)
    TextView mTvPersonDetailSameinterestCount;
    @Bind(R.id.flowl_person_detail_same_interest)
    FlowLayout mFlowlPersonDetailSameInterest;
    @Bind(R.id.ll_userdetail_commontags)
    LinearLayout mLlUserdetailCommontags;
    @Bind(R.id.tv_person_detail_tags_count)
    TextView mTvPersonDetailTagsCount;
    @Bind(R.id.flowl_person_detail_mytags)
    FlowLayout mFlowlPersonDetailMytags;
    @Bind(R.id.parallaxScroll_userdetail_content)
    ParallaxScrollView mParallaxScrollUserdetailContent;
    @Bind(R.id.ibtn_person_detail_dislike)
    ImageButton mIbtnPersonDetailDislike;
    @Bind(R.id.ibtn_person_detail_like)
    ImageButton mIbtnPersonDetailLike;
    @Bind(R.id.ll_person_detail_bottom)
    LinearLayout mLlPersonDetailBottom;
    @Bind(R.id.tv_userdetail_job)
    TextView mTvUserdetailJob;
    @Bind(R.id.ll_userdetail_detailinfo)
    LinearLayout mLlUserdetailDetailinfo;
    @Bind(R.id.ibtn_userdetail_back)
    ImageButton mIbtnUserdetailBack;
    @Bind(R.id.ibtn_userdetail_edit)
    Button mIbtnUserdetailEdit;
    @Bind(R.id.recycler_userdetail_giftlist)
    RecyclerView mRecyclerUserdetailGiftlist;
    @Bind(R.id.iv_userdetail_big_viplogo)
    ImageView mIvUserdetailBigViplogo;
    @Bind(R.id.ll_userdetail_nogifts)
    LinearLayout mLlUserdetailNogifts;
    @Bind(R.id.btv_userdetail_blurtips)
    BlurTipsView mBtvUserdetailBlurtips;
    @Bind(R.id.ibtn_userdetail_report)
    Button mIbtnUserdetailReport;
    @Bind(R.id.tv_userdetail_declaration)
    TextView mTvUserdetailDeclaration;


    private ExitActivityTransition exitTransition;
    private float mImageWhRatio;/*顶部图片宽高比*/
    private UserEntity mCurrentUserEntity;
    private boolean mNeedBlur;
    private SpringSystem springSystem;
    private Spring spring_like, spring_disklike;
    private Runnable mLikeBtnRb, mDisLikeBtnRb;
    private UserEntity selfUserEntity;

    private UserDetailPresenter mUserDetailPresenter;
    private ArrayList<GiftEntity> mGiftEntities;
    private UserDetailGiftAdapter mUserDetailGiftAdapter;
    private int mFromSource;
    ArrayList<QuestionEntity.OptionsEntity> reportResons = new ArrayList<>();

    public static Intent launchIntent(Context context, UserEntity userEntity) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        requireTransStatusBar();
        ButterKnife.bind(this);
        mFromSource = getIntent().getIntExtra(IntentExtraKey.INTENT_EXTRA_KEY_DETAIL_SOURCE, SearchService.likeSource_main);
        mImageWhRatio = getIntent().getFloatExtra(INTENT_EXTRA_KEY_IMAGE_WH_RATIO, 0.8f);
        mCurrentUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);
        setTitle(mCurrentUserEntity.nickname);
        setupView(savedInstanceState);
        mUserDetailPresenter = new UserDetailPresenter(mCurrentUserEntity.id, this);
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                mUserDetailPresenter.gift_record_list();
            }
        });
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果是自己
        if (selfUserEntity != null && mCurrentUserEntity != null && selfUserEntity.id != null && selfUserEntity.id.equals(mCurrentUserEntity.id)) {
            selfUserEntity = UserUtil.getSavedUserInfoNotNull();
            mCurrentUserEntity = selfUserEntity;
        }
        bindData();
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        /*让顶部图片跟首页卡片比例一致*/

        ViewGroup.LayoutParams vlp = mVpgUserdetailImages.getLayoutParams();
        vlp.height = (int) (DeviceUtils.getScreenWidth(this) * Constant.IMAGE_CARD_RATIO_WH);
        mVpgUserdetailImages.setLayoutParams(vlp);
        mVpgUserdetailImages.setCanLoop(true);
        mVpgUserdetailImages.startTurning(5000);
        mVpgUserdetailImages.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        mVpgUserdetailImages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCpiUserdetailImgindicator.setCurrentItem(mVpgUserdetailImages.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
        mCpiUserdetailImgindicator.setCountGetter(new CircleNoPageIndicator.RealCountGetter() {
            @Override
            public int getRealCount() {
                return mVpgUserdetailImages.getViewPager().getAdapter().getRealCount();
            }

        });


        exitTransition = ActivityTransition.with(getIntent()).to(mVpgUserdetailImages).duration(300).start(savedInstanceState);

        mLlUserdetailDetailinfo.setY(DeviceUtils.getScreenHeight(getApplicationContext()));
        mLlUserdetailDetailinfo.animate().translationY(0).setDuration(400).setInterpolator(new DecelerateInterpolator(1.5f)).start();
        mIbtnUserdetailBack.setAlpha(0.0f);
        mIbtnUserdetailBack.animate().alpha(1.0f).setDuration(400).setInterpolator(new DecelerateInterpolator(1.5f)).start();

        mBtvUserdetailBlurtips.setAlpha(0.0f);
        mBtvUserdetailBlurtips.animate().alpha(1.0f).setDuration(400).setInterpolator(new DecelerateInterpolator(1.5f)).start();

        mCpiUserdetailImgindicator.setAlpha(0.0f);
        mCpiUserdetailImgindicator.animate().alpha(1.0f).setDuration(400).setInterpolator(new DecelerateInterpolator(1.5f)).start();
        mLlPersonDetailBottom.setY(mLlPersonDetailBottom.getY() + 100 * DeviceUtils.getScreenDensity(this));
        mLlPersonDetailBottom.animate().translationYBy(-100 * DeviceUtils.getScreenDensity(this)).setDuration(400).setInterpolator(new AnticipateInterpolator()).start();


        mIbtnPersonDetailDislike.setOnClickListener(this);
        mIbtnPersonDetailLike.setOnClickListener(this);
        mIbtnUserdetailBack.setOnClickListener(this);
        mIbtnUserdetailEdit.setOnClickListener(this);
        mIbtnUserdetailReport.setOnClickListener(this);


        // Create a system to run the physics loop for a set of springs.
        springSystem = SpringSystem.create();
        // Add a spring to the system.
        spring_like = springSystem.createSpring();
        spring_like.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(60, 1));


        // Add a listener to observe the motion of the spring.
        spring_like.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mIbtnPersonDetailLike.setScaleX(value);
                mIbtnPersonDetailLike.setScaleY(value);
            }
        });
        spring_disklike = springSystem.createSpring();
        spring_disklike.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(60, 1));


        // Add a listener to observe the motion of the spring.
        spring_disklike.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mIbtnPersonDetailDislike.setScaleX(value);
                mIbtnPersonDetailDislike.setScaleY(value);
            }
        });

        spring_like.setCurrentValue(1.0);
        spring_disklike.setCurrentValue(1.0);
        mLikeBtnRb = new Runnable() {
            @Override
            public void run() {
                spring_like.setEndValue(1.0);
            }
        };

        mDisLikeBtnRb = new Runnable() {
            @Override
            public void run() {
                spring_disklike.setEndValue(1.0);
            }
        };

        mUserDetailGiftAdapter = new UserDetailGiftAdapter();
        mRecyclerUserdetailGiftlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerUserdetailGiftlist.setAdapter(mUserDetailGiftAdapter);

        mBtvUserdetailBlurtips.setOnClickListener(this);
    }

    private void bindData() {
        selfUserEntity = UserUtil.getSavedUserInfoNotNull();

        mTvUserdetailDeclaration.setText(mCurrentUserEntity.declaration);

        if (mCurrentUserEntity.pictureList == null || mCurrentUserEntity.pictureList.size() <= 1) {
            mVpgUserdetailImages.setCanLoop(false);
            mCpiUserdetailImgindicator.setVisibility(View.GONE);
        } else {
            mVpgUserdetailImages.setCanLoop(true);
            mCpiUserdetailImgindicator.setVisibility(View.VISIBLE);
        }
        mVpgUserdetailImages.stopTurning();
        mNeedBlur = false;
        if (!mCurrentUserEntity.id.equals(selfUserEntity.id) && mCurrentUserEntity.atomization == 1 && selfUserEntity.memberLevel <= 0) {
            mNeedBlur = true;
        }
        if (mCurrentUserEntity.id.equals(selfUserEntity.id)) {
            mNeedBlur = false;
            mIbtnUserdetailEdit.setVisibility(View.VISIBLE);
            mIbtnUserdetailReport.setVisibility(View.INVISIBLE);
        } else {
            mIbtnUserdetailEdit.setVisibility(View.INVISIBLE);
            if (mCurrentUserEntity.helper != 1 && ConfigUtil.getInstance().getConfig() != null && ConfigUtil.getInstance().getConfig().reportReasons != null) {
                reportResons = ConfigUtil.getInstance().getConfig().reportReasons;
                if (reportResons.size() > 0) {
                    mIbtnUserdetailReport.setVisibility(View.VISIBLE);
                }
            }
        }
        int oldPos = mVpgUserdetailImages.getCurrentItem();
        mVpgUserdetailImages.setPages(new CBViewHolderCreator<PersonDetailGalleryAdapter.DetailImageHolderView>() {
            @Override
            public PersonDetailGalleryAdapter.DetailImageHolderView createHolder() {
                return new PersonDetailGalleryAdapter.DetailImageHolderView(mNeedBlur);
            }
        }, mCurrentUserEntity.pictureList);
        mVpgUserdetailImages.setcurrentitem(oldPos);


        mTvPersonDetailAge.setText(mCurrentUserEntity.age + "");
        mTvPersonDetailConstellation.setText(mCurrentUserEntity.constellation);


        mTvUserdetailName.setText(mCurrentUserEntity.nickname);
        if (!StringUtils.isEmpty(mCurrentUserEntity.distance)) {
            mTvUserdetailLocation.setVisibility(View.VISIBLE);
            mTvUserdetailLocation.setText(String.format("%s,", mCurrentUserEntity.distance));
        } else {
            mTvUserdetailLocation.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(mCurrentUserEntity.activeTime)) {
            mTvUserdetailActiveTime.setVisibility(View.VISIBLE);
            mTvUserdetailActiveTime.setText(String.format("%s活跃", TimeUtil.getRelaStrStamp(mCurrentUserEntity.activeTime)));
        } else {
            mTvUserdetailActiveTime.setVisibility(View.GONE);
        }
        mTvUserdetailJob.setText(mCurrentUserEntity.occupation);
        if (mCurrentUserEntity.sex == UserEntity.SEX_FEMALE) {
            mIvUserdetailSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_female);
        } else {
            mIvUserdetailSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_male);
        }
        mIvUserdetailLevelsymbol.setVisibility(View.INVISIBLE);
        mIvUserdetailBigViplogo.setVisibility(View.GONE);
        ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
        if (configEntity != null && configEntity.members != null && configEntity.members.size() > 0) {
            for (MemberEntity memberEntity : configEntity.members) {
                if (!StringUtils.isEmpty(memberEntity.icon) && memberEntity.level == mCurrentUserEntity.memberLevel) {
                    mIvUserdetailLevelsymbol.setVisibility(View.VISIBLE);
                    Glide.with(UserDetailActivity.this).load(memberEntity.icon).into(mIvUserdetailLevelsymbol);
                    mIvUserdetailBigViplogo.setVisibility(View.VISIBLE);
                    Glide.with(this).load(memberEntity.signIcon).into(mIvUserdetailBigViplogo);
                    break;
                }
            }
        }

        mParallaxScrollUserdetailContent.measure(View.MeasureSpec.makeMeasureSpec(DeviceUtils.getScreenWidth(this), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(DeviceUtils.getScreenHeight(this), View.MeasureSpec.EXACTLY));
        mFlowlPersonDetailSameInterest.removeAllViews();
        if (mCurrentUserEntity.commons != null && mCurrentUserEntity.commons.size() > 0) {
            mLlUserdetailCommontags.setVisibility(View.VISIBLE);
            mTvPersonDetailSameinterestCount.setText(mCurrentUserEntity.commons.size() + "");
            for (int i = 0; i < mCurrentUserEntity.commons.size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.item_orange_tag, mFlowlPersonDetailSameInterest, false);
                TextView tagTextView = (TextView) view.findViewById(R.id.tv_item_orange_tag);
                tagTextView.setText(mCurrentUserEntity.tags.get(i));
                ViewGroup.LayoutParams vlp = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tagTextView.setMinimumWidth((int) (56 * DeviceUtils.getScreenDensity(this)));
                mFlowlPersonDetailSameInterest.addView(view, vlp);
            }
        } else {
            mLlUserdetailCommontags.setVisibility(View.GONE);
        }

        mFlowlPersonDetailMytags.removeAllViews();
        if (mCurrentUserEntity.tags != null && mCurrentUserEntity.tags.size() > 0) {
            mFlowlPersonDetailMytags.setVisibility(View.VISIBLE);
            mTvPersonDetailTagsCount.setText(mCurrentUserEntity.tags.size() + "");
            for (int i = 0; i < mCurrentUserEntity.tags.size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.item_orange_tag, mFlowlPersonDetailMytags, false);
                TextView tagTextView = (TextView) view.findViewById(R.id.tv_item_orange_tag);
                tagTextView.setText(mCurrentUserEntity.tags.get(i));
                ViewGroup.LayoutParams vlp = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tagTextView.setMinimumWidth((int) (56 * DeviceUtils.getScreenDensity(this)));
                mFlowlPersonDetailMytags.addView(view, vlp);
            }
        } else {
            mFlowlPersonDetailMytags.setVisibility(View.GONE);
        }

        mFlowlPersonDetailMytags.post(new Runnable() {
            @Override
            public void run() {
//                mFlowlPersonDetailMytags.relayoutToCompressAndAlign();
//                mFlowlPersonDetailSameInterest.relayoutToCompressAndAlign();

                mFlowlPersonDetailMytags.relayoutToCompress();
                mFlowlPersonDetailSameInterest.relayoutToCompress();

            }
        });

        //如果是自己，或者已经喜欢过的人，隐藏底部按钮

        if (mCurrentUserEntity.id.equals(selfUserEntity.id) || !StringUtils.isEmpty(mCurrentUserEntity.matchingTime)) {
            mLlPersonDetailBottom.setVisibility(View.GONE);
        } else {
            mLlPersonDetailBottom.setVisibility(View.VISIBLE);
        }


        if (!mCurrentUserEntity.id.equals(selfUserEntity.id) && selfUserEntity.memberLevel <= 0 && mCurrentUserEntity.atomization != 0) {
            //模糊

            mBtvUserdetailBlurtips.setVisibility(View.VISIBLE);
        } else {

            mBtvUserdetailBlurtips.setVisibility(View.INVISIBLE);
        }

        bindGifts();
    }

    private void bindGifts() {
        if (mGiftEntities == null || mGiftEntities.size() <= 0) {
            mLlUserdetailNogifts.setVisibility(View.VISIBLE);
        } else {
            mLlUserdetailNogifts.setVisibility(View.GONE);
            mUserDetailGiftAdapter.setGiftEntities(mGiftEntities);
            mUserDetailGiftAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBtvUserdetailBlurtips) {
            CommonWebActivity.launchIntent(this, Constant._sUrl_vip_buy);
        } else if (view == mIbtnPersonDetailDislike) {
            //不喜欢
//            mIbtnPersonDetailDislike.removeCallbacks(mDisLikeBtnRb);
//            spring_disklike.setEndValue(0.8);
//            mIbtnPersonDetailDislike.postDelayed(mDisLikeBtnRb, 200);
            mIbtnPersonDetailDislike.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 300);
            mIbtnPersonDetailDislike.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GlobalLocalBroadCastManager.getInstance().notifyLikeOrDisLike(mCurrentUserEntity.id, false);
                }
            }, 800);
        } else if (view == mIbtnPersonDetailLike) {
            //喜欢
//            mIbtnPersonDetailLike.removeCallbacks(mLikeBtnRb);
//            spring_like.setEndValue(0.8);
//            mIbtnPersonDetailLike.postDelayed(mLikeBtnRb, 200);

            if (mFromSource == SearchService.likeSource_main && ConfigUtil.getInstance().getLikeTimes() <= 0) {
                //没机会了
                showAppMsg(null, R.mipmap.abn_yueai_ic_main_like, getString(R.string.abn_yueai_no_chance_tolike), null, null);
                return;
            }

            /*喜欢次数－1*/
            ConfigUtil.getInstance().saveLikeTimesRemain(ConfigUtil.getInstance().getLikeTimes() - 1);
            ApiHelper
                    .getInstance()
                    .create(SearchService.class)
                    .like(mCurrentUserEntity.id, mFromSource)
                    .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                    .subscribe(
                            new Subscriber<JsonRetEntity<String>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    /*喜欢次数＋1*/
                                    if (mFromSource == SearchService.likeSource_main) {
                                        ConfigUtil.getInstance().saveLikeTimesRemain(ConfigUtil.getInstance().getLikeTimes() + 1);
                                    }
                                }

                                @Override
                                public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                }
                            }
                    );

            mIbtnPersonDetailLike.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 300);
            mIbtnPersonDetailLike.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GlobalLocalBroadCastManager.getInstance().notifyLikeOrDisLike(mCurrentUserEntity.id, true);
                }
            }, 800);
        } else if (view == mIbtnUserdetailBack) {
            onBackPressed();
        } else if (view == mIbtnUserdetailEdit) {
            startActivity(UserInfoEditActivity.class);
        } else if (view == mIbtnUserdetailReport) {
            //举报
            showReportDialog();
        } else {
            super.onClick(view);
        }
    }

    private void showReportDialog() {
        if (reportResons == null || reportResons.size() <= 0) {
            return;
        }
        ArrayList<String> resons = new ArrayList<>(reportResons.size());
        for (QuestionEntity.OptionsEntity optionsEntity : reportResons) {
            resons.add(optionsEntity.content);
        }
        SelectItemIOSDialog itemIOSDialog = SelectItemIOSDialog.newInstance(resons);
        itemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
            @Override
            public void onSelectItem(int position) {
                if (position < reportResons.size()) {
                    QuestionEntity.OptionsEntity optionsEntity = reportResons.get(position);
                    DialogUtil.showIndeternimateDialog(UserDetailActivity.this, null);
                    mUserDetailPresenter.report(optionsEntity);
                }
            }

            @Override
            public void onSelectNone() {

            }
        }, getFragmentManager(), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mLlUserdetailDetailinfo.animate().translationY(DeviceUtils.getScreenHeight(getApplicationContext())).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        mIbtnUserdetailBack.animate().alpha(0.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        mBtvUserdetailBlurtips.animate().alpha(0.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();

        mIbtnPersonDetailDislike.animate().scaleX(0).scaleY(0).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
        mIbtnPersonDetailLike.animate().scaleX(0).scaleY(0).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
        mCpiUserdetailImgindicator.animate().alpha(0.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        exitTransition.exit(this);
//        finish();
    }

    @Override
    public void onGetGiftListSuccess(ArrayList<GiftEntity> giftEntities) {
        mGiftEntities = giftEntities;
        bindGifts();
    }

    @Override
    public void onGetGiftListFailed(ResponseResult e) {

    }

    @Override
    public void onReportSuccess() {
        DialogUtil.hideDialog(this);
        showErrToast(new ResponseResult(-1, "举报成功"));
    }

    @Override
    public void onReportFailed(ResponseResult e) {
        DialogUtil.hideDialog(this);
        showErrToast(e);
    }
}
