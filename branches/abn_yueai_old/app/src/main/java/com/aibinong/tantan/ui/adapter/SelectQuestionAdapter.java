package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/16.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.QuestionEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectQuestionAdapter extends BaseExpandableListAdapter {
    private List<QuestionEntity> mQuestionEntities;
    private List<QuestionEntity> mSelectedQuestions;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SelectQuestionAdapter(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setQuestionEntities(List<QuestionEntity> questionEntities) {
        mQuestionEntities = questionEntities;
        notifyDataSetChanged();
    }

    public List<QuestionEntity> getQuestionEntities() {
        return mQuestionEntities;
    }

    public void setSelectedQuestions(List<QuestionEntity> questionEntities) {
        mSelectedQuestions = questionEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mQuestionEntities == null ? 0 : mQuestionEntities.size();
    }

    @Override
    public int getChildrenCount(int i) {
        QuestionEntity questionEntity = mQuestionEntities.get(i);
        return questionEntity.options == null ? 0 : questionEntity.options.size();
    }

    @Override
    public Object getGroup(int i) {
        return mQuestionEntities.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mQuestionEntities.get(i).options.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return Long.parseLong(mQuestionEntities.get(i).options.get(i1).id);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        QuestionGroupHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_selectquestion_group, viewGroup, false);
            holder = new QuestionGroupHolder(view, mOnCheckedChangeListener);
        } else {
            holder = (QuestionGroupHolder) view.getTag();
        }
        QuestionEntity questionEntity = mQuestionEntities.get(groupPosition);
        holder.bindData(questionEntity, b);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        QuestionChildHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_selectquestion_child, viewGroup, false);
            holder = new QuestionChildHolder(view);
        } else {
            holder = (QuestionChildHolder) view.getTag();
        }
        QuestionEntity.OptionsEntity optionsEntity = mQuestionEntities.get(i).options.get(i1);
        holder.bindData(optionsEntity);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class QuestionGroupHolder {
        @Bind(R.id.checkbox_item_selectquestion_group)
        CheckBox mCheckboxItemSelectquestionGroup;
        @Bind(R.id.tv_item_selectquestion_group_text)
        TextView mTvItemSelectquestionGroupText;
        @Bind(R.id.iv_item_selectquestion_group_arr)
        ImageView mIvItemSelectquestionGroupArr;
        private QuestionEntity mQuestionEntity;
        private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

        public QuestionGroupHolder(View itemView, CompoundButton.OnCheckedChangeListener listener) {
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
            mOnCheckedChangeListener = listener;
            mCheckboxItemSelectquestionGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }

        public void bindData(QuestionEntity questionEntity, boolean expanded) {
            mQuestionEntity = questionEntity;
            mTvItemSelectquestionGroupText.setText(mQuestionEntity.content);
            if (expanded) {
                mIvItemSelectquestionGroupArr.setRotation(90);
            } else {
                mIvItemSelectquestionGroupArr.setRotation(0);
            }
            mCheckboxItemSelectquestionGroup.setTag(R.id.common_tag, mQuestionEntity);
            mCheckboxItemSelectquestionGroup.setOnCheckedChangeListener(null);
            if (mSelectedQuestions!=null&&mSelectedQuestions.contains(mQuestionEntity)) {
                mCheckboxItemSelectquestionGroup.setChecked(true);
            } else {
                mCheckboxItemSelectquestionGroup.setChecked(false);
            }
            mCheckboxItemSelectquestionGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

    static class QuestionChildHolder {
        @Bind(R.id.tv_item_selectquestion_child_text)
        TextView mTvItemSelectquestionChildText;
        private QuestionEntity.OptionsEntity mOptionsEntity;

        public QuestionChildHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }

        public void bindData(QuestionEntity.OptionsEntity optionsEntity) {
            mOptionsEntity = optionsEntity;
            mTvItemSelectquestionChildText.setText(mOptionsEntity.content);
        }
    }


}
