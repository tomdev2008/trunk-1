package com.aibinong.tantan.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.tantan.presenter.RecommendFragPresenter;
import com.aibinong.tantan.presenter.SayHiPresenter;
import com.aibinong.tantan.ui.adapter.PairCardsAdapter;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.apiInterface.ISearchFollow;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.view.RoundAngleImageView;
import com.gigamole.library.PulseView;
import com.huxq17.swipecardsview.SwipeCardsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tajchert.sample.DotsTextView;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class RecommendFragment extends FragmentBase implements ISearchList, ISearchFollow {


    @Bind(R.id.tv_fragment_recommend_last)
    TextView mTvFragmentRecommendLast;
    @Bind(R.id.ibtn_fragment_recommendSayhi)
    ImageView mIbtnFragmentRecommendSayhi;
    @Bind(R.id.tv_fragment_recommend_next)
    TextView mTvFragmentRecommendNext;
    @Bind(R.id.ll_fragment_pair_bottom)
    LinearLayout mLlFragmentPairBottom;
    @Bind(R.id.pv_fragment_pair_wave)
    PulseView mPvFragmentPairWave;
    @Bind(R.id.pv_fragment_pair_avatar)
    RoundAngleImageView mPvFragmentPairAvatar;
    @Bind(R.id.tv_fragment_pair_searching)
    TextView mTvFragmentPairSearching;
    @Bind(R.id.tv_fragment_pair_searching_loadingdots)
    DotsTextView mTvFragmentPairSearchingLoadingdots;
    @Bind(R.id.ll_fragment_pair_searching_tips)
    LinearLayout mLlFragmentPairSearchingTips;
    @Bind(R.id.fr_fragment_pair_wave)
    RelativeLayout mFrFragmentPairWave;
    @Bind(R.id.swipeView_fragment_pair_cards)
    SwipeCardsView mSwipeViewFragmentPairCards;
    private View mContentView;
    private RecommendFragPresenter mRecommendFragPresenter;
    private PairCardsAdapter mPairCardsAdapter;
    private SpringSystem springSystem;
    private Spring spring, likeSpring;
    private Runnable mAvatarScaleRb1, mLikeScaleRb1;
    private int curIndex;

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecommendFragPresenter = new RecommendFragPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity()).load(UserUtil.getSavedUserInfoNotNull().getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mPvFragmentPairAvatar);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Glide.with(getActivity()).load(UserUtil.getSavedUserInfoNotNull().getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mPvFragmentPairAvatar);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_pair, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                mRecommendFragPresenter.select_list(true, RecommendFragment.this);
            }
        });
        EventBus.getDefault().register(this);
        return mContentView;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mContentView.measure(View.MeasureSpec.makeMeasureSpec((int) (DeviceUtils.getScreenWidth(getActivity())), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec((int) (DeviceUtils.getScreenHeight(getActivity()) - getResources().getDimension(R.dimen.dim_actionbar_height_small)), View.MeasureSpec.EXACTLY));

        int maxCardWidth = mSwipeViewFragmentPairCards.getMeasuredWidth();
        int maxCardHeight = mSwipeViewFragmentPairCards.getMeasuredHeight();

        //把图片设置成1:1，然后重新设置卡片的大小
        View cardView = LayoutInflater.from(getActivity()).inflate(R.layout.item_pair_card, null);
        cardView.measure(View.MeasureSpec.makeMeasureSpec(maxCardWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(maxCardHeight, View.MeasureSpec.EXACTLY));
        LinearLayout cardBottom = (LinearLayout) cardView.findViewById(R.id.ll_item_pair_card_bottom);
        int cardBottomHeight = cardBottom.getMeasuredHeight();

        int finalCardWidth = maxCardWidth;
        int finalCardHeight = maxCardHeight;

        //看看全宽的时候是否超高了
        if (maxCardWidth + cardBottomHeight > maxCardHeight) {
            finalCardWidth = maxCardHeight - cardBottomHeight;
        } else {
            finalCardHeight = cardBottomHeight + maxCardWidth;
        }
        ViewGroup.LayoutParams vlp = mSwipeViewFragmentPairCards.getLayoutParams();
        vlp.width = finalCardWidth;
        vlp.height = finalCardHeight;
        mSwipeViewFragmentPairCards.setLayoutParams(vlp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContentView.setPadding(
                    mContentView.getPaddingLeft(),
                    mContentView.getPaddingTop() + DeviceUtils.getStatusBarHeight(getActivity()),
                    mContentView.getPaddingRight(),
                    mContentView.getPaddingBottom()
            );
        }

        mPairCardsAdapter = new PairCardsAdapter(finalCardWidth, finalCardHeight);

        mSwipeViewFragmentPairCards.setAdapter(mPairCardsAdapter);
        //whether retain last card,defalut false
        mSwipeViewFragmentPairCards.retainLastCard(false);
        //Pass false if you want to disable swipe feature,or do nothing.
        mSwipeViewFragmentPairCards.enableSwipe(true);
        //设置滑动监听
        mSwipeViewFragmentPairCards.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {
                curIndex = index;
                checkSayHiStatus();
            }

            @Override
            public void onNoChance(int index) {
                //没机会了
//                ((ActivityBase) getActivity()).showAppMsg(null, R.mipmap.abn_yueai_ic_main_like, getString(R.string.abn_yueai_no_chance_tolike), null, null);
            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                switch (type) {
                    case LEFT:
                        refreshStatus();

                        //检查一下是否需要加载下一页了
                        if (mPairCardsAdapter.getUserEntitiesList() == null || mPairCardsAdapter.getUserEntitiesList().size() <= 5 + curIndex) {
                            mRecommendFragPresenter.select_list(false, RecommendFragment.this);
                        }
                        break;
                    case RIGHT:

                        refreshStatus();
                        //检查一下是否需要加载下一页了
                        if (mPairCardsAdapter.getUserEntitiesList() == null || mPairCardsAdapter.getUserEntitiesList().size() <= 5 + curIndex) {
                            mRecommendFragPresenter.select_list(false, RecommendFragment.this);
                        }
                        break;
                }
            }

            @Override
            public void onItemClick(View cardImageView, int index) {

            }
        });


        mIbtnFragmentRecommendSayhi.setOnClickListener(this);
        mTvFragmentRecommendNext.setOnClickListener(this);
        mTvFragmentRecommendLast.setOnClickListener(this);

        // Create a system to run the physics loop for a set of springs.
        springSystem = SpringSystem.create();
        // Add a spring to the system.
        spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(60, 1));


        // Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mPvFragmentPairAvatar.setScaleX(value);
                mPvFragmentPairAvatar.setScaleY(value);
            }
        });
        spring.setCurrentValue(1.0);

        likeSpring = springSystem.createSpring();
        likeSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(10, 1));


        // Add a listener to observe the motion of the spring.
        likeSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mIbtnFragmentRecommendSayhi.setScaleX(value);
                mIbtnFragmentRecommendSayhi.setScaleY(value);
            }
        });
        likeSpring.setCurrentValue(1.0);


        mAvatarScaleRb1 = new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(1.0);
            }
        };

        mLikeScaleRb1 = new Runnable() {
            @Override
            public void run() {
                likeSpring.setCurrentValue(mIbtnFragmentRecommendSayhi.getScaleX());
                likeSpring.setEndValue(1.0);
            }
        };

        mPvFragmentPairAvatar.setOnClickListener(this);

        refreshStatus();
    }

    private void refreshStatus() {
        //如果卡片为空了，隐藏卡片和背景
        //显示loading
        if (mPairCardsAdapter.getCount() <= curIndex + 1) {
            mSwipeViewFragmentPairCards.setVisibility(View.INVISIBLE);
            mLlFragmentPairBottom.setVisibility(View.INVISIBLE);

            mFrFragmentPairWave.setVisibility(View.VISIBLE);
            mLlFragmentPairSearchingTips.setVisibility(View.VISIBLE);

            mPvFragmentPairWave.startPulse();
        } else {
            mSwipeViewFragmentPairCards.setVisibility(View.VISIBLE);
            mLlFragmentPairBottom.setVisibility(View.VISIBLE);

            mFrFragmentPairWave.setVisibility(View.GONE);
            mLlFragmentPairSearchingTips.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        spring.removeAllListeners();
        likeSpring.removeAllListeners();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPvFragmentPairAvatar.removeCallbacks(mAvatarScaleRb1);
        mIbtnFragmentRecommendSayhi.removeCallbacks(mLikeScaleRb1);

    }

    @Override
    public void onClick(View view) {
        if (view == mIbtnFragmentRecommendSayhi) {
            mIbtnFragmentRecommendSayhi.removeCallbacks(mLikeScaleRb1);
            likeSpring.setEndValue(0.9);
            mIbtnFragmentRecommendSayhi.postDelayed(mLikeScaleRb1, 200);
            //打招呼
            if (mPairCardsAdapter.getUserEntitiesList().size() > curIndex) {
                UserEntity userEntity = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
                SayHiPresenter.getInstance().sayHi(userEntity);

            }
            //喜欢
            /*if (mPairCardsAdapter.getCount() > 0) {
                mSwipeViewFragmentPairCards.slideCardOut(SwipeCardsView.SlideType.RIGHT);
            }*/
        } else if (view == mTvFragmentRecommendLast) {
            //上一个
            if (mSwipeViewFragmentPairCards.getmShowingIndex() > 0) {
                mSwipeViewFragmentPairCards.slideBack();
            } else {
                showToast("没有更多数据了");
            }

        } else if (view == mTvFragmentRecommendNext) {
            //下一个
            mSwipeViewFragmentPairCards.slideCardOut(SwipeCardsView.SlideType.RIGHT);
        } else if (view == mPvFragmentPairAvatar) {
            //动画，外加去加载

            mPvFragmentPairAvatar.removeCallbacks(mAvatarScaleRb1);
            spring.setEndValue(0.8);
            mPvFragmentPairAvatar.postDelayed(mAvatarScaleRb1, 200);
            mRecommendFragPresenter.select_list(false, RecommendFragment.this);
        }
    }

    @Override
    public void onListStart() {
        mTvFragmentPairSearchingLoadingdots.animate().cancel();
        mTvFragmentPairSearchingLoadingdots.animate().alpha(1.0f).setDuration(200).start();
        mTvFragmentPairSearching.setText(R.string.abn_yueai_pairfrag_searching);
    }

    @Override
    public void onListFailed(Throwable e) {
//        showErrToast(e);
        mTvFragmentPairSearchingLoadingdots.animate().cancel();
        mTvFragmentPairSearchingLoadingdots.animate().alpha(0).setDuration(400).start();
        mTvFragmentPairSearching.setText(e.getLocalizedMessage());
    }

    @Override
    public void onListSuccess(ArrayList<UserEntity> userEntities, Page page) {
        mTvFragmentPairSearchingLoadingdots.animate().cancel();
        mTvFragmentPairSearchingLoadingdots.animate().alpha(0).setDuration(400).start();
        if (userEntities.size() <= 0) {
            mTvFragmentPairSearching.setText(R.string.abn_yueai_pairfrag_searching_nomore);
        } else {
            mTvFragmentPairSearching.setText(R.string.abn_yueai_pairfrag_searching);
        }
        mPairCardsAdapter.getUserEntitiesList().addAll(userEntities);

        mSwipeViewFragmentPairCards.notifyDatasetChanged(curIndex);
        refreshStatus();
    }

    public void checkSayHiStatus() {
        if (mPairCardsAdapter.getUserEntitiesList().size() > curIndex) {
            UserEntity userEntity = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
            if (SqlBriteUtil.getInstance().getUserDb().isSaiedHi(userEntity.id)) {
                mIbtnFragmentRecommendSayhi.setEnabled(false);
            } else {
                mIbtnFragmentRecommendSayhi.setEnabled(true);
            }

        }
    }

    @Override
    public void onFollowStart() {
        DialogUtil.showIndeternimateDialog(getActivity(), null);
    }

    @Override
    public void onFollowFailed(Throwable e) {
        DialogUtil.hideDialog(getActivity());
        showErrToast(e);
    }

    @Override
    public void onFollowSuccess(String ret) {
        DialogUtil.hideDialog(getActivity());
        showToast("关注成功");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(BaseEvent<UserEntity> event) {
        UserEntity mCurrentUserEntity;
        if (mPairCardsAdapter.getUserEntitiesList().size() > curIndex) {
            mCurrentUserEntity = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
            if (event.data != null && mCurrentUserEntity != null && event.data instanceof UserEntity && event.data.id.equals(mCurrentUserEntity.id)) {
                if (BaseEvent.ACTION_SAYHI_FAILED.equals(event.action)) {
//                showToast("打招呼失败，请重试");
                } else if (BaseEvent.ACTION_SAYHI_SUCCESS.equals(event.action)) {
//                showToast("打招呼成功");
                    checkSayHiStatus();
                } else if (BaseEvent.ACTION_SAYHI_START.equals(event.action)) {

                }


            }
        }

    }
}
