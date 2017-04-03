package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.viewholder.BaseCommonVH;
import com.aibinong.tantan.ui.adapter.viewholder.CommonUserInfoVH;
import com.aibinong.tantan.ui.widget.ListUserInfoView;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;
import java.util.List;

/*
 *顶上固定显示一个谁关注了我
 *
 * */
public class FollowListAdapter extends RecyclerView.Adapter<BaseCommonVH> {
    private static final int VIEW_TYPE_WHO_ATTENTION = 0, VIEW_TYPE_NORMAL = 1;
    private final String TAG = "FollowListAdapter";

    public List<UserEntity> mUsersList = new ArrayList<>();
    private CommonUserInfoVH holder;
    private boolean sayHiState = false;

    public boolean isSayHiState() {
        return sayHiState;
    }

    public void setSayHiState(boolean sayHiState) {
        this.sayHiState = sayHiState;
    }

    public List<UserEntity> getUsersList() {
        return mUsersList;
    }

    public void onSayHiSended(String userId) {
        //找到对应的位置，刷新
        for (int position = 0; position < getItemCount(); position++) {
            UserEntity userEntity = mUsersList.get(position);
            if (userEntity.id.equals(userId)) {
                notifyItemChanged(position + 1);
                break;
            }
        }
    }

    public void onSayHiFailed(String userId) {
        for (int position = 0; position < getItemCount(); position++) {
            UserEntity userEntity = mUsersList.get(position);
            if (userEntity.id.equals(userId)) {
                notifyItemChanged(position + 1);
                break;
            }
        }
    }

    public void removeItem(String uuid) {
        for (int i = 0; i < mUsersList.size(); i++) {
            if (mUsersList.get(i).id.equals(uuid)) {
                mUsersList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @Override
    public BaseCommonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_pmlist, parent, false);
        holder = new CommonUserInfoVH(view);
        return holder;

    }


    @Override
    public void onViewDetachedFromWindow(BaseCommonVH holder) {
        holder = null;
        super.onViewDetachedFromWindow(holder);


    }

    @Override
    public void onBindViewHolder(final BaseCommonVH holder, int position) {
        int viewType = getItemViewType(position);
        if (VIEW_TYPE_NORMAL == viewType) {
            ListUserInfoView.sayHiState = isSayHiState();
            holder.bindData(mUsersList.get(position - 1), ListUserInfoView.ITEM_TYPE_USERLIST);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick:===== " + holder.getAdapterPosition());
                    if (null != mOnSwipeListener) {
                        mOnSwipeListener.onDel(holder.getAdapterPosition());
                    }
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.bindData(null, ListUserInfoView.ITEM_TYPE_FOLLOWER);
        }
        holder.btnDelete.setText(R.string.abn_yueai_msg_tab_cancle_follow);
    }

    @Override
    public int getItemCount() {
        return mUsersList == null ? 1 : (1 + mUsersList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_WHO_ATTENTION;
        }
        return VIEW_TYPE_NORMAL;
    }

    private PMListAdapter.onSwipeListener mOnSwipeListener;


    public void setOnDelListener(PMListAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

    }
}
