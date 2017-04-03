//package com.aibinong.tantan.ui.adapter;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/10/18.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.aibinong.tantan.R;
//import com.aibinong.tantan.constant.IntentExtraKey;
//import com.aibinong.tantan.ui.activity.UserDetailActivity;
//import com.aibinong.yueaiapi.pojo.MemberEntity;
//import com.aibinong.yueaiapi.pojo.UserEntity;
//import com.aibinong.yueaiapi.utils.UserUtil;
//import com.bumptech.glide.Glide;
//import com.fatalsignal.util.StringUtils;
//import com.fatalsignal.util.TimeUtil;
//import com.fatalsignal.view.RoundAngleImageView;
//import com.kogitune.activity_transition.ActivityTransitionLauncher;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import jp.wasabeef.glide.transformations.BlurTransformation;
//
//public class PeriodGridListAdapter extends BaseAdapter {
//
//    private List<UserEntity> mUserEntitiesList = new ArrayList<>();
//    private List<MemberEntity> mMembers;
//    private int mCardWidth;
//
//    public PeriodGridListAdapter(int cardWidth) {
//        mCardWidth = cardWidth;
//    }
//
//    public void setMembers(List<MemberEntity> members) {
//        mMembers = members;
//        notifyDataSetChanged();
//    }
//
//    public List<UserEntity> getUserEntitiesList() {
//        return mUserEntitiesList;
//    }
//
//    public void removeTop() {
//        if (mUserEntitiesList != null && mUserEntitiesList.size() > 0) {
//            mUserEntitiesList.remove(0);
//            notifyDataSetChanged();
//        }
//    }
//
//
//    @Override
//    public int getCount() {
//        return mUserEntitiesList == null ? 0 : mUserEntitiesList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mUserEntitiesList.get(i);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHolder holder = null;
//        if (view == null) {
//            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pair_card, viewGroup, false);
//            holder = new ViewHolder(view, mCardWidth);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        holder.bindData(mUserEntitiesList.get(i), mMembers);
//        return view;
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @Bind(R.id.iv_item_pair_card_image)
//        RoundAngleImageView mIvItemPairCardImage;
//        @Bind(R.id.iv_item_pair_card_blurtips)
//        ImageView mIvItemPairCardBlurtips;
//        @Bind(R.id.ll_item_pair_card_blurtips)
//        LinearLayout mLlItemPairCardBlurtips;
//        @Bind(R.id.tv_item_pair_card_name)
//        TextView mTvItemPairCardName;
//        @Bind(R.id.iv_item_pair_card_sexsymbol)
//        ImageView mIvItemPairCardSexsymbol;
//        @Bind(R.id.iv_item_pair_card_levelsymbol)
//        ImageView mIvItemPairCardLevelsymbol;
//        @Bind(R.id.ll_par_card_name)
//        LinearLayout mLlParCardName;
//        @Bind(R.id.tv_item_pair_card_location)
//        TextView mTvItemPairCardLocation;
//        @Bind(R.id.tv_item_pair_card_activeTime)
//        TextView mTvItemPairCardActiveTime;
//        @Bind(R.id.ll_par_card_location_activetime)
//        LinearLayout mLlParCardLocationActivetime;
//        @Bind(R.id.tv_item_pair_card_age)
//        TextView mTvItemPairCardAge;
//        @Bind(R.id.tv_item_pair_card_constellation)
//        TextView mTvItemPairCardConstellation;
//        @Bind(R.id.tv_item_pair_card_occupation)
//        TextView mTvItemPairCardOccupation;
//
//        private UserEntity mUserEntity;
//        private List<MemberEntity> mMemberEntities;
//
//        ViewHolder(View view, int cardW) {
//            super(view);
//            ButterKnife.bind(this, view);
//            view.setOnClickListener(this);
//            LinearLayout.LayoutParams llp_name = (LinearLayout.LayoutParams) mLlParCardName.getLayoutParams();
//            LinearLayout.LayoutParams llp_location = (LinearLayout.LayoutParams) mLlParCardLocationActivetime.getLayoutParams();
//            llp_name.leftMargin = (int) (cardW / 6.0f - itemView.getResources().getDimension(R.dimen.dim_ailiulian_common_margin_double));
//            llp_location.leftMargin = llp_name.leftMargin;
//            mLlParCardName.setLayoutParams(llp_name);
//            mLlParCardLocationActivetime.setLayoutParams(llp_location);
//
//            //图片1:1
//
//        }
//
//        public void bindData(UserEntity periodEntity, List<MemberEntity> memberEntities) {
//            mUserEntity = periodEntity;
//            mMemberEntities = memberEntities;
//            //检查是否需要模糊处理
//            UserEntity selfUserEntity = UserUtil.getSavedUserInfoNotNull();
//            if (selfUserEntity.memberLevel <= 0 && mUserEntity.atomization != 0) {
//                //模糊
//                Glide
//                        .with(itemView.getContext())
//                        .load(mUserEntity.getFirstPicture())
//                        .placeholder(R.mipmap.abn_yueai_ic_default_avatar)
//                        .bitmapTransform(new BlurTransformation(itemView.getContext(), 25))
//                        .into(mIvItemPairCardImage);
//                mLlItemPairCardBlurtips.setVisibility(View.VISIBLE);
//            } else {
//                Glide
//                        .with(itemView.getContext())
//                        .load(mUserEntity.getFirstPicture())
//                        .placeholder(R.mipmap.abn_yueai_ic_default_avatar)
//                        .into(mIvItemPairCardImage);
//                mLlItemPairCardBlurtips.setVisibility(View.INVISIBLE);
//            }
//            mTvItemPairCardName.setText(mUserEntity.nickname);
//            if (!StringUtils.isEmpty(mUserEntity.distance)) {
//                mTvItemPairCardLocation.setVisibility(View.VISIBLE);
//                mTvItemPairCardLocation.setText(String.format("%s,", mUserEntity.distance));
//            } else {
//                mTvItemPairCardLocation.setVisibility(View.GONE);
//            }
//
//            mTvItemPairCardActiveTime.setText(String.format("%s活跃", TimeUtil.getRelaStrStamp(mUserEntity.activeTime)));
//            mTvItemPairCardAge.setText(mUserEntity.age + "");
//            mTvItemPairCardConstellation.setText(mUserEntity.constellation);
//            mTvItemPairCardOccupation.setText(mUserEntity.occupation);
//            if (mUserEntity.sex == UserEntity.SEX_FEMALE) {
//                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_female);
//            } else {
//                mIvItemPairCardSexsymbol.setImageResource(R.mipmap.abn_yueai_ic_detail_male);
//            }
//            mIvItemPairCardLevelsymbol.setVisibility(View.INVISIBLE);
//            if (mMemberEntities != null && mMemberEntities.size() > 0) {
//                for (MemberEntity memberEntity : mMemberEntities) {
//                    if (!StringUtils.isEmpty(memberEntity.icon) && memberEntity.level == mUserEntity.memberLevel) {
//                        mIvItemPairCardLevelsymbol.setVisibility(View.VISIBLE);
//                        Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIvItemPairCardLevelsymbol);
//                        break;
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
//            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
//            intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, mIvItemPairCardImage.getWidth() / (mIvItemPairCardImage.getHeight() * 1.0f));
//            ActivityTransitionLauncher.with((Activity) view.getContext()).from(mIvItemPairCardImage).launch(intent);
//        }
//    }
//
//
//}
