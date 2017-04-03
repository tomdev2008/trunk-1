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
import com.aibinong.yueaiapi.api.ApiHelper;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemSelfGiftHolder extends ChatItemBaseHolder {
    @Bind(R.id.tv_item_chat_self_gift_content)
    ImageView mTvItemChatSelfGiftContent;
    @Bind(R.id.tv_item_chat_self_gift_text)
    TextView mTvItemChatSelfGiftText;

    public ChatItemSelfGiftHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_self_gift, parent, true);
        ButterKnife.bind(this, parent);
    }


    @Override
    protected void onBindData(EMMessage msg) {

        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            //看看是否需要高亮
            try {
                JSONArray highLightArr = msg.getJSONArrayAttribute(EMessageConstant.KEY_EXT_highLight);
                ArrayList<HightLightEntity> highLightEntitys = ApiHelper.getInstance().getGson().fromJson(highLightArr.toString(), new TypeToken<ArrayList<HightLightEntity>>() {
                }.getType());
                msgBody = EaseSmileUtils.getHighLightText(itemView.getContext(), msgBody, highLightEntitys, this);
                mTvItemChatSelfGiftText.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            mTvItemChatSelfGiftText.setText(msgBody);
            //礼物图片
            String imgUrl = msg.getStringAttribute(EMessageConstant.KEY_EXT_imgUrl, null);
            Glide.with(itemView.getContext()).load(imgUrl).into(mTvItemChatSelfGiftContent);
        } else {
            mTvItemChatSelfGiftText.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == mTvItemChatSelfGiftText) {
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
