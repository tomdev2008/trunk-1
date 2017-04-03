package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.viewholder.BaseCommonVH;
import com.aibinong.tantan.ui.adapter.viewholder.CommonUserInfoVH;
import com.aibinong.tantan.ui.widget.ListUserInfoView;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
* 固定客服小助手和谁谁看过我
* */
public class
PMListAdapter extends RecyclerView.Adapter<BaseCommonVH> {
    private static final int VIEW_TYPE_SERVICE = 0, VIEW_TYPE_WHO_LOOKME = 1, VIEW_TYPE_CONVERSITION = 2;
    public List<UserEntity> mUsersList = new ArrayList<>();
    private UserEntity mHelperEntity;
    public static final String TAG = "PMListAdapter";

    public PMListAdapter() {
        if (ConfigUtil.getInstance().getConfig() != null) {
            mHelperEntity = ConfigUtil.getInstance().getConfig().helper;
        }
    }

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
    public BaseCommonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
        CommonUserInfoVH holder = new CommonUserInfoVH(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(final BaseCommonVH holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_SERVICE) {
            holder.bindData(mHelperEntity, ListUserInfoView.ITEM_TYPE_HELPER);
            holder.btnDelete.setVisibility(View.GONE);
        } else if (viewType == VIEW_TYPE_WHO_LOOKME) {
            holder.bindData(null, ListUserInfoView.ITEM_TYPE_WHOLOOKME);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.bindData(mUsersList.get(position - 1 - getHelperCount()), ListUserInfoView.ITEM_TYPE_CONVERSATION);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick:===== " + holder.getAdapterPosition());
                    if (null != mOnSwipeListener) {
                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                        //((CstSwipeDelMenu) holder.itemView).quickClose();
                        mOnSwipeListener.onDel(holder.getAdapterPosition());
//                        UserUtil.deleteSavedUUID();
                    }
                }
            });
            holder.btnDelete.setText(R.string.abn_yueai_msg_tab_clean_pm);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_WHO_LOOKME;
        }else if (getHelperCount() > 0 && position == 1) {
            return VIEW_TYPE_SERVICE;
        }else {
            return VIEW_TYPE_CONVERSITION;
        }


    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? (getHelperCount() + 1) : (getHelperCount() + 1 + mUsersList.size());
    }

    public int getHelperCount() {
        if (mHelperEntity != null) {
            return 1;
        }
        return 0;
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

    }
    /*

    abstract class PMListVh extends RecyclerView.ViewHolder {
        public PMListVh(View itemView) {
            super(itemView);
        }

        abstract void bindData(UserEntity userEntity);
    }

    class ServiceVH extends PMListVh implements View.OnClickListener {
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
        @Bind(R.id.btn_item_pmlist_sayhi)
        Button mBtnItemPmlistSayhi;

        public ServiceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mBtnItemPmlistSayhi.setVisibility(View.GONE);
            mTvItemPmlistLastchattime.setVisibility(View.GONE);
            mIvItemPmlistSex.setVisibility(View.GONE);
            mIvItemPmlistMemberlevel.setVisibility(View.GONE);
        }

        @Override
        void bindData(UserEntity userEntity) {
            mTvItemPmlistName.setText("客服小助手");
            mTvItemPmlistLastmsg.setText("这是客服小助手的最后一条消息");
        }

        @Override
        public void onClick(View v) {
            //TODO:谁关注了我
        }
    }

    class WhoLookMeVH extends PMListVh implements View.OnClickListener {
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
        @Bind(R.id.btn_item_pmlist_sayhi)
        Button mBtnItemPmlistSayhi;

        public WhoLookMeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mBtnItemPmlistSayhi.setVisibility(View.GONE);
            mTvItemPmlistLastchattime.setVisibility(View.GONE);
            mIvItemPmlistSex.setVisibility(View.GONE);
            mIvItemPmlistMemberlevel.setVisibility(View.GONE);
        }

        @Override
        void bindData(UserEntity userEntity) {
            mTvItemPmlistName.setText("谁看了我");
            mTvItemPmlistLastmsg.setText("查看谁看过我");
            Glide.with(itemView.getContext()).load(R.mipmap.abn_yueai_ic_look).into(mRivItemPmlistAvatar);
        }

        @Override
        public void onClick(View v) {
            //TODO:谁关注了我
        }
    }

    class MessageListVH extends PMListVh implements View.OnClickListener, View.OnLongClickListener {
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
        @Bind(R.id.btn_item_pmlist_sayhi)
        Button mBtnItemPmlistSayhi;
        private UserEntity mUserEntity;
        private EMConversation mEMConversation;
        private PMListAdapter mPMListAdapter;

        public MessageListVH(View itemView, PMListAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPMListAdapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTvItemPmlistIsPaired.setVisibility(View.GONE);
            mBtnItemPmlistSayhi.setVisibility(View.GONE);
        }

        @Override
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
    }*/
}
