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
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.widget.AuthenticationView;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PairCardsAdapter extends BaseCardAdapter<UserEntity> {

    private ArrayList<UserEntity> mUserEntitiesList = new ArrayList<>();
    private int mCardWidth;
    private int mCardHeight;

    public PairCardsAdapter(int cardWidth, int cardHeight) {
        mCardWidth = cardWidth;
        mCardHeight = cardHeight;
    }

    public ArrayList<UserEntity> getUserEntitiesList() {
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
        holder.bindData(mUserEntitiesList.get(position));
    }

/*    @Override
    public void onTopViewMoved(View cardView, int dx, int dy) {
        ViewHolder holder = (ViewHolder) cardView.getTag(R.id.common_tag);
        if (holder != null) {
            if (cardView.getLeft() < 0) {
                holder.mIvItemPairCardLike.setAlpha(0.0f);
                holder.mIvItemPairCardDislike.setAlpha(-2 * cardView.getLeft() / (cardView.getWidth() * 1.0f));
            } else {
                holder.mIvItemPairCardDislike.setAlpha(0.0f);
                holder.mIvItemPairCardLike.setAlpha(2 * cardView.getLeft() / (cardView.getWidth() * 1.0f));
            }
        }
    }*/

/*    @Override
    public void onTopViewReuse(View cardView) {
        ViewHolder holder = (ViewHolder) cardView.getTag(R.id.common_tag);
        if (holder != null) {
            holder.mIvItemPairCardLike.setAlpha(0.0f);
            holder.mIvItemPairCardDislike.setAlpha(0.0f);
        }
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_item_pair_card_name)
        TextView mTvItemPairCardName;
        @Bind(R.id.iv_item_pair_card_sexsymbol)
        ImageView mIvItemPairCardSexsymbol;
        @Bind(R.id.iv_item_pair_card_levelsymbol)
        ImageView mIvItemPairCardLevelsymbol;
        @Bind(R.id.ll_par_card_name)
        LinearLayout mLlParCardName;
        @Bind(R.id.atv_item_pair_card_cert)
        AuthenticationView mAtvItemPairCardCert;
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
        @Bind(R.id.fl_item_pair_card)
        FrameLayout mFlItemPairCard;
        @Bind(R.id.iv_pair_card_big_viplogo)
        ImageView mIvPairCardBigViplogo;

        private UserEntity mUserEntity;

        ViewHolder(View view, int cardW, int cardHeight) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);


            //图片1:1
            ViewGroup.LayoutParams vlp = mFlItemPairCard.getLayoutParams();
            vlp.width = cardW;
            vlp.height = (int) (cardW / Constant.IMAGE_CARD_RATIO_WH);
            mFlItemPairCard.setLayoutParams(vlp);

        }

        public void bindData(UserEntity periodEntity) {
            mUserEntity = periodEntity;
            //检查是否需要模糊处理
            DrawableRequestBuilder drawableRequestBuilder = Glide
                    .with(itemView.getContext())
                    .load(mUserEntity.getFirstPicture())
//                        .placeholder(R.mipmap.abn_yueai_ic_default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);
            UserEntity selfUserEntity = UserUtil.getSavedUserInfoNotNull();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawableRequestBuilder.bitmapTransform(
                        new CenterCrop(itemView.getContext())
                        , new RoundedCornersTransformation(itemView.getContext(), itemView.getResources().getDimensionPixelOffset(R.dimen.dim_ailiulian_common_radius_maincard), 0, RoundedCornersTransformation.CornerType.TOP)
                );
                mIvItemPairCardImage.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                mIvItemPairCardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
            } else {
                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
            }
            mIvItemPairCardLevelsymbol.setVisibility(View.INVISIBLE);
            mIvPairCardBigViplogo.setVisibility(View.GONE);
            MemberEntity memberEntity = ConfigUtil.getInstance().getMemberByLevel(mUserEntity.memberLevel);
            if (memberEntity != null) {
                if (!StringUtils.isEmpty(memberEntity.icon) && memberEntity.level == mUserEntity.memberLevel) {
                    mIvItemPairCardLevelsymbol.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIvItemPairCardLevelsymbol);
                    mIvPairCardBigViplogo.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(memberEntity.signIcon).into(mIvPairCardBigViplogo);
                }
            }
            mAtvItemPairCardCert.bindUserEntity(mUserEntity);
        }

        @Override
        public void onClick(View view) {
            {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, mIvItemPairCardImage.getWidth() / (mIvItemPairCardImage.getHeight() * 1.0f));
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_DETAIL_SOURCE, SearchService.likeSource_main);
                ActivityTransitionLauncher.with((Activity) view.getContext()).from(mIvItemPairCardImage).launch(intent);
            }
        }
    }
}
