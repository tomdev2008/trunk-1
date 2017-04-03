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

import butterknife.Bind;
import butterknife.ButterKnife;

public class OppositeVoiceChatItemHolder extends ChatItemBaseHolder implements View.OnClickListener, ChatVoicePlayerHelper.IVoicePlayerHelper {

    @Bind(R.id.iv_item_chat_opposite_voice_content)
    ImageView mIvItemChatOppositeVoiceContent;
    @Bind(R.id.tv_item_chat_opposite_voice_voicelength)
    TextView mTvItemChatOppositeVoiceVoicelength;
    @Bind(R.id.view_item_chat_opposite_voice_new)
    View mViewItemChatOppositeVoiceNew;

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_voice, viewGroup, true);
        ButterKnife.bind(this, viewGroup);
        mIvItemChatOppositeVoiceContent.setOnLongClickListener(this);

    }

    public OppositeVoiceChatItemHolder(View itemView) {
        super(itemView);
        mIvItemChatOppositeVoiceContent.setOnClickListener(this);
    }


    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.isListened()) {
            mViewItemChatOppositeVoiceNew.setVisibility(View.INVISIBLE);
        } else {
            mViewItemChatOppositeVoiceNew.setVisibility(View.VISIBLE);
        }

        if (msg.status() == EMMessage.Status.SUCCESS) {
            if (mEMMessage.getBody() instanceof EMVoiceMessageBody) {
                mTvItemChatOppositeVoiceVoicelength.setVisibility(View.VISIBLE);
                EMVoiceMessageBody emVoiceMessageBody = (EMVoiceMessageBody) msg.getBody();
                mTvItemChatOppositeVoiceVoicelength.setText(emVoiceMessageBody.getLength() + "\"");
            } else {
                mTvItemChatOppositeVoiceVoicelength.setVisibility(View.INVISIBLE);
            }
        } else if (msg.status() == EMMessage.Status.FAIL) {
            mTvItemChatOppositeVoiceVoicelength.setVisibility(View.INVISIBLE);
        } else if (msg.status() == EMMessage.Status.INPROGRESS || msg.status() == EMMessage.Status.CREATE) {
            mTvItemChatOppositeVoiceVoicelength.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mIvItemChatOppositeVoiceContent) {
            if (ChatVoicePlayerHelper.getInstance().isPlaying() && mEMMessage.getMsgId().equals(ChatVoicePlayerHelper.getInstance().getPlayingMsgId())) {
                ChatVoicePlayerHelper.getInstance().stopPlayVoice();
            } else {
                ChatVoicePlayerHelper.getInstance().startPlay(mEMMessage, itemView.getContext().getApplicationContext(), this);
            }
        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onPlayVoiceStop(EMMessage msg) {
        bindData(mEMMessage);
        mIvItemChatOppositeVoiceContent.setImageResource(R.drawable.ease_chatfrom_voice_playing);
    }

    @Override
    public void onPlayVoiceStart(EMMessage msg) {
        // 如果是接收的消息
        if (mEMMessage.direct() == EMMessage.Direct.RECEIVE) {
            markMsgAsReaded();
            if (!mEMMessage.isListened()) {
                mEMMessage.setListened(true);
                EMClient.getInstance().chatManager().setMessageListened(mEMMessage);
            }

        }
        bindData(mEMMessage);
        //播放动画
        mIvItemChatOppositeVoiceContent.setImageResource(R.drawable.voice_from_icon);
        AnimationDrawable voiceAnimation = (AnimationDrawable) mIvItemChatOppositeVoiceContent.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onPlayVoiceFailed(EMMessage msg) {
        Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.abn_yueai_chat_record_playfailed), Toast.LENGTH_SHORT).show();
        mIvItemChatOppositeVoiceContent.setImageResource(R.drawable.ease_chatfrom_voice_playing);
    }

    @Override
    public void onVoiceNotLoaded(EMMessage msg) {

    }
}
