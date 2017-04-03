package com.tk.mediapicker.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aibinong.videorecorder.ui.VideoShotActivity;
import com.tk.mediapicker.ui.activity.VideoShotResultActivity;

/**
 * Created by TK on 2016/10/27.
 * 录像请求
 */

public final class VideoShotRequest extends Request {
    private int mMaxDuration;

    private VideoShotRequest(Activity activity, Bundle bundle, int requestCode, int maxDuration) {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mRequestCode = requestCode;
        mMaxDuration = maxDuration;
    }


    @Override
    public Intent initIntent() {
        Intent intent = new Intent(mActivity, VideoShotResultActivity.class);
        mBundle.putInt(VideoShotActivity.INTENT_EXTRA_MAX_DURATION, mMaxDuration);
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
        private int maxDuration;

        public Builder(Activity activity, int requestCode) {
            bundle = new Bundle();
            this.activity = activity;
            this.requestCode = requestCode;
        }

        public Builder maxDuration(int duration) {
            maxDuration = duration;
            return this;
        }

        /**
         * 录像是否调用系统支持的录像
         *
         * @return
         */
//        public Builder asSystem(boolean asSystem) {
//            bundle.putBoolean("System", asSystem);
//            return this;
//        }
        public VideoShotRequest build() {
            return new VideoShotRequest(activity, bundle, requestCode, maxDuration);
        }
    }
}
