package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/8.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.widget.FuncBadgeView;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhoLikeMeAdapter extends RecyclerView.Adapter<WhoLikeMeAdapter.WhoLikeMeHolderBase> {
    public static final int ITEM_VIEW_TYPE_COUNT = 0, ITEM_VIEW_TYPE_NORMAL = 1;
    private int mLikeCount;

    public WhoLikeMeAdapter(int likeCount) {
        mLikeCount = likeCount;
    }

    private List<UserEntity> mUsersList = new ArrayList<>();

    public List<UserEntity> getUsersList() {
        return mUsersList;
    }

    @Override
    public WhoLikeMeHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_COUNT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_wholikeme_count, parent, false);
            LikeMeCountHolder holder = new LikeMeCountHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
            WhoLikeMeHolder holder = new WhoLikeMeHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(WhoLikeMeHolderBase holder, int position) {
        if (position > 0) {
            holder.bindData(mUsersList.get(position - 1), mUsersList.size());
        } else {
            holder.bindData(null, mLikeCount);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE_COUNT;
        } else {
            return ITEM_VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? 0 : (mUsersList.size() + 1);
    }

    static abstract class WhoLikeMeHolderBase extends RecyclerView.ViewHolder {
        public WhoLikeMeHolderBase(View itemView) {
            super(itemView);
        }

        public abstract void bindData(UserEntity userEntity, int totalCount);
    }

    static class LikeMeCountHolder extends WhoLikeMeHolderBase {
//        @Bind(R.id.tv_wholikeme_count)
//        TextView mTvWholikemeCount;

        public LikeMeCountHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindData(UserEntity userEntity, int totalCount) {
//            mTvWholikemeCount.setText(totalCount + "");
        }
    }

    static class WhoLikeMeHolder extends WhoLikeMeHolderBase implements View.OnClickListener {
        @Bind(R.id.riv_item_pmlist_avatar)
        RoundAngleImageView mRivItemPmlistAvatar;
        @Bind(R.id.fbv_item_pmlist_badge)
        FuncBadgeView mFbvItemPmlistBadge;
        @Bind(R.id.tv_item_pmlist_name)
        TextView mTvItemPmlistName;
        @Bind(R.id.iv_item_pmlist_sex)
        ImageView mIvItemPmlistSex;
        @Bind(R.id.iv_item_pmlist_memberlevel)
        ImageView mIvItemPmlistMemberlevel;
        @Bind(R.id.tv_item_pmlist_lastchattime)
        TextView mTvItemPmlistLastchattime;
        @Bind(R.id.tv_item_pmlist_lastmsg)
        TextView mTvItemPmlistLastmsg;
        @Bind(R.id.tv_item_pmlist_isPaired)
        TextView mTvItemPmlistIsPaired;

        private UserEntity mUserEntity;

        public WhoLikeMeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("StringFormatMatches")
        @Override
        public void bindData(UserEntity userEntity, int totalCount) {
            mUserEntity = userEntity;
            Glide.with(itemView.getContext()).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivItemPmlistAvatar);
            mTvItemPmlistName.setText(mUserEntity.nickname);
            if (mUserEntity.sex == UserEntity.SEX_MALE) {
                mIvItemPmlistSex.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
            } else if (mUserEntity.sex == UserEntity.SEX_FEMALE) {
                mIvItemPmlistSex.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
            } else {
                mIvItemPmlistSex.setImageResource(0);
            }
            boolean everTalk = SqlBriteUtil.getInstance().getUserDb().isClickedFromLikeMe(mUserEntity.id);
            if (everTalk) {
                mFbvItemPmlistBadge.setPoint(false);
            } else {
                mFbvItemPmlistBadge.setPoint(true);
            }

            mTvItemPmlistLastchattime.setText(TimeUtil.getRelaStrStamp(mUserEntity.followTime));

            mTvItemPmlistLastmsg.setText(itemView.getResources().getString(R.string.abn_yueai_fmt_likeme_info, mUserEntity.age, mUserEntity.constellation, mUserEntity.occupation));
            //是否匹配
            if (StringUtils.isEmpty(mUserEntity.matchingTime)) {
                mTvItemPmlistIsPaired.setVisibility(View.GONE);
            } else {
                mTvItemPmlistIsPaired.setVisibility(View.VISIBLE);
            }

            //会员等级
            mIvItemPmlistMemberlevel.setVisibility(View.GONE);
            ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
            if (configEntity != null && configEntity.members != null) {
                for (MemberEntity memberEntity : configEntity.members) {
                    if (memberEntity.level == mUserEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            mIvItemPmlistMemberlevel.setVisibility(View.VISIBLE);
                            Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIvItemPmlistMemberlevel);
                        }
                        break;
                    }
                }
            }
        }

        @Override
        public void onClick(View view) {

            //如果未配对，且我不是会员，询问是否开会员
            if (StringUtils.isEmpty(mUserEntity.matchingTime)) {
                if (UserUtil.getSavedUserInfoNotNull().memberLevel <= 0) {
                    AlertDialog exitDialog = new AlertDialog.Builder(itemView.getContext()).create();

                    exitDialog.setButton(Dialog.BUTTON_NEGATIVE, "开通会员",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    CommonWebActivity.launchIntent(itemView.getContext(), Constant._sUrl_vip_buy);
                                }
                            });

                    exitDialog.setButton(Dialog.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    exitDialog.setTitle("提示");
                    exitDialog.setMessage("只有会员可以查看详细信息");
                    exitDialog.show();
                    return;
                }
            }
            SqlBriteUtil.getInstance().getUserDb().markClikedFromLikeMe(mUserEntity.id);
            bindData(mUserEntity, 0);
            //进入个人详情
            Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
            intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_DETAIL_SOURCE, SearchService.likeSource_likedlist);
            ActivityTransitionLauncher
                    .with((Activity) view.getContext())
                    .from(mRivItemPmlistAvatar)
                    .launch(intent);
        }
    }
}
