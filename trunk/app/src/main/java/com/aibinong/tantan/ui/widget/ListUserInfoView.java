package com.aibinong.tantan.ui.widget;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.events.BaseEvent;
import com.aibinong.tantan.presenter.SayHiPresenter;
import com.aibinong.tantan.ui.activity.FansListActivity;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.activity.message.ChatActivity;
import com.aibinong.tantan.util.message.EMChatMsgHelper;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
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
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListUserInfoView extends FrameLayout implements View.OnClickListener {
    public static final int ITEM_TYPE_HELPER = 0, ITEM_TYPE_WHOLOOKME = 1, ITEM_TYPE_CONVERSATION = 2, ITEM_TYPE_FOLLOWER = 3, ITEM_TYPE_USERLIST = 4;

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
    @Bind(R.id.pb_view_pmlist_hi)
    ContentLoadingProgressBar mPbViewPmlistHi;
    @Bind(R.id.fl_item_pmlist_sayhi)
    FrameLayout mFlItemPmlistSayhi;
    public static boolean sayHiState;
    private UserEntity mUserEntity;
    private EMConversation mEMConversation;

    private int mItemType = ITEM_TYPE_USERLIST;

    public ListUserInfoView(Context context) {
        super(context);
        initView();
    }

    public ListUserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ListUserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListUserInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_view_list_userinfo, this, true);
        ButterKnife.bind(this);
        setOnClickListener(this);
        mFlItemPmlistSayhi.setVisibility(View.VISIBLE);
        mBtnItemPmlistSayhi.setOnClickListener(this);
        mTvItemPmlistLastchattime.setText(null);
        mPbViewPmlistHi.hide();
        mIvItemPmlistSex.setVisibility(GONE);
    }

    public void bindData(UserEntity data, int itemType) {
        if (sayHiState) {
            mBtnItemPmlistSayhi.setEnabled(false);
        } else {
            mBtnItemPmlistSayhi.setEnabled(true);
        }
        mUserEntity = data;
        mItemType = itemType;
        if (itemType == ITEM_TYPE_WHOLOOKME) {
            //谁看了我
            mFlItemPmlistSayhi.setVisibility(GONE);
            mTvItemPmlistName.setText("谁看过我");
            mTvItemPmlistLastmsg.setText("查看谁看过我");
            Glide.with(getContext()).load(R.mipmap.abn_yueai_ic_look).into(mRivItemPmlistAvatar);

            mTvItemPmlistLastchattime.setVisibility(View.GONE);
            mIvItemPmlistSex.setVisibility(View.GONE);
            mIvItemPmlistMemberlevel.setVisibility(View.GONE);
            return;
        } else if (itemType == ITEM_TYPE_FOLLOWER) {
            //谁关注了我
            mFlItemPmlistSayhi.setVisibility(GONE);
            mTvItemPmlistName.setText("谁关注了我");
            mTvItemPmlistLastmsg.setText("查看谁关注了我");
            Glide.with(getContext()).load(R.mipmap.abn_yueai_ic_attention).into(mRivItemPmlistAvatar);

            mTvItemPmlistLastchattime.setVisibility(View.GONE);
            mIvItemPmlistSex.setVisibility(View.GONE);
            mIvItemPmlistMemberlevel.setVisibility(View.GONE);
            return;
        }
        if (mUserEntity == null) {
            return;
        }
        //小助手、用户列表、会话列表公用：头像、名字会员、会员等级
        Glide.with(getContext()).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivItemPmlistAvatar);
        mTvItemPmlistName.setText(mUserEntity.nickname);
        //会员等级
        mIvItemPmlistMemberlevel.setVisibility(View.GONE);
        ConfigEntity configEntity = ConfigUtil.getInstance().getConfig();
        if (configEntity != null && configEntity.members != null) {
            for (MemberEntity memberEntity : configEntity.members) {
                if (mUserEntity.nickname.equals("客服小助手")) {
                    break;
                } else {
                    if (memberEntity.level == mUserEntity.memberLevel) {
                        if (!StringUtils.isEmpty(memberEntity.icon)) {
                            mIvItemPmlistMemberlevel.setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(memberEntity.icon).into(mIvItemPmlistMemberlevel);
                        }
                        break;
                    }
                }

            }
        }
