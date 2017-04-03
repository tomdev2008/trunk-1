package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/10.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.SysPresenter;
import com.aibinong.tantan.ui.dialog.SelectItemDialog;
import com.aibinong.tantan.util.ConstellationUtil;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditInfoSubActivity extends ActivityBase implements SysPresenter.ISysPresenter, TextWatcher {

    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_infoedit_sub_nick)
    EditText mEditInfoeditSubNick;
    @Bind(R.id.tv_infoedit_sub_birth)
    TextView mTvInfoeditSubBirth;
    @Bind(R.id.ll_infoedit_sub_birth)
    LinearLayout mLlInfoeditSubBirth;
    @Bind(R.id.tv_infoedit_sub_occupation)
    TextView mTvInfoeditSubOccupation;
    @Bind(R.id.ll_infoedit_sub_occupation)
    LinearLayout mLlInfoeditSubOccupation;
    private UserEntity mUserEntity;
    private String mCurrentNick;
    private String mCurrentBirth;
    private String mCurrentOccupation;
    private SysPresenter mSysPresenter;
    /**
     * 星座
     */
    private String newconstellation;
    private int newage;

    public static Intent launchIntent(Context context, boolean startDirect, UserEntity userEntity) {
        Intent intent = new Intent(context, EditInfoSubActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        if (startDirect) {
            context.startActivity(intent);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_infoedit_subinfo);
        ButterKnife.bind(this);
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO);
        mCurrentNick = mUserEntity.nickname;
        mCurrentBirth = mUserEntity.birthdate;
        setupView(savedInstanceState);
        bindData();
        mSysPresenter = new SysPresenter(this);
    }

    @Override
    protected boolean swipeBackEnable() {
        return false;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mLlInfoeditSubBirth.setOnClickListener(this);
        mLlInfoeditSubOccupation.setOnClickListener(this);
        mEditInfoeditSubNick.addTextChangedListener(this);
    }

    private void bindData() {
        mEditInfoeditSubNick.removeTextChangedListener(this);
        mEditInfoeditSubNick.setHint(mUserEntity.nickname);
        mEditInfoeditSubNick.setText(mCurrentNick);

        mEditInfoeditSubNick.setSelection(mEditInfoeditSubNick.getText().length());

        if (!StringUtils.isEmpty(mCurrentBirth)) {
            mTvInfoeditSubBirth.setText(mCurrentBirth);
        } else {
            mTvInfoeditSubBirth.setText(mUserEntity.birthdate);
            mCurrentBirth = mUserEntity.birthdate;
        }

        if (!StringUtils.isEmpty(mCurrentOccupation)) {
            mTvInfoeditSubOccupation.setText(mCurrentOccupation);
        } else {
            mTvInfoeditSubOccupation.setText(mUserEntity.occupation);
        }

        mEditInfoeditSubNick.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLlInfoeditSubBirth) {
            selectBirthday();
        } else if (view == mLlInfoeditSubOccupation) {
            //选择职业
            mSysPresenter.performSelectOccupation();
        } else {
            super.onClick(view);

        }
    }

    private void selectBirthday() {
        Long birthTs = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(LoginService.registerBithFm);
        try {
            birthTs = format.parse(mCurrentBirth).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar time = Calendar.getInstance();
        final int YY = time.get(Calendar.YEAR);
        // 编辑生日
        Calendar currentCalendar = Calendar.getInstance(TimeZone
                .getTimeZone("GMT+8"));
        currentCalendar.clear();
        currentCalendar
                .setTimeInMillis(birthTs);
        DialogUtil.showDatePicker(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                Calendar currentCalendar = Calendar.getInstance(TimeZone
                        .getTimeZone("GMT+8"));
                currentCalendar.clear();
                currentCalendar.set(year, monthOfYear, dayOfMonth);
                //不能晚于当前时间
                if (!currentCalendar.before(Calendar.getInstance(TimeZone
                        .getTimeZone("GMT+8")))) {
                    Toast.makeText(EditInfoSubActivity.this, "您确定您来自未来吗？", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentBirth = TimeUtil.getFormatedDate(
                        currentCalendar.getTimeInMillis(), "yyyy-MM-dd");
                mUserEntity.birthdate = mCurrentBirth;
                String[] split = mCurrentBirth.split("-");
                mUserEntity.birthdate = mCurrentBirth;
                //星座计算
                newconstellation = ConstellationUtil.calculateConstellation(mCurrentBirth);
                mUserEntity.constellation = newconstellation;
                newage = YY - Integer.parseInt(split[0]);
                mUserEntity.age = newage;
                bindData();
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar
                .get(Calendar.MONTH), currentCalendar
                .get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onUploadFileSuccess(String url) {

    }

    @Override
    public void onUploadFileFailed(Throwable e) {

    }

    @Override
    public void onUploadFileProgress(File file, long readedSize, long totalSize) {

    }

    @Override
    public void onGetConfigFailed(Throwable e) {

    }

    ArrayList<String> mOccupations;

    @Override
    public void onGetConfigSuccess(ConfigEntity configEntity) {
        mOccupations = configEntity.occupations;
        SelectItemDialog dialog = SelectItemDialog.newInstance(getResources().getString(R.string.abn_yueai_hint_compinfo_job), false, mOccupations);
        dialog.show(new SelectItemDialog.SelectItemCallback() {
            @Override
            public void onSelectItem(int position) {
                mCurrentOccupation = mOccupations.get(position);
                mUserEntity.occupation = mCurrentOccupation;
                bindData();
            }

            @Override
            public void onSelectNone() {

            }
        }, getFragmentManager(), null);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mCurrentNick = mEditInfoeditSubNick.getText().toString();

        bindData();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        if (!StringUtils.isEmpty(mCurrentNick)) {
            mUserEntity.nickname = mCurrentNick;
        }
        if (!StringUtils.isEmpty(newconstellation)) {
            mUserEntity.constellation = newconstellation;
        }
        mUserEntity.birthdate = mCurrentBirth;
        if (newage == 0) {
            Calendar time = Calendar.getInstance();
            final int YY = time.get(Calendar.YEAR);
            String[] split = mCurrentBirth.split("-");
            newage = YY - Integer.parseInt(split[0]);
            mUserEntity.birthdate = mCurrentBirth;
        }
        if (!StringUtils.isEmpty(newage + "")) {
            mUserEntity.age = newage;
        }
        Intent intent = getIntent();
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mUserEntity);
        setResult(RESULT_OK, intent);
        finish();
    }
}
