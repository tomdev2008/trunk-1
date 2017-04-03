package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OppositeTextChatItemHolder extends ChatItemBaseHolder {


    @Bind(R.id.tv_item_chat_opposite_text_content)
    TextView mTvItemChatOppositeTextContent;

    public OppositeTextChatItemHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_text, viewGroup, true);
        ButterKnife.bind(this, viewGroup);
    }


    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            mTvItemChatOppositeTextContent.setText(msgBody);
        } else {
            mTvItemChatOppositeTextContent.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

        if (!mEMMessage.isAcked() && mEMMessage.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(mEMMessage.getFrom(), mEMMessage.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        markMsgAsReaded();
    }
}
