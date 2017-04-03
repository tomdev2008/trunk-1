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
import com.fatalsignal.util.DeviceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FirstRegisterGiftSendDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.ibtn_dialog_i_know_close)
    ImageButton ibtnDialogIKnowClose;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.btn_dialog_i_know)
    TextView btnDialogIKnow;
    @Bind(R.id.tv_gift)
    TextView tvGift;
    @Bind(R.id.ll_gift)
    LinearLayout llGift;
    private View mContentView;
    private String giftsendmsg;

    public static FirstRegisterGiftSendDialog newInstance(String giftsend) {
        Bundle args = new Bundle();
        args.putSerializable("giftsend", giftsend);
        FirstRegisterGiftSendDialog fragment = new FirstRegisterGiftSendDialog();
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
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_first_register_send, container, false);

        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {

        ibtnDialogIKnowClose.setOnClickListener(this);
        btnDialogIKnow.setOnClickListener(this);

        bindData();

    }

    private void bindData() {
        giftsendmsg = (String) getArguments().getSerializable("giftsend");
        if (giftsendmsg != null) {
            tvGift.setText("  " + giftsendmsg + "  ");
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyCode==KeyEvent.KEYCODE_HOME) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        getDialog().setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 0.85), (int) (360 * DeviceUtils.getScreenDensity(getActivity())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (ibtnDialogIKnowClose == view) {
            dismiss();
        } else if (btnDialogIKnow == view) {
            dismiss();
        }
    }
}
