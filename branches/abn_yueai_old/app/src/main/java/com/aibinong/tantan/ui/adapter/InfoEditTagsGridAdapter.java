package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibinong.tantan.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoEditTagsGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEMVIEW_TYPE_NORMAL = 0, ITEMVIEW_TYPE_ADD = 1;
    List<String> mUserTags;

    public InfoEditTagsGridAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEMVIEW_TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orange_addtag, parent, false);
            TagsAddHolder holder = new TagsAddHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orange_tag, parent, false);
            TagsHolder holder = new TagsHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagsHolder) {
            ((TagsHolder) holder).bindData(mUserTags.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return getTagsCount() + 1;
    }

    public int getTagsCount() {
        return mUserTags == null ? 0 : mUserTags.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getTagsCount()) {
            return ITEMVIEW_TYPE_NORMAL;
        }
        return ITEMVIEW_TYPE_ADD;
    }

    public void setUserTags(List<String> userTags) {
        mUserTags = userTags;
    }

    static class TagsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_orange_tag)
        TextView mTvItemOrangeTag;
        private String mTag;

        TagsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(String tag) {
            mTag = tag;
            mTvItemOrangeTag.setText(tag);
        }
    }

    static class TagsAddHolder extends RecyclerView.ViewHolder {

        TagsAddHolder(View view) {
            super(view);

        }

    }
}
