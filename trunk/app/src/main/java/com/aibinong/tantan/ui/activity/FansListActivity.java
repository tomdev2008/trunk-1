package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/8.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.FansListPresenter;
import com.aibinong.tantan.ui.adapter.WhoLikeMeAdapter;
import com.aibinong.tantan.ui.fragment.message.DividerItemDecoration;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FansListActivity extends ActivityBase implements ISearchList {
    public static final String INTENT_EXTRA_KEY_LIST_TYPE = "INTENT_EXTRA_KEY_LIST_TYPE";
    public static final int LIST_TYPE_FANS_LIST = 0;//谁关注了我
    public static final int LIST_TYPE_BROWSE_LIST = 1;//谁看了我

    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_wholikeme_list)
    RecyclerView mRecyclerWholikemeList;
    @Bind(R.id.emptyview_wholikeme)
    EmptyView mEmptyviewWholikeme;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mRotateHeaderListViewFrame;
    private WhoLikeMeAdapter mWhoLikeMeAdapter;
    private FansListPresenter mFunsListPresenter;
    private int mListType = LIST_TYPE_FANS_LIST;

    public static Intent launchIntentBrowseList(Context context) {
        Intent intent = new Intent(context, FansListActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_LIST_TYPE, LIST_TYPE_BROWSE_LIST);
        context.startActivity(intent);
        return intent;
    }

    public static Intent launchIntentFansList(Context context) {
        Intent intent = new Intent(context, FansListActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_LIST_TYPE, LIST_TYPE_FANS_LIST);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_wholikeme);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        mListType = getIntent().getIntExtra(INTENT_EXTRA_KEY_LIST_TYPE, LIST_TYPE_FANS_LIST);
        setupView(savedInstanceState);
        requireTransStatusBar();
        if (mListType == LIST_TYPE_FANS_LIST) {
            setTitle("谁关注了我");
        } else {
            setTitle("谁看过我");
        }
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mFunsListPresenter = new FansListPresenter(mListType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FansListActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerWholikemeList.setLayoutManager(linearLayoutManager);
        mWhoLikeMeAdapter = new WhoLikeMeAdapter(mListType);

        mRecyclerWholikemeList.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mWhoLikeMeAdapter));
        mRecyclerWholikemeList.addItemDecoration(new DividerItemDecoration(FansListActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
                                                     @Override
                                                     public void onRefreshBegin(PtrFrameLayout frame) {
                                                         loadData(true);
                                                     }

                                                     @Override
                                                     public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                                                         return super.checkCanDoRefresh(frame, mRecyclerWholikemeList, header);
                                                     }
                                                 }

        );
        mRecyclerWholikemeList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);

                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerWholikemeList);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(FansListActivity.this, mRecyclerWholikemeList, mRecyclerWholikemeList.getAdapter().getItemCount() <= 1, LoadingFooter.State.Loading, null);
                //加载更多
                loadData(false);
            }
        });
        mEmptyviewWholikeme.setCallBack(new EmptyView.CallBack() {
            @Override
            public void onRetryClick(EmptyView.LoadingState state) {
                if (state == EmptyView.LoadingState.EMPTYDATA) {
                    //无数据，一般来说是去看别的
                    Intent intent = new Intent(FansListActivity.this, MainActivity.class);
                    intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_TAB, 3);
                    startActivity(intent);
                } else {
                    //加载失败点击重试
                    loadData(true);
                }
            }

            @Override
            public boolean needHideContentView() {
                return false;
            }
        });
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                loadData(true);
            }
        });
    }

    private void loadData(boolean refresh) {
        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
            mEmptyviewWholikeme.startLoading();
        }
        mFunsListPresenter.funs_list(this, refresh);
    }


    @Override
    public void onListStart() {

    }

    @Override
    public void onListFailed(Throwable e) {
        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
//            mEmptyviewWholikeme.loadingFailed(e.getMessage());
            mEmptyviewWholikeme.emptyData();
        } else {
            mEmptyviewWholikeme.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();
        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerWholikemeList, mWhoLikeMeAdapter.getItemCount() <= 1, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    @Override
    public void onListSuccess(ArrayList<UserEntity> userEntities, Page page) {
        if (page.toPage <= 1) {
            int oldSize = mWhoLikeMeAdapter.getItemCount();
            mWhoLikeMeAdapter.getUsersList().clear();
            mWhoLikeMeAdapter.notifyItemRangeRemoved(0, oldSize);
        }
        if (userEntities.size() > 0) {
            int oldSize = mWhoLikeMeAdapter.getItemCount();
            mWhoLikeMeAdapter.getUsersList().addAll(userEntities);
            mWhoLikeMeAdapter.notifyDataSetChanged();
            mWhoLikeMeAdapter.notifyItemRangeInserted(oldSize, userEntities.size());
        }
        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerWholikemeList, mWhoLikeMeAdapter.getItemCount() <= 1, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);

        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
            mEmptyviewWholikeme.emptyData();
        } else {
            mEmptyviewWholikeme.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();
    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventReceived(BaseEvent<String> event) {
//        if (BaseEvent.ACTION_SAYHI_FAILED.equals(event.action)) {
//            mWhoLikeMeAdapter.onSayHiFailed(event.data);
//            showToast("打招呼失败");
//        } else if (BaseEvent.ACTION_SAYHI_SUCCESS.equals(event.action)) {
//            mWhoLikeMeAdapter.onSayHiSended(event.action);
//            showToast("打招呼成功");
//        } else if (BaseEvent.ACTION_SAYHI_FAILED.equals(event.action)) {
//            mWhoLikeMeAdapter.onSayHiFailed(event.data);
//            showToast("打招呼失败");
//        }
//    }

}
