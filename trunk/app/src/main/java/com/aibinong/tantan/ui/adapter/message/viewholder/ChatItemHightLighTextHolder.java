package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.CommonPushMessageHandler;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.pojo.chat.HightLightEntity;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONArray;

import java.util.ArrayList;

public class ChatItemHightLighTextHolder extends ChatItemBaseHolder {
    public ChatItemHightLighTextHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {

    }


    @Override
    protected void onBindData(EMMessage msg) {
        mRl_item_chat_self_base_content.setVisibility(View.GONE);
        mTv_item_chat_self_base_tips.setVisibility(View.VISIBLE);

        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            //看看是否需要高亮
            try {
                JSONArray highLightArr = msg.getJSONArrayAttribute(EMessageConstant.KEY_EXT_highLight);
                ArrayList<HightLightEntity> highLightEntitys = ApiHelper.getInstance().getGson().fromJson(highLightArr.toString(), new TypeToken<ArrayList<HightLightEntity>>() {
                }.getType());
                msgBody = EaseSmileUtils.getHighLightText(itemView.getContext(), msgBody, highLightEntitys, this);
                mTv_item_chat_self_base_tips.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            mTv_item_chat_self_base_tips.setText(msgBody);
        } else {
            mTv_item_chat_self_base_tips.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == mTv_item_chat_self_base_tips) {
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
