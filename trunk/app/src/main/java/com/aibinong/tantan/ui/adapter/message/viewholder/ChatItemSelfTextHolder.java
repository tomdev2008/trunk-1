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
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemSelfTextHolder extends ChatItemBaseHolder {


    @Bind(R.id.tv_item_chat_self_text_content)
    TextView mTvItemChatSelfTextContent;

    public ChatItemSelfTextHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_self_text, parent, true);
        ButterKnife.bind(this, parent);
    }


    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            mTvItemChatSelfTextContent.setText(msgBody);
        } else {
            mTvItemChatSelfTextContent.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

    }
}
