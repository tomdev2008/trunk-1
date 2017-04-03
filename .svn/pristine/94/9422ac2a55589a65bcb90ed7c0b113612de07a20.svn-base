package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/7.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.widget.BlurTipsView;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PairCardsAdapter extends BaseCardAdapter<UserEntity> {


    private List<UserEntity> mUserEntitiesList = new ArrayList<>();
    private List<MemberEntity> mMembers;
    private int mCardWidth;
    private int mCardHeight;

    public PairCardsAdapter(int cardWidth, int cardHeight) {
        mCardWidth = cardWidth;
        mCardHeight = cardHeight;
    }

    public void setMembers(List<MemberEntity> members) {
        mMembers = members;
    }

    public List<UserEntity> getUserEntitiesList() {
        return mUserEntitiesList;
    }


    @Override
    public int getCount() {
        return mUserEntitiesList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.item_pair_card;
    }

    @Override
    public void onBindData(int position, View cardview) {
        ViewHolder holder = (ViewHolder) cardview.getTag(R.id.common_tag);
        if (holder == null) {
            holder = new ViewHolder(cardview, mCardWidth, mCardHeight);
            cardview.setTag(R.id.common_tag, holder);
        }
        holder.bindData(mUserEntitiesList.get(position), mMembers);
    }

    @Override
    public void onTopViewMoved(View cardView, int dx, int dy) {
        ViewHolder holder = (ViewHolder) cardView.getTag(R.id.common_tag);
        if (holder != null) {
            Log.i("onViewMove", "dx=" + dx + ",dy=" + dy + ",cardLeft=" + cardView.getLeft());
            if (cardView.getLeft() < 0) {
                holder.mIvItemPairCardLike.setAlpha(0.0f);
                holder.mIvItemPairCardDislike.setAlpha(-2 * cardView.getLeft() / (cardView.getWidth() * 1.0f));
            } else {
                holder.mIvItemPairCardDislike.setAlpha(0.0f);
                holder.mIvItemPairCardLike.setAlpha(2 * cardView.getLeft() / (cardView.getWidth() * 1.0f));
            }
        }
    }

    @Override
    public void onTopViewReuse(View cardView) {
        ViewHolder holder = (ViewHolder) cardView.getTag(R.id.common_tag);
        if (holder != null) {
            holder.mIvItemPairCardLike.setAlpha(0.0f);
            holder.mIvItemPairCardDislike.setAlpha(0.0f);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_item_pair_card_name)
        TextView mTvItemPairCardName;
        @Bind(R.id.iv_item_pair_card_sexsymbol)
        ImageView mIvItemPairCardSexsymbol;
        @Bind(R.id.iv_item_pair_card_levelsymbol)
        ImageView mIvItemPairCardLevelsymbol;
        @Bind(R.id.ll_par_card_name)
        LinearLayout mLlParCardName;
        @Bind(R.id.tv_item_pair_card_location)
        TextView mTvItemPairCardLocation;
        @Bind(R.id.tv_item_pair_card_activeTime)
        TextView mTvItemPairCardActiveTime;
        @Bind(R.id.ll_par_card_location_activetime)
        LinearLayout mLlParCardLocationActivetime;
        @Bind(R.id.tv_item_pair_card_age)
        TextView mTvItemPairCardAge;
        @Bind(R.id.tv_item_pair_card_constellation)
        TextView mTvItemPairCardConstellation;
        @Bind(R.id.tv_item_pair_card_occupation)
        TextView mTvItemPairCardOccupation;
        @Bind(R.id.ll_item_pair_card_bottom)
        LinearLayout mLlItemPairCardBottom;
        @Bind(R.id.iv_item_pair_card_image)
        ImageView mIvItemPairCardImage;
        @Bind(R.id.btv_item_pair_card_blurtips)
        BlurTipsView mBtvItemPairCardBlurtips;
        @Bind(R.id.iv_item_pair_card_like)
        ImageView mIvItemPairCardLike;
        @Bind(R.id.iv_item_pair_card_dislike)
        ImageView mIvItemPairCardDislike;
        @Bind(R.id.fl_item_pair_card)
        FrameLayout mFlItemPairCard;
        @Bind(R.id.iv_pair_card_big_viplogo)
        ImageView mIvPairCardBigViplogo;

        private UserEntity mUserEntity;
        private List<MemberEntity> mMemberEntities;

        ViewHolder(View view, int cardW, int cardHeight) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
//            LinearLayout.LayoutParams llp_name = (LinearLayout.LayoutParams) mLlParCardName.getLayoutParams();
//            LinearLayout.LayoutParams llp_location = (LinearLayout.LayoutParams) mLlParCardLocationActivetime.getLayoutParams();
//            llp_name.leftMargin = (int) (cardW / 6.0f - itemView.getResources().getDimension(R.dimen.dim_ailiulian_common_margin_double));
//            llp_location.leftMargin = llp_name.leftMargin;
//            mLlParCardName.setLayoutParams(llp_name);
//            mLlParCardLocationActivetime.setLayoutParams(llp_location);


            //图片1:1
            ViewGroup.LayoutParams vlp = mFlItemPairCard.getLayoutParams();
            vlp.width = cardW;
            vlp.height = (int) (cardW / Constant.IMAGE_CARD_RATIO_WH);
            mFlItemPairCard.setLayoutParams(vlp);

            mBtvItemPairCardBlurtips.setOnClickListener(this);
        }

        public void bindData(UserEntity periodEntity, List<MemberEntity> memberEntities) {
            mUserEntity = periodEntity;
            mMemberEntities = memberEntities;
            //检查是否需要模糊处理
            DrawableRequestBuilder drawableRequestBuilder = Glide
                    .with(itemView.getContext())
                    .load(mUserEntity.getFirstPicture())
//                        .placeholder(R.mipmap.abn_yueai_ic_default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);
            UserEntity selfUserEntity = UserUtil.getSavedUserInfoNotNull();
            if (selfUserEntity.memberLevel <= 0 && mUserEntity.atomization != 0) {
                //模糊
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    drawableRequestBuilder.bitmapTransform(
                            new CenterCrop(itemView.getContext())
                            , new BlurTransformation(itemView.getContext(), 25)
                            , new RoundedCornersTransformation(itemView.getContext(), itemView.getResources().getDimensionPixelOffset(R.dimen.dim_ailiulian_common_radius_maincard), 0, RoundedCornersTransformation.CornerType.TOP)
                    );
                    mIvItemPairCardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    drawableRequestBuilder.bitmapTransform(
                            new BlurTransformation(itemView.getContext(), 25)
                    );
                    mIvItemPairCardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                mBtvItemPairCardBlurtips.setVisibility(View.VISIBLE);
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    drawableRequestBuilder.bitmapTransform(
                            new CenterCrop(itemView.getContext())
                            , new RoundedCornersTransformation(itemView.getContext(), itemView.getResources().getDimensionPixelOffset(R.dimen.dim_ailiulian_common_radius_maincard), 0, RoundedCornersTransformation.CornerType.TOP)
                    );
                    mIvItemPairCardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    mIvItemPairCardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                mBtvItemPairCardBlurtips.setVisibility(View.INVISIBLE);
            }

            drawableRequestBuilder.into(mIvItemPairCardImage);

            mTvItemPairCardName.setText(mUserEntity.nickname);
            if (!StringUtils.isEmpty(mUserEntity.distance)) {
                mTvItemPairCardLocation.setVisibility(View.VISIBLE);
                mTvItemPairCardLocation.setText(String.format("%s,", mUserEntity.distance));
            } else {
                mTvItemPairCardLocation.setVisibility(View.GONE);
            }

            mTvItemPairCardActiveTime.setText(String.format("%s活跃", TimeUtil.getRelaStrStamp(mUserEntity.activeTime)));
            mTvItemPairCardAge.setText(mUserEntity.age + "");
            mTvItemPairCardConstellation.setText(mUserEntity.constellation);
            mTvItemPairCardOccupation.setText(mUserEntity.occupation);
            if (mUserEntity.sex == UserEntity.SEX_FEMALE) {
                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_female);
            } else {
                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_male);
            }
            mIvItemPairCardLevelsymbol.setVisibility(View.INVISIBLE);
            mIvPairCardBigViplogo.setVisibility(View.GONE);
            if (mMemberEntities != null && mMemberEntities.size() > 0) {
                for (MemberEntity memberEntity : mMemberEntities) {
                    if (!StringUtils.isEmpty(memberEntity.icon) && memberEntity.level == mUserEntity.memberLevel) {
                        mIvItemPairCardLevelsymbol.setVisibility(View.VISIBLE);
                        Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIvItemPairCardLevelsymbol);
                        mIvPairCardBigViplogo.setVisibility(View.VISIBLE);
                        Glide.with(itemView.getContext()).load(memberEntity.signIcon).into(mIvPairCardBigViplogo);
                        break;
                    }
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (view == mBtvItemPairCardBlurtips) {
                CommonWebActivity.launchIntent(itemView.getContext(), Constant._sUrl_vip_buy);
            } else {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, mIvItemPairCardImage.getWidth() / (mIvItemPairCardImage.getHeight() * 1.0f));
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_DETAIL_SOURCE, SearchService.likeSource_main);
                ActivityTransitionLauncher.with((Activity) view.getContext()).from(mIvItemPairCardImage).launch(intent);
            }
        }
    }
}
