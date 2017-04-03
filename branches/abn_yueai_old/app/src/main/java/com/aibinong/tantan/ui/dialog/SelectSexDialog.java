package com.aibinong.tantan.ui.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.fatalsignal.util.DeviceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectSexDialog extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public interface SelectSexCallback {
        void onSelectSex(int sex);
    }

    @Bind(R.id.rbtn_dialog_select_sex_male)
    AppCompatRadioButton mRbtnDialogSelectSexMale;
    @Bind(R.id.ll_dialog_select_sex_male)
    LinearLayout mLlDialogSelectSexMale;
    @Bind(R.id.rbtn_dialog_select_sex_female)
    AppCompatRadioButton mRbtnDialogSelectSexFemale;
    @Bind(R.id.ll_dialog_select_sex_female)
    LinearLayout mLlDialogSelectSexFemale;
    private View mContentView;
    private SelectSexCallback mSelectSexCallback;
    private int mCurrentSex;

    public static SelectSexDialog newInstance() {

        Bundle args = new Bundle();

        SelectSexDialog fragment = new SelectSexDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void show(int sex, SelectSexCallback callback, FragmentManager manager, String tag) {
        super.show(manager, tag);
        mSelectSexCallback = callback;
        mCurrentSex = sex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_select_sex, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {

        if (mCurrentSex == LoginService.registerSexMale) {
            mRbtnDialogSelectSexMale.setChecked(true);
        } else {
            mRbtnDialogSelectSexFemale.setChecked(true);
        }

        mLlDialogSelectSexFemale.setOnClickListener(this);
        mLlDialogSelectSexMale.setOnClickListener(this);
        mRbtnDialogSelectSexFemale.setOnCheckedChangeListener(this);
        mRbtnDialogSelectSexMale.setOnCheckedChangeListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLlDialogSelectSexFemale) {
            mRbtnDialogSelectSexFemale.performClick();
        } else if (view == mLlDialogSelectSexMale) {
            mRbtnDialogSelectSexMale.performClick();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (compoundButton == mRbtnDialogSelectSexMale) {
                //男
                mRbtnDialogSelectSexFemale.setChecked(false);
                if (mSelectSexCallback != null) {
                    mSelectSexCallback.onSelectSex(LoginService.registerSexMale);
                }
            } else if (compoundButton == mRbtnDialogSelectSexFemale) {
                //女
                mRbtnDialogSelectSexMale.setChecked(false);
                if (mSelectSexCallback != null) {
                    mSelectSexCallback.onSelectSex(LoginService.registerSexFemale);
                }
            }
        }
        dismiss();
    }
}
