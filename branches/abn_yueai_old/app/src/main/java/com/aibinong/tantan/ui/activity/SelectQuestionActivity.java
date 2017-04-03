package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/16.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.SelectQuestionPresenter;
import com.aibinong.tantan.ui.adapter.SelectQuestionAdapter;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectQuestionActivity extends ActivityBase implements SelectQuestionPresenter.ISelectQuestion, ExpandableListView.OnGroupExpandListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.expList_selectquestion_list)
    ExpandableListView mExpListSelectquestionList;
    private SelectQuestionPresenter mSelectQuestionPresenter;
    private SelectQuestionAdapter mSelectQuestionAdapter;
    private ArrayList<QuestionEntity> mSelectedQuestions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_selectquestion);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        setupView(savedInstanceState);
        mSelectQuestionPresenter = new SelectQuestionPresenter(this);
        mSelectQuestionPresenter.loadQuestion();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mSelectQuestionAdapter = new SelectQuestionAdapter(this);
        mExpListSelectquestionList.setAdapter(mSelectQuestionAdapter);
        mExpListSelectquestionList.setOnGroupExpandListener(this);
        mExpListSelectquestionList.setGroupIndicator(null);
    }

    @Override
    public void onQuestionLoaded(List<QuestionEntity> questionEntities) {
        mSelectQuestionAdapter.setQuestionEntities(questionEntities);
    }

    @Override
    public void onQuestionLoadFailed(ResponseResult e) {
        showErrDialog(e).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
    }

    @Override
    public void onGroupExpand(int i) {
        if (i < mSelectQuestionAdapter.getQuestionEntities().size()) {
            QuestionEntity questionEntity = (QuestionEntity) mSelectQuestionAdapter.getQuestionEntities().get(i);
            if (!mSelectedQuestions.contains(questionEntity)) {
                mSelectedQuestions.clear();
                mSelectedQuestions.add(questionEntity);
            }
            mSelectQuestionAdapter.setSelectedQuestions(mSelectedQuestions);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //选择问题
        Object obj = compoundButton.getTag(R.id.common_tag);
        if (obj != null && obj instanceof QuestionEntity) {
            QuestionEntity questionEntity = (QuestionEntity) obj;
            if (b) {
                if (!mSelectedQuestions.contains(questionEntity)) {
                    mSelectedQuestions.clear();
                    mSelectedQuestions.add(questionEntity);
                }
            } else {
                if (mSelectedQuestions.contains(questionEntity)) {
                    mSelectedQuestions.remove(questionEntity);
                }
            }
            mSelectQuestionAdapter.setSelectedQuestions(mSelectedQuestions);
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mSelectedQuestions.isEmpty()) {
            MenuItem menuItem = menu.findItem(R.id.abn_yueai_menuItem_commonRight);
            if (menuItem == null) {
                menuItem = menu.add(R.id.abn_yueai_default_menu_group, R.id.abn_yueai_menuItem_commonRight, 0, getString(R.string.abn_yueai_send));
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        } else {
            menu.removeItem(R.id.abn_yueai_menuItem_commonRight);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.abn_yueai_menuItem_commonRight) {
            //发送
            Intent intent = getIntent();
            intent.putExtra(IntentExtraKey.INTENT_RESULT_KEY_SELECTED_QUESTIONS, mSelectedQuestions);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
