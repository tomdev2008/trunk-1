package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/8.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.activity.FansListActivity;
import com.aibinong.tantan.ui.adapter.viewholder.BaseCommonVH;
import com.aibinong.tantan.ui.adapter.viewholder.CommonUserInfoVH;
import com.aibinong.tantan.ui.widget.ListUserInfoView;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhoLikeMeAdapter extends RecyclerView.Adapter<BaseCommonVH> {
    public static final int ITEM_VIEW_TYPE_COUNT = 0, ITEM_VIEW_TYPE_NORMAL = 1;

    private int mListType;
    private List<UserEntity> mUsersList = new ArrayList<>();

    public WhoLikeMeAdapter(int listType) {
        mListType = listType;
    }

    public List<UserEntity> getUsersList() {
        return mUsersList;
    }


    public void onSayHiSended(String userId) {
        //找到对应的位置，刷新
        for (int position = 0; position < getItemCount(); position++) {
            UserEntity userEntity = mUsersList.get(position);
            if (userEntity.id != null && userEntity.id.equals(userId)) {
                notifyItemChanged(position + 1);
                break;
            }
        }
    }

    public void onSayHiFailed(String userId) {
        for (int position = 0; position < getItemCount(); position++) {
            UserEntity userEntity = mUsersList.get(position);
            if (userEntity.id != null && userEntity.id.equals(userId)) {
                notifyItemChanged(position + 1);
                break;
            }
        }
    }

    @Override
    public BaseCommonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_COUNT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_wholikeme_count, parent, false);
            LikeMeCountHolder holder = new LikeMeCountHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
            CommonUserInfoVH holder = new CommonUserInfoVH(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(BaseCommonVH holder, int position) {
        if (position > 0) {
            holder.bindData(mUsersList.get(position - 1), ListUserInfoView.ITEM_TYPE_USERLIST);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.bindData(null, mListType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE_COUNT;
        } else {
            return ITEM_VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? 0 : (mUsersList.size() + 1);
    }

    static class LikeMeCountHolder extends BaseCommonVH<UserEntity> {
        @Bind(R.id.tv_wholikeme_count)
        TextView mTvWholikemeCount;

        public LikeMeCountHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindData(UserEntity data, int type) {
            if (type == FansListActivity.LIST_TYPE_FANS_LIST) {
                mTvWholikemeCount.setText(R.string.abn_yueai_wholikeme_howmuch_left);
            } else {
                mTvWholikemeCount.setText(R.string.abn_yueai_browse_howmuch_left);
            }
        }


    }

}
