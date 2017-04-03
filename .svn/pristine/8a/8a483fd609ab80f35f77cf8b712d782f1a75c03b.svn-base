package com.aibinong.tantan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.presenter.ActivityListPresenter;
import com.aibinong.tantan.presenter.ImgPlazaPresenter;
import com.aibinong.tantan.ui.adapter.ActivityListAdapter;
import com.aibinong.tantan.ui.adapter.ImgPlazaAdapter;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.CommonUtils;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.yueaiapi.pojo.ActivityListEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.bumptech.glide.Glide;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fatalsignal.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by hubin on 2017/3/3.
 */

public class ActivityListActivity extends ActivityBase implements ActivityListPresenter.IActivityList,
        EmptyView.CallBack{
    private static final String TAG = "ActivityListActivity";
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_activity_list)
    RecyclerView recyclerActivityList;
    @Bind(R.id.emptyview_activity_list)
    EmptyView emptyviewActivitylist;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout rotateHeaderListViewFrame;
    private LinearLayoutManager mLinearLayoutManager;
    private ActivityListAdapter mActivityListAdapter;
    private ActivityListPresenter mActivityListPresenter;

    public static Intent launchIntent(Context context) {
        Intent intent = new Intent(context, ActivityListActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_activitylist);
        ButterKnife.bind(this);
        setupToolbar(toolbar, tvToolbarTitle, true);
        mActivityListPresenter = new ActivityListPresenter(this);
        mActivityListPresenter.onCreate();
        mActivityListAdapter = new ActivityListAdapter();

        setupView(savedInstanceState);
        requireTransStatusBar();

    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mActivityListPresenter.getActivityLists(false);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerActivityList.setLayoutManager(mLinearLayoutManager);

        recyclerActivityList.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mActivityListAdapter));

        recyclerActivityList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean getSupportsChangeAnimations() {
                return false;
            }
        });
        emptyviewActivitylist.attatchWithView(recyclerActivityList, this);

        tvToolbarTitle.setText(R.string.abn_yueai_activity_list);

        rotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
            //下拉刷新
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, recyclerActivityList, header);
            }
        });

        //分页
        recyclerActivityList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerActivityList);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(ActivityListActivity.this, recyclerActivityList, recyclerActivityList.getAdapter().getItemCount() <= 0, LoadingFooter.State.Loading, null);
                //加载更多
                loadData(false);
            }
        });
        recyclerActivityList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i("onScrollStateChanged", "" + newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    Glide.with(getApplicationContext()).pauseRequests();
                } else {
                    Glide.with(getApplicationContext()).resumeRequests();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onReuireStatusFailed(ResponseResult e) {
        emptyviewActivitylist.loadingFailed(e.getMessage());
        rotateHeaderListViewFrame.refreshComplete();
        RecyclerViewStateUtils.setFooterViewState(this, recyclerActivityList, recyclerActivityList.getAdapter().getItemCount() <= 1, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    @Override
    public void onRequireStatusSuccess(ActivityListEntity activityListEntities, Page page) {
        emptyviewActivitylist.loadingComplete();
        rotateHeaderListViewFrame.refreshComplete();
        if (activityListEntities != null) {
//            emptyviewActivitylist.setVisibility(View.GONE);
//            recyclerActivityList.setVisibility(View.VISIBLE);
            mActivityListAdapter.setActivityListEntities(activityListEntities.list);
            android.util.Log.e(TAG, "onRequireStatusSuccess: activityListEntities" + activityListEntities.list.size());

        }
        int oldCount = mActivityListAdapter.getItemCount();
        if (page.toPage <= 1) {
            mActivityListAdapter.getmActivityListEntities().clear();
            mActivityListAdapter.getmActivityListEntities().addAll(activityListEntities.list);
            mActivityListAdapter.notifyDataSetChanged();
        } else {
            mActivityListAdapter.getmActivityListEntities().addAll(activityListEntities.list);
            mActivityListAdapter.notifyItemRangeInserted(oldCount, mActivityListAdapter.getItemCount() - oldCount);
        }
        RecyclerViewStateUtils.setFooterViewState(this, recyclerActivityList, recyclerActivityList.getAdapter().getItemCount() <= 1, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);
    }

    @Override
    public void onRetryClick(EmptyView.LoadingState state) {

    }

    @Override
    public boolean needHideContentView() {
        return mActivityListAdapter.getItemCount() <= 0;
    }

    private void loadData(boolean refresh) {
        if (mActivityListAdapter.getItemCount() <= 1) {
            emptyviewActivitylist.startLoading();
        }
        mActivityListPresenter.getActivityLists(refresh);
    }

}
