package com.aibinong.tantan.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.PairFragPresenter;
import com.aibinong.tantan.ui.activity.ActivityBase;
import com.aibinong.tantan.ui.adapter.PairCardsAdapter;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tajchert.sample.DotsTextView;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class PairFragment extends FragmentBase implements PairFragPresenter.IPairFragPresenter {

    @Bind(R.id.iv_fragment_pair_backimage)
    ImageView mIvFragmentPairBackimage;
    @Bind(R.id.ibtn_fragment_pair_dislike)
    ImageButton mIbtnFragmentPairDislike;
    @Bind(R.id.ibtn_fragment_pair_like)
    ImageButton mIbtnFragmentPairLike;
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
    private PairFragPresenter mPairFragPresenter;
    //    private PeriodGridListAdapter mPeriodListGridAdapter;
    private PairCardsAdapter mPairCardsAdapter;

    private SpringSystem springSystem;
    private Spring spring, likeSpring, disLikeSpring;
    private Runnable mAvatarScaleRb1, mLikeScaleRb1, mDislikeScaleRb1;
    private BroadcastReceiver mLikeDislikeReceiver;
    private int curIndex;

    public static PairFragment newInstance() {
        PairFragment fragment = new PairFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPairFragPresenter = new PairFragPresenter(this);
        mLikeDislikeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isLike = intent.getBooleanExtra(IntentExtraKey.INTENT_EXTRA_KEY_IS_LIKE, false);
                String uuid = intent.getStringExtra(IntentExtraKey.INTENT_EXTRA_KEY_UUUID);
                if (mContentView != null && mPairCardsAdapter.getUserEntitiesList() != null && mPairCardsAdapter.getUserEntitiesList().size() > 0) {
                    //看看最上面一个是不是当前这个，是的话左右滑，否则直接删掉就好
                    UserEntity topUser = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
                    if (topUser.id.equals(uuid)) {
                        if (isLike) {
                            mIbtnFragmentPairLike.performClick();
                        } else {
                            mIbtnFragmentPairDislike.performClick();
                        }
                    } /*else {
                        List<UserEntity> userEntities = mPairCardsAdapter.getUserEntitiesList();
                        for (int i = 0; i < userEntities.size(); i++) {
                            UserEntity userEntity = userEntities.get(i);
                            if (userEntity.id.equals(uuid)) {
                                mPairCardsAdapter.getUserEntitiesList().remove(i);
                                mSwipeViewFragmentPairCards.notifyDatasetChanged(curIndex);
                                break;
                            }
                        }
                    }*/
                }
            }
        };
        mPairFragPresenter.userActive();
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
        mPairFragPresenter.getMembers();
        GlobalLocalBroadCastManager.getInstance().registerLikeOrDisLike(mLikeDislikeReceiver);
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                mPairFragPresenter.list();
            }
        });
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
            }

            @Override
            public void onNoChance(int index) {
                //没机会了
                ((ActivityBase) getActivity()).showAppMsg(null, R.mipmap.abn_yueai_ic_main_like, getString(R.string.abn_yueai_no_chance_tolike), null, null);
            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                switch (type) {
                    case LEFT:

                        //不喜欢
                        if (mPairCardsAdapter.getUserEntitiesList().size() > curIndex) {
                            UserEntity userEntity = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
                        }
                        refreshStatus();
                        //检查一下是否需要加载下一页了
                        if (mPairCardsAdapter.getUserEntitiesList() == null || mPairCardsAdapter.getUserEntitiesList().size() <= 5 + curIndex) {
                            mPairFragPresenter.list();
                        }
                        break;
                    case RIGHT:
                        if (mPairCardsAdapter.getUserEntitiesList().size() > curIndex) {
                            UserEntity userEntity = mPairCardsAdapter.getUserEntitiesList().get(curIndex);
                            mPairFragPresenter.like(userEntity);
                            ConfigUtil.getInstance().saveLikeTimesRemain(ConfigUtil.getInstance().getLikeTimes() - 1);//剩余喜欢次数－1
                            if (ConfigUtil.getInstance().getLikeTimes() <= 0) {
                                //不能再喜欢了
                                mSwipeViewFragmentPairCards.setNoChance(true);
                            }
                        }
                        refreshStatus();
                        //检查一下是否需要加载下一页了
                        if (mPairCardsAdapter.getUserEntitiesList() == null || mPairCardsAdapter.getUserEntitiesList().size() <= 5 + curIndex) {
                            mPairFragPresenter.list();
                        }
                        break;
                }
            }

            @Override
            public void onItemClick(View cardImageView, int index) {
                Toast.makeText(getActivity(), "点击了 position=" + index, Toast.LENGTH_SHORT).show();
            }
        });


        mIbtnFragmentPairLike.setOnClickListener(this);
        mIbtnFragmentPairDislike.setOnClickListener(this);


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
                mIbtnFragmentPairLike.setScaleX(value);
                mIbtnFragmentPairLike.setScaleY(value);
            }
        });
        likeSpring.setCurrentValue(1.0);

        disLikeSpring = springSystem.createSpring();
        disLikeSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(10, 1));


        // Add a listener to observe the motion of the spring.
        disLikeSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mIbtnFragmentPairDislike.setScaleX(value);
                mIbtnFragmentPairDislike.setScaleY(value);
            }
        });
        disLikeSpring.setCurrentValue(1.0);


        mAvatarScaleRb1 = new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(1.0);
            }
        };

        mLikeScaleRb1 = new Runnable() {
            @Override
            public void run() {
                likeSpring.setCurrentValue(mIbtnFragmentPairLike.getScaleX());
                likeSpring.setEndValue(1.0);
            }
        };

        mDislikeScaleRb1 = new Runnable() {
            @Override
            public void run() {
                disLikeSpring.setCurrentValue(mIbtnFragmentPairDislike.getScaleX());
                disLikeSpring.setEndValue(1.0);
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
            mIvFragmentPairBackimage.setVisibility(View.INVISIBLE);
            mLlFragmentPairBottom.setVisibility(View.INVISIBLE);

            mFrFragmentPairWave.setVisibility(View.VISIBLE);
            mLlFragmentPairSearchingTips.setVisibility(View.VISIBLE);

            mPvFragmentPairWave.startPulse();
        } else {
            mSwipeViewFragmentPairCards.setVisibility(View.VISIBLE);
            mIvFragmentPairBackimage.setVisibility(View.VISIBLE);
            mLlFragmentPairBottom.setVisibility(View.VISIBLE);

            mFrFragmentPairWave.setVisibility(View.GONE);
            mLlFragmentPairSearchingTips.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mLikeDislikeReceiver);
        spring.removeAllListeners();
        disLikeSpring.removeAllListeners();
        likeSpring.removeAllListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPvFragmentPairAvatar.removeCallbacks(mAvatarScaleRb1);
        mIbtnFragmentPairDislike.removeCallbacks(mDislikeScaleRb1);
        mIbtnFragmentPairLike.removeCallbacks(mLikeScaleRb1);

    }

    @Override
    public void onClick(View view) {
        if (view == mIbtnFragmentPairDislike) {
            //不喜欢
            if (mPairCardsAdapter.getCount() > 0) {
                mSwipeViewFragmentPairCards.slideCardOut(SwipeCardsView.SlideType.LEFT);
            }
            mIbtnFragmentPairDislike.removeCallbacks(mDislikeScaleRb1);
            disLikeSpring.setEndValue(0.9);
            mIbtnFragmentPairDislike.postDelayed(mDislikeScaleRb1, 200);
        } else if (view == mIbtnFragmentPairLike) {
            mIbtnFragmentPairLike.removeCallbacks(mLikeScaleRb1);
            likeSpring.setEndValue(0.9);
            mIbtnFragmentPairLike.postDelayed(mLikeScaleRb1, 200);

            if (mSwipeViewFragmentPairCards.isNoChance()) {
                //没机会了
                ((ActivityBase) getActivity()).showAppMsg(null, R.mipmap.abn_yueai_ic_main_like, getString(R.string.abn_yueai_no_chance_tolike), null, null);
                return;
            }
            //喜欢
            if (mPairCardsAdapter.getCount() > 0) {
                mSwipeViewFragmentPairCards.slideCardOut(SwipeCardsView.SlideType.RIGHT);
            }
        } else if (view == mPvFragmentPairAvatar) {
            //动画，外加去加载

            mPvFragmentPairAvatar.removeCallbacks(mAvatarScaleRb1);
            spring.setEndValue(0.8);
            mPvFragmentPairAvatar.postDelayed(mAvatarScaleRb1, 200);
            mPairFragPresenter.list();
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
    public void onListSuccess(ArrayList<UserEntity> userEntities) {
        mTvFragmentPairSearchingLoadingdots.animate().cancel();
        mTvFragmentPairSearchingLoadingdots.animate().alpha(0).setDuration(400).start();
        if (userEntities.size() <= 0) {
            mTvFragmentPairSearching.setText(R.string.abn_yueai_pairfrag_searching_nomore);
        } else {
            mTvFragmentPairSearching.setText(R.string.abn_yueai_pairfrag_searching);
        }
        mPairCardsAdapter.getUserEntitiesList().addAll(userEntities);
        if (ConfigUtil.getInstance().getLikeTimes() <= 0) {
            //不能再喜欢了
            mSwipeViewFragmentPairCards.setNoChance(true);
        }
        mSwipeViewFragmentPairCards.notifyDatasetChanged(curIndex);
        refreshStatus();
    }

    @Override
    public void onLikeFailed(Throwable e) {
        ConfigUtil.getInstance().saveLikeTimesRemain(ConfigUtil.getInstance().getLikeTimes() + 1);//剩余喜欢次数+1
    }

    @Override
    public void onLikeSuccess() {

    }

    @Override
    public void onGetMembersSuccess(List<MemberEntity> members) {
        mPairCardsAdapter.setMembers(members);
        mSwipeViewFragmentPairCards.notifyDatasetChanged(curIndex);
    }
}
