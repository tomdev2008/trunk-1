package com.aibinong.tantan.ui.fragment.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.message.PairListPresenter;
import com.aibinong.tantan.ui.adapter.message.PairListAdapter;
import com.aibinong.tantan.ui.fragment.FragmentBase;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PairListFragment extends FragmentBase implements PairListPresenter.IPairList {

    @Bind(R.id.emptyview_msg_pairlist)
    EmptyView mEmptyviewMsgPairlist;
    @Bind(R.id.recycler_msg_pairlist)
    RecyclerView mRecyclerMsgPairlist;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mRotateHeaderListViewFrame;
    private View mContentView;
    private PairListPresenter mPairListPresenter;
    private PairListAdapter mPairListAdapter;

    public static PairListFragment newInstance() {
        PairListFragment fragment = new PairListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPairListPresenter = new PairListPresenter(this);
        mPairListAdapter = new PairListAdapter(mPairListPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_frag_msg_pairlist, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        return mContentView;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerMsgPairlist.setLayoutManager(linearLayoutManager);


        mRecyclerMsgPairlist.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mPairListAdapter));
        mRecyclerMsgPairlist.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
                                                     @Override
                                                     public void onRefreshBegin(PtrFrameLayout frame) {
                                                         mPairListPresenter.machingList(true);
                                                     }

                                                     @Override
                                                     public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                                                         return super.checkCanDoRefresh(frame, mRecyclerMsgPairlist, header);
                                                     }
                                                 }

        );
        mRecyclerMsgPairlist.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);

                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerMsgPairlist);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPairlist, mRecyclerMsgPairlist.getAdapter().getItemCount() <= 0, LoadingFooter.State.Loading, null);
                //加载更多
                mPairListPresenter.machingList(false);

            }
        });
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                mPairListPresenter.machingList(true);
            }
        });
    }

    @Override
    public void onMachingListStart() {
        if (mPairListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPairlist.startLoading();
        }
    }

    @Override
    public void onMachingListFailed(Throwable e) {
        if (mPairListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPairlist.loadingFailed(e.getMessage());
        } else {
            mEmptyviewMsgPairlist.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPairlist, mPairListAdapter.getItemCount() <= 0, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    @Override
    public void onMachingListSuccess(ArrayList<UserEntity> userEntities, Page page) {

        if (page.toPage <= 1) {
            int oldSize = mPairListAdapter.getItemCount();
            mPairListAdapter.getUsersList().clear();
            mPairListAdapter.notifyItemRangeRemoved(0, oldSize);
        }
        if (userEntities.size() > 0) {
            int oldSize = mPairListAdapter.getItemCount();
            mPairListAdapter.getUsersList().addAll(userEntities);
            mPairListAdapter.notifyDataSetChanged();
            mPairListAdapter.notifyItemRangeInserted(oldSize, userEntities.size());
        }
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPairlist, mPairListAdapter.getItemCount() <= 0, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);

        if (mPairListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPairlist.emptyData();
        } else {
            mEmptyviewMsgPairlist.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();
    }

    @Override
    public void onCancelPairStart() {
        DialogUtil.showIndeternimateDialog(getActivity(), null);
    }

    @Override
    public void onCancelPairSuccess(String uuid) {
        DialogUtil.hideDialog(getActivity());
        mPairListAdapter.removeItem(uuid);
    }

    @Override
    public void onCancelPairFailed(Throwable e, String uuid) {
        DialogUtil.showDialog(getActivity(), e.getMessage(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onReportSuccess() {

    }

    @Override
    public void onReportFailed(ResponseResult e) {

    }
}
