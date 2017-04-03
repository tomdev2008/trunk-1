package com.aibinong.tantan.ui.fragment;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.LocalBroadCastConst;
import com.aibinong.tantan.presenter.ImgPlazaPresenter;
import com.aibinong.tantan.ui.adapter.ImgPlazaAdapter;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.bumptech.glide.Glide;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fatalsignal.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ImgPlazaFragment extends FragmentBase implements ISearchList, EmptyView.CallBack {
    @Bind(R.id.recycler_fragment_imgplaza)
    RecyclerView mRecyclerFragmentImgplaza;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.empty_fragment_imgplaza)
    EmptyView mEmptyFragmentImgplaza;
    @Bind(R.id.rotate_header_list_view_frame)
    SwipeRefreshLayout mRotateHeaderListViewFrame;
    private View mContentView;
    private ImgPlazaPresenter mImgPlazaPresenter;
    private ImgPlazaAdapter mImgPlazaAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private LocalBroadcastManager localBroadcastManager;
    private FlushDateFromPMReceiver mFlushDateFromPMReceiver;
    public static ImgPlazaFragment newInstance() {

        Bundle args = new Bundle();

        ImgPlazaFragment fragment = new ImgPlazaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImgPlazaPresenter = new ImgPlazaPresenter(this);
        mImgPlazaAdapter = new ImgPlazaAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_fragment_imgplaza, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        mImgPlazaPresenter.list(true);
        return mContentView;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerFragmentImgplaza.setLayoutManager(mLinearLayoutManager);
        mRecyclerFragmentImgplaza.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mImgPlazaAdapter));
        mRecyclerFragmentImgplaza.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean getSupportsChangeAnimations() {
                return false;
            }
        });
        mRotateHeaderListViewFrame.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mRotateHeaderListViewFrame.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mRotateHeaderListViewFrame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mImgPlazaPresenter.list(true);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerFragmentImgplaza.setLayoutManager(mLinearLayoutManager);
        mRecyclerFragmentImgplaza.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mImgPlazaAdapter));
        mRecyclerFragmentImgplaza.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean getSupportsChangeAnimations() {
                return false;
            }
        });
        mEmptyFragmentImgplaza.attatchWithView(mRecyclerFragmentImgplaza, this);

        mTvToolbarTitle.setText(R.string.abn_yueai_maintab_home);

//        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                mImgPlazaPresenter.list(true);
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return super.checkCanDoRefresh(frame, mRecyclerFragmentImgplaza, header);
//            }
//        });

        //分页
        mRecyclerFragmentImgplaza.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);

                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerFragmentImgplaza);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerFragmentImgplaza, mRecyclerFragmentImgplaza.getAdapter().getItemCount() <= 0, LoadingFooter.State.Loading, null);
                //加载更多
                mImgPlazaPresenter.list(false);
            }
        });
        mRecyclerFragmentImgplaza.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i("onScrollStateChanged", "" + newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    Glide.with(getActivity()).pauseRequests();
                } else {
                    Glide.with(getActivity()).resumeRequests();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mFlushDateFromPMReceiver = new FlushDateFromPMReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalBroadCastConst.LocalBroadIntentAction_onHomeDateFlush);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(mFlushDateFromPMReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onListStart() {
        mEmptyFragmentImgplaza.startLoading();
    }

    @Override
    public void onListFailed(Throwable e) {
        mEmptyFragmentImgplaza.loadingFailed(e.getMessage());
//        mRotateHeaderListViewFrame.refreshComplete();
        mRotateHeaderListViewFrame.setRefreshing(false);
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerFragmentImgplaza, mRecyclerFragmentImgplaza.getAdapter().getItemCount() <= 1, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    private Random mRandom = new Random(System.currentTimeMillis());

    @Override
    public void onListSuccess(ArrayList<UserEntity> userEntities, Page page) {
        mEmptyFragmentImgplaza.loadingComplete();
//        mRotateHeaderListViewFrame.refreshComplete();
        mRotateHeaderListViewFrame.setRefreshing(false);
        if (userEntities != null && userEntities.size() > 3) {
            //对数据冲排序
            int rowCount = (int) Math.ceil(userEntities.size() / 3.0f);
            for (int position = 0; position < rowCount; position++) {
                //重新来过
                List<UserEntity> groupEntities = userEntities.subList(position * 3, Math.min((position + 1) * 3, userEntities.size() - 1));
                int bigIdx = -1;
                for (int i = 0; i < groupEntities.size(); i++) {
                    UserEntity userEntity = groupEntities.get(i);
                    if (userEntity.isVIP() || userEntity.memberLevel > 0) {
                        bigIdx = i;
                        break;
                    }
                }
                //随机调换顺序
                int randIdx = mRandom.nextInt(3);
                {
                    if (randIdx != bigIdx) {
//                bigIdx和randIdx互换位置
                        Collections.swap(userEntities, position * 3 + bigIdx, position * 3 + randIdx);
                    }
                }
            }
        }


        int oldCount = mImgPlazaAdapter.getItemCount();
        if (page.toPage <= 1) {
            mImgPlazaAdapter.getUserEntities().clear();
            mImgPlazaAdapter.getUserEntities().addAll(userEntities);
            mImgPlazaAdapter.notifyDataSetChanged();
        } else {
            mImgPlazaAdapter.getUserEntities().addAll(userEntities);
            mImgPlazaAdapter.notifyItemRangeInserted(oldCount, mImgPlazaAdapter.getItemCount() - oldCount);
        }
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerFragmentImgplaza, mRecyclerFragmentImgplaza.getAdapter().getItemCount() <= 1, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);
    }

    @Override
    public void onRetryClick(EmptyView.LoadingState state) {

    }

    @Override
    public boolean needHideContentView() {
        return mImgPlazaAdapter.getItemCount() <= 0;
    }
    class FlushDateFromPMReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("====================收到私信页发过来的刷新请求了");
            mImgPlazaAdapter.notifyDataSetChanged();
//            mImgPlazaPresenter.list(true);
        }
    }
/*
    @Override
    public void onSayHiSended(final String userId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImgPlazaAdapter.onSayHiSended(userId);
                showToast("打招呼成功");
            }
        });
    }

    @Override
    public void onSayHiFailed(final String userId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImgPlazaAdapter.onSayHiFailed(userId);
                showToast("打招呼失败");
            }
        });
    }

    Toast toast;

    @Override
    public void onNotLogin() {
        askForLogin();
    }*/
}
