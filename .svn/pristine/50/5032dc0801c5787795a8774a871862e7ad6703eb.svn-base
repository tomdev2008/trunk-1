package com.aibinong.tantan.ui.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommonCardDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.ibtn_dialog_select_sayhi_close)
    ImageButton mIbtnDialogSelectSayhiClose;
    @Bind(R.id.tv_dialog_commoncard_title)
    TextView mTvDialogCommoncardTitle;
    @Bind(R.id.tv_dialog_commoncard_message)
    TextView mTvDialogCommoncardMessage;
    @Bind(R.id.btn_dialog_commoncard_left)
    Button mBtnDialogCommoncardLeft;
    @Bind(R.id.btn_dialog_commoncard_right)
    Button mBtnDialogCommoncardRight;
    @Bind(R.id.ll_dialog_commoncard_button)
    LinearLayout mLlDialogCommoncardButton;
    private View mContentView;
    private String mTitle, mMessage;
    private DialogInterface.OnClickListener mOnClickListener;
    private String mPositiveStr, mNegativeStr;

    public static CommonCardDialog newInstance(String title, String msg, String positiveStr, String negativeStr) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", msg);
        args.putString("positiveStr", positiveStr);
        args.putString("negativeStr", negativeStr);
        CommonCardDialog fragment = new CommonCardDialog();
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
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_commoncard, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        mIbtnDialogSelectSayhiClose.setOnClickListener(this);
        mBtnDialogCommoncardLeft.setOnClickListener(this);
        mBtnDialogCommoncardRight.setOnClickListener(this);
        bindData();
    }

    private void bindData() {
        mTitle = getArguments().getString("title");
        mMessage = getArguments().getString("message");
        mPositiveStr = getArguments().getString("positiveStr");
        mNegativeStr = getArguments().getString("negativeStr");

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (!StringUtils.isEmpty(mTitle)) {
            mTvDialogCommoncardTitle.setText(mTitle);
            mTvDialogCommoncardTitle.setVisibility(View.VISIBLE);
        } else {
            mTvDialogCommoncardTitle.setVisibility(View.GONE);
        }
        mTvDialogCommoncardMessage.setText(mMessage);

        mBtnDialogCommoncardRight.setVisibility(View.GONE);
        mBtnDialogCommoncardLeft.setVisibility(View.GONE);
        if (!StringUtils.isEmpty(mPositiveStr)) {
            mBtnDialogCommoncardRight.setVisibility(View.VISIBLE);
            mBtnDialogCommoncardRight.setText(mPositiveStr);
        }
        if (!StringUtils.isEmpty(mNegativeStr)) {
            mBtnDialogCommoncardLeft.setVisibility(View.VISIBLE);
            mBtnDialogCommoncardLeft.setText(mNegativeStr);
        }
    }

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenDensity(getActivity()) * 280), (int) (238 * DeviceUtils.getScreenDensity(getActivity())));
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
        } else if (mBtnDialogCommoncardRight == view) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(getDialog(), Dialog.BUTTON_POSITIVE);
            }
        } else if (mBtnDialogCommoncardLeft == view) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(getDialog(), Dialog.BUTTON_NEGATIVE);
            }
        }

    }

}
