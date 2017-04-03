package com.tk.mediapicker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aibinong.videorecorder.ui.VideoShotActivity;
import com.tk.mediapicker.Constants;
import com.tk.mediapicker.R;
import com.tk.mediapicker.base.BaseActivity;
import com.tk.mediapicker.utils.PermissionHelper;

import static com.tk.mediapicker.Constants.DEFAULT_REQUEST;


/**
 * Created by TK on 2016/10/14.
 */

public class VideoShotResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void init() {
        Intent intent = new Intent(this, VideoShotActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivityForResult(intent, DEFAULT_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.DEFAULT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String outPath = data.getExtras().getString(VideoShotActivity.INTENT_RESULT_EXTRA_PATH);
                if (outPath != null) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.RESULT_SINGLE, true);
                    intent.putExtra(Constants.RESULT_DATA, outPath);
                    setResult(Activity.RESULT_OK, intent);
                }

            } else {
//                clearTemp();
            }
            finish();
        }
    }

}
