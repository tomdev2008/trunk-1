package com.aibinong.tantan.ui.activity;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/2.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.presenter.SysPresenter;
import com.aibinong.tantan.ui.dialog.SelectItemDialog;
import com.aibinong.tantan.ui.dialog.SelectSexDialog;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.tk.mediapicker.MediaPicker;
import com.tk.mediapicker.callback.Callback;
import com.tk.mediapicker.request.AlbumRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CompleteInfoActivity extends ActivityBase implements SysPresenter.ISysPresenter, SelectSexDialog.SelectSexCallback {
    private static final int REQUEST_CODE_PICK_PHOTO = 0x100;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.riv_completeinfo_avatar)
    RoundAngleImageView mRivCompleteinfoAvatar;
    @Bind(R.id.circularpb_completeinfo_avatar_upload)
    CircularProgressBar mCircularpbCompleteinfoAvatarUpload;
    @Bind(R.id.iv_completeinfo_addavatar)
    ImageView mIvCompleteinfoAddavatar;
    @Bind(R.id.tv_completeinfo_avatar_uploadfailed)
    TextView mTvCompleteinfoAvatarUploadfailed;
    @Bind(R.id.fl_completeinfo_avatar)
    FrameLayout mFlCompleteinfoAvatar;
    @Bind(R.id.edit_completeinfo_nick)
    EditText mEditCompleteinfoNick;
    @Bind(R.id.tv_completeinfo_select_sex)
    TextView mTvCompleteinfoSelectSex;
    @Bind(R.id.tv_completeinfo_select_birth)
    TextView mTvCompleteinfoSelectBirth;
    @Bind(R.id.tv_completeinfo_select_job)
    TextView mTvCompleteinfoSelectJob;
    @Bind(R.id.btn_completeinfo_next)
    Button mBtnCompleteinfoNext;

    private SysPresenter mSysPresenter;
    private String mCurrentAvatarUrl;
    private int mCurrentSex = LoginService.registerSexMale;
    private long mCurrentBirth = -2209017600000L;
    private String mCurrentBithStr;
    private Uri mCurrentAvatarUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_completeinfo);
        ButterKnife.bind(this);
        requireTransStatusBar();
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        setupView(savedInstanceState);
        mSysPresenter = new SysPresenter(this);
        bindData();
    }

    @Override
    protected boolean swipeBackEnable() {
        return true;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        mFlCompleteinfoAvatar.setOnClickListener(this);
        mTvCompleteinfoSelectSex.setOnClickListener(this);
        mTvCompleteinfoSelectBirth.setOnClickListener(this);
        mTvCompleteinfoSelectJob.setOnClickListener(this);

        mBtnCompleteinfoNext.setOnClickListener(this);
        mTvCompleteinfoAvatarUploadfailed.setOnClickListener(this);

        /*mEditCompleteinfoNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nickStr = charSequence.toString();
                byte[] nickBytes = nickStr.getBytes();

                int sizeOfZh = (nickBytes.length - nickStr.length()) / 2;
                int sizeOfEn = (nickStr.length() * 3 - nickBytes.length) / 2;

                if (2*sizeOfZh + sizeOfEn > 16) {
                    mEditCompleteinfoNick.setText(nickStr.substring(0, nickStr.length() - 1));
                    mEditCompleteinfoNick.setSelection(mEditCompleteinfoNick.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    private void bindData() {
        UserEntity userEntity = UserUtil.getSavedUserInfo();
        if (mCurrentSex == LoginService.registerSexMale) {
            mTvCompleteinfoSelectSex.setText(getString(R.string.abn_yueai_sex_male));
        } else {
            mTvCompleteinfoSelectSex.setText(getString(R.string.abn_yueai_sex_female));
        }

        mTvCompleteinfoSelectBirth.setText(mCurrentBithStr);
        if (mCurrentAvatarUri != null) {
            Glide.with(this).load(mCurrentAvatarUri).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivCompleteinfoAvatar);
            mIvCompleteinfoAddavatar.setVisibility(View.GONE);
        } else if (userEntity != null && !StringUtils.isEmpty(userEntity.getFirstPicture())) {
            Glide.with(this).load(userEntity.getFirstPicture()).placeholder(R.mipmap.abn_yueai_ic_default_avatar).into(mRivCompleteinfoAvatar);
            mIvCompleteinfoAddavatar.setVisibility(View.GONE);
        } else {
            mIvCompleteinfoAddavatar.setVisibility(View.VISIBLE);
        }

        mTvCompleteinfoSelectJob.setText(mCurrentOccupation);
    }

    @Override
    public void onClick(View view) {
        if (view == mFlCompleteinfoAvatar) {
            //选择头像
            //选取照片
            MediaPicker.startRequest(new AlbumRequest.Builder(this, REQUEST_CODE_PICK_PHOTO)
                    .needCrop(true)
                    .asSystem(false)
                    .asSingle(true)
                    .showCameraIndex(true)
                    .showVideoContent(false)
                    .setCheckLimit(9)
                    .cropRatioWh(1.0f)
                    .maxCropWidth(800)
                    .build());
        } else if (view == mTvCompleteinfoSelectSex) {
            //选择性别
            selectSex();
        } else if (view == mTvCompleteinfoSelectBirth) {
            //选择生日
            selectBirthday();
        } else if (view == mTvCompleteinfoSelectJob) {
            //选择职业
            mSysPresenter.performSelectOccupation();
        } else if (view == mBtnCompleteinfoNext) {
            //检查各个信息无误后，进入下一步
            performComplete();
        } else if (view == mTvCompleteinfoAvatarUploadfailed) {
            //重新上传头像
            startUploadAvatar();
        } else {
            super.onClick(view);
        }
    }

    private void selectSex() {
        SelectSexDialog selectSexDialog = SelectSexDialog.newInstance();
        selectSexDialog.show(mCurrentSex, this, getFragmentManager(), null);
    }

    private void selectBirthday() {
        // 编辑生日
        Calendar currentCalendar = Calendar.getInstance(TimeZone
                .getTimeZone("GMT+8"));
        currentCalendar.clear();
        if (mCurrentBirth <= -2209017600000L) {
            Calendar defaultBirth = Calendar.getInstance(TimeZone
                    .getTimeZone("GMT+8"));
            defaultBirth.set(Calendar.YEAR, 1990);
            mCurrentBirth = defaultBirth.getTimeInMillis();
        }
        currentCalendar
                .setTimeInMillis(mCurrentBirth);
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
                    Toast.makeText(CompleteInfoActivity.this, "您确定您来自未来吗？", Toast.LENGTH_SHORT).show();
                    return;
                }

                mCurrentBirth = currentCalendar.getTimeInMillis();
                mCurrentBithStr = TimeUtil.getFormatedDate(
                        mCurrentBirth, "yyyy-MM-dd");
                bindData();
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar
                .get(Calendar.MONTH), currentCalendar
                .get(Calendar.DAY_OF_MONTH));

    }

    private void performComplete() {
        if (mCurrentAvatarUrl == null) {
            Toast.makeText(this, getString(R.string.abn_yueai_hint_compinfo_avatar), Toast.LENGTH_SHORT).show();
            return;
        }
        String mCurrentNick = mEditCompleteinfoNick.getText().toString();
        if (StringUtils.isEmpty(mCurrentNick)) {
            mEditCompleteinfoNick.requestFocus();
            mEditCompleteinfoNick.setError(getString(R.string.abn_yueai_hint_compinfo_nick));
            return;
        }
        if (mCurrentBirth <= -2209017600000L) {
            Toast.makeText(this, getString(R.string.abn_yueai_hint_compinfo_birth), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCurrentOccupation == null) {
            Toast.makeText(this, getString(R.string.abn_yueai_hint_compinfo_job), Toast.LENGTH_SHORT).show();
            return;
        }
        //进到选择标签页
        Intent intent = new Intent(this, SelectTagsActivity.class);
        UserEntity userEntity = UserUtil.getSavedUserInfo();
        if (userEntity == null) {
            userEntity = new UserEntity();
        }
        userEntity.pictureList = new ArrayList<>(1);
        userEntity.pictureList.add(mCurrentAvatarUrl);
        userEntity.nickname = mCurrentNick;
        userEntity.birthdate = mCurrentBithStr;
        userEntity.sex = mCurrentSex;
        userEntity.occupation = mCurrentOccupation;
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, userEntity);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_PHOTO && resultCode == RESULT_OK && data != null) {
            //选取照片回来
            MediaPicker.onMediaResult(resultCode, data, new Callback() {
                @Override
                public void onComplete(File source) {
                    mCurrentAvatarUri = Uri.fromFile(source);
                    bindData();
                    startUploadAvatar();
                }

                @Override
                public void onComplete(List<File> sourceList) {

                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startUploadAvatar() {
        mTvCompleteinfoAvatarUploadfailed.setVisibility(View.INVISIBLE);
        mCircularpbCompleteinfoAvatarUpload.clearAnimation();
        mCircularpbCompleteinfoAvatarUpload.animate().alpha(1).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCircularpbCompleteinfoAvatarUpload.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mCircularpbCompleteinfoAvatarUpload.setProgress(0);
        mSysPresenter.uploadFile(new File(mCurrentAvatarUri.getPath()));
    }

    @Override
    public void onUploadFileSuccess(String url) {
        //上传头像成功
        DialogUtil.hideDialog(this);
        mCurrentAvatarUrl = url;
        bindData();
        mTvCompleteinfoAvatarUploadfailed.setVisibility(View.INVISIBLE);
        mCircularpbCompleteinfoAvatarUpload.clearAnimation();
        mCircularpbCompleteinfoAvatarUpload.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCircularpbCompleteinfoAvatarUpload.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onUploadFileFailed(Throwable e) {
        DialogUtil.showDialog(this, e.getMessage(), true);
        mTvCompleteinfoAvatarUploadfailed.setVisibility(View.VISIBLE);
        mCircularpbCompleteinfoAvatarUpload.clearAnimation();
        mCircularpbCompleteinfoAvatarUpload.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCircularpbCompleteinfoAvatarUpload.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onUploadFileProgress(File file, final long readedSize, final long totalSize) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float percent = readedSize / (totalSize * 0.01f);
                mCircularpbCompleteinfoAvatarUpload.setProgressWithAnimation(percent);
            }
        });

    }

    @Override
    public void onSelectSex(int sex) {
        mCurrentSex = sex;
        bindData();
    }

    @Override
    public void onGetConfigFailed(Throwable e) {
        DialogUtil.showDialog(this, e.getMessage(), true);
    }

    ArrayList<String> mOccupations;
    private String mCurrentOccupation;

    @Override
    public void onGetConfigSuccess(ConfigEntity configEntity) {
        mOccupations = configEntity.occupations;
        SelectItemDialog dialog = SelectItemDialog.newInstance(getResources().getString(R.string.abn_yueai_hint_compinfo_job), false, mOccupations);
        dialog.show(new SelectItemDialog.SelectItemCallback() {
            @Override
            public void onSelectItem(int position) {
                mCurrentOccupation = mOccupations.get(position);
                bindData();
            }

            @Override
            public void onSelectNone() {

            }
        }, getFragmentManager(), null);
    }
}