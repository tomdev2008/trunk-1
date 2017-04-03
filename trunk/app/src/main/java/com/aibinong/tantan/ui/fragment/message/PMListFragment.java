package com.aibinong.tantan.ui.fragment.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.broadcast.LocalBroadCastConst;
import com.aibinong.tantan.presenter.SayHiPresenter;
import com.aibinong.tantan.presenter.message.ChatMsgListPresenter;
import com.aibinong.tantan.presenter.message.PMListPresenter;
import com.aibinong.tantan.ui.activity.MainActivity;
import com.aibinong.tantan.ui.adapter.message.PMListAdapter;
import com.aibinong.tantan.ui.fragment.FragmentBase;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.aibinong.tantan.util.RecyclerViewStateUtils;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PMListFragment extends FragmentBase implements PMListPresenter.IPMListPresenter, PMListAdapter.onSwipeListener,ChatMsgListPresenter.IChatMsgList {

    private static final String TAG = "PMListFragment";
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
    private PMlocalBroadReceiver mlocalBroadReceiver;
    LocalBroadcastManager broadcastManager;
    private ChatMsgListPresenter mChatMsgListPresenter;
    ItemTouchHelper itemTouchHelper;
    public static PMListFragment newInstance() {
        PMListFragment fragment = new PMListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPmListAdapter = new PMListAdapter();
        mPmListAdapter.setOnDelListener(this);
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
        mRecyclerMsgPmlist.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
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
        if (mPmListAdapter.getItemCount() <= 1 + mPmListAdapter.getHelperCount()) {
            mEmptyviewMsgPmlist.startLoading();
        }
        mPmListPresenter.loadPMList(refresh);
    }

    @Override
    public void onListFailed(Throwable e) {
        if (mPmListAdapter.getItemCount() <= 1 + mPmListAdapter.getHelperCount()) {
            mEmptyviewMsgPmlist.loadingFailed(e.getMessage());
        } else {
            mEmptyviewMsgPmlist.loadingComplete();
            mPmListAdapter.getUsersList().remove(0);
            mPmListAdapter.notifyDataSetChanged();
            mEmptyviewMsgPmlist.emptyData();
        }
        mRotateHeaderListViewFrame.refreshComplete();
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPmlist, mPmListAdapter.getItemCount() <= 2, LoadingFooter.State.NetWorkError, null, e.getMessage());
    }

    @Override
    public void onListSuccess(ArrayList<UserEntity> userEntities, Page page) {
        if (page.toPage <= 1) {
            mPmListAdapter.getUsersList().clear();
            mPmListAdapter.notifyDataSetChanged();
        }
        if (userEntities.size() > 0) {
            for (int i = 0; i < userEntities.size(); i++) {
                if (userEntities.get(i).nickname.equals("客服小助手")){
                   userEntities.remove(i);
                }
            }
            int oldSize = mPmListAdapter.getItemCount();
            mPmListAdapter.getUsersList().addAll(userEntities);
            mPmListAdapter.reSort();
//            mPmListAdapter.notifyItemRangeInserted(oldSize, userEntities.size());
        }
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerMsgPmlist, mPmListAdapter.getItemCount() <= 2, page.noMore() ? LoadingFooter.State.TheEnd : LoadingFooter.State.Normal, null);

        if (mPmListAdapter.getItemCount() <= 1 + mPmListAdapter.getHelperCount()) {
            mEmptyviewMsgPmlist.emptyData();
//            mRecyclerMsgPmlist.setVisibility(View.GONE);

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


        mlocalBroadReceiver = new PMlocalBroadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalBroadCastConst.LocalBroadIntentAction_PersonMessageActivityChange);
        intentFilter.addAction(LocalBroadCastConst.LocalBroadIntentAction_onHomeDateFlush);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(mlocalBroadReceiver, intentFilter);


    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().unregisterReceiver(mMsgReceivedReceiver);

        if (mlocalBroadReceiver != null) {
            broadcastManager.unregisterReceiver(mlocalBroadReceiver);
            mlocalBroadReceiver = null;
        }
    }

    @Override
    public void onDel(int pos) {
        if (pos > 1 && pos < mPmListAdapter.getItemCount()) {
            UserEntity userEntity = mPmListAdapter.mUsersList.get(pos - 2);
            EMConversation em = EMClient.getInstance().chatManager().getConversation(userEntity.id);
            if (em != null) {
                em.markAllMessagesAsRead();
            }
            GlobalLocalBroadCastManager.getInstance().registerMessageRead();
            mPmListAdapter.mUsersList.remove(pos - 2);
//            mPmListAdapter.notifyItemRemoved(pos);//推荐用这个
            //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
            //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            mPmListAdapter.notifyDataSetChanged();
            mChatMsgListPresenter = new ChatMsgListPresenter(this, userEntity.id, userEntity, UserUtil.getSavedUserInfo(), getActivity());
            mChatMsgListPresenter.onCreate();
            mChatMsgListPresenter.clearMessageLog();


            SayHiPresenter.getInstance().onSayNoHiSended(userEntity);
            //同时通知首页刷新数据  同时通知关注页刷新数据
            GlobalLocalBroadCastManager.getInstance().onHomeDateFlush(userEntity);

        }
    }


    @Override
    public void onOppositeRevokeMsg(String msgId) {

    }

    @Override
    public void onRevocationMsgSuccess(EMMessage msg, EMMessage notifyMsg) {

    }

    @Override
    public void onDeleteMessage(String msgId) {

    }

    @Override
    public void onLoadAllMsg(List<EMMessage> msgs, boolean nomore) {

    }

    @Override
    public void onLoadMoreMsg(List<EMMessage> msgs, boolean nomore) {

    }

    @Override
    public void onEMmsgSend(EMMessage msg) {

    }

    @Override
    public void onEmmsgSendErr(EMMessage msg, int code, String info) {

    }

    @Override
    public void onEmmsgSended(EMMessage msg) {

    }

    @Override
    public void onMessageReceived(EMMessage message) {

    }

    @Override
    public void onPopupTruth(EMMessage message) {

    }

    @Override
    public void onListGiftFailed(ResponseResult e) {

    }

    @Override
    public void onListGiftSuccess(ArrayList<GiftEntity> gifts) {

    }

    @Override
    public void onGiveGiftFailed(ResponseResult e) {

    }

    @Override
    public void onGiveGiftSuccess() {

    }

    @Override
    public boolean onSwitchHi(boolean visible) {
        return false;
    }

    @Override
    public void sendChatRecordSuccess(String s) {

    }

    @Override
    public void sendChatRecordFailer(Throwable t) {

    }

    @Override
    public void deleteChatRecord() {

    }

    class PMlocalBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " + "==========pm");
            mPmListAdapter.notifyDataSetChanged();
            postDelayLoad(new Runnable() {
                @Override
                public void run() {
                    loadPMList(true);
                }
            });
        }
    }
}
