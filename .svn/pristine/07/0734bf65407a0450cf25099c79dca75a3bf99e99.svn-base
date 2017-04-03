package com.aibinong.tantan.ui.adapter.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/28.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.viewholder.BaseCommonVH;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.bumptech.glide.Glide;
import com.fatalsignal.view.RoundAngleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectToSayHiAdapter extends RecyclerView.Adapter<BaseCommonVH<UserEntity>> {

    private ArrayList<UserEntity> mUserEntities;
    private ArrayList<UserEntity> mSelectedUsers = new ArrayList<>();

    public ArrayList<UserEntity> getSelectedUsers() {
        return mSelectedUsers;
    }

    public void setData(ArrayList<UserEntity> userEntities) {
        mUserEntities = userEntities;
        if (mUserEntities != null) {
            mSelectedUsers.addAll(mUserEntities);
        }
    }

    @Override
    public BaseCommonVH<UserEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_select_sayhi, parent, false);
        SelectSayHiVH holder = new SelectSayHiVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseCommonVH<UserEntity> holder, int position) {
        holder.bindData(mUserEntities.get(position), 0);
    }

    @Override
    public int getItemCount() {
        return mUserEntities == null ? 0 : mUserEntities.size();
    }

    class SelectSayHiVH extends BaseCommonVH<UserEntity> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        @Bind(R.id.riv_item_select_sayhi)
        RoundAngleImageView mRivItemSelectSayhi;
        @Bind(R.id.checkbox_item_selectquestion_group)
        CheckBox mCheckboxItemSelectquestionGroup;
        @Bind(R.id.tv_item_select_sayhi_job)
        TextView mTvItemSelectSayhiJob;
        private UserEntity mUserEntity;

        public SelectSayHiVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(UserEntity data, int dataType) {
            mUserEntity = data;
            Glide.with(itemView.getContext()).load(mUserEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivItemSelectSayhi);
            mTvItemSelectSayhiJob.setText(mUserEntity.occupation);

            mCheckboxItemSelectquestionGroup.setOnCheckedChangeListener(null);
            if (mSelectedUsers.contains(mUserEntity)) {
                mCheckboxItemSelectquestionGroup.setChecked(true);
            } else {
                mCheckboxItemSelectquestionGroup.setChecked(false);
            }
            mCheckboxItemSelectquestionGroup.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mSelectedUsers.add(mUserEntity);
            } else {
                mSelectedUsers.remove(mUserEntity);
            }
        }

        @Override
        public void onClick(View v) {
            mCheckboxItemSelectquestionGroup.performClick();
        }
    }
}
