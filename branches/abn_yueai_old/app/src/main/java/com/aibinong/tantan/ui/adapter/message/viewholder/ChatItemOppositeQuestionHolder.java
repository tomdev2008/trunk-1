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
import com.fatalsignal.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemOppositeQuestionHolder extends ChatItemBaseHolder implements View.OnClickListener {


    @Bind(R.id.tv_item_chat_opposite_question_content)
    TextView mTvItemChatOppositeQuestionContent;
    @Bind(R.id.ll_item_chat_opposite_question_answers)
    LinearLayout mLlItemChatOppositeQuestionAnswers;
    private String mQuestionId;

    public ChatItemOppositeQuestionHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void bindContentView(ViewGroup parent) {
        LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_question, parent, true);
        ButterKnife.bind(this, parent);
    }


    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            mTvItemChatOppositeQuestionContent.setText(msgBody);
        } else {
            mTvItemChatOppositeQuestionContent.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }

        try {
            JSONArray answerArr = msg.getJSONArrayAttribute(EMessageConstant.KEY_EXT_ANSWERS);
            try {
                mQuestionId = msg.getStringAttribute(EMessageConstant.KEY_EXT_QUESTIONID);
            } catch (Exception e) {
                Log.i("fuuuuuu", "ck");
            }
            ArrayList<QuestionEntity.OptionsEntity> answers = ApiHelper.getInstance().getGson().fromJson(answerArr.toString(), new TypeToken<ArrayList<QuestionEntity.OptionsEntity>>() {
            }.getType());
            mLlItemChatOppositeQuestionAnswers.removeAllViews();
            for (int i = 0; i < answers.size(); i++) {
                QuestionEntity.OptionsEntity optionsEntity = answers.get(i);
                TextView answerView = (TextView) LayoutInflater.from(itemView.getContext()).inflate(R.layout.abn_yueai_item_chat_self_answer_text, mLlItemChatOppositeQuestionAnswers, false);
                answerView.setText(optionsEntity.content);
                answerView.setTag(R.id.common_tag, optionsEntity);
                boolean beenAnswered = msg.getBooleanAttribute(EMessageConstant.KEY_EXT_been_answer, false);
                if (!beenAnswered) {
                    answerView.setOnClickListener(this);
                }
                mLlItemChatOppositeQuestionAnswers.addView(answerView);
            }
            mLlItemChatOppositeQuestionAnswers.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            mLlItemChatOppositeQuestionAnswers.setVisibility(View.GONE);
        }
        markMsgAsReaded();
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag(R.id.common_tag);
        if (tag != null && tag instanceof QuestionEntity.OptionsEntity) {
            QuestionEntity.OptionsEntity optionsEntity = (QuestionEntity.OptionsEntity) tag;
            //回答问题
            mChatMsgListPresenter.answerQuestion(mQuestionId, optionsEntity);
            mEMMessage.setAttribute(EMessageConstant.KEY_EXT_been_answer, true);
            EMClient.getInstance().chatManager().saveMessage(mEMMessage);
            onBindData(mEMMessage);
        }else{
            super.onClick(view);
        }
    }
}
