package com.aibinong.tantan.ui.fragment.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.message.ChatMsgListPresenter;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.SelectQuestionActivity;
import com.aibinong.tantan.ui.adapter.message.ChatMsgListAdapter;
import com.aibinong.tantan.ui.dialog.SelectTruthDialog;
import com.aibinong.tantan.ui.fragment.FragmentBase;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.ui.widget.chat.ChatInputMenu;
import com.aibinong.tantan.ui.widget.chat.EaseChatExtendMenu;
import com.aibinong.tantan.ui.widget.chat.EaseChatGiftMenu;
import com.aibinong.tantan.ui.widget.chat.EaseVoiceRecorderView;
import com.aibinong.tantan.util.ChatListRecyclerOnScrollListener;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.tantan.util.message.ChatVoicePlayerHelper;
import com.aibinong.tantan.util.message.EaseEmojicon;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fatalsignal.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.tk.mediapicker.MediaPicker;
import com.tk.mediapicker.callback.Callback;
import com.tk.mediapicker.request.AlbumRequest;
import com.tk.mediapicker.request.CameraRequest;
import com.tk.mediapicker.request.VideoShotRequest;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

public class ChatMsgListFragment extends FragmentBase implements ChatMsgListPresenter.IChatMsgList, EaseVoiceRecorderView.EaseVoiceRecorderCallback, ChatInputMenu.ChatInputMenuListener, EaseChatExtendMenu.EaseChatExtendMenuItemClickListener, Callback, EaseChatGiftMenu.GiftMenuItemClickListener {
    @Bind(R.id.recycler_msg_chat_list)
    RecyclerView mRecyclerMsgChatList;
    @Bind(R.id.chatinput_chat_bottom)
    ChatInputMenu mChatinputChatBottom;
    @Bind(R.id.recorderView_msg_chat)
    EaseVoiceRecorderView mRecorderViewMsgChat;
    @Bind(R.id.iv_chat_listbg)
    ImageView mIvChatListbg;
    @Bind(R.id.ll_chat_bottom_nochance)
    LinearLayout mLlChatBottomNochance;
    @Bind(R.id.fl_chat_bottom)
    FrameLayout mFlChatBottom;
    private String mCurrentUUID;
    private UserEntity mChatToUseEntity;
    private View mContentView;
    private ChatMsgListAdapter mChatMsgListAdapter;
    private ChatMsgListPresenter mChatMsgListPresenter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    public static ChatMsgListFragment newInstance(String uuid, UserEntity userEntity) {
        Bundle args = new Bundle();
        args.putString(IntentExtraKey.INTENT_EXTRA_KEY_UUUID, uuid);
        args.putSerializable(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        ChatMsgListFragment fragment = new ChatMsgListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setUser(UserEntity user) {
        mChatToUseEntity = user;
        getArguments().putSerializable(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, user);
        bindUserInfo();
        mChatMsgListPresenter.setUserInfo(mChatToUseEntity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUUID = getArguments().getString(IntentExtraKey.INTENT_EXTRA_KEY_UUUID);
        mChatToUseEntity = (UserEntity) getArguments().getSerializable(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);
        mChatMsgListPresenter = new ChatMsgListPresenter(this, mCurrentUUID, mChatToUseEntity, UserUtil.getSavedUserInfo(), getActivity());
        mChatMsgListAdapter = new ChatMsgListAdapter(mChatMsgListPresenter);

    }

    @Override
    public void onPause() {
        ChatVoicePlayerHelper.getInstance().stopPlayVoice();
        mChatMsgListPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mChatMsgListPresenter.onResume();
        //清除通知栏消息
        if (mCurrentUUID != null) {
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(mCurrentUUID.hashCode());
        }
        mChatMsgListPresenter.listGifts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatMsgListPresenter.onDestoryView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_fragment_chatlist, container, false);
        ButterKnife.bind(this, mContentView);
        mChatMsgListPresenter.onCreate();
        setupView(savedInstanceState);
        return mContentView;
    }

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerMsgChatList.setLayoutManager(linearLayoutManager);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mChatMsgListAdapter);
        mLlChatBottomNochance.setOnClickListener(this);

        mRecyclerMsgChatList.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerMsgChatList.setItemAnimator(new ScaleInBottomAnimator(new AccelerateInterpolator()) {
            @Override
            public boolean getSupportsChangeAnimations() {
                return false;
            }
/*
            @Override
            public long getAddDuration() {
                return 200;
            }*/
        });

        mRecyclerMsgChatList.addOnScrollListener(new ChatListRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                if (mListNoMore) {
                    return;
                }
                LoadingFooter.State state = RecyclerViewStateUtils.getHeaderViewState(mRecyclerMsgChatList);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setHeaderViewState(getActivity(), mRecyclerMsgChatList, mChatMsgListAdapter.getItemCount() <= 0, LoadingFooter.State.Loading, null, null);
                //加载更多
                mChatMsgListPresenter.loadMoreMsg(mChatMsgListAdapter.getLastMsgId());

            }
        });


        mChatinputChatBottom.setChatInputMenuListener(this);
        mChatinputChatBottom.registerExtendMenuItem(R.string.abn_yueai_chat_extends_menu_pic, R.drawable.abn_yueai_selector_chatinput_extendmenu_pic, R.id.abn_yueai_chat_extendsmenu_pic, this);
        mChatinputChatBottom.registerExtendMenuItem(R.string.abn_yueai_chat_extends_menu_shot, R.drawable.abn_yueai_selector_chatinput_extendmenu_shot, R.id.abn_yueai_chat_extendsmenu_shot, this);
        mChatinputChatBottom.registerExtendMenuItem(R.string.abn_yueai_chat_extends_menu_gift, R.drawable.abn_yueai_selector_chatinput_extendmenu_gift, R.id.abn_yueai_chat_extendsmenu_gift, this);
        mChatinputChatBottom.registerExtendMenuItem(R.string.abn_yueai_chat_extends_menu_truewords, R.drawable.abn_yueai_selector_chatinput_extendmenu_trueword, R.id.abn_yueai_chat_extendsmenu_trueword, this);
        mChatinputChatBottom.registerExtendMenuItem(R.string.abn_yueai_chat_extends_menu_video, R.drawable.abn_yueai_selector_chatinput_extendmenu_video, R.id.abn_yueai_chat_extendsmenu_video, this);
        mChatinputChatBottom.init();

        mChatinputChatBottom.setGiftMenuItemClickListener(this);

        mRecyclerMsgChatList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mChatinputChatBottom.closeExtendsMenu();
                mChatinputChatBottom.hideKeyboard();
                mChatinputChatBottom.closeEmojiMenu();
                mChatinputChatBottom.closeGiftMenu();
                return false;
            }
        });

        ViewTreeObserver.OnGlobalLayoutListener mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int scorllViewHeight;

            @Override
            public void onGlobalLayout() {
                int currHeight = mRecyclerMsgChatList.getHeight();

                if (scorllViewHeight > 0 && (currHeight - scorllViewHeight) < -100) {
                    mChatMsgListAdapter.scrollToEnd(false);
                }
                scorllViewHeight = currHeight;
            }

        };
        mRecyclerMsgChatList.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);

        bindUserInfo();
        mChatMsgListPresenter.loadAllMsg();
    }

    public void bindUserInfo() {

        //设置聊天背景
        Glide.with(getActivity()).load(mChatToUseEntity.getFirstPicture()).asBitmap().transform(new BlurTransformation(getActivity(), 25)).into(new SimpleTarget<Bitmap>(100, 100) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mIvChatListbg.setImageBitmap(resource);
            }
        });

        mChatMsgListAdapter.setChatToUserEntity(mChatToUseEntity);

    }

    public boolean onBackPressed() {
        return mChatinputChatBottom.onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void clearMessageLog() {
        mChatMsgListPresenter.clearMessageLog();
    }

    private boolean mListNoMore;

    @Override
    public void onLoadAllMsg(final List<EMMessage> msgs, final boolean nomore) {
        mChatMsgListAdapter.onLoadAllMsg(msgs);
        mChatMsgListAdapter.scrollToEnd(true);
        mListNoMore = nomore;
        RecyclerViewStateUtils.setHeaderViewState(getActivity(), mRecyclerMsgChatList, nomore, nomore ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null, null);
        if (mChatMsgListAdapter.getItemCount() <= 3) {
            linearLayoutManager.setStackFromEnd(false);
        } else {
            linearLayoutManager.setStackFromEnd(true);
        }
    }

    @Override
    public void onLoadMoreMsg(final List<EMMessage> msgs, final boolean nomore) {
        mListNoMore = nomore;
        RecyclerViewStateUtils.setHeaderViewState(getActivity(), mRecyclerMsgChatList, false, nomore ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null, null);
        mChatMsgListAdapter.onLoadMoreMsg(msgs);
//        mChatMsgListAdapter.scrollToEnd(true);
        if (mChatMsgListAdapter.getItemCount() <= 3) {
            linearLayoutManager.setStackFromEnd(false);
        } else {
            linearLayoutManager.setStackFromEnd(true);
        }
    }

    @Override
    public void onEMmsgSend(final EMMessage msg) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatMsgListAdapter.onEMmsgSend(msg);
            }
        });
    }


    @Override
    public void onSendMessage(String content) {
        mChatMsgListPresenter.sendTextMsg(content);
    }

    @Override
    public void onBigExpressionClicked(EaseEmojicon emojicon) {
        mChatMsgListPresenter.sendBigExpresion(emojicon);
    }

    @Override
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
        return mRecorderViewMsgChat.onPressToSpeakBtnTouch(v, event, this);
    }

    @Override
    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
        mChatMsgListPresenter.sendVoiceMessage(voiceFilePath, voiceTimeLength);
    }


    @Override
    public void onMessageReceived(final EMMessage message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatMsgListAdapter.onMessageReceived(message);
            }
        });
    }

    SelectTruthDialog selectTruthDialog;

    @Override
    public void onPopupTruth(final EMMessage message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //显示真心话大冒险
                try {
                    JSONArray answerArr = message.getJSONArrayAttribute(EMessageConstant.KEY_EXT_ANSWERS);
                    ArrayList<QuestionEntity.OptionsEntity> answers = ApiHelper.getInstance().getGson().fromJson(answerArr.toString(), new TypeToken<ArrayList<QuestionEntity.OptionsEntity>>() {
                    }.getType());
                    String question = null;
                    if (message.getBody() instanceof EMTextMessageBody) {
                        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                        question = txtBody.getMessage();

                    }
                    String mQuestionId = null;
                    try {
                        mQuestionId = message.getStringAttribute(EMessageConstant.KEY_EXT_QUESTIONID);
                    } catch (Exception e) {
                        Log.i("fuuuuuu", "ck");
                    }
                    if (selectTruthDialog != null) {
                        selectTruthDialog.dismiss();
                        selectTruthDialog = null;
                    }
                    selectTruthDialog = SelectTruthDialog.newInstance(answers, question);
                    final String finalMQuestionId = mQuestionId;
                    selectTruthDialog.show(new SelectTruthDialog.SelectItemCallback() {
                        @Override
                        public void onSelectItem(int position, QuestionEntity.OptionsEntity answer) {
                            if (answer != null) {
                                //回答问题
                                mChatMsgListPresenter.answerQuestion(finalMQuestionId, answer);
                                message.setAttribute(EMessageConstant.KEY_EXT_been_answer, true);
                                EMClient.getInstance().chatManager().saveMessage(message);
                            }
                        }

                        @Override
                        public void onSelectNone() {

                        }
                    }, getFragmentManager(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onEmmsgSendErr(final EMMessage msg, final int code, final String info) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatMsgListAdapter.onEmmsgSendErr(msg, code, info);

            }
        });
    }

    @Override
    public void onEmmsgSended(final EMMessage msg) {
        mChatMsgListPresenter.checkGiftSended(msg);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatMsgListAdapter.onEmmsgSended(msg);

            }
        });
    }

    private static final int REQUEST_CODE_PICK_MEIDA = 0x100;
    private static final int REQUEST_CODE_VIDEO_RECORD = 0x102;
    private static final int REQUEST_CODE_CAMERA = 0x103;
    private static final int REQUEST_CODE_SELECTQUESTION = 0x104;

    @Override
    public void onClick(int itemId, View view) {
        //更多菜单里的4个按钮
        if (itemId == R.id.abn_yueai_chat_extendsmenu_pic) {
            //图片
            MediaPicker.startRequest(new AlbumRequest.Builder(getActivity(), REQUEST_CODE_PICK_MEIDA)
                    .needCrop(true)
                    .asSystem(false)
                    .asSingle(false)
                    .showCameraIndex(true)
                    .showVideoContent(false)
                    .setCheckLimit(9)
                    .build());
            mChatinputChatBottom.closeExtendsMenu();
        } else if (itemId == R.id.abn_yueai_chat_extendsmenu_shot) {
            //拍照
            MediaPicker.startRequest(new CameraRequest.Builder(getActivity(), REQUEST_CODE_CAMERA)
                    .needCrop(false)
                    .build());
            mChatinputChatBottom.closeExtendsMenu();
        } else if (itemId == R.id.abn_yueai_chat_extendsmenu_video) {
            //视频
            //开始录像
            MediaPicker.startRequest(new VideoShotRequest.Builder(getActivity(), REQUEST_CODE_VIDEO_RECORD).maxDuration(20 * 1000)
                    .build());
            mChatinputChatBottom.closeExtendsMenu();
        } else if (itemId == R.id.abn_yueai_chat_extendsmenu_trueword) {
            //真心话
            Intent intent = new Intent(getActivity(), SelectQuestionActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECTQUESTION);
            mChatinputChatBottom.closeExtendsMenu();
        } else if (itemId == R.id.abn_yueai_chat_extendsmenu_gift) {
            //礼物
            mChatinputChatBottom.openGiftMenu();
            mChatinputChatBottom.closeExtendsMenu();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECTQUESTION:
                //真心话
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<QuestionEntity> questionEntities = (ArrayList<QuestionEntity>) data.getSerializableExtra(IntentExtraKey.INTENT_RESULT_KEY_SELECTED_QUESTIONS);
                    mChatMsgListPresenter.sendQuestions(questionEntities);
                }
                break;
            case REQUEST_CODE_PICK_MEIDA:
            case REQUEST_CODE_VIDEO_RECORD:
            case REQUEST_CODE_CAMERA:
                MediaPicker.onMediaResult(resultCode, data, this);
                break;
            default:
        }
    }

    @Override
    public void onComplete(File source) {
        //选图或者视频回来
        mChatMsgListPresenter.sendMediaMsg(getActivity(), source.getPath());
    }

    @Override
    public void onComplete(List<File> sourceList) {
        //选图或者视频回来
        for (File source : sourceList) {
            //选图或者视频回来
            mChatMsgListPresenter.sendMediaMsg(getActivity(), source.getPath());
        }
    }

    @Override
    public void onListGiftFailed(ResponseResult e) {
//        showErrToast(e);
    }

    @Override
    public void onListGiftSuccess(ArrayList<GiftEntity> gifts) {
        mChatinputChatBottom.setOwnedGifts(gifts);
    }

    @Override
    public void onClick(GiftEntity gift, View view) {
        //送礼物
        mChatinputChatBottom.closeGiftMenu();
        mChatMsgListPresenter.sendGiftMessage(gift);

    }

    @Override
    public void onBuyGift() {
        CommonWebActivity.launchIntent(getActivity(), CommonUtils.getCommonPageUrl(Constant._sUrl_gift_buy, null));
    }

    @Override
    public void onGiveGiftFailed(ResponseResult e) {
        showErrToast(e);
    }

    @Override
    public void onGiveGiftSuccess() {
        Log.e("onGiveGiftSuccess", "success");
    }

    @Override
    public void onDeleteMessage(String msgId) {
        mChatMsgListAdapter.onDeleteMessage(msgId);
    }

    @Override
    public void onRevocationMsgSuccess(EMMessage msg, EMMessage notifyMsg) {
        mChatMsgListAdapter.onRevocationMsgSuccess(msg, notifyMsg);
    }


    @Override
    public void onOppositeRevokeMsg(final String msgId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatMsgListAdapter.onOppositeRevokeMsg(msgId);

            }
        });
    }

    @Override
    public boolean onSwitchHi(boolean visible) {
        boolean configShowHi = false;
        if (ConfigUtil.getInstance().getConfig() != null) {
            configShowHi = (ConfigUtil.getInstance().getConfig().rechargeButton == 1);
        }
        if (configShowHi && visible) {
            mLlChatBottomNochance.setVisibility(View.VISIBLE);
            mChatinputChatBottom.setVisibility(View.GONE);
            hideKeyboard();
            return true;
        } else {
            mLlChatBottomNochance.setVisibility(View.GONE);
            mChatinputChatBottom.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == mLlChatBottomNochance) {
            PushMessage message = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mCurrentUUID);
            CommonPushMessageHandler.handleCommonEvent(getActivity(), message);
        } else {
            super.onClick(view);
        }
    }
}
