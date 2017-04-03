package com.aibinong.tantan.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.ShakePresenter;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.view.RoundAngleImageView;
import com.gigamole.library.PulseView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import butterknife.Bind;
import butterknife.ButterKnife;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class ShakeFragment extends FragmentBase implements ShakePresenter.IShake {


    @Bind(R.id.iv_shake_hand)
    ImageView mIvShakeHand;
    @Bind(R.id.fr_shake_anim_frame)
    FrameLayout mFrShakeAnimFrame;
    @Bind(R.id.riv_shake_user_avatar)
    RoundAngleImageView mRivShakeUserAvatar;
    @Bind(R.id.tv_shake_user_name)
    TextView mTvShakeUserName;
    @Bind(R.id.iv_shake_user_sex)
    ImageView mIvShakeUserSex;
    @Bind(R.id.tv_shake_user_age)
    TextView mTvShakeUserAge;
    @Bind(R.id.tv_shake_user_constelation)
    TextView mTvShakeUserConstelation;
    @Bind(R.id.ll_shake_user)
    LinearLayout mLlShakeUser;
    @Bind(R.id.pv_fragment_shake_wave)
    PulseView mPvFragmentShakeWave;
    @Bind(R.id.tv_shake_searching)
    TextView mTvShakeSearching;
    @Bind(R.id.view_fragment_shake_circle)
    View mViewFragmentShakeCircle;
    @Bind(R.id.iv_shake_memberlevel)
    ImageView mIvShakeMemberlevel;
    private View mContentView;
    private ShakePresenter mShakePresenter;
    private SensorManager mSensorManager;
    private SoundPool soundPool;
    private int shakeSound;
    private int shakeResultSound;
    private SpringSystem springSystem;
    private Spring spring;
    private Runnable mAvatarScaleRb1;

    public static ShakeFragment newInstance() {
        ShakeFragment fragment = new ShakeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mShakePresenter = new ShakePresenter(this, mSensorManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_shake, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    boolean mIsHidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mShakePresenter.unRegShakeListener();
        } else {
            mShakePresenter.registerShakeListener();
        }
        mIsHidden = hidden;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsHidden) {
            mShakePresenter.registerShakeListener();
        }
    }

    @Override
    public void onPause() {
        mShakePresenter.unRegShakeListener();
        super.onPause();
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLlShakeUser.setOnClickListener(this);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        mPvFragmentShakeWave.startPulse();

        //载入音频流，返回在池中的id
        shakeSound = soundPool.load(getActivity(), R.raw.shake, 0);
        shakeResultSound = soundPool.load(getActivity(), R.raw.shake_result, 0);

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
                mViewFragmentShakeCircle.setScaleX(value);
                mViewFragmentShakeCircle.setScaleY(value);
            }
        });
        spring.setCurrentValue(1.0);
        mAvatarScaleRb1 = new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(1.0);
            }
        };

        mViewFragmentShakeCircle.setOnClickListener(this);
    }

    private UserEntity mUserEntity;

    private void bindData() {
        if (mUserEntity != null) {
            mLlShakeUser.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivShakeUserAvatar);
            mTvShakeUserName.setText(mUserEntity.nickname);
            if (mUserEntity.sex == UserEntity.SEX_MALE) {
                mIvShakeUserSex.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
            } else {
                mIvShakeUserSex.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
            }
            mTvShakeUserAge.setText(mUserEntity.age + "");
            mTvShakeUserConstelation.setText(mUserEntity.constellation);
        } else {
            mLlShakeUser.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLlShakeUser) {
            if (mUserEntity != null) {
                //进入个人详情
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_DETAIL_SOURCE, SearchService.likeSource_shake);
                ActivityTransitionLauncher.with((Activity) view.getContext()).from(mRivShakeUserAvatar).launch(intent);
            }
        } else if (view == mViewFragmentShakeCircle) {
            mViewFragmentShakeCircle.removeCallbacks(mAvatarScaleRb1);
            spring.setEndValue(0.8);
            mViewFragmentShakeCircle.postDelayed(mAvatarScaleRb1, 200);
        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onShake() {


        mIvShakeHand.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_hand_shake));

        soundPool.play(shakeSound, 1, 1, 0, 0, 1);

        if (mLlShakeUser.getVisibility() == View.VISIBLE) {
            mLlShakeUser.setTranslationY(0);
            mLlShakeUser.animate().translationY(3 * mLlShakeUser.getHeight()).setInterpolator(new DecelerateInterpolator()).setDuration(800).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mLlShakeUser.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        mShakePresenter.shake();
    }

    @Override
    public void onShakeFailed(Throwable e) {
        showErrToast(e);
    }

    @Override
    public void onShakeSuccess(UserEntity userEntity) {
        mUserEntity = userEntity;
        if (mUserEntity != null) {
            soundPool.stop(shakeSound);
            soundPool.play(shakeResultSound, 1, 1, 0, 0, 1);

            //如果摇到人
            mLlShakeUser.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivShakeUserAvatar);
            mTvShakeUserName.setText(mUserEntity.nickname);
            if (mUserEntity.sex == UserEntity.SEX_MALE) {
                mIvShakeUserSex.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
            } else {
                mIvShakeUserSex.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
            }
            mTvShakeUserAge.setText(mUserEntity.age + "");
            mTvShakeUserConstelation.setText(mUserEntity.constellation);

            //会员等级
            mIvShakeMemberlevel.setVisibility(View.GONE);
            ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
            if (configEntity != null && configEntity.members != null) {
                for (MemberEntity memberEntity : configEntity.members) {
                    if (memberEntity.level == mUserEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            mIvShakeMemberlevel.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(memberEntity.icon).into(mIvShakeMemberlevel);
                        }
                        break;
                    }
                }
            }

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_shake_onpeople);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setFillAfter(false);
            animation.setFillBefore(true);

            mLlShakeUser.setTranslationY(0);
            mLlShakeUser.animate().cancel();
            mLlShakeUser.startAnimation(animation);
        } else {
            mLlShakeUser.setVisibility(View.INVISIBLE);
//            ((ActivityBase) getActivity()).showAppMsg(null, 0, null, "亲，没摇到人，再试试哦", null);
        }


    }
}
