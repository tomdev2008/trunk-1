package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemSelfQuestionHolder extends ChatItemBaseHolder implements View.OnClickListener {

    @Bind(R.id.tv_item_chat_self_question_content)
    TextView mTvItemChatSelfQuestionContent;
    @Bind(R.id.ll_item_chat_self_question_answers)
    LinearLayout mLlItemChatSelfQuestionAnswers;

    public ChatItemSelfQuestionHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_self_question, parent, true);
        ButterKnife.bind(this, parent);
    }


    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            mTvItemChatSelfQuestionContent.setText(msgBody);
        } else {
            mTvItemChatSelfQuestionContent.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

        int extType = msg.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL);
        try {
            JSONArray answerArr = msg.getJSONArrayAttribute(EMessageConstant.KEY_EXT_ANSWERS);
            ArrayList<QuestionEntity.OptionsEntity> answers = ApiHelper.getInstance().getGson().fromJson(answerArr.toString(), new TypeToken<ArrayList<QuestionEntity.OptionsEntity>>() {
            }.getType());
            mLlItemChatSelfQuestionAnswers.removeAllViews();
            for (int i = 0; i < answers.size(); i++) {
                QuestionEntity.OptionsEntity optionsEntity = answers.get(i);
                TextView answerView = (TextView) LayoutInflater.from(itemView.getContext()).inflate(R.layout.abn_yueai_item_chat_self_answer_text, mLlItemChatSelfQuestionAnswers, false);
                answerView.setText(optionsEntity.content);
//                answerView.setTag(R.id.common_tag, optionsEntity);
//                answerView.setOnClickListener(this);
                mLlItemChatSelfQuestionAnswers.addView(answerView);
            }
            mLlItemChatSelfQuestionAnswers.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            mLlItemChatSelfQuestionAnswers.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        {
            super.onClick(view);
        }
    }
}
