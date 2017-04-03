package com.aibinong.tantan.ui.activity.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/6.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.message.ChatActivityPresenter;
import com.aibinong.tantan.presenter.message.PairListPresenter;
import com.aibinong.tantan.ui.activity.ActivityBase;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.ui.dialog.SelectItemIOSDialog;
import com.aibinong.tantan.ui.fragment.message.ChatMsgListFragment;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.tantan.util.message.EMChatHelper;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.fatalsignal.util.StringUtils;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ChatActivity extends ActivityBase implements PairListPresenter.IPairList, ChatActivityPresenter.IChatActivity {
    @Bind(R.id.fl_chat_msglist_fragment)
    FrameLayout mFlChatMsglistFragment;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.abn_yueai_activity_chat)
    LinearLayout mAbnYueaiActivityChat;
    @Bind(R.id.ibtn_toolbar_dots)
    ImageButton mIbtnToolbarDots;
    @Bind(R.id.emptyview_chat)
    EmptyView mEmptyviewChat;
    private ChatMsgListFragment mChatMsgListFragment;
    private String mCurrentUUID;
    private UserEntity mCurrentUser;
    private PairListPresenter mPairListPresenter;
    private ArrayList<QuestionEntity.OptionsEntity> reportResons;
    private ChatActivityPresenter mMineFragPresenter;

    public static Intent launchIntent(final Context context, String uuid, UserEntity userEntity, boolean autoStart) {
        final Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        bundle.putString(IntentExtraKey.INTENT_EXTRA_KEY_UUUID, uuid);
        intent.putExtras(bundle);
        if (autoStart) {
            //先登录到聊天服务器
            if (!EMClient.getInstance().isLoggedInBefore()) {
                DialogUtil.showIndeternimateDialog((Activity) context, null);
                EMChatHelper.getInstance().loginChat().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        //登录成功
                        DialogUtil.hideDialog((Activity) context);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.showDialog((Activity) context, e.getMessage(), true);
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
            } else {
                context.startActivity(intent);
            }
        }

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_chat);
        ButterKnife.bind(this);
        mEmptyviewChat.loadingComplete();
        mCurrentUUID = getIntent().getExtras().getString(IntentExtraKey.INTENT_EXTRA_KEY_UUUID);
        mCurrentUser = (UserEntity) getIntent().getExtras().getSerializable(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);

        if (StringUtils.isEmpty(mCurrentUUID) && mCurrentUser != null) {
            mCurrentUUID = mCurrentUser.id;
        }
        if (StringUtils.isEmpty(mCurrentUUID)) {
            finish();
            return;
        }
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mPairListPresenter = new PairListPresenter(this);
        mMineFragPresenter = new ChatActivityPresenter(this);

        if (mCurrentUser == null) {
            mEmptyviewChat.startLoading();
            mMineFragPresenter.getUserInfo(mCurrentUUID);
        } else {
            mChatMsgListFragment = ChatMsgListFragment.newInstance(mCurrentUUID, mCurrentUser);
            getFragmentManager().beginTransaction().replace(R.id.fl_chat_msglist_fragment, mChatMsgListFragment).commit();
            mIbtnToolbarDots.setOnClickListener(this);
            mMineFragPresenter.getUserInfo(mCurrentUUID);
            mTvToolbarTitle.setText(mCurrentUser.nickname);
        }
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        if (view == mIbtnToolbarDots) {


            ArrayList<String> itemDatas = new ArrayList<>(3);
            itemDatas.add("查看个人资料");
            itemDatas.add("清除聊天记录");
            if (mCurrentUser.helper != 1) {
                itemDatas.add("解除匹配");

                if (ConfigUtil.getInstance().getConfig() != null && ConfigUtil.getInstance().getConfig().reportReasons != null) {
                    reportResons = ConfigUtil.getInstance().getConfig().reportReasons;
                    if (reportResons.size() > 0) {
                        itemDatas.add("举报");
                    }
                }
            }
            SelectItemIOSDialog selectItemIOSDialog = SelectItemIOSDialog.newInstance(itemDatas);
            selectItemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
                @Override
                public void onSelectItem(int position) {
                    if (position == 0) {
                        //查看个人资料
                        UserDetailActivity.launchIntent(ChatActivity.this, mCurrentUser);
                    } else if (position == 1) {
                        //清除聊天记录
                        mChatMsgListFragment.clearMessageLog();
                    } else if (position == 2) {
                        //接触匹配
                        DialogUtil.showIndeternimateDialog(ChatActivity.this, null);
                        mPairListPresenter.cancelPair(mCurrentUUID);
                    } else if (position == 3) {
                        showReportDialog();
                    }
                }

                @Override
                public void onSelectNone() {

                }
            }, getFragmentManager(), null);
        } else {
            super.onClick(view);
        }
    }

    private void showReportDialog() {
        if (reportResons == null || reportResons.size() <= 0) {
            return;
        }
        ArrayList<String> resons = new ArrayList<>(reportResons.size());
        for (QuestionEntity.OptionsEntity optionsEntity : reportResons) {
            resons.add(optionsEntity.content);
        }
        SelectItemIOSDialog itemIOSDialog = SelectItemIOSDialog.newInstance(resons);
        itemIOSDialog.show(new SelectItemIOSDialog.SelectItemCallback() {
            @Override
            public void onSelectItem(int position) {
                if (position < reportResons.size()) {
                    QuestionEntity.OptionsEntity optionsEntity = reportResons.get(position);
                    DialogUtil.showIndeternimateDialog(ChatActivity.this, null);
                    mPairListPresenter.report(mCurrentUUID, optionsEntity);
                }
            }

            @Override
            public void onSelectNone() {

            }
        }, getFragmentManager(), null);
    }

    @Override
    public void onReportSuccess() {
        DialogUtil.hideDialog(this);
        showErrToast(new ResponseResult(-1, "举报成功"));
    }

    @Override
    public void onReportFailed(ResponseResult e) {
        DialogUtil.hideDialog(this);
        showErrToast(e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mChatMsgListFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mChatMsgListFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setComponent(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
//        startActivity(intent);
    }

    @Override
    public void onMachingListStart() {

    }

    @Override
    public void onMachingListFailed(Throwable e) {

    }

    @Override
    public void onMachingListSuccess(ArrayList<UserEntity> userEntities, Page page) {

    }

    @Override
    public void onCancelPairStart() {

    }

    @Override
    public void onCancelPairSuccess(String uuid) {
        finish();
    }

    @Override
    public void onCancelPairFailed(Throwable e, String uuid) {
        showErrDialog(e);
    }


    @Override
    public void onGetUserSuc(UserEntity userEntity) {
        mEmptyviewChat.loadingComplete();
        if (mCurrentUser == null) {
            mCurrentUser = userEntity;
            mChatMsgListFragment = ChatMsgListFragment.newInstance(mCurrentUUID, mCurrentUser);
            getFragmentManager().beginTransaction().replace(R.id.fl_chat_msglist_fragment, mChatMsgListFragment).commit();
            mIbtnToolbarDots.setOnClickListener(this);
        } else {
            mCurrentUser = userEntity;
            mChatMsgListFragment.setUser(mCurrentUser);
        }
        mTvToolbarTitle.setText(mCurrentUser.nickname);

    }

    @Override
    public void onGetUserFailed(ResponseResult e) {
        if (mCurrentUser == null) {
            DialogUtil.showDialog(this, e.getMessage(), true).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
    }
}
