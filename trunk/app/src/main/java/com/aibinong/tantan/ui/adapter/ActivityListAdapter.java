package com.aibinong.tantan.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityListHolder> {


    private static final String TAG = "ActivityListAdapter";

    private ArrayList<ActivityListEntity.ListBean> mActivityListEntities = new ArrayList<>();

    private void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private   ItemClickListener mItemClickListener;

   public interface ItemClickListener {
        void click(ActivityListEntity.ListBean listBean);
    }

    public ArrayList<ActivityListEntity.ListBean> getmActivityListEntities() {
        return mActivityListEntities;
    }

    public void setActivityListEntities(List<ActivityListEntity.ListBean> beans) {
        this.mActivityListEntities.clear();
        this.mActivityListEntities.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public ActivityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_activity_list, parent, false);
        ActivityListHolder holder = new ActivityListHolder(view);
        return holder;

    }


//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    @Override
    public void onBindViewHolder(ActivityListHolder holder, int position) {
        Log.d("====onBindViewHolder===", holder.toString());

        ActivityListEntity.ListBean listBean = mActivityListEntities.get(position);
        holder.bindData(listBean);
    }

    @Override
    public int getItemCount() {
        Log.d("====getItemCount=====", mActivityListEntities.size() + "");

        return mActivityListEntities.size();

    }

    class ActivityListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.ll_activity_list_item)
        RelativeLayout llActivityListItem;
        ActivityListEntity.ListBean mListBean =new ActivityListEntity.ListBean();
        public ActivityListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llActivityListItem.setOnClickListener(this);
        }

        void bindData(ActivityListEntity.ListBean bean) {
            mListBean = bean;
            Glide.with(itemView.getContext()).load(bean.icon).placeholder(null).into(ivIcon);
            tvTitle.setText(bean.title);
            tvContent.setText(bean.content);
        }

        @Override
        public void onClick(View v) {
            CommonWebActivity.launchIntent(v.getContext(), CommonUtils.getCommonPageUrl(mListBean.event.data.url, null));
        }
    }
}
