package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/23.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.FeedbackPresenter;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.fatalsignal.util.StringUtils;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedBackActivity extends ActivityBase implements FeedbackPresenter.IFeedBack, TextWatcher {
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_feedback_content)
    EditText mEditFeedbackContent;
    @Bind(R.id.btn_feedback_submit)
    Button mBtnFeedbackSubmit;
    private FeedbackPresenter mFeedbackPresenter;

    public static Intent launchIntent(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_feedback);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mFeedbackPresenter = new FeedbackPresenter(this);
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mBtnFeedbackSubmit.setOnClickListener(this);
        mBtnFeedbackSubmit.setVisibility(View.INVISIBLE);
        mEditFeedbackContent.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnFeedbackSubmit) {
            String content = mEditFeedbackContent.getText().toString().trim();
            if (StringUtils.isEmpty(content)) {
                mEditFeedbackContent.requestFocus();
                mEditFeedbackContent.setError(getString(R.string.abn_yueai_feedback_hint));
                return;
            }
            DialogUtil.showIndeternimateDialog(this, null);
            mFeedbackPresenter.submit(content);
        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onSubmitFailed(ResponseResult e) {
        showErrDialog(e);
    }

    @Override
    public void onSubmitSuccess(String result) {
        Toast.makeText(this, "提交成功，感谢您的反馈", Toast.LENGTH_SHORT).show();
        SwipeBackHelper.finish(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = mEditFeedbackContent.getText().toString().trim();
        if (!StringUtils.isEmpty(content)) {
            mBtnFeedbackSubmit.setVisibility(View.VISIBLE);
        } else {
            mBtnFeedbackSubmit.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
