package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.message.PairListPresenter;
import com.aibinong.tantan.ui.activity.ActivityBase;
import com.aibinong.tantan.ui.activity.message.ChatActivity;
import com.aibinong.tantan.ui.dialog.SelectItemDialog;
import com.aibinong.tantan.ui.widget.FuncBadgeView;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PairListAdapter extends RecyclerView.Adapter<PairListAdapter.PairListVH> {

    private List<UserEntity> mUsersList = new ArrayList<>();
    private PairListPresenter mPairListPresenter;

    public List<UserEntity> getUsersList() {
        return mUsersList;
    }

    public PairListAdapter(PairListPresenter pairListPresenter) {
        mPairListPresenter = pairListPresenter;
    }

    public void removeItem(String uuid) {
        for (int i = 0; i < mUsersList.size(); i++) {
            if (mUsersList.get(i).id.equals(uuid)) {
                mUsersList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @Override
    public PairListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
        PairListVH holder = new PairListVH(view, mPairListPresenter);
        return holder;
    }

    @Override
    public void onBindViewHolder(PairListVH holder, int position) {
        holder.bindData(mUsersList.get(position), mRecyclerView);
    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? 0 : mUsersList.size();
    }

    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class PairListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
        private RecyclerView mRecyclerView;
        private PairListPresenter mPairListPresenter;

        public PairListVH(View itemView, PairListPresenter presenter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPairListPresenter = presenter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindData(UserEntity userEntity, RecyclerView view) {
            mRecyclerView = view;
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
            boolean everTalk = SqlBriteUtil.getInstance().getUserDb().isClickedFromPairList(mUserEntity.id);
            if (everTalk) {
                mFbvItemPmlistBadge.setPoint(false);
            } else {
                mFbvItemPmlistBadge.setPoint(true);
            }

            if (mUserEntity.helper == 1) {
                mTvItemPmlistLastmsg.setVisibility(View.GONE);
                mTvItemPmlistLastchattime.setText(null);
            } else {
                mTvItemPmlistLastchattime.setText(TimeUtil.getRelaStrStamp("" + mUserEntity.matchingTime));
                mTvItemPmlistLastmsg.setText(itemView.getResources().getString(R.string.abn_yueai_fmt_matchtime, TimeUtil.getRelaStrStamp("" + mUserEntity.matchingTime)));
                mTvItemPmlistLastmsg.setVisibility(View.VISIBLE);
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

        private long lastClick;

        @Override
        public void onClick(View view) {
            long timeDelta = System.currentTimeMillis() - lastClick;
            if (timeDelta >= 1000) {
                SqlBriteUtil.getInstance().getUserDb().markClikedFromPairList(mUserEntity.id);
                bindData(mUserEntity, mRecyclerView);
                //进入聊天界面
                Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_UUUID, mUserEntity.id);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                itemView.getContext().startActivity(intent);
                lastClick = System.currentTimeMillis();
            }

        }

        @Override
        public boolean onLongClick(View view) {
            if (mUserEntity == null || mUserEntity.helper == 1) {
                return false;
            }
            //长按选择 删除 标记为已读
            ArrayList<String> itemList = new ArrayList<>(3);
            itemList.add("解除配对");

            SelectItemDialog selectItemIOSDialog = SelectItemDialog.newInstance(null, false, itemList);
            selectItemIOSDialog.show(new SelectItemDialog.SelectItemCallback() {
                @Override
                public void onSelectItem(int position) {
                    if (position == 0) {
                        mPairListPresenter.cancelPair(mUserEntity.id);
                    }
                }

                @Override
                public void onSelectNone() {

                }
            }, ((ActivityBase) itemView.getContext()).getFragmentManager(), null);
            return true;
        }
    }
}
