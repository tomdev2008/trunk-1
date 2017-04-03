package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.message.ChatMsgListPresenter;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.adapter.message.ChatMsgListAdapter;
import com.aibinong.tantan.util.message.EMChatMsgHelper;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import static android.content.Context.CLIPBOARD_SERVICE;

public abstract class ChatItemBaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public static final int VIEWTYPE_SELF_TEXT = 0x100, VIEWTYPE_SELF_VOICE = 0x101, VIEWTYPE_SELF_IMAGE = 0x102, VIEWTYPE_SELF_VIDEO = 0x103, VIEWTYPE_SELF_QUESTION = 0x104, VIEWTYPE_SELF_GIFT = 0x105, VIEWTYPE_SELF_BIG_EXPRESSION = 0x106;
    public static final int VIEWTYPE_OPPOSITE_TEXT = 0x200, VIEWTYPE_OPPOSITE_VOICE = 0x201, VIEWTYPE_OPPOSITE_IMAGE = 0x202, VIEWTYPE_OPPOSITE_QUESTION = 0x203, VIEWTYPE_OPPOSITE_VIDEO = 0x204, VIEWTYPE_OPPOSITE_INFOCARD = 0x205, VIEWTYPE_OPPOSITE_GIFT = 0x206, VIEWTYPE_OPPOSITE_BIG_EXPRESSION = 0x207;
    public static final int VIEWTYPE_HIGHLIGHT_TEXT = 0x300;

    private TextView mTv_item_chat_self_base_time;
    private android.support.v4.widget.ContentLoadingProgressBar mPb_item_chat_self_base_sendpb;
    private ImageButton mIbtn_item_chat_self_base_senderr;
    private FrameLayout mFl_item_chat_self_base_content;
    private com.fatalsignal.view.RoundAngleImageView mRiv_item_chat_self_base_avatar;
    protected TextView mTv_item_chat_self_base_tips;
    protected RelativeLayout mRl_item_chat_self_base_content;
    protected ImageView mIv_item_chat_memberlevel;

    protected EMMessage mEMMessage;
    ChatMsgListPresenter mChatMsgListPresenter;
    protected UserEntity mChatToUserEntity, mSelfUserEntity;
    protected ChatMsgListAdapter mChatMsgListAdapter;
    protected int mItemViewType;

    public static final ChatItemBaseHolder newInstance(ViewGroup parent, int type, ChatMsgListPresenter presenter, UserEntity userEntity, ChatMsgListAdapter adapter) {
        ChatItemBaseHolder holder = null;
        View contentView = null;
        boolean isReceivedMsg = false;
        if (type >= 0x100 && type < 0x200) {
            //发送的消息
            contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_self_base, parent, false);
        } else {
            //接受的消息
            isReceivedMsg = true;
            contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_base, parent, false);
        }
        if (type == VIEWTYPE_SELF_VOICE) {
            holder = new ChatItemSelfVoiceHolder(contentView);
        } else if (type == VIEWTYPE_SELF_IMAGE) {
            holder = new ChatItemSelfImageHolder(contentView);
        } else if (type == VIEWTYPE_SELF_VIDEO) {
            holder = new ChatItemSelfVideoHolder(contentView);
        } else if (type == VIEWTYPE_SELF_GIFT) {
            holder = new ChatItemSelfGiftHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_VOICE) {
            holder = new OppositeVoiceChatItemHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_IMAGE) {
            holder = new ChatItemOppositeImageHolder(contentView);
        } else if (type == VIEWTYPE_SELF_QUESTION) {
            holder = new ChatItemSelfQuestionHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_QUESTION) {
            holder = new ChatItemOppositeQuestionHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_VIDEO) {
            holder = new ChatItemOppositeVideoHolder(contentView);
        } else if (type == VIEWTYPE_HIGHLIGHT_TEXT) {
            holder = new ChatItemHightLighTextHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_INFOCARD) {
            holder = new ChatItemOppositeInfoCardHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_GIFT) {
            holder = new ChatItemOppositeGiftHolder(contentView);
        } else if (type == VIEWTYPE_SELF_BIG_EXPRESSION) {
            holder = new ChatItemSelfBigExpressionHolder(contentView);
        } else if (type == VIEWTYPE_OPPOSITE_BIG_EXPRESSION) {
            holder = new ChatItemOppositeBigExpressionHolder(contentView);
        } else {
            if (isReceivedMsg) {
                holder = new OppositeTextChatItemHolder(contentView);
            } else {
                holder = new ChatItemSelfTextHolder(contentView);
            }
        }
        holder.mItemViewType = type;
        holder.mChatMsgListPresenter = presenter;
        holder.mChatToUserEntity = userEntity;
        holder.mSelfUserEntity = UserUtil.getSavedUserInfoNotNull();
        holder.mChatMsgListAdapter = adapter;
        return holder;
    }


    private void bindViews() {
        mRl_item_chat_self_base_content = (RelativeLayout) itemView.findViewById(R.id.rl_item_chat_self_base_content);
        mTv_item_chat_self_base_time = (TextView) itemView.findViewById(R.id.tv_item_chat_self_base_time);
        mPb_item_chat_self_base_sendpb = (android.support.v4.widget.ContentLoadingProgressBar) itemView.findViewById(R.id.pb_item_chat_self_base_sendpb);
        mIbtn_item_chat_self_base_senderr = (ImageButton) itemView.findViewById(R.id.ibtn_item_chat_self_base_senderr);
        mFl_item_chat_self_base_content = (FrameLayout) itemView.findViewById(R.id.fl_item_chat_self_base_content);
        mRiv_item_chat_self_base_avatar = (com.fatalsignal.view.RoundAngleImageView) itemView.findViewById(R.id.riv_item_chat_self_base_avatar);
        mTv_item_chat_self_base_tips = (TextView) itemView.findViewById(R.id.tv_item_chat_self_base_tips);
        mIv_item_chat_memberlevel = (ImageView) itemView.findViewById(R.id.iv_item_chat_memberlevel);
    }

    protected abstract void bindContentView(ViewGroup viewGroup);

    public ChatItemBaseHolder(View itemView) {
        super(itemView);
        bindViews();
        bindContentView(mFl_item_chat_self_base_content);
        mFl_item_chat_self_base_content.setOnClickListener(this);
        mFl_item_chat_self_base_content.setOnLongClickListener(this);
        mRiv_item_chat_self_base_avatar.setOnClickListener(this);
    }

    public final void bindData(EMMessage msg) {
        /*if (mChatMsgListAdapter.getItemCount() <= 1) {
            //// TODO: 16/12/14 就一张的时候，大一点，高一点
            ViewGroup.LayoutParams vlp = itemView.getLayoutParams();
            vlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(vlp);
        }else{
            ViewGroup.LayoutParams vlp = itemView.getLayoutParams();
            vlp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.setLayoutParams(vlp);
        }*/

        mEMMessage = msg;
        Log.e(mEMMessage.ext().toString());
        mTv_item_chat_self_base_time.setText(TimeUtil.getRelaStrStamp(mEMMessage.getMsgTime() + ""));
        if (mEMMessage.direct() == EMMessage.Direct.RECEIVE) {
            Glide.with(itemView.getContext()).load(mChatToUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRiv_item_chat_self_base_avatar);
            //会员等级
            mIv_item_chat_memberlevel.setVisibility(View.GONE);
            ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
            if (configEntity != null && configEntity.members != null) {
                for (MemberEntity memberEntity : configEntity.members) {
                    if (memberEntity.level == mChatToUserEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            mIv_item_chat_memberlevel.setVisibility(View.VISIBLE);
                            Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIv_item_chat_memberlevel);
                        }
                        break;
                    }
                }
            }
        } else {
            Glide.with(itemView.getContext()).load(mSelfUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRiv_item_chat_self_base_avatar);
            //会员等级
            mIv_item_chat_memberlevel.setVisibility(View.GONE);
            ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
            if (configEntity != null && configEntity.members != null) {
                for (MemberEntity memberEntity : configEntity.members) {
                    if (memberEntity.level == mSelfUserEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            mIv_item_chat_memberlevel.setVisibility(View.VISIBLE);
                            Glide.with(itemView.getContext()).load(memberEntity.icon).into(mIv_item_chat_memberlevel);
                        }
                        break;
                    }
                }
            }
        }
        if (getAdapterPosition() == 0) {//第一条肯定要显示
            mTv_item_chat_self_base_time.setText(TimeUtil.getFriendlyTimeStr(mEMMessage.getMsgTime()));
            mTv_item_chat_self_base_time.setVisibility(View.VISIBLE);
        } else {

            // show time stamp if interval with last message is > 30 seconds
            EMMessage prevMessage = (EMMessage) mChatMsgListAdapter.getPreItem(getAdapterPosition());
            if (prevMessage != null && DateUtils.isCloseEnough(mEMMessage.getMsgTime(), prevMessage.getMsgTime())) {
                mTv_item_chat_self_base_time.setVisibility(View.GONE);
            } else {
                mTv_item_chat_self_base_time.setText(TimeUtil.getFriendlyTimeStr(mEMMessage.getMsgTime()));
                mTv_item_chat_self_base_time.setVisibility(View.VISIBLE);
            }
        }

        if (msg.status() == EMMessage.Status.SUCCESS) {
            mIbtn_item_chat_self_base_senderr.setVisibility(View.INVISIBLE);
            mPb_item_chat_self_base_sendpb.setVisibility(View.INVISIBLE);
        } else if (msg.status() == EMMessage.Status.FAIL) {
            mIbtn_item_chat_self_base_senderr.setVisibility(View.VISIBLE);
            mPb_item_chat_self_base_sendpb.setVisibility(View.INVISIBLE);
        } else if (msg.status() == EMMessage.Status.INPROGRESS || msg.status() == EMMessage.Status.CREATE) {
            mIbtn_item_chat_self_base_senderr.setVisibility(View.INVISIBLE);
            mPb_item_chat_self_base_sendpb.setVisibility(View.VISIBLE);
        }



        onBindData(msg);
        markMsgAsReaded();
    }

    protected abstract void onBindData(EMMessage msg);

    protected void markMsgAsReaded() {
        EMChatMsgHelper.markReadedAndListened(mEMMessage);
    }

    @Override
    public void onClick(View view) {
        if (view == mFl_item_chat_self_base_content) {
            onContentClick();
        }
        if (view == mRiv_item_chat_self_base_avatar) {
            Intent intent = new Intent(itemView.getContext(), UserDetailActivity.class);
            if (mEMMessage.direct() == EMMessage.Direct.RECEIVE) {
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mChatToUserEntity);
            } else {
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mSelfUserEntity);
            }
            ActivityTransitionLauncher.with((Activity) itemView.getContext()).from(view).launch(intent);
        }
    }

    protected void onContentClick() {

    }

    @Override
    public boolean onLongClick(View view) {
        if (
                view == mFl_item_chat_self_base_content ||
                        view.getId() == R.id.tv_item_chat_self_image_content ||
                        view.getId() == R.id.tv_item_chat_opposite_image_content ||
                        view.getId() == R.id.iv_item_chat_self_voice_content ||
                        view.getId() == R.id.iv_item_chat_opposite_voice_content
                ) {
            if (mItemViewType == VIEWTYPE_OPPOSITE_INFOCARD || mItemViewType == VIEWTYPE_OPPOSITE_GIFT || mItemViewType == VIEWTYPE_SELF_GIFT) {
                return false;
            }
            //弹出菜单
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
            if (mItemViewType == VIEWTYPE_SELF_TEXT || mItemViewType == VIEWTYPE_OPPOSITE_TEXT) {
                popupMenu.getMenu().add("复制");
            }
            popupMenu.getMenu().add("删除");
            //发出去的，且2分钟内可以撤回
            long deltaTime = System.currentTimeMillis() - mEMMessage.getMsgTime();
            if (mEMMessage.direct() == EMMessage.Direct.SEND && mEMMessage.status() == EMMessage.Status.SUCCESS && deltaTime < 60 * 2000L) {
                popupMenu.getMenu().add("撤回");
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().equals("复制")) {
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) itemView.getContext().getSystemService(CLIPBOARD_SERVICE);
                        ClipData myClip;
                        String text = EMChatMsgHelper.getMessageDigest(mEMMessage, itemView.getContext());
                        myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                    } else if (item.getTitle().equals("删除")) {
                        mChatMsgListPresenter.onDeleteMessage(mEMMessage.getMsgId());
                        return true;
                    } else if (item.getTitle().equals("撤回")) {
                        mChatMsgListPresenter.onRevocationMsg(mEMMessage);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.setGravity(Gravity.CENTER);
            popupMenu.show();
            return true;
        }
        return false;
    }
}
