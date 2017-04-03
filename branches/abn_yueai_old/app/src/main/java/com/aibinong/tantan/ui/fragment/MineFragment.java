package com.aibinong.tantan.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.MineFragPresenter;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.PrivacySettingActivity;
import com.aibinong.tantan.ui.activity.SettingActivity;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.activity.UserInfoEditActivity;
import com.aibinong.tantan.ui.activity.WhoLikeMeActivity;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class MineFragment extends FragmentBase implements PtrClassicFrameLayout.MyScrollViewListener, MineFragPresenter.IMineFrag {


    @Bind(R.id.iv_fragment_mine_backimage)
    ImageView mIvFragmentMineBackimage;
    @Bind(R.id.iv_fragment_mine_maskimage)
    ImageView mIvFragmentMineMaskimage;
    @Bind(R.id.riv_fragment_mine_avatar)
    RoundAngleImageView mRivFragmentMineAvatar;
    @Bind(R.id.iv_mine_level_symbol)
    ImageView mIvMineLevelSymbol;
    @Bind(R.id.fl_mine_avatar)
    FrameLayout mFlMineAvatar;
    @Bind(R.id.tv_fragment_mine_name)
    TextView mTvFragmentMineName;
    @Bind(R.id.tv_mine_top_age)
    TextView mTvMineTopAge;
    @Bind(R.id.tv_mine_top_constellation)
    TextView mTvMineTopConstellation;
    @Bind(R.id.tv_mine_top_occupation)
    TextView mTvMineTopOccupation;
    @Bind(R.id.ll_mine_top_info)
    LinearLayout mLlMineTopInfo;
    @Bind(R.id.ibtn_fragment_mine_edit)
    ImageButton mIbtnFragmentMineEdit;
    @Bind(R.id.ibtn_mine_vip_buy)
    ImageView mIbtnMineVipBuy;
    @Bind(R.id.ll_fragment_mine_item_vip)
    LinearLayout mLlFragmentMineItemVip;
    @Bind(R.id.ll_fragment_mine_likeme)
    LinearLayout mLlFragmentMineLikeme;
    @Bind(R.id.ll_fragment_mine_item_setting)
    LinearLayout mLlFragmentMineItemSetting;
    @Bind(R.id.ll_fragment_mine_item_privacy)
    LinearLayout mLlFragmentMineItemPrivacy;
    @Bind(R.id.ll_fragment_mine_item_help)
    LinearLayout mLlFragmentMineItemHelp;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mRotateHeaderListViewFrame;
    @Bind(R.id.rl_mine_top)
    RelativeLayout mRlMineTop;
    @Bind(R.id.iv_fragment_mine_sexsymbol)
    ImageView mIvFragmentMineSexsymbol;
    @Bind(R.id.tv_fragment_mine_fanscount)
    TextView mTvFragmentMineFanscount;
    @Bind(R.id.tv_mine_vip_validdata)
    TextView mTvMineVipValiddata;
    @Bind(R.id.ll_mine_viptips)
    LinearLayout mLlMineViptips;
    @Bind(R.id.ll_fragment_mine_item_giftshop)
    LinearLayout mLlFragmentMineItemGiftshop;
    private int originalHeaderHeight;
    private View mContentView;
    private MineFragPresenter mMineFragPresenter;
    private float pullHeight;
    private int detailImgHeight;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMineFragPresenter = new MineFragPresenter(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMineFragPresenter.getMyInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        bindData();
        mMineFragPresenter.onCreate();
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mMineFragPresenter.onDestoryView();
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mRotateHeaderListViewFrame.setPinContent(true);
        mRotateHeaderListViewFrame.setMyOnScrollListener(this);
        mRotateHeaderListViewFrame.setPullToRefresh(true);

        mRotateHeaderListViewFrame.getHeader().setVisibility(View.INVISIBLE);
        //顶部750:550
        ViewGroup.LayoutParams topVlp = mRlMineTop.getLayoutParams();
        topVlp.height = (int) (DeviceUtils.getScreenWidth(getActivity()) / Constant.IMAGE_MINE_RATIO_WH);
        mRlMineTop.setLayoutParams(topVlp);

        detailImgHeight = (int) (DeviceUtils.getScreenWidth(getActivity()) / Constant.IMAGE_CARD_RATIO_WH);

        pullHeight = detailImgHeight - topVlp.height;
        float headHeight = (60 * DeviceUtils.getScreenDensity(getActivity()));
        float releaseRatio = (pullHeight) / headHeight;
        mRotateHeaderListViewFrame.setRatioOfHeaderHeightToRefresh(releaseRatio);

        Log.e("setupView", "detailImgHeight=" + detailImgHeight + ",topVlp.height=" + topVlp.height);

//        ViewGroup.LayoutParams topVlpImage = mIvFragmentMineBackimage.getLayoutParams();
//        topVlpImage.height = (int) (DeviceUtils.getScreenWidth(getActivity()) / Constant.IMAGE_MINE_RATIO_WH);
//        mIvFragmentMineBackimage.setLayoutParams(topVlpImage);

        originalHeaderHeight = (int) (DeviceUtils.getScreenWidth(getActivity()) / Constant.IMAGE_MINE_RATIO_WH);

        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, UserUtil.getSavedUserInfoNotNull());
                intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, Constant.IMAGE_CARD_RATIO_WH);
                ActivityTransitionLauncher.with((Activity) getActivity()).from(mIvFragmentMineBackimage).launch(intent);
                mRotateHeaderListViewFrame.refreshComplete();
                Log.e("onRefreshBegin" + mIvFragmentMineBackimage.getHeight());

            }
        });
        mLlFragmentMineItemSetting.setOnClickListener(this);
        mLlFragmentMineLikeme.setOnClickListener(this);
        mLlFragmentMineItemVip.setOnClickListener(this);
        mIbtnFragmentMineEdit.setOnClickListener(this);
        mLlFragmentMineItemPrivacy.setOnClickListener(this);
        mRivFragmentMineAvatar.setOnClickListener(this);
        mLlFragmentMineItemHelp.setOnClickListener(this);
        mLlFragmentMineItemGiftshop.setOnClickListener(this);
    }

    private void bindData() {
        mLlMineViptips.setVisibility(View.VISIBLE);
        mTvMineVipValiddata.setVisibility(View.GONE);
        mIbtnMineVipBuy.setImageResource(R.mipmap.abn_yueai_ic_mine_vip_buy);

        UserEntity userEntity = UserUtil.getSavedUserInfo();
        if (userEntity != null) {
            mTvFragmentMineName.setText(userEntity.nickname);
            if (userEntity.pictureList != null) {
                Glide.with(getActivity()).load(userEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivFragmentMineAvatar);
                Glide.with(getActivity()).load(userEntity.getFirstPicture()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mIvFragmentMineBackimage.setImageDrawable(resource);
                    }
                });
            }
            if (userEntity.sex == UserEntity.SEX_FEMALE) {
                mIvFragmentMineSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
            } else {
                mIvFragmentMineSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
            }
            mTvMineTopAge.setText(userEntity.age + "");
            mTvMineTopConstellation.setText(userEntity.constellation);
            mTvMineTopOccupation.setText(userEntity.occupation);

            mTvFragmentMineFanscount.setText(userEntity.fans <= 0 ? null : (userEntity.fans + ""));

            ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
            if (configEntity != null && configEntity.members != null) {
                for (MemberEntity memberEntity : configEntity.members) {
                    if (memberEntity.level == userEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            Glide.with(this).load(memberEntity.icon).into(mIvMineLevelSymbol);
                        }
                        break;
                    }
                }
            }
            if (userEntity.memberLevel > 0) {
                mLlMineViptips.setVisibility(View.GONE);
                mTvMineVipValiddata.setVisibility(View.VISIBLE);
                mTvMineVipValiddata.setText(getString(R.string.abn_yueai_mine_vipvaliddata_fmt, TimeUtil.getFormatedDate(userEntity.memberDate, "yyyy-MM-dd")));
                mIbtnMineVipBuy.setImageResource(R.mipmap.abn_yueai_ic_mine_vip_renew);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLlFragmentMineItemSetting) {
            //设置
            startActivity(SettingActivity.class);
        } else if (view == mLlFragmentMineLikeme) {
            WhoLikeMeActivity.launchIntent(getActivity(), UserUtil.getSavedUserInfoNotNull().fans);
        } else if (view == mLlFragmentMineItemVip) {
            CommonWebActivity.launchIntent(getActivity(), CommonUtils.getCommonPageUrl(Constant._sUrl_vip_buy, null));
        } else if (view == mLlFragmentMineItemPrivacy) {
            PrivacySettingActivity.launchIntent(getActivity());
        } else if (view == mIbtnFragmentMineEdit) {
            startActivity(UserInfoEditActivity.class);
        } else if (view == mRivFragmentMineAvatar) {
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, UserUtil.getSavedUserInfoNotNull());
            intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, Constant.IMAGE_CARD_RATIO_WH);
            ActivityTransitionLauncher.with((Activity) getActivity()).from(mRivFragmentMineAvatar).launch(intent);
        } else if (view == mLlFragmentMineItemHelp) {
            CommonWebActivity.launchIntent(getActivity(), Constant._sUrl_help);
        } else if (view == mLlFragmentMineItemGiftshop) {
            CommonWebActivity.launchIntent(getActivity(), Constant._sUrl_gift_buy);
        } else {
            super.onClick(view);
        }
    }


    @Override
    public void onScrollChanged(int curY, int oldy) {
        if (curY > detailImgHeight - originalHeaderHeight) {
            curY = detailImgHeight - originalHeaderHeight;
        }
        ViewGroup.LayoutParams llp = mRlMineTop
                .getLayoutParams();
        llp.height = originalHeaderHeight + curY;
        mRlMineTop.setLayoutParams(llp);
        mIvFragmentMineMaskimage.setAlpha(1 - curY / pullHeight);
    }

    @Override
    public void onGetMyInfoFailed(ResponseResult e) {
        mRotateHeaderListViewFrame.refreshComplete();
    }

    @Override
    public void onGetMyInfoSuccess(UserEntity userEntity) {
        mRotateHeaderListViewFrame.refreshComplete();
        bindData();
    }
}
