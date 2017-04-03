package com.aibinong.tantan.ui.adapter.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/9.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.QuestionEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectTruthAdapter extends BaseAdapter {
    private List<QuestionEntity.OptionsEntity> mItemDataList;


    public void setItemDataList(List<QuestionEntity.OptionsEntity> list) {
        mItemDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemDataList == null ? 0 : mItemDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_selecttruth, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindData(mItemDataList.get(position).content);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_item_selecttruth_content)
        TextView mTvItemSelecttruthContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(String itemData) {
            mTvItemSelecttruthContent.setText(itemData);
        }
    }

}
