package com.tk.mediapicker.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tk.mediapicker.ui.activity.FileListActivity;

import static com.tk.mediapicker.Constants.AS_SYSTEM;

/**
 * Created by TK on 2016/10/28.
 * 选择文件请求
 */

public final class FileRequest extends Request {

    private FileRequest(Activity activity, Bundle bundle, int requestCode) {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mRequestCode = requestCode;
    }


    @Override
    public Intent initIntent() {
        Intent intent = new Intent(mActivity, FileListActivity.class);
        intent.putExtras(mBundle);
        return intent;
    }

    public static Builder builder(Activity activity, int requestCode) {
        return new Builder(activity, requestCode);
    }

    public static final class Builder {
        private Bundle bundle;
        private Activity activity;
        private int requestCode;

        public Builder(Activity activity, int requestCode) {
            bundle = new Bundle();
            this.activity = activity;
            this.requestCode = requestCode;
        }

        /**
         * 是否调用系统支持的文件预览器,默认false
         * 系统限制单选
         *
         * @return
         */
        public Builder asSystem(boolean asSystem) {
            bundle.putBoolean(AS_SYSTEM, asSystem);
            return this;
        }

        public FileRequest build() {
            return new FileRequest(activity, bundle, requestCode);
        }
    }
}
