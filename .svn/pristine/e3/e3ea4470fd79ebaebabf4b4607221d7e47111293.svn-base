package com.aibinong.tantan.ui.adapter.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/9.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.dialog.SelectItemDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.JobSelectHolder> {
    private SelectItemDialog.SelectItemCallback mSelectItemCallback;
    private List<?> mItemDataList;

    public void setSelectItemCallback(SelectItemDialog.SelectItemCallback selectItemCallback) {
        mSelectItemCallback = selectItemCallback;
    }

    public void setItemDataList(List<?> list) {
        mItemDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public JobSelectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_dialog_selectitem, parent, false);
        JobSelectHolder holder = new JobSelectHolder(view, mSelectItemCallback);
        return holder;
    }

    @Override
    public void onBindViewHolder(JobSelectHolder holder, int position) {
        holder.bindData(mItemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemDataList == null ? 0 : mItemDataList.size();
    }

    static class JobSelectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_item_selectjob_jobname)
        TextView mTvItemSelectjobJobname;
        private Object mItemData;
        private SelectItemDialog.SelectItemCallback mSelectItemCallback;

        public JobSelectHolder(View itemView, SelectItemDialog.SelectItemCallback selectItemCallback) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mSelectItemCallback = selectItemCallback;
        }

        public void bindData(Object itemData) {
            mItemData = itemData;
            mTvItemSelectjobJobname.setText(mItemData.toString());
        }

        @Override
        public void onClick(View view) {
            if (mSelectItemCallback != null) {
                mSelectItemCallback.onSelectItem(getAdapterPosition());
            }
        }
    }
}
