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
import android.widget.Toast;

import com.aibinong.tantan.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TagsEditAdapter extends RecyclerView.Adapter<TagsEditAdapter.TagHolder> {
    public static int ITEM_VIEW_TYPE_MYTAG_HEADER = 0, ITEM_VIEW_TYPE_SELECTED_TAG = 1, ITEM_VIEW_TYPE_ALLTAG_HEADER = 2, ITEM_VIEW_TYPE_UNSELECTED_TAG = 3;


    private ArrayList<String> mSelectedTags, mUnSelectedTags;
    private EditTagAdapterListener mEditTagAdapterListener;

    public TagsEditAdapter() {
        mSelectedTags = new ArrayList<>();
        mUnSelectedTags = new ArrayList<>();
        mEditTagAdapterListener = new EditTagAdapterListener() {
            @Override
            public void unSelect(int adapterPos) {
                //y移到未选头部
                int arrPos = adapterPos - getMyTagHeaderCount();
                mUnSelectedTags.add(0, mSelectedTags.remove(arrPos));
                int moveToPos = getMyTagHeaderCount() + getSelectedTagCount() + getAllTagHeaderCount();
                notifyItemMoved(adapterPos, moveToPos);
                if (getMyTagHeaderCount() > 0) {
                    notifyItemChanged(getMyTagHeaderCount() - 1);
                }
                if (getAllTagHeaderCount() > 0) {
                    notifyItemChanged(getMyTagHeaderCount() + getSelectedTagCount());
                }
            }

            @Override
            public void select(int adapterPos) {
                //移动到已选列表尾部
                int arrPos = adapterPos - getMyTagHeaderCount() - getSelectedTagCount() - getAllTagHeaderCount();
                int moveToPos = getMyTagHeaderCount() + getSelectedTagCount();
                mSelectedTags.add(mUnSelectedTags.remove(arrPos));
                notifyItemMoved(adapterPos, moveToPos);
                if (getMyTagHeaderCount() > 0) {
                    notifyItemChanged(getMyTagHeaderCount() - 1);
                }
                if (getAllTagHeaderCount() > 0) {
                    notifyItemChanged(getMyTagHeaderCount() + getSelectedTagCount());
                }
            }

            @Override
            public int getSelectedCount() {
                return getSelectedTagCount();
            }
        };
    }

    public ArrayList<String> getSelectedTags() {
        return mSelectedTags;
    }

    public ArrayList<String> getUnSelectedTags() {
        return mUnSelectedTags;
    }

    public int getMyTagHeaderCount() {
        return 1;
    }

    public int getAllTagHeaderCount() {
        return 1;
    }

    public int getSelectedTagCount() {
        return mSelectedTags.size();
    }

    public int getUnSelectedTagCount() {
        return mUnSelectedTags.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position < getMyTagHeaderCount()) {
            return ITEM_VIEW_TYPE_MYTAG_HEADER;
        }
        if (position < (getMyTagHeaderCount() + getSelectedTagCount())) {
            return ITEM_VIEW_TYPE_SELECTED_TAG;
        }
        if (position < (getMyTagHeaderCount() + getSelectedTagCount() + getAllTagHeaderCount())) {
            return ITEM_VIEW_TYPE_ALLTAG_HEADER;
        }
        if (position < (getMyTagHeaderCount() + getSelectedTagCount() + getAllTagHeaderCount() + getUnSelectedTagCount())) {
            return ITEM_VIEW_TYPE_UNSELECTED_TAG;
        }
        return super.getItemViewType(position);
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_MYTAG_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_tags_edit_divider, parent, false);
            TagHeaderHolder holder = new TagHeaderHolder(view);
            return holder;
        } else if (viewType == ITEM_VIEW_TYPE_ALLTAG_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_tags_edit_divider, parent, false);
            TagHeaderHolder holder = new TagHeaderHolder(view);
            return holder;
        } else if (viewType == ITEM_VIEW_TYPE_SELECTED_TAG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orange_tag, parent, false);
            SelectedTagHolder holder = new SelectedTagHolder(view, mEditTagAdapterListener);
            return holder;
        } else if (viewType == ITEM_VIEW_TYPE_UNSELECTED_TAG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orange_tag_hollow, parent, false);
            UnSelectedTagHolder holder = new UnSelectedTagHolder(view, mEditTagAdapterListener);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(TagHolder holder, int position) {
        if (holder instanceof SelectedTagHolder) {
            SelectedTagHolder selectedTagHolder = (SelectedTagHolder) holder;
            int tagPos = position - getMyTagHeaderCount();
            String tag = mSelectedTags.get(tagPos);
            selectedTagHolder.bindData(tag);
        } else if (holder instanceof UnSelectedTagHolder) {
            UnSelectedTagHolder unSelectedTagHolder = (UnSelectedTagHolder) holder;
            int tagPos = position - getMyTagHeaderCount() - getSelectedTagCount() - getAllTagHeaderCount();
            String tag = mUnSelectedTags.get(tagPos);
            unSelectedTagHolder.bindData(tag);
        } else if (holder instanceof TagHeaderHolder) {
            TagHeaderHolder tagHeaderHolder = (TagHeaderHolder) holder;
            int viewType = getItemViewType(position);
            if (viewType == ITEM_VIEW_TYPE_ALLTAG_HEADER) {
                tagHeaderHolder.bindData(getUnSelectedTagCount(), false);
            } else {
                tagHeaderHolder.bindData(getSelectedTagCount(), true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getMyTagHeaderCount() + getSelectedTagCount() + getAllTagHeaderCount() + getUnSelectedTagCount();
    }

    static abstract class TagHolder extends RecyclerView.ViewHolder {

        public TagHolder(View itemView) {
            super(itemView);
        }
    }

    static class TagHeaderHolder extends TagHolder {

        @Bind(R.id.tv_item_tags_edit_divider_title)
        TextView mTvItemTagsEditDividerTitle;
        @Bind(R.id.tv_item_tags_edit_count)
        TextView mTvItemTagsEditCount;
        @Bind(R.id.tv_item_tags_edit_limit_tips)
        TextView mTvItemTagsEditLimitTips;
        private int mCount;
        private boolean mSelectedTag;

        public TagHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(int count, boolean selectedTag) {
            mCount = count;
            mSelectedTag = selectedTag;
            mTvItemTagsEditCount.setText(mCount + "");
            if (!mSelectedTag) {
                mTvItemTagsEditDividerTitle.setText(itemView.getResources().getString(R.string.abn_yueai_tag_edit_title_alltag));
                mTvItemTagsEditLimitTips.setVisibility(View.INVISIBLE);
            } else {
                mTvItemTagsEditDividerTitle.setText(itemView.getResources().getString(R.string.abn_yueai_tag_edit_title_mytag));
                mTvItemTagsEditLimitTips.setVisibility(View.VISIBLE);
            }
        }
    }

    static class SelectedTagHolder extends TagHolder implements View.OnClickListener {
        @Bind(R.id.tv_item_orange_tag)
        TextView mTvItemOrangeTag;
        private String mTag;
        private EditTagAdapterListener mEditTagAdapterListener;

        public SelectedTagHolder(View itemView, EditTagAdapterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTvItemOrangeTag.setOnClickListener(this);
            mEditTagAdapterListener = listener;
        }

        public void bindData(String tag) {
            mTag = tag;
            mTvItemOrangeTag.setText(mTag);
        }

        @Override
        public void onClick(View v) {
            //至少留一个
            if (mEditTagAdapterListener.getSelectedCount() <= 1) {
                Toast.makeText(itemView.getContext(), "至少需要保留一个标签", Toast.LENGTH_SHORT).show();
                return;
            }
            if (v == mTvItemOrangeTag) {
                mEditTagAdapterListener.unSelect(getAdapterPosition());
            }
        }
    }

    static class UnSelectedTagHolder extends TagHolder implements View.OnClickListener {

        @Bind(R.id.tv_item_orange_tag)
        TextView mTvItemOrangeTag;
        private String mTag;
        private EditTagAdapterListener mEditTagAdapterListener;

        public UnSelectedTagHolder(View itemView, EditTagAdapterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTvItemOrangeTag.setOnClickListener(this);
            mEditTagAdapterListener = listener;
        }

        public void bindData(String tag) {
            mTag = tag;
            mTvItemOrangeTag.setText(mTag);
        }

        @Override
        public void onClick(View v) {
            if (mEditTagAdapterListener.getSelectedCount() >= 10) {
                Toast.makeText(itemView.getContext(), "最多只能选择10个标签", Toast.LENGTH_SHORT).show();
                return;
            }
            if (v == mTvItemOrangeTag) {
                mEditTagAdapterListener.select(getAdapterPosition());
            }
        }
    }

    interface EditTagAdapterListener {
        void unSelect(int adapterPos);

        void select(int adapterPos);

        int getSelectedCount();
    }
}
