package com.aibinong.tantan.ui.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.presenter.SayHiPresenter;
import com.aibinong.tantan.ui.adapter.dialog.SelectToSayHiAdapter;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.DeviceUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectToSayHiDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.tv_dialog_select_sayhi_title)
    TextView mTvDialogSelectSayhiTitle;
    @Bind(R.id.recycler_dialog_select_sayhi_list)
    RecyclerView mRecyclerDialogSelectSayhiList;
    @Bind(R.id.btn_dialog_select_sayhi_sayhi)
    Button mBtnDialogSelectSayhiSayhi;
    @Bind(R.id.ibtn_dialog_select_sayhi_close)
    ImageButton mIbtnDialogSelectSayhiClose;
    private View mContentView;
    private String title;
    private ArrayList<UserEntity> mItemDataList;
    private SelectToSayHiAdapter mSelectItemAdapter;

    public static SelectToSayHiDialog newInstance(ArrayList<UserEntity> itemDataList, String title) {

        Bundle args = new Bundle();
        args.putSerializable("itemDataList", itemDataList);
        args.putString("title", title);
        SelectToSayHiDialog fragment = new SelectToSayHiDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_select_to_sayhi, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerDialogSelectSayhiList.setLayoutManager(linearLayoutManager);
        mSelectItemAdapter = new SelectToSayHiAdapter();

        mRecyclerDialogSelectSayhiList.setAdapter(mSelectItemAdapter);
        mIbtnDialogSelectSayhiClose.setOnClickListener(this);
        mBtnDialogSelectSayhiSayhi.setOnClickListener(this);
        bindData();

    }

    private void bindData() {
        UserEntity savedUserInfo = UserUtil.getSavedUserInfo();
        if (savedUserInfo != null && savedUserInfo.sex == 0) {  //女性用户 可以关闭推荐
            mIbtnDialogSelectSayhiClose.setVisibility(View.VISIBLE);

        } else {
            mIbtnDialogSelectSayhiClose.setVisibility(View.GONE);
        }
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        getDialog().setCancelable(false);
        mItemDataList = (ArrayList<UserEntity>) getArguments().getSerializable("itemDataList");
        title = getArguments().getString("title",null);
        if (title !=null){
            mTvDialogSelectSayhiTitle.setText(title);
            mIbtnDialogSelectSayhiClose.setVisibility(View.VISIBLE);
        }
        mSelectItemAdapter.setData(mItemDataList);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 0.85), (int) (420 * DeviceUtils.getScreenDensity(getActivity())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (mIbtnDialogSelectSayhiClose == view) {
            dismiss();
            GlobalLocalBroadCastManager.getInstance().onCanGiftShow();
        } else if (mBtnDialogSelectSayhiSayhi == view) {
            //打招呼
            if (mSelectItemAdapter.getSelectedUsers() != null && mSelectItemAdapter.getSelectedUsers().size() > 0) {
                SayHiPresenter.getInstance().sayHiBatch(mSelectItemAdapter.getSelectedUsers());
                dismiss();
                GlobalLocalBroadCastManager.getInstance().onCanGiftShow();
            } else {
                Toast.makeText(getActivity(), "请至少选择一个吧", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
