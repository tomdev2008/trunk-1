package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.util.message.ChatVoicePlayerHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemSelfVoiceHolder extends ChatItemBaseHolder implements View.OnClickListener, ChatVoicePlayerHelper.IVoicePlayerHelper {


    @Bind(R.id.tv_item_chat_self_voice_voicelength)
    TextView mTvItemChatSelfVoiceVoicelength;
    @Bind(R.id.iv_item_chat_self_voice_content)
    ImageView mIvItemChatSelfVoiceContent;

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_self_voice, viewGroup, true);
        ButterKnife.bind(this, viewGroup);

    }

    public ChatItemSelfVoiceHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mIvItemChatSelfVoiceContent.setOnClickListener(this);
        mIvItemChatSelfVoiceContent.setOnLongClickListener(this);
    }

    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.status() == EMMessage.Status.SUCCESS) {
            mTvItemChatSelfVoiceVoicelength.setVisibility(View.VISIBLE);
            if (mEMMessage.getBody() instanceof EMVoiceMessageBody) {
                EMVoiceMessageBody emVoiceMessageBody = (EMVoiceMessageBody) msg.getBody();
                mTvItemChatSelfVoiceVoicelength.setText(emVoiceMessageBody.getLength() + "\"");
            }
        } else if (msg.status() == EMMessage.Status.FAIL) {
            mTvItemChatSelfVoiceVoicelength.setVisibility(View.INVISIBLE);
        } else if (msg.status() == EMMessage.Status.INPROGRESS || msg.status() == EMMessage.Status.CREATE) {
            mTvItemChatSelfVoiceVoicelength.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mIvItemChatSelfVoiceContent) {
            if (ChatVoicePlayerHelper.getInstance().isPlaying() && mEMMessage.getMsgId().equals(ChatVoicePlayerHelper.getInstance().getPlayingMsgId())) {
                ChatVoicePlayerHelper.getInstance().stopPlayVoice();
            } else {
                ChatVoicePlayerHelper.getInstance().startPlay(mEMMessage, itemView.getContext().getApplicationContext(), this);
            }
        }else{
            super.onClick(view);
        }
    }

    @Override
    public void onPlayVoiceStop(EMMessage msg) {
        bindData(mEMMessage);
        mIvItemChatSelfVoiceContent.setImageResource(R.drawable.ease_chatto_voice_playing);
    }

    @Override
    public void onPlayVoiceStart(EMMessage msg) {
        // 如果是接收的消息
        if (mEMMessage.direct() == EMMessage.Direct.RECEIVE) {
            if (!mEMMessage.isAcked() && mEMMessage.getChatType() == EMMessage.ChatType.Chat) {
                // 告知对方已读这条消息
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(mEMMessage.getFrom(), mEMMessage.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
            if (!mEMMessage.isListened()) {
                mEMMessage.setListened(true);
                EMClient.getInstance().chatManager().setMessageListened(mEMMessage);
            }
        }
        bindData(mEMMessage);
        //播放动画
        //播放动画
        mIvItemChatSelfVoiceContent.setImageResource(R.drawable.voice_to_icon);
        AnimationDrawable voiceAnimation = (AnimationDrawable) mIvItemChatSelfVoiceContent.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onPlayVoiceFailed(EMMessage msg) {
        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.abn_yueai_chat_record_playfailed), Toast.LENGTH_SHORT).show();
        mIvItemChatSelfVoiceContent.setImageResource(R.drawable.ease_chatto_voice_playing);
    }

    @Override
    public void onVoiceNotLoaded(EMMessage msg) {

    }
}
