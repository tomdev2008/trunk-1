package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.ui.activity.ActivityBase;
import com.aibinong.tantan.ui.activity.message.ChatActivity;
import com.aibinong.tantan.ui.dialog.SelectItemDialog;
import com.aibinong.tantan.ui.widget.FuncBadgeView;
import com.aibinong.tantan.util.message.EMChatMsgHelper;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PMListAdapter extends RecyclerView.Adapter<PMListAdapter.PMListVH> {


    private List<UserEntity> mUsersList = new ArrayList<>();

    public void reSort() {
        Collections.sort(mUsersList, new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity o1, UserEntity o2) {
                EMConversation o1Cvs = EMClient.getInstance().chatManager().getConversation(o1.id);
                EMConversation o2Cvs = EMClient.getInstance().chatManager().getConversation(o2.id);
                if (o1Cvs == null || o2Cvs == null) {
                    return -1;
                }
                EMMessage o1Msg = o1Cvs.getLastMessage();
                EMMessage o2Msg = o2Cvs.getLastMessage();

                //都为空
                if (o1Msg == null && o2Msg == null) {
                    return 0;
                }
                //谁为空谁排后
                if (o1Msg == null) {
                    return 1;
                }
                if (o2Msg == null) {
                    return -1;
                }
                //有未读消息的排前面
          /*      if (o1Cvs.getUnreadMsgCount() > 0 && o2Cvs.getUnreadMsgCount() > 0) {

                } else if (o1Cvs.getUnreadMsgCount() > 0) {
                    return -1;
                } else if (o2Cvs.getUnreadMsgCount() > 0) {
                    return 1;
                }*/


                //按时间
                if (o1Msg.getMsgTime() < o2Msg.getMsgTime()) {
                    return 1;
                } else if (o1Msg.getMsgTime() == o2Msg.getMsgTime()) {
                    return -1;
                }
                return -1;
            }
        });
        notifyDataSetChanged();
    }

    public List<UserEntity> getUsersList() {
        return mUsersList;
    }

    public void removeItem(int pos) {
        mUsersList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public PMListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
        PMListVH holder = new PMListVH(view, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(PMListVH holder, int position) {
        holder.bindData(mUsersList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? 0 : mUsersList.size();
    }


    static class PMListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
        private EMConversation mEMConversation;
        private PMListAdapter mPMListAdapter;

        public PMListVH(View itemView, PMListAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPMListAdapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTvItemPmlistIsPaired.setVisibility(View.GONE);
        }

        public void bindData(UserEntity userEntity) {
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
            mEMConversation = EMClient.getInstance().chatManager().getConversation(mUserEntity.id);
            EMMessage lastMsg = mEMConversation.getLastMessage();
            if (lastMsg != null) {
                //最后一条消息
                mTvItemPmlistLastchattime.setText(TimeUtil.getRelaStrStamp(lastMsg.getMsgTime()));
                CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), EMChatMsgHelper.getMessageDigest(lastMsg, itemView.getContext()));
                mTvItemPmlistLastmsg.setText(msgBody);
            }
            //未读消息数量
            mFbvItemPmlistBadge.setBadge(mEMConversation.getUnreadMsgCount());

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
                EMConversation em = EMClient.getInstance().chatManager().getConversation(mUserEntity.id);
                if (em != null) {
                    em.markAllMessagesAsRead();
                    bindData(mUserEntity);
                }
                //进入聊天界面
                ChatActivity.launchIntent(itemView.getContext(), mUserEntity.id, mUserEntity, true);
                lastClick = System.currentTimeMillis();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            //长按选择 删除 标记为已读
            ArrayList<String> itemList = new ArrayList<>(3);
            itemList.add("删除该聊天");
            if (mEMConversation.getUnreadMsgCount() > 0) {
                itemList.add("标记为已读");
            }

            SelectItemDialog selectItemIOSDialog = SelectItemDialog.newInstance(null, false, itemList);
            selectItemIOSDialog.show(new SelectItemDialog.SelectItemCallback() {
                @Override
                public void onSelectItem(int position) {
                    if (position == 0) {
                        EMClient.getInstance().chatManager().deleteConversation(mUserEntity.id, false);
                        mPMListAdapter.removeItem(getAdapterPosition());
                    } else {
                        mEMConversation.markAllMessagesAsRead();
                        mPMListAdapter.notifyItemChanged(getAdapterPosition());

                    }
                    NotificationManager notificationManager = (NotificationManager) itemView.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(mUserEntity.id.hashCode());
                    mEMConversation.markAllMessagesAsRead();
                    GlobalLocalBroadCastManager.getInstance().notifyMessegeReceived(null);
                }

                @Override
                public void onSelectNone() {

                }
            }, ((ActivityBase) itemView.getContext()).getFragmentManager(), null);
            return true;
        }
    }
}
