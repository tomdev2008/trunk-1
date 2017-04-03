package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.util.message.EaseEmojicon;
import com.aibinong.tantan.util.message.EaseUI;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemOppositeBigExpressionHolder extends ChatItemBaseHolder {

    @Bind(R.id.tv_item_chat_opposite_gift_content)
    ImageView mTvItemChatOppositeGiftContent;
    @Bind(R.id.tv_item_chat_opposite_gift_text)
    TextView mTvItemChatOppositeGiftText;

    public ChatItemOppositeBigExpressionHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_gift, parent, true);
        ButterKnife.bind(this, parent);
        ViewGroup.LayoutParams vlp = mTvItemChatOppositeGiftContent.getLayoutParams();
        vlp.width = (int) (90 * DeviceUtils.getScreenDensity(itemView.getContext()));
        vlp.height = vlp.width;
        mTvItemChatOppositeGiftContent.setLayoutParams(vlp);
    }


    @Override
    protected void onBindData(EMMessage msg) {

        if (msg.getBody() instanceof EMTextMessageBody) {
            String idCode = msg.getStringAttribute(EMessageConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
            EaseEmojicon emojicon = null;
            if (EaseUI.getInstance().getEmojiconInfoProvider() != null) {
                emojicon = EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(idCode);
            }
            if (emojicon != null) {
                if (emojicon.getBigIcon() != 0) {
                    Glide.with(itemView.getContext()).load(emojicon.getBigIcon()).placeholder(R.drawable.ease_default_expression).into(mTvItemChatOppositeGiftContent);
                } else if (emojicon.getBigIconPath() != null) {
                    Glide.with(itemView.getContext()).load(emojicon.getBigIconPath()).placeholder(R.drawable.ease_default_expression).into(mTvItemChatOppositeGiftContent);
                } else {
                    mTvItemChatOppositeGiftContent.setImageResource(R.drawable.ease_default_expression);
                }
            }
        } else {
            mTvItemChatOppositeGiftText.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

    }

}
