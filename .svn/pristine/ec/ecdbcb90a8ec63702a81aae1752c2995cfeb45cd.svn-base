//package com.aibinong.tantan.ui.activity;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/11/8.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.TextView;
//
//import com.aibinong.tantan.R;
//import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
//import com.aibinong.tantan.constant.IntentExtraKey;
//import com.aibinong.tantan.presenter.WhoLikeMePresenter;
//import com.aibinong.tantan.ui.adapter.WhoLikeMeAdapter;
//import com.aibinong.tantan.ui.widget.EmptyView;
//import com.aibinong.tantan.ui.widget.LoadingFooter;
//import com.aibinong.tantan.util.RecyclerViewStateUtils;
//import com.aibinong.yueaiapi.pojo.Page;
//import com.aibinong.yueaiapi.pojo.ResponseResult;
//import com.aibinong.yueaiapi.pojo.UserEntity;
//import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
//import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import in.srain.cube.views.ptr.PtrClassicFrameLayout;
//import in.srain.cube.views.ptr.PtrDefaultHandler;
//import in.srain.cube.views.ptr.PtrFrameLayout;
//
//public class WhoLikeMeActivity extends ActivityBase implements WhoLikeMePresenter.IWhoLikeMe {
//    @Bind(R.id.tv_toolbar_title)
//    TextView mTvToolbarTitle;
//    @Bind(R.id.toolbar)
//    Toolbar mToolbar;
//    @Bind(R.id.recycler_wholikeme_list)
//    RecyclerView mRecyclerWholikemeList;
//    @Bind(R.id.emptyview_wholikeme)
//    EmptyView mEmptyviewWholikeme;
//    @Bind(R.id.rotate_header_list_view_frame)
//    PtrClassicFrameLayout mRotateHeaderListViewFrame;
//    private WhoLikeMeAdapter mWhoLikeMeAdapter;
//    private WhoLikeMePresenter mWhoLikeMePresenter;
//    private BroadcastReceiver mLikeDislikeReceiver;
//    private int mLikeMeCount;
//
//    public static Intent launchIntent(Context context, int count) {
//        Intent intent = new Intent(context, WhoLikeMeActivity.class);
//        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USER_COUNT, count);
//        context.startActivity(intent);
//        return intent;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.abn_yueai_activity_wholikeme);
//        ButterKnife.bind(this);
//        setupToolbar(mToolbar, mTvToolbarTitle, true);
//        mLikeMeCount = getIntent().getIntExtra(IntentExtraKey.INTENT_EXTRA_KEY_USER_COUNT, 0);
//        setupView(savedInstanceState);
//        requireTransStatusBar();
//        mLikeDislikeReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                boolean isLike = intent.getBooleanExtra(IntentExtraKey.INTENT_EXTRA_KEY_IS_LIKE, false);
//                String uuid = intent.getStringExtra(IntentExtraKey.INTENT_EXTRA_KEY_UUUID);
//                if (isLike) {
//                    //更新单个用户的资料
//                    mWhoLikeMePresenter.getUserInfo(uuid);
//                } else {
//
//                }
//            }
//        };
//        GlobalLocalBroadCastManager.getInstance().registerLikeOrDisLike(mLikeDislikeReceiver);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mLikeDislikeReceiver);
//    }
//
//    @Override
//    protected boolean swipeBackEnable() {
//        return true;
//    }
//
//    @Override
//    protected void setupView(@Nullable Bundle savedInstanceState) {
//        mWhoLikeMePresenter = new WhoLikeMePresenter();
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WhoLikeMeActivity.this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerWholikemeList.setLayoutManager(linearLayoutManager);
//        mWhoLikeMeAdapter = new WhoLikeMeAdapter();
//
//        mRecyclerWholikemeList.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mWhoLikeMeAdapter));
//        mRecyclerWholikemeList.addItemDecoration(new DividerItemDecoration(WhoLikeMeActivity.this, DividerItemDecoration.VERTICAL));
//        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
//                                                     @Override
//                                                     public void onRefreshBegin(PtrFrameLayout frame) {
//                                                         loadData(true);
//                                                     }
//
//                                                     @Override
//                                                     public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                                                         return super.checkCanDoRefresh(frame, mRecyclerWholikemeList, header);
//                                                     }
//                                                 }
//
//        );
//        mRecyclerWholikemeList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
//
//            @Override
//            public void onLoadNextPage(View view) {
//                super.onLoadNextPage(view);
//
//                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerWholikemeList);
//                if (state == LoadingFooter.State.Loading) {
//                    return;
//                }
//                if (state == LoadingFooter.State.TheEnd) {
//                    return;
//                }
//                RecyclerViewStateUtils.setFooterViewState(WhoLikeMeActivity.this, mRecyclerWholikemeList, mRecyclerWholikemeList.getAdapter().getItemCount() <= 1, LoadingFooter.State.Loading, null);
//                //加载更多
//                loadData(false);
//            }
//        });
//        mEmptyviewWholikeme.setCallBack(new EmptyView.CallBack() {
//            @Override
//            public void onRetryClick(EmptyView.LoadingState state) {
//                if (state == EmptyView.LoadingState.EMPTYDATA) {
//                    //无数据，一般来说是去看别的
//                    Intent intent = new Intent(WhoLikeMeActivity.this, MainActivity.class);
//                    intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_TAB, 3);
//                    startActivity(intent);
//                } else {
//                    //加载失败点击重试
//                    loadData(true);
//                }
//            }
//
//            @Override
//            public boolean needHideContentView() {
//                return false;
//            }
//        });
//        postDelayLoad(new Runnable() {
//            @Override
//            public void run() {
//                loadData(true);
//            }
//        });
//    }
//
//    private void loadData(boolean refresh) {
//        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
//            mEmptyviewWholikeme.startLoading();
//        }
//        mWhoLikeMePresenter.getFuns(refresh);
//    }
//
//    @Override
//    public void onGetFunsFailed(ResponseResult e) {
//        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
////            mEmptyviewWholikeme.loadingFailed(e.getMessage());
//            mEmptyviewWholikeme.emptyData();
//        } else {
//            mEmptyviewWholikeme.loadingComplete();
//        }
//        mRotateHeaderListViewFrame.refreshComplete();
//        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerWholikemeList, mWhoLikeMeAdapter.getItemCount() <= 1, LoadingFooter.State.NetWorkError, null, e.getMessage());
//    }
//
//    @Override
//    public void onGetFunsSuccess(ArrayList<UserEntity> userEntities, Page page) {
//
//        if (page.toPage <= 1) {
//            int oldSize = mWhoLikeMeAdapter.getItemCount();
//            mWhoLikeMeAdapter.getUsersList().clear();
//            mWhoLikeMeAdapter.notifyItemRangeRemoved(0, oldSize);
//        }
//        if (userEntities.size() > 0) {
//            int oldSize = mWhoLikeMeAdapter.getItemCount();
//            mWhoLikeMeAdapter.getUsersList().addAll(userEntities);
//            mWhoLikeMeAdapter.notifyDataSetChanged();
//            mWhoLikeMeAdapter.notifyItemRangeInserted(oldSize, userEntities.size());
//        }
//        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerWholikemeList, mWhoLikeMeAdapter.getItemCount() <= 1, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);
//
//        if (mWhoLikeMeAdapter.getItemCount() <= 1) {
//            mEmptyviewWholikeme.emptyData();
//        } else {
//            mEmptyviewWholikeme.loadingComplete();
//        }
//        mRotateHeaderListViewFrame.refreshComplete();
//    }
//
//    @Override
//    public void onGetUserInfoSuccess(UserEntity userEntity) {
//        List<UserEntity> userEntities = mWhoLikeMeAdapter.getUsersList();
//        for (int i = 0; i < userEntities.size(); i++) {
//            UserEntity userEntity1 = userEntities.get(i);
//            if (userEntity1.id.equals(userEntity.id)) {
//                mWhoLikeMeAdapter.getUsersList().set(i, userEntity);
//                mWhoLikeMeAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }
//}
