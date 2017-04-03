package com.aibinong.tantan.ui.dialog;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.dialog.SelectItemIosAdapter;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectItemIOSDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.btn_dialog_select_item_ios_cancel)
    Button mBtnDialogSelectItemIosCancel;
    @Bind(R.id.listview_dialog_selectitem_ios)
    ListView mListviewDialogSelectitemIos;
    @Bind(R.id.tv_dialog_select_item_ios_title)
    TextView mTvDialogSelectItemIosTitle;
    @Bind(R.id.view_dialog_select_item_divider)
    View mViewDialogSelectItemDivider;
    private SelectItemIosAdapter mSelectItemAdapter;
    private List<?> mItemDataList;


    public interface SelectItemCallback {
        void onSelectItem(int position);

        void onSelectNone();
    }

    private View mContentView;
    private SelectItemCallback mSelectItemCallback;

    public static SelectItemIOSDialog newInstance(ArrayList<? extends Serializable> itemDataList) {
        return newInstance(itemDataList, null);
    }

    public static SelectItemIOSDialog newInstance(ArrayList<? extends Serializable> itemDataList, String title) {

        Bundle args = new Bundle();
        args.putSerializable("itemDataList", itemDataList);
        args.putString("title", title);

        SelectItemIOSDialog fragment = new SelectItemIOSDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void show(SelectItemCallback callback, FragmentManager manager, String tag) {
        mSelectItemCallback = callback;
        super.show(manager, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectItemAdapter = new SelectItemIosAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_select_item_ios, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        mListviewDialogSelectitemIos.setAdapter(mSelectItemAdapter);
        mListviewDialogSelectitemIos.setOnItemClickListener(this);
        mBtnDialogSelectItemIosCancel.setOnClickListener(this);
        bindData();

    }

    private void bindData() {
        mItemDataList = (List<?>) getArguments().getSerializable("itemDataList");
        String title = getArguments().getString("title");
        if (StringUtils.isEmpty(title)) {
            mTvDialogSelectItemIosTitle.setVisibility(View.GONE);
            mViewDialogSelectItemDivider.setVisibility(View.GONE);
        } else {
            mTvDialogSelectItemIosTitle.setVisibility(View.VISIBLE);
            mViewDialogSelectItemDivider.setVisibility(View.VISIBLE);
            mTvDialogSelectItemIosTitle.setText(title);
        }
        mSelectItemAdapter.setItemDataList(mItemDataList);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DeviceUtils.getScreenWidth(getActivity()) * 1.0f), ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnDialogSelectItemIosCancel) {
            if (mSelectItemCallback != null) {
                mSelectItemCallback.onSelectNone();
            }
        }
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectItemCallback != null) {
            mSelectItemCallback.onSelectItem(position);
            dismiss();
        }
    }

}
