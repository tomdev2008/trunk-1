package com.aibinong.tantan.ui.fragment.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.presenter.message.PMListPresenter;
import com.aibinong.tantan.ui.adapter.message.PMListAdapter;
import com.aibinong.tantan.ui.fragment.FragmentBase;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
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

public class PMListFragment extends FragmentBase implements PMListPresenter.IPMListPresenter {

    @Bind(R.id.emptyview_msg_pmlist)
    EmptyView mEmptyviewMsgPmlist;
    @Bind(R.id.recycler_msg_pmlist)
    RecyclerView mRecyclerMsgPmlist;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mRotateHeaderListViewFrame;
    private View mContentView;
    private PMListAdapter mPmListAdapter;
    private PMListPresenter mPmListPresenter;
    private BroadcastReceiver mMsgReceivedReceiver;

    public static PMListFragment newInstance() {
        PMListFragment fragment = new PMListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPmListAdapter = new PMListAdapter();
        mPmListPresenter = new PMListPresenter();
        mPmListPresenter.setIpmListPresenter(this);
        mMsgReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPmListAdapter.notifyDataSetChanged();
                mPmListPresenter.loadPMList(true);

            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_frag_msg_pmlist, container, false);
        ButterKnife.bind(this, mContentView);
        setupView(savedInstanceState);
        return mContentView;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerMsgPmlist.setLayoutManager(linearLayoutManager);


        mRecyclerMsgPmlist.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mPmListAdapter));
        mRecyclerMsgPmlist.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerMsgPmlist.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean getSupportsChangeAnimations() {
                return false;
            }

            @Override
            public long getAddDuration() {
                return 1080;
            }

        });
        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
                                                     @Override
                                                     public void onRefreshBegin(PtrFrameLayout frame) {
                                                         loadPMList(true);
                                                     }

                                                     @Override
                                                     public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                                                         return super.checkCanDoRefresh(frame, mRecyclerMsgPmlist, header);
                                                     }
                                                 }

        );
        mRecyclerMsgPmlist.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);

                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerMsgPmlist);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (state == LoadingFooter.State.TheEnd) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPmlist, mRecyclerMsgPmlist.getAdapter().getItemCount() <= 0, LoadingFooter.State.Loading, null);
                loadPMList(false);

            }
        });
        postDelayLoad(new Runnable() {
            @Override
            public void run() {
                loadPMList(true);
            }
        });
    }

    private void loadPMList(boolean refresh) {
        if (mPmListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPmlist.startLoading();
        }
        mPmListPresenter.loadPMList(refresh);
    }

    @Override
    public void onListFailed(Throwable e) {
        if (mPmListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPmlist.loadingFailed(e.getMessage());
        } else {
            mEmptyviewMsgPmlist.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPmlist, mPmListAdapter.getItemCount() <= 0, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    @Override
    public void onListSuccess(ArrayList<UserEntity> userEntities, Page page) {

        if (page.toPage <= 1) {
            mPmListAdapter.getUsersList().clear();
            mPmListAdapter.notifyDataSetChanged();
        }
        if (userEntities.size() > 0) {
            int oldSize = mPmListAdapter.getItemCount();
            mPmListAdapter.getUsersList().addAll(userEntities);
            mPmListAdapter.reSort();
//            mPmListAdapter.notifyItemRangeInserted(oldSize, userEntities.size());
        }
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPmlist, mPmListAdapter.getItemCount() <= 0, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);

        if (mPmListAdapter.getItemCount() <= 0) {
            mEmptyviewMsgPmlist.emptyData();
        } else {
            mEmptyviewMsgPmlist.loadingComplete();
        }
        mRotateHeaderListViewFrame.refreshComplete();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPmListPresenter.loadPMList(true);
        GlobalLocalBroadCastManager.getInstance().registerMessageRecieved(mMsgReceivedReceiver);

    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mMsgReceivedReceiver);
    }
}
