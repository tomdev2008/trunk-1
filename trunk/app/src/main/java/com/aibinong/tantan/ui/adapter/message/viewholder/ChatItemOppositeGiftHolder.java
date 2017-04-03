package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.pojo.chat.HightLightEntity;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemOppositeGiftHolder extends ChatItemBaseHolder {

    @Bind(R.id.tv_item_chat_opposite_gift_content)
    ImageView mTvItemChatOppositeGiftContent;
    @Bind(R.id.tv_item_chat_opposite_gift_text)
    TextView mTvItemChatOppositeGiftText;

    public ChatItemOppositeGiftHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_gift, parent, true);
        ButterKnife.bind(this, parent);
    }


    @Override
    protected void onBindData(EMMessage msg) {

        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();

            //礼物图片
            String imgUrl = msg.getStringAttribute(EMessageConstant.KEY_EXT_imgUrl, null);
            String productName = msg.getStringAttribute(EMessageConstant.KEY_EXT_productName, null);
            int count = msg.getIntAttribute(EMessageConstant.KEY_EXT_count, 0);
            ArrayList<HightLightEntity> highLightEntitys = new ArrayList<>(1);
            HightLightEntity hightLightEntity = new HightLightEntity();
            hightLightEntity.color = String.format("#%x", itemView.getContext().getResources().getColor(R.color.abn_yueai_color_red_primary));
            hightLightEntity.text = productName;
            highLightEntitys.add(hightLightEntity);

            CharSequence msgBody = itemView.getContext().getString(R.string.abn_yueai_chat_fmt_receive_gift, productName, count);
            msgBody = EaseSmileUtils.getHighLightText(itemView.getContext(), msgBody, highLightEntitys, this);
            mTvItemChatOppositeGiftText.setMovementMethod(LinkMovementMethod.getInstance());
            mTvItemChatOppositeGiftText.setText(msgBody);

            Glide.with(itemView.getContext()).load(imgUrl).into(mTvItemChatOppositeGiftContent);
        } else {
            mTvItemChatOppositeGiftText.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == mTvItemChatOppositeGiftText) {
            Object tag = view.getTag(R.id.common_tag);
            if (tag != null && tag instanceof HightLightEntity) {
                HightLightEntity entity = (HightLightEntity) tag;
                if (entity.event != null) {
                    CommonPushMessageHandler.handleCommonEvent((Activity) itemView.getContext(), entity.event);
                }
            }
        } else {
            super.onClick(view);
        }
    }
}
