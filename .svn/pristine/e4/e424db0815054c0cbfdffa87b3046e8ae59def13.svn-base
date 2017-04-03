package com.aibinong.tantan.ui.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.SayHiPresenter;
import com.aibinong.tantan.ui.adapter.dialog.SelectToSayHiAdapter;
import com.aibinong.yueaiapi.pojo.OrderResultEntitiy;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.TimeUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VipGiftSendDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.btn_dialog_i_know)
    TextView btnDialogIKnow;
    @Bind(R.id.tv_gift)
    TextView tvGift;
    @Bind(R.id.ll_gift)
    LinearLayout llGift;
    @Bind(R.id.tv_dialog_select_sayhi_title)
    TextView tvDialogSelectSayhiTitle;
    @Bind(R.id.recycler_dialog_select_sayhi_list)
    RecyclerView recyclerDialogSelectSayhiList;
    @Bind(R.id.tv_can_use_date)
    TextView tvCanUseDate;
    @Bind(R.id.ll_can_use_date)
    LinearLayout llCanUseDate;
    @Bind(R.id.ibtn_dialog_select_sayhi_close)
    ImageButton ibtnDialogSelectSayhiClose;
    private View mContentView;
    private String giftsendmsg;
    private SelectToSayHiAdapter mSelectItemAdapter;
    private ArrayList<UserEntity> mItemDataList;
    private OrderResultEntitiy orderResultEntitiy;
    private Context mcontext;

    public static VipGiftSendDialog newInstance(OrderResultEntitiy orderResultEntitiy, Context context) {
        Bundle args = new Bundle();
        args.putSerializable("orderResultEntitiy", orderResultEntitiy);
        VipGiftSendDialog fragment = new VipGiftSendDialog();
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
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_vip_gift_send, container, false);

        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerDialogSelectSayhiList.setLayoutManager(linearLayoutManager);
        mSelectItemAdapter = new SelectToSayHiAdapter();
        recyclerDialogSelectSayhiList.setAdapter(mSelectItemAdapter);
        btnDialogIKnow.setOnClickListener(this);
        ibtnDialogSelectSayhiClose.setOnClickListener(this);
        bindData();

    }

    @SuppressLint("NewApi")
    private void bindData() {
        orderResultEntitiy = (OrderResultEntitiy) getArguments().getSerializable("orderResultEntitiy");
        mItemDataList = (ArrayList<UserEntity>) orderResultEntitiy.list;
        mSelectItemAdapter.setData(mItemDataList);
        tvCanUseDate.setText(getString(R.string.abn_yueai_mine_vipvaliddate_fmt,
                TimeUtil.getFormatedDate(orderResultEntitiy.user.memberDate, "yyyy-MM-dd")));

        if (orderResultEntitiy.user.memberLevel.equals("1")) {   //黄金会员
            Glide.with(getActivity().getApplicationContext()).load(R.mipmap.abn_ya_vip__goden_icon_bg).into(ivIcon);
        } else if (orderResultEntitiy.user.memberLevel.equals("2")) {
            Glide.with(getActivity().getApplicationContext()).load(R.mipmap.abn_ya_vip_icon_white_goden_bg).into(ivIcon);
        } else if (orderResultEntitiy.user.memberLevel.equals("3")) {
            Glide.with(getActivity().getApplicationContext()).load(R.mipmap.abn_ya_vip_icon_dimon_bg).into(ivIcon);
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
        tvGift.setText(orderResultEntitiy.giftAmount);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 0.85), (int) (600 * DeviceUtils.getScreenDensity(getActivity())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogIKnow == view) {
            //打招呼
            if (mSelectItemAdapter.getSelectedUsers() != null && mSelectItemAdapter.getSelectedUsers().size() > 0) {
                SayHiPresenter.getInstance().sayHiBatch(mSelectItemAdapter.getSelectedUsers());
                dismiss();
                dismiss();
            } else if (ibtnDialogSelectSayhiClose == view) {
                dismiss();
            }
        }
    }
}