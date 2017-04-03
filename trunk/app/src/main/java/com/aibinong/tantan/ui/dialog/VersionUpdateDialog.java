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
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.util.update.DownLoadApk;
import com.aibinong.yueaiapi.pojo.VersionEntity;
import com.fatalsignal.util.DeviceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VersionUpdateDialog extends DialogFragment implements View.OnClickListener {


    @Bind(R.id.tv_dialog_version_update_title)
    TextView tvDialogVersionUpdateTitle;
    @Bind(R.id.tv_dialog_version_update_content)
    TextView tvDialogVersionUpdateContent;
    @Bind(R.id.btn_dialog_version_update_hold)
    TextView btnDialogVersionUpdateHold;
    @Bind(R.id.btn_dialog_select_version_update_now)
    TextView btnDialogSelectVersionUpdateNow;
    private View mContentView;
    private VersionEntity mversionEntity;

    public static VersionUpdateDialog newInstance(VersionEntity versionEntity) {
        Bundle args = new Bundle();
        args.putSerializable("versionEntity", versionEntity);
        VersionUpdateDialog fragment = new VersionUpdateDialog();
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
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_version_update, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        btnDialogVersionUpdateHold.setOnClickListener(this);
        btnDialogSelectVersionUpdateNow.setOnClickListener(this);
        bindData();

    }

    private void bindData() {
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
        mversionEntity = (VersionEntity) getArguments().getSerializable("versionEntity");
        if (mversionEntity.status.equals("0")) {  //非强制升级
            btnDialogVersionUpdateHold.setVisibility(View.VISIBLE);
        } else {  //强制升级
            btnDialogVersionUpdateHold.setVisibility(View.GONE);
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 0.85), (int) (240 * DeviceUtils.getScreenDensity(getActivity())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogVersionUpdateHold == view) {
            dismiss();
        } else if (btnDialogSelectVersionUpdateNow == view) {
            //现在升级  应该去下载最新版本 并且安装
//            forceUpdate(MainActivity.this,appName,downUrl,updateinfo);
            DownLoadApk.download(view.getContext(),"http://ont9j0dy7.bkt.clouddn.com/app-YA001-debug_201704021429.apk","版本升级","约爱");
        }
    }

}
