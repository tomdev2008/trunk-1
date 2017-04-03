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
import android.widget.ListView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.dialog.SelectTruthAdapter;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.fatalsignal.util.DeviceUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectTruthDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.tv_dialog_slelecttruth_question)
    TextView mTvDialogSlelecttruthQuestion;
    @Bind(R.id.listview_dialog_selecttruth)
    ListView mListviewDialogSelecttruth;
    private SelectTruthAdapter mSelectItemAdapter;
    private ArrayList<QuestionEntity.OptionsEntity> mAnswers;
    private String mQuestion;


    public interface SelectItemCallback {
        void onSelectItem(int position, QuestionEntity.OptionsEntity answer);

        void onSelectNone();
    }

    private View mContentView;
    private SelectItemCallback mSelectItemCallback;

    public static SelectTruthDialog newInstance(ArrayList<QuestionEntity.OptionsEntity> itemDataList, String question) {

        Bundle args = new Bundle();
        args.putSerializable("mAnswers", itemDataList);
        args.putString("question", question);

        SelectTruthDialog fragment = new SelectTruthDialog();
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

        mSelectItemAdapter = new SelectTruthAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_dialog_select_truth, container, false);
        ButterKnife.bind(this, mContentView);
        setupView();
        return mContentView;
    }

    private void setupView() {
        mListviewDialogSelecttruth.setAdapter(mSelectItemAdapter);
        mListviewDialogSelecttruth.setOnItemClickListener(this);
        bindData();

    }

    private void bindData() {
        mAnswers = (ArrayList<QuestionEntity.OptionsEntity>) getArguments().getSerializable("mAnswers");
        mQuestion = getArguments().getString("question");
        mTvDialogSlelecttruthQuestion.setText(mQuestion);
        mSelectItemAdapter.setItemDataList(mAnswers);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectItemCallback != null) {
            mSelectItemCallback.onSelectItem(position, (QuestionEntity.OptionsEntity) mSelectItemAdapter.getItem(position));
            dismiss();
        }
    }

}
