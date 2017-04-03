package com.aibinong.tantan.presenter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/7.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.pojo.chat.HightLightEntity;
import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.tantan.ui.adapter.message.ChatMsgListAdapter;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.tantan.util.message.EMChatHelper;
import com.aibinong.tantan.util.message.EMChatMsgHelper;
import com.aibinong.tantan.util.message.EaseEmojicon;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.PushMessage;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.ChatService;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fatalsignal.util.BitmapUtils;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.MediaFile;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class ChatMsgListPresenter extends PresenterBase {
    private static final String TAG = "ChatMsgListPresenter";

    public void onDeleteMessage(String msgId) {
        //删除消息
        mEMConversation.removeMessage(msgId);
        mIChatMsgList.onDeleteMessage(msgId);
    }

    public void onRevocationMsg(EMMessage msg) {

        DialogUtil.showIndeternimateDialog((Activity) mContext, "正在撤回");

        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        String action = "revocation";//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setReceipt(mUUID);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute(EMessageConstant.CMD_EXT_TYPE, EMessageConstant.CMD_EXT_TYPE_REVOCATION_MSG);
        cmdMsg.setAttribute(EMessageConstant.KEY_CMD_EXT_targetId, msg.getMsgId());
        cmdMsg.setMessageStatusCallback(new YueaiEMCallBack(this, cmdMsg, false));
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    public void onRevocationMsgFailed(EMMessage msg) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //撤回失败
                DialogUtil.showDialog((Activity) mContext, "撤回消息失败", true);
            }
        });

    }

    public void onRevocationMsgSuccess(final EMMessage msg) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtil.hideDialog((Activity) mContext);
                String targetId = null;
                try {
                    targetId = msg.getStringAttribute(EMessageConstant.KEY_CMD_EXT_targetId);
                    EMMessage oriMsg = EMClient.getInstance().chatManager().getMessage(targetId);
                    mEMConversation.removeMessage(targetId);
                    if (oriMsg != null) {
                        //插入一条系统消息
                        EMMessage mHighLightMsg = EMMessage.createTxtSendMessage("你成功撤回了一条消息", mUUID);
                        mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
                        mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
                        mHighLightMsg.setMsgTime(oriMsg.getMsgTime());
                        EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
                        mIChatMsgList.onRevocationMsgSuccess(oriMsg, mHighLightMsg);
                    }

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public interface IChatMsgList {
        void onOppositeRevokeMsg(String msgId);

        void onRevocationMsgSuccess(EMMessage msg, EMMessage notifyMsg);

        void onDeleteMessage(String msgId);

        void onLoadAllMsg(List<EMMessage> msgs, boolean nomore);

        void onLoadMoreMsg(List<EMMessage> msgs, boolean nomore);


        void onEMmsgSend(EMMessage msg);

        void onEmmsgSendErr(EMMessage msg, int code, String info);

        void onEmmsgSended(EMMessage msg);

        void onMessageReceived(EMMessage message);

        void onPopupTruth(EMMessage message);

        void onListGiftFailed(ResponseResult e);

        void onListGiftSuccess(ArrayList<GiftEntity> gifts);

        void onGiveGiftFailed(ResponseResult e);

        void onGiveGiftSuccess();

        boolean onSwitchHi(boolean visible);

        void sendChatRecordSuccess(String s);

        void sendChatRecordFailer(Throwable t);
        void deleteChatRecord();
    }

    private IChatMsgList mIChatMsgList;
    private String mUUID;
    private UserEntity mChatToUserEntity, mSelfUseEntity;
    private EMConversation mEMConversation;
    private Context mContext;
    private BroadcastReceiver mMsgReceivedReceiver, mGlobalMsgReceivedReceiver;

    public void setUserInfo(UserEntity userInfo) {
        mChatToUserEntity = userInfo;
        boolean showHiBtn = false;
        //对方设置了消息级别，我不是会员，畅聊次数已尽
        if (mChatToUserEntity.freeTimes <= 0) {
            if (mChatToUserEntity != null && !UserUtil.hasChatToVipPer()) {
                //无权限
                showHiBtn = true;
            }
        }
        mIChatMsgList.onSwitchHi(showHiBtn);
    }

    public ChatMsgListPresenter(IChatMsgList iChatMsgList, String uUID, UserEntity chatToUserEntity, UserEntity mSelfEntity, Activity context) {
        mIChatMsgList = iChatMsgList;
        mUUID = uUID;
        mChatToUserEntity = chatToUserEntity;
        mSelfUseEntity = mSelfEntity;
        mContext = context;
        init();
        mMsgReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EMMessage message = intent.getParcelableExtra(IntentExtraKey.INTENT_EXTRA_KEY_EMMESSAGE);
                if (message != null) {
                    if (message.getType() == EMMessage.Type.CMD) {
                        if (message.getIntAttribute(EMessageConstant.CMD_EXT_TYPE, 0) == EMessageConstant.CMD_EXT_TYPE_REVOCATION_MSG) {
                            //撤回消息
                            String targetId = null;
                            try {
                                targetId = message.getStringAttribute(EMessageConstant.KEY_CMD_EXT_targetId);
                                mIChatMsgList.onOppositeRevokeMsg(targetId);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //普通消息
                        //看看是否真心话，是的话，自动弹出
                        if (mChatToUserEntity == null || !UserUtil.isHelper(mChatToUserEntity)) {
                            if (message.getType() == EMMessage.Type.TXT) {
                                if (message.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_QUESTION) {
                                    mIChatMsgList.onPopupTruth(message);
                                }
                            }
                        }
                        mIChatMsgList.onMessageReceived(message);
                    }
                }
            }
        };
        mGlobalMsgReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EMMessage message = intent.getParcelableExtra(IntentExtraKey.INTENT_EXTRA_KEY_EMMESSAGE);
                if (message != null && message.getFrom().equals(mUUID)) {
                    if (message.getType() == EMMessage.Type.CMD) {
                        if (message.getIntAttribute(EMessageConstant.CMD_EXT_TYPE, 0) == EMessageConstant.CMD_EXT_TYPE_REVOCATION_MSG) {
                            //撤回消息
                            String targetId = null;
                            try {
                                targetId = message.getStringAttribute(EMessageConstant.KEY_CMD_EXT_targetId);
                                mIChatMsgList.onOppositeRevokeMsg(targetId);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //普通消息
                        //看看是否真心话，是的话，自动弹出
                        if (mChatToUserEntity == null || !UserUtil.isHelper(mChatToUserEntity)) {
                            if (message.getType() == EMMessage.Type.TXT) {
                                if (message.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_QUESTION) {
                                    mIChatMsgList.onPopupTruth(message);
                                }
                            }
                        }
                        mIChatMsgList.onMessageReceived(message);
                    }
                }
            }
        };
    }

    private void init() {/*如果没有欢迎卡片，那就加一张进去*/

        mEMConversation = EMClient.getInstance().chatManager().getConversation(mUUID, EMConversation.EMConversationType.Chat, true);


    }

    public void clearMessageLog() {
        //删除和某个user会话，如果需要保留聊天记录，传false
        EMClient.getInstance().chatManager().deleteConversation(mUUID, true);
        init();
        loadAllMsg();

    }

    int totalMsgIdx = 0;

    public void loadAllMsg() {
        addToCycle(EMChatHelper
                .getInstance()
                .loginChat()
                .compose(ApiHelper.doIoObserveMain())
                .subscribe(
                        new Subscriber() {
                            @Override
                            public void onCompleted() {
                                //登录成功
                                List<EMMessage> allMessages = mEMConversation.loadMoreMsgFromDB(null, 20);

                                mIChatMsgList.onLoadAllMsg(allMessages, allMessages.size() < 20);
                                //看看是否真心话，是的话，自动弹出
                                if (mChatToUserEntity == null || !UserUtil.isHelper(mChatToUserEntity)) {
                                    if (allMessages != null && allMessages.size() > 0) {
                                        EMMessage lastMsg = allMessages.get(allMessages.size() - 1);
                                        if (lastMsg.direct() == EMMessage.Direct.RECEIVE && lastMsg.getType() == EMMessage.Type.TXT) {
                                            if (lastMsg.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_QUESTION) {
                                                mIChatMsgList.onPopupTruth(lastMsg);
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        }
                )
        );
    }

    public void loadMoreMsg(final String lastMsgId) {

        addToCycle(EMChatHelper
                .getInstance()
                .loginChat()
                .compose(ApiHelper.doIoObserveMain())
                .subscribe(
                        new Subscriber() {
                            @Override
                            public void onCompleted() {
                                //登录成功
                                List<EMMessage> allMessages = mEMConversation.loadMoreMsgFromDB(lastMsgId, 20);
                                mIChatMsgList.onLoadMoreMsg(allMessages, allMessages.size() < 20);
                                //如果最后一条是真心话，显示
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        }
                )
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public void onResume() {
        mEMConversation.markAllMessagesAsRead();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mGlobalMsgReceivedReceiver);
        GlobalLocalBroadCastManager.getInstance().registerMessageRecievedSingle(mMsgReceivedReceiver, mUUID);

        boolean showHiBtn = false;
        //对方设置了消息级别，我不是会员，畅聊次数已尽
        if (mChatToUserEntity.freeTimes <= 0) {
            //看看是否有权限发送，没有的话，提示无法发送
            boolean noPermissionSend = false;
            if (mChatToUserEntity != null && !UserUtil.hasChatToVipPer()) {
                //无权限
                showHiBtn = true;
            }
        }
        mIChatMsgList.onSwitchHi(showHiBtn);
    }

    public void onPause() {
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mMsgReceivedReceiver);
        GlobalLocalBroadCastManager.getInstance().registerMessageRecieved(mGlobalMsgReceivedReceiver);
    }

    @Override
    public void onDestoryView() {
        super.onDestoryView();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mGlobalMsgReceivedReceiver);
    }

    /**
     * 发送文本消息
     *
     * @param msg
     */
    public void sendTextMsg(String msg) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(msg, mUUID);
        message.getBody();
        //发送消息
        sendMessage(message, true, null);
    }

    public void sendBigExpresion(EaseEmojicon emojicon) {
        EMMessage message = EMMessage.createTxtSendMessage("[" + emojicon.getName() + "]", mUUID);
        if (emojicon.getIdentityCode() != null) {
            message.setAttribute(EMessageConstant.MESSAGE_ATTR_EXPRESSION_ID, emojicon.getIdentityCode());
        }
        message.setAttribute(EMessageConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
        sendMessage(message, true, null);
    }

    public void sendQuestions(ArrayList<QuestionEntity> questionEntities) {
        for (QuestionEntity questionEntity : questionEntities) {
            EMMessage message = EMMessage.createTxtSendMessage(questionEntity.content, mUUID);
            message.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_QUESTION);
            message.setAttribute(EMessageConstant.KEY_EXT_QUESTIONID, questionEntity.questionId);
            try {
                message.setAttribute(EMessageConstant.KEY_EXT_ANSWERS, new JSONArray(ApiHelper.getInstance().getGson().toJson(questionEntity.options)));
                sendMessage(message, true, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void answerQuestion(String questionId, QuestionEntity.OptionsEntity answer) {
        EMMessage message = EMMessage.createTxtSendMessage(answer.content, mUUID);
        message.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_ANSWER);
        message.setAttribute(EMessageConstant.KEY_EXT_QUESTIONID, questionId);
        message.setAttribute(EMessageConstant.KEY_EXT_ANSWERID, answer.id);
        sendMessage(message, false, null);
    }

    public void sendMediaMsg(Context context, final String filePath) {
        if (MediaFile.isImageFileType(filePath)) {
            //图片
            //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
            EMMessage message = EMMessage.createImageSendMessage(filePath, false, mUUID);
            sendMessage(message, true, null);
        } else if (MediaFile.isVideoFileType(filePath)) {
            //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
            //获取缩略图和视频时长
            final String durationStr = EMChatMsgHelper.getRingDuring(filePath);
            final int duration = Integer.parseInt(durationStr);
            Glide.with(context).load(filePath).asBitmap().override(context.getResources().getDimensionPixelOffset(R.dimen.abn_yueai_dimen_video_thumb), context.getResources().getDimensionPixelOffset(R.dimen.abn_yueai_dimen_video_thumb))
                    .centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    String cacheThumb = Constant.cachePath + System.currentTimeMillis();
                    if (BitmapUtils.saveBitmap(resource, cacheThumb)) {
                        Log.i("sendMediaMsg", "filePath=" + filePath + ", cacheThumb=" + cacheThumb + ", duration=" + duration);
                        EMMessage message = EMMessage.createVideoSendMessage(filePath, cacheThumb, duration, mUUID);
                        sendMessage(message, true, null);
                    }

                }
            });
        }

    }

    /**
     * 发送语音消息
     *
     * @param filePath
     * @param length
     */
    public void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, mUUID);
        sendMessage(message, true, null);

    }

    public void insertSystemMsg(String msg) {
        EMMessage mHighLightMsg = EMMessage.createTxtSendMessage(msg, mUUID);
        mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
        mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
        EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
        mIChatMsgList.onEMmsgSend(mHighLightMsg);
    }

    /*
    *
    *takeFreeTimes 是否消耗免费次数
    * needPermission 无发消息权限后是否限制发送
    * **/
    private void sendMessage(EMMessage emMessage, boolean takeFreeTimes, EMCallBack callBack) {
//        boolean isFirstWarn = true;
        if (takeFreeTimes && mChatToUserEntity != null && mChatToUserEntity.freeTimes <= 0 &&
                !UserUtil.hasChatToVipPer()) {
            //免费次数用完了
//            emMessage.setStatus(EMMessage.Status.FAIL);
//            EMClient.getInstance().chatManager().saveMessage(emMessage);
            //将失败的不显示
//            mIChatMsgList.onEMmsgSend(emMessage);
            PushMessage message = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mUUID);
            CommonPushMessageHandler.handleCommonEvent((Activity) mContext, message);
//            插入一条提示
//            EMMessage mHighLightMsg = EMMessage.createTxtSendMessage("女生才能无限畅聊哦，我也要畅聊", mUUID);
//            mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
//            HightLightEntity clickableLightEntity = new HightLightEntity();
//            clickableLightEntity.color = String.format("#%x", mContext.getResources().getColor(R.color.abn_yueai_color_red_primary));
//            clickableLightEntity.text = "我也要畅聊";
//            clickableLightEntity.event = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mUUID);
//
//
//
//            ArrayList<HightLightEntity> hightLightEntities = new ArrayList<>(1);
//            hightLightEntities.add(clickableLightEntity);
//            try {
//                mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_highLight, new JSONArray(ApiHelper.getInstance().getGson().toJson(hightLightEntities)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
//            EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
//            mIChatMsgList.onEMmsgSend(mHighLightMsg);
            mIChatMsgList.onSwitchHi(true);

            return;
        }
        //发送消息
        if (callBack == null) {
            callBack = new YueaiEMCallBack(this, emMessage, takeFreeTimes);
        }
        emMessage.setMessageStatusCallback(callBack);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
        mIChatMsgList.onEMmsgSend(emMessage);
    }

    public void sendGiftMessage(GiftEntity giftEntity) {
        EMMessage message = EMMessage.createTxtSendMessage(mContext.getString(R.string.abn_yueai_chat_fmt_send_gift, giftEntity.name, 1), mUUID);
        message.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_GIFT);
        message.setAttribute(EMessageConstant.KEY_EXT_imgUrl, giftEntity.img);
        message.setAttribute(EMessageConstant.KEY_EXT_count, 1);
        message.setAttribute(EMessageConstant.KEY_EXT_productId, giftEntity.id);
        message.setAttribute(EMessageConstant.KEY_EXT_productName, giftEntity.name);

        HightLightEntity hightLightEntity = new HightLightEntity();
        hightLightEntity.color = String.format("#%x", mContext.getResources().getColor(R.color.abn_yueai_color_red_primary));
        hightLightEntity.text = giftEntity.name;
        ArrayList<HightLightEntity> hightLightEntities = new ArrayList<>(1);
        hightLightEntities.add(hightLightEntity);
        try {
            message.setAttribute(EMessageConstant.KEY_EXT_highLight, new JSONArray(ApiHelper.getInstance().getGson().toJson(hightLightEntities)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(message, false, null);
    }

    public void checkGiftSended(EMMessage emMessage) {

        if (emMessage.direct() == EMMessage.Direct.SEND && emMessage.getType() == EMMessage.Type.TXT) {

            if (emMessage.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_GIFT) {
                String productId = emMessage.getStringAttribute(EMessageConstant.KEY_EXT_productId, null);
                int count = emMessage.getIntAttribute(EMessageConstant.KEY_EXT_count, 0);

                addToCycle(
                        ApiHelper
                                .getInstance()
                                .create(ChatService.class)
                                .give_gift(mUUID, String.format("{\"id\":\"%s\",\"count\":%d}", productId, count))
                                .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                                .subscribe(new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIChatMsgList.onGiveGiftFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIChatMsgList.onGiveGiftSuccess();
                                    }
                                })
                );
            }
        }
    }

    static class YueaiEMCallBack implements EMCallBack {
        private WeakReference<ChatMsgListPresenter> mIChatMsgListWeakReference;
        private WeakReference<EMMessage> mEMMessageWeakReference;
        private boolean mTakeFreeTimes;


        public YueaiEMCallBack(ChatMsgListPresenter presenter, EMMessage msg, boolean takeFreeTimes) {
            mIChatMsgListWeakReference = new WeakReference<ChatMsgListPresenter>(presenter);
            mEMMessageWeakReference = new WeakReference<EMMessage>(msg);
            mTakeFreeTimes = takeFreeTimes;

        }

        @Override
        public void onSuccess() {
            EMMessage message = mEMMessageWeakReference.get();
            ChatMsgListPresenter presenter = mIChatMsgListWeakReference.get();
            if (message != null && presenter != null) {
                if (message.getType() == EMMessage.Type.CMD) {
                    //撤回消息
                    presenter.onRevocationMsgSuccess(message);
                } else {
                    presenter.mIChatMsgList.onEmmsgSended(message);
                    if (presenter.mChatToUserEntity != null) {
                        if (mTakeFreeTimes && !UserUtil.hasChatToVipPer() && presenter.mChatToUserEntity.freeTimes > 0) {
                            presenter.markFreeChat();
//                            ChatRecordEntiy chatRecordEntiy = new ChatRecordEntiy();
                            StringBuffer sb = new StringBuffer();
                            sb.append("{");
                            sb.append("type:" + "chatmessage" + ",");
                            sb.append("from:" + message.getFrom() + ",");
                            sb.append("msg_id:" + message.getMsgId() + ",");
                            sb.append("chat_type:" + message.getChatType() + ",");
                            sb.append("payload:" + "{");
                            sb.append("bodies:" + "[" + "{");
                            if (message.getBody().toString().startsWith("txt")) { //文本类型
                                sb.append("type:" + "txt,");
                                sb.append("msg:" + message.getBody().toString().split(":")[1]
                                );
                            } else if (message.getBody().toString().startsWith("image")) {  //图片类型
                                sb.append("type:" + "img,");
                                sb.append("filename:" + message.getBody().toString().split(",")[0].
                                        toString().split(":")[1] + ",");
                                sb.append("url" + message.getBody().toString().split(",")[2].toString().substring(9) + ",");
                                sb.append("secret:" + message.getUserName());

                            } else if (message.getBody().toString().startsWith("video")) {  //视频
                                sb.append("type:" + "video,");
                                sb.append("filename:" + message.getBody().toString().split(",")[0].
                                        toString().split(":")[1] + ",");
                                sb.append("url" + message.getBody().toString().split(",")[2].toString().substring(9) + ",");
                                sb.append( message.getBody().toString().split(",")[4].toString() + ",");
                                sb.append("secret:" + message.getUserName());
                            } else if (message.getBody().toString().startsWith("voice")) {  //音频
                                sb.append("type:" + "audio,");
                                sb.append("filename:" + message.getBody().toString().split(",")[0].
                                        toString().split(":")[1] + ",");
                                sb.append("url" + message.getBody().toString().split(",")[2].toString().substring(9) + ",");
                                sb.append( message.getBody().toString().split(",")[3].toString() + ",");
                                sb.append("secret:" + message.getUserName());
                            }
                            sb.append("}" + "],");
                            sb.append("ext:" + message.ext());
                            sb.append("},");
                            sb.append("timestamp:" + message.getMsgTime() + ",");
                            sb.append("to:" + message.getTo());
                            sb.append("}");
                            presenter.sendChatRecordToServet(sb.toString());
                        }

                    }
                }
            }


        }

        @Override
        public void onError(int i, String s) {
            EMMessage message = mEMMessageWeakReference.get();
            ChatMsgListPresenter presenter = mIChatMsgListWeakReference.get();
            if (message != null && presenter != null) {
                if (message.getType() == EMMessage.Type.CMD) {
                    presenter.onRevocationMsgFailed(message);
                } else {
                    presenter.mIChatMsgList.onEmmsgSendErr(message, i, s);
                }
            }
        }

        @Override
        public void onProgress(int i, String s) {

        }
    }

    public void listGifts() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .gift_list(null)
                        .compose(ApiHelper.<JsonRetEntity<ArrayList<GiftEntity>>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<ArrayList<GiftEntity>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIChatMsgList.onListGiftFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ArrayList<GiftEntity>> arrayListJsonRetEntity) {
                                        mIChatMsgList.onListGiftSuccess(arrayListJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }

    /**
     * 将聊天记录发送到服务器
     */
    public void sendChatRecordToServet(String record) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .send_chat_record(record)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIChatMsgList.sendChatRecordFailer(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIChatMsgList.sendChatRecordSuccess(stringJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }

    public void markFreeChat() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .free_chat(mUUID)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        //减免费聊天次数成功
                                        mChatToUserEntity.freeTimes--;
//                                        插入一条提示
                                        EMMessage mHighLightMsg = EMMessage.createTxtSendMessage("女生才能无限畅聊哦，我也要畅聊", mUUID);
                                        mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
                                        HightLightEntity clickableLightEntity = new HightLightEntity();
                                        clickableLightEntity.color = String.format("#%x", mContext.getResources().getColor(R.color.abn_yueai_color_red_primary));
                                        clickableLightEntity.text = "我也要畅聊";
                                        clickableLightEntity.event = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mUUID);

                                        ArrayList<HightLightEntity> hightLightEntities = new ArrayList<>(1);
                                        hightLightEntities.add(clickableLightEntity);
                                        try {
                                            mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_highLight, new JSONArray(ApiHelper.getInstance().getGson().toJson(hightLightEntities)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
                                        EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
                                        mIChatMsgList.onEMmsgSend(mHighLightMsg);

                                        //如果免费次数用完了，插入一条提示消息
                                        if (mChatToUserEntity.freeTimes < 0) {
                                            PushMessage message = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mUUID);
                                            CommonPushMessageHandler.handleCommonEvent((Activity) mContext, message);
//                                            插入一条提示
//                                            EMMessage mHighLightMsg = EMMessage.createTxtSendMessage("女生才能无限畅聊哦，我也要畅聊", mUUID);
//                                            mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_SYSTEM_MSG);
//                                            HightLightEntity clickableLightEntity = new HightLightEntity();
//                                            clickableLightEntity.color = String.format("#%x", mContext.getResources().getColor(R.color.abn_yueai_color_red_primary));
//                                            clickableLightEntity.text = "我也要畅聊";
//                                            clickableLightEntity.event = PushMessage.createVipBuyMessage(Constant._sUrl_vip_buy, mUUID);
//
//                                            ArrayList<HightLightEntity> hightLightEntities = new ArrayList<>(1);
//                                            hightLightEntities.add(clickableLightEntity);
//                                            try {
//                                                mHighLightMsg.setAttribute(EMessageConstant.KEY_EXT_highLight, new JSONArray(ApiHelper.getInstance().getGson().toJson(hightLightEntities)));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            mHighLightMsg.setStatus(EMMessage.Status.SUCCESS);
//                                            EMClient.getInstance().chatManager().saveMessage(mHighLightMsg);
//                                            mIChatMsgList.onEMmsgSend(mHighLightMsg);

                                        }
                                    }
                                }
                        )
        );
    }
}
