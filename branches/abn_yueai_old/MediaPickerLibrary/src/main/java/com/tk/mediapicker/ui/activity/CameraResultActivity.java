package com.tk.mediapicker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tk.mediapicker.Constants;
import com.tk.mediapicker.R;
import com.tk.mediapicker.base.BaseActivity;
import com.tk.mediapicker.utils.MediaUtils;
import com.tk.mediapicker.utils.PermissionHelper;
import com.tk.mediapicker.widget.ConfirmButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import uk.co.senab.photoview.PhotoView;


/**
 * Created by TK on 2016/10/14.
 */

public class CameraResultActivity extends BaseActivity {
    private File tempCameraFile;
    private File tempCropFile;

    private LinearLayout headerLayout;
    private ImageView back;
    private TextView title;
    private ConfirmButton confirmBtn;
    private PhotoView photoview;
    private Subscription subscription;
    //记录相机是否开启
    private boolean hasStart;
    //记录拍照回调
    private boolean cameraResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restroreData(savedInstanceState);
        //校验权限
        int result = PermissionHelper.getPermission(this, PermissionHelper.PHOTO_PERMISSIONS);
        if (result == -1) {
            finish();
        }
        if (result == 1) {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.checkOnResult(requestCode, permissions, grantResults, new PermissionHelper.OnPermissionListener() {
            @Override
            public void onFailure(String[] failurePermissions) {
                Toast.makeText(getApplicationContext(), R.string.permission_camera_null, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onSuccess() {
                init();
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {

        if (!hasStart) {
            hasStart = true;
            startCamera();
        }
        if (cameraResult) {
            setContentView(R.layout.activity_camera_result);
            initView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tempCameraFile != null) {
            outState.putString("tempCameraFile", tempCameraFile.getAbsolutePath());
        }
        if (tempCropFile != null) {
            outState.putString("tempCropFile", tempCropFile.getAbsolutePath());
        }

        outState.putBoolean("hasStart", hasStart);
        outState.putBoolean("cameraResult", cameraResult);
    }

    /**
     * 恢复数据
     *
     * @param savedInstanceState
     */
    private void restroreData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        String tempCameraFileP = savedInstanceState.getString("tempCameraFile");
        String tempCropFileP = savedInstanceState.getString("tempCropFile");
        if (!TextUtils.isEmpty(tempCameraFileP)) {
            tempCameraFile = new File(tempCameraFileP);
        }
        if (!TextUtils.isEmpty(tempCropFileP)) {
            tempCropFile = new File(tempCropFileP);
        }

        hasStart = savedInstanceState.getBoolean("hasStart");
        cameraResult = savedInstanceState.getBoolean("cameraResult");
    }


    /**
     * 调用Android系统拍照
     */
    private void startCamera() {
        Log.e("startCamera", "startCamera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 创建临时文件，并设置系统相机拍照后的输出路径
            try {
                tempCameraFile = MediaUtils.createCameraTmpFile(this);
                if (tempCameraFile != null && tempCameraFile.exists()) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempCameraFile));
                    startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(), "创建缓存文件失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "创建缓存文件失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "您的手机不支持相机", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 调用Android系统裁剪
     */
    private void startCrop() {
        int maxSizeW = getIntent().getIntExtra(Constants.AlbumRequestConstants.MAX_CROP_WIDTH, Constants.DEFAULT_MAX_CROPED_SIZE);
        float cropRatioWh = getIntent().getFloatExtra(Constants.AlbumRequestConstants.CROP_RATIO_WH, Constants.DEFAULT_CROPED_RATIO_WH);
        int maxSizeH = (int) (maxSizeW / cropRatioWh);

        try {
            tempCropFile = MediaUtils.createCameraTmpFile(this);
            UCrop
                    .of(Uri.fromFile(tempCameraFile), Uri.fromFile(tempCropFile))
                    .withAspectRatio(cropRatioWh, 1)
                    .withMaxResultSize(maxSizeW, maxSizeH)
                    .start(this, Constants.CROP_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                cameraResult = false;
                clearTemp();
                finish();
            } else {
                if (getIntent().getExtras().getBoolean(Constants.CameraRequestConstants.NEED_CROP, false)) {
                    //去裁剪
                    startCrop();
                } else {
                    //展示结果
                    cameraResult = true;
                    setContentView(R.layout.activity_camera_result);
                    initView();
                }
            }
        } else if (requestCode == Constants.CROP_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                clearTemp();
                finish();
            } else {
                //将裁减结果回调
                Intent intent = new Intent();
                intent.putExtra(Constants.RESULT_SINGLE, true);
                intent.putExtra(Constants.RESULT_DATA, UCrop.getOutput(data).getPath());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    /**
     * 延迟初始化界面元素
     */
    private void initView() {

        headerLayout = (LinearLayout) findViewById(R.id.header_layout);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        confirmBtn = (ConfirmButton) findViewById(R.id.confirm_btn);
        photoview = (PhotoView) findViewById(R.id.photoview);
        headerLayout.setBackgroundColor(0xBB000000);

        title.setText("1/1");
        confirmBtn.setEnabled(true);
        subscription = Luban.get(this)
                .load(tempCameraFile)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                })
                .subscribe(file -> {
                    try {
                        Glide.with(CameraResultActivity.this)
                                .load(file)
                                .override(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels)
                                .fitCenter()
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        photoview.setImageDrawable(resource);
                                    }
                                });
                    } catch (Exception e) {
                    }
                });

        confirmBtn.setOnClickListener(v -> {
            //点击完成将拍照结果回调给PhotoPicker
            Intent data = new Intent();
            data.putExtra(Constants.RESULT_SINGLE, true);
            data.putExtra(Constants.RESULT_DATA, tempCameraFile.getAbsolutePath());
            setResult(Activity.RESULT_OK, data);
            finish();
        });
        back.setOnClickListener(v -> {
            clearTemp();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if (photoview != null) {
            photoview.setImageDrawable(null);
        }
        super.onDestroy();
    }

    /**
     * 清除临时文件
     */
    private void clearTemp() {
        if (tempCameraFile != null) {
            if (tempCameraFile.exists()) {
                tempCameraFile.delete();
            }
        }
        if (tempCropFile != null) {
            if (tempCropFile.exists()) {
                tempCropFile.delete();
            }
        }
    }
}
