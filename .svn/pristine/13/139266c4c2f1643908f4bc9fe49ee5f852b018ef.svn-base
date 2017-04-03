package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.TagsEditPresenter;
import com.aibinong.tantan.ui.adapter.TagsEditAdapter;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TagsEditActivity extends ActivityBase implements TagsEditPresenter.ITagsEdit {

    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_tags_edit)
    RecyclerView mRecyclerTagsEdit;
    private UserEntity mUserEntity;
    private TagsEditAdapter mTagsEditAdapter;
    private TagsEditPresenter mTagsEditPresenter;

    public static Intent launchIntent(Context context, boolean startDirect, UserEntity userEntity) {
        Intent intent = new Intent(context, TagsEditActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        if (startDirect) {
            context.startActivity(intent);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_tags_edit);
        ButterKnife.bind(this);
        requireTransStatusBar();
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);
        mTagsEditPresenter = new TagsEditPresenter(this, mUserEntity.sex);
        setupView(savedInstanceState);
        mTagsEditPresenter.loadAllTags(mUserEntity.tags);
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }


    @Override
    public void onBackPressed() {
        mUserEntity.tags = mTagsEditAdapter.getSelectedTags();
        Intent intent = getIntent();
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < mTagsEditAdapter.getMyTagHeaderCount()) {
                    return 4;
                }
                if (position >= (mTagsEditAdapter.getMyTagHeaderCount() + mTagsEditAdapter.getSelectedTagCount()) && position < (mTagsEditAdapter.getMyTagHeaderCount() + mTagsEditAdapter.getSelectedTagCount() + mTagsEditAdapter.getAllTagHeaderCount())) {
                    return 4;
                }
                return 1;
            }
        });
        mRecyclerTagsEdit.setLayoutManager(gridLayoutManager);
        mTagsEditAdapter = new TagsEditAdapter();
        mRecyclerTagsEdit.setAdapter(mTagsEditAdapter);
        if (mUserEntity.tags != null) {
            mTagsEditAdapter.getSelectedTags().addAll(mUserEntity.tags);
            mTagsEditAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadAllTagsFailed(ResponseResult e) {

    }

    @Override
    public void onLoadAllTagsSuccess(ArrayList<String> tags) {
        if (tags != null) {
            mTagsEditAdapter.getUnSelectedTags().addAll(tags);
            mTagsEditAdapter.notifyDataSetChanged();
        }
    }
}
