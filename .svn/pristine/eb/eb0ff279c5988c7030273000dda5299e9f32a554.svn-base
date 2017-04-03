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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.dialog.SelectItemAdapter;
import com.aibinong.tantan.ui.fragment.message.DividerItemDecoration;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectItemDialog extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.tv_dialog_select_item_title)
    TextView mTvDialogSelectItemTitle;
    @Bind(R.id.recycler_dialog_select_item_list)
    RecyclerView mRecyclerDialogSelectItemList;
    @Bind(R.id.btn_dialog_select_item_left)
    Button mBtnDialogSelectItemLeft;
    @Bind(R.id.btn_dialog_select_item_right)
    Button mBtnDialogSelectItemRight;
    private String mTitle;
    private SelectItemAdapter mSelectItemAdapter;
    private boolean mCanSelectNone;
    private List<?> mItemDataList;
    private SelectItemCallback mInnerCallback;


    public interface SelectItemCallback {
        void onSelectItem(int position);

        void onSelectNone();
    }

    private View mContentView;
    private SelectItemCallback mSelectItemCallback;

    public static SelectItemDialog newInstance(String title, boolean canSelectNone, ArrayList<? extends Serializable> itemDataList) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("canSelectNone", canSelectNone);
        args.putSerializable("itemDataList", itemDataList);

        SelectItemDialog fragment = new SelectItemDialog();
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
        mInnerCallback = new SelectItemCallback() {
            @Override
            public void onSelectItem(int position) {
                mSelectItemCallback.onSelectItem(position);
                dismiss();
            }

            @Override
            public void onSelectNone() {
                mSelectItemCallback.onSelectNone();
                dismiss();
            }
        };
        mSelectItemAdapter = new SelectItemAdapter();
        mSelectItemAdapter.setSelectItemCallback(mInnerCallback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_select_item, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerDialogSelectItemList.setLayoutManager(linearLayoutManager);

        mRecyclerDialogSelectItemList.setAdapter(mSelectItemAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerDialogSelectItemList.addItemDecoration(dividerItemDecoration);

        mBtnDialogSelectItemLeft.setOnClickListener(this);
        mBtnDialogSelectItemRight.setOnClickListener(this);
        bindData();

    }

    private void bindData() {
        mTitle = getArguments().getString("title");
        mCanSelectNone = getArguments().getBoolean("canSelectNone");
        mItemDataList = (List<?>) getArguments().getSerializable("itemDataList");


        mSelectItemAdapter.setItemDataList(mItemDataList);
        if (StringUtils.isEmpty(mTitle)) {
            mTvDialogSelectItemTitle.setVisibility(View.GONE);
        } else {
            mTvDialogSelectItemTitle.setText(mTitle);
            mTvDialogSelectItemTitle.setVisibility(View.VISIBLE);
        }
        if (mCanSelectNone) {
            mBtnDialogSelectItemLeft.setText(R.string.abn_yueai_clear);
            mBtnDialogSelectItemLeft.setVisibility(View.VISIBLE);
        } else {
            mBtnDialogSelectItemLeft.setVisibility(View.GONE);
        }
        mBtnDialogSelectItemRight.setText(R.string.abn_yueai_cancel);
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
        if (view == mBtnDialogSelectItemLeft) {
            if (mSelectItemCallback != null) {
                mSelectItemCallback.onSelectNone();
            }
        }
        dismiss();
    }

}