//        if (mUserEntity.sex == UserEntity.SEX_MALE) {
//            mIvItemPmlistSex.setImageResource(R.mipmap.abn_yueai_ic_sex_male);
//        } else if (mUserEntity.sex == UserEntity.SEX_FEMALE) {
//            mIvItemPmlistSex.setImageResource(R.mipmap.abn_yueai_ic_sex_female);
//        } else {
//            mIvItemPmlistSex.setImageResource(0);
//        }


        //小助手、会话不需要判断是否打过招呼，显示最后一条消息
        if (itemType == ITEM_TYPE_HELPER || itemType == ITEM_TYPE_CONVERSATION) {

            mEMConversation = EMClient.getInstance().chatManager().getConversation(mUserEntity.id);
            if (mEMConversation != null) {
                EMMessage lastMsg = mEMConversation.getLastMessage();
                if (lastMsg != null) {
                    //最后一条消息
                    mTvItemPmlistLastchattime.setText(TimeUtil.getRelaStrStamp(lastMsg.getMsgTime()));
                    CharSequence msgBody = EaseSmileUtils.getSmiledText(getContext(), EMChatMsgHelper.getMessageDigest(lastMsg, getContext()));
                    mTvItemPmlistLastmsg.setText(msgBody);
                }
                //未读消息数量
                mFbvItemPmlistBadge.setBadge(mEMConversation.getUnreadMsgCount());
            }
            mFlItemPmlistSayhi.setVisibility(GONE);
        } else {
            mFlItemPmlistSayhi.setVisibility(VISIBLE);
            //判断是否打过招呼
            if (SqlBriteUtil.getInstance().getUserDb().isSaiedHi(mUserEntity.id)) {
                mBtnItemPmlistSayhi.setEnabled(false);
            } else {
                mBtnItemPmlistSayhi.setSelected(true);
            }
            //几岁，地址，职业
            StringBuilder infoSb = new StringBuilder();
            infoSb.append(mUserEntity.age).append("岁");
            if (!StringUtils.isEmpty(mUserEntity.area)) {
                infoSb.append("｜").append(mUserEntity.area);
            }
            infoSb.append("｜").append(mUserEntity.occupation);
            mTvItemPmlistLastmsg.setText(infoSb.toString());
            mTvItemPmlistLastmsg.setVisibility(View.VISIBLE);
        }



           /* boolean everTalk = SqlBriteUtil.getInstance().getUserDb().isClickedFromPairList(mUserEntity.id);
            if (everTalk) {
                mFbvItemPmlistBadge.setPoint(false);
            } else {
                mFbvItemPmlistBadge.setPoint(true);
            }*/


    }

    private long lastClick;

    @Override
    public void onClick(View view) {
        if (mBtnItemPmlistSayhi == view) {
            //打招呼
            SayHiPresenter.getInstance().sayHi(mUserEntity);
        } else {
            long timeDelta = System.currentTimeMillis() - lastClick;
            if (timeDelta >= 1000) {
                if (mItemType == ITEM_TYPE_FOLLOWER) {
                    //谁关注了我
                    FansListActivity.launchIntentFansList(getContext());
                    return;
                } else if (mItemType == ITEM_TYPE_WHOLOOKME) {
                    //谁看过我
                    FansListActivity.launchIntentBrowseList(getContext());
                    return;
                } else if (mItemType == ITEM_TYPE_CONVERSATION || mItemType == ITEM_TYPE_HELPER) {
                    EMConversation em = EMClient.getInstance().chatManager().getConversation(mUserEntity.id);
                    if (em != null) {
                        em.markAllMessagesAsRead();
                        bindData(mUserEntity, mItemType);
                    }
                    //进入聊天界面
                    ChatActivity.launchIntent(getContext(), mUserEntity.id, mUserEntity, mItemType == ITEM_TYPE_HELPER, true);
                    return;
                }
                //进详情
                if (mUserEntity != null&& ! mUserEntity.nickname.equals("客服小助手") ) {
                    Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                    intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                    ActivityTransitionLauncher.with((Activity) view.getContext()).from(mRivItemPmlistAvatar).launch(intent);
                }
                /*SqlBriteUtil.getInstance().getUserDb().markClikedFromPairList(mUserEntity.id);
                bindData(mUserEntity);
                //进入聊天界面
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_UUUID, mUserEntity.id);
                intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
                getContext().startActivity(intent);*/

                lastClick = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<UserEntity> event) {
        if (event.data != null && mUserEntity != null && event.data instanceof UserEntity && event.data.id.equals(mUserEntity.id)) {
            if (BaseEvent.ACTION_SAYHI_FAILED.equals(event.action)) {
                mPbViewPmlistHi.hide();
                mBtnItemPmlistSayhi.setVisibility(VISIBLE);
                mBtnItemPmlistSayhi.setSelected(true);
            } else if (BaseEvent.ACTION_SAYHI_SUCCESS.equals(event.action)) {
                mPbViewPmlistHi.hide();
                mBtnItemPmlistSayhi.setVisibility(VISIBLE);
                mBtnItemPmlistSayhi.setEnabled(false);
            } else if (BaseEvent.ACTION_SAYHI_START.equals(event.action)) {
                mBtnItemPmlistSayhi.setEnabled(false);
                mPbViewPmlistHi.show();
            }
        }
    }

    class BroadCastFromPM extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mBtnItemPmlistSayhi.setVisibility(VISIBLE);
            mBtnItemPmlistSayhi.setSelected(true);
        }
    }
}
