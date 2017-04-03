package com.aibinong.videorecorder.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibinong.videorecorder.R;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.FileUtils;
import com.fatalsignal.util.StringUtils;
import com.yang.camera.record.CameraUtils;
import com.yang.camera.record.ConvertToUtils;
import com.yang.camera.record.MediaObject;
import com.yang.camera.record.MediaRecorderBase;
import com.yang.camera.record.MediaRecorderBase.OnErrorListener;
import com.yang.camera.record.MediaRecorderBase.OnParamsPreparedListener;
import com.yang.camera.record.MediaRecorderBase.OnPreparedListener;
import com.yang.camera.record.MediaRecorderNative;
import com.yang.camera.record.MediaRecorderSystem;
import com.yang.camera.record.VCamera;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;

public class VideoShotActivity extends Activity implements OnErrorListener,
        OnClickListener, OnPreparedListener, MediaRecorderBase.OnEncodeListener {
    public static final String INTENT_RESULT_EXTRA_PATH = "INTENT_RESULT_EXTRA_PATH";
    public static final String INTENT_EXTRA_MAX_DURATION = "INTENT_EXTRA_MAX_DURATION";

    private RelativeLayout mInner_rl_top;
    private ImageButton mIb_shot_close;
    private ImageButton mIb_shot_cameraswitch;
    private ImageButton mIb_shot_flashtollgate;
    private LinearLayout mInner_ll_bottom;
    private CheckedTextView mCt_shot_delete;
    private ImageButton mCiv_shot_video_shot;
    private ImageButton mIbtn_shot_complete;
    private com.yang.camera.record.ProgressView mRecord_progress;
    private RelativeLayout mCamera_layout;
    private SurfaceView mSurface_shot_video;
    private ImageView mIv_shot_focusing;

    private SurfaceHolder mSurfaceHolder;

    private MediaRecorderBase mMediaRecorder;
    private Camera mCamera;
    /**
     * 刷新进度条
     */
    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
    /**
     * 延迟拍摄停止
     */
    private static final int HANDLE_STOP_RECORD = 1;
    /**
     * 对焦
     */
    private static final int HANDLE_HIDE_RECORD_FOCUS = 2;
    /**
     * 录制最长时间
     */
    public static int RECORD_TIME_MAX = 10 * 1000;
    /**
     * 录制最小时间
     */
    public final static int RECORD_TIME_MIN = 3 * 1000;
    private int mFocusWidth, mWindowWidth;
    /**
     * 需要重新编译（拍摄新的或者回删）
     */
    private boolean mRebuild;
    /**
     * 视频信息
     */
    private MediaObject mMediaObject;
    /**
     * on
     */
    private boolean mCreated;
    /**
     * 是否是点击状态
     */
    private volatile boolean mPressedStatus;
    /**
     * 是否已经释放
     */
    private volatile boolean mReleased;
    /**
     * 对焦动画
     */
    private Animation mFocusAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCreated = false;
        super.onCreate(savedInstanceState);
        int maxDuration = getIntent().getExtras().getInt(INTENT_EXTRA_MAX_DURATION, RECORD_TIME_MAX);
        if (maxDuration > RECORD_TIME_MIN) {
            RECORD_TIME_MAX = maxDuration;
        }
        View view = getLayoutInflater().inflate(R.layout.activity_videoshot,
                null);
        setContentView(view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        bindViews();
        // bindData();
        mCreated = true;
    }

    // End Of Content View Elements

    private void bindViews() {

        mInner_rl_top = (RelativeLayout) findViewById(R.id.inner_rl_top);
        mIb_shot_close = (ImageButton) findViewById(R.id.ib_shot_close);
        mIb_shot_cameraswitch = (ImageButton) findViewById(R.id.ib_shot_cameraswitch);
        mIb_shot_flashtollgate = (ImageButton) findViewById(R.id.ib_shot_flashtollgate);
        mInner_ll_bottom = (LinearLayout) findViewById(R.id.inner_ll_bottom);
        mCt_shot_delete = (CheckedTextView) findViewById(R.id.ct_shot_delete);
        mCiv_shot_video_shot = (ImageButton) findViewById(R.id.civ_shot_video_shot);
        mIbtn_shot_complete = (ImageButton) findViewById(R.id.ibtn_shot_complete);
        mRecord_progress = (com.yang.camera.record.ProgressView) findViewById(R.id.record_progress);
        mCamera_layout = (RelativeLayout) findViewById(R.id.camera_layout);
        mSurface_shot_video = (SurfaceView) findViewById(R.id.surface_shot_video);
        mIv_shot_focusing = (ImageView) findViewById(R.id.iv_shot_focusing);


        // ~~~ 绑定事件
        if (DeviceUtils.hasICS()) {
            mSurface_shot_video.setOnTouchListener(mOnSurfaveViewTouchListener);
        }
        mCiv_shot_video_shot
                .setOnTouchListener(mOnVideoControllerTouchListener);
        mSurfaceHolder = mSurface_shot_video.getHolder();

        // ~~~ 设置数据

        // 是否支持前置摄像头
        if (!CameraUtils.isSupportFrontCamera()) {
            mIb_shot_cameraswitch.setVisibility(View.GONE);
        }
        // 是否支持闪光灯
        if (!DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
            mIb_shot_flashtollgate.setVisibility(View.GONE);
        }

        try {
            mIv_shot_focusing.setImageResource(R.drawable.video_focus);
        } catch (OutOfMemoryError e) {
            Log.e(this.getClass().getSimpleName(), e + "");
        }

        mRecord_progress.setMaxDuration(RECORD_TIME_MAX);
        mWindowWidth = DeviceUtils.getScreenWidth(this);
        mFocusWidth = ConvertToUtils.dipToPX(this, 64);
    }

    /**
     * 初始化画布
     */
    private void initSurfaceView(int w, int h) {
        final int sw = DeviceUtils.getScreenWidth(this);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurface_shot_video
                .getLayoutParams();

        lp.width = sw;// w; TODO:设置分辨率
        lp.height = (int) (sw * w / (1.0 * h));

        mSurface_shot_video.setLayoutParams(lp);
    }

    public static final String RECORD_CACHE_DIR = "videoshot";

    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
        VCamera.setVideoCachePath(getExternalCacheDir() + File.separator
                + RECORD_CACHE_DIR);// TODO: 16/11/18 缓存路径
        VCamera.initialize(this);

        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setPreferPreviewSize(480);
        mMediaRecorder.setParamsListener(new OnParamsPreparedListener() {

            @Override
            public void onPrepared(int w, int h) {
                initSurfaceView(w, h);
            }
        });
        mRebuild = true;

        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        File f = new File(VCamera.getVideoCachePath());
        if (!FileUtils.checkFile(f)) {
            f.mkdirs();
        }
        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key,
                VCamera.getVideoCachePath() + key);
        mMediaRecorder.setSurfaceHolder(mSurfaceHolder);
        mMediaRecorder.prepare();
    }

    /**
     * 点击屏幕录制
     */
    private View.OnTouchListener mOnSurfaveViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null || !mCreated) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 检测是否手动对焦
                    if (checkCameraFocus(event))
                        return true;
                    break;
            }
            return true;
        }

    };

    /**
     * 点击屏幕录制
     */
    private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 检测是否手动对焦
                    // 判断是否已经超时
                    if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
                        return true;
                    }

                    // 取消回删
                    if (cancelDelete())
                        return true;

                    startRecord();
                    break;

                case MotionEvent.ACTION_UP:
                    // 暂停
                    if (mPressedStatus) {
                        stopRecord();

                        // 检测是否已经完成
                        if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
                            mIbtn_shot_complete.performClick();
                        }
                    }
                    break;
            }
            return true;
        }

    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_INVALIDATE_PROGRESS:
                    if (mMediaRecorder != null && !isFinishing()) {
                        if (mRecord_progress != null)
                            mRecord_progress.invalidate();
                        // if (mPressedStatus)
                        // titleText.setText(String.format("%.1f",
                        // mMediaRecorder.getDuration() / 1000F));
                        if (mPressedStatus)
                            sendEmptyMessageDelayed(0, 30);
                    }
                    break;
                case HANDLE_STOP_RECORD:
                    stopRecord();
                    break;
            }
        }
    };

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {
            MediaObject.MediaPart part = mMediaRecorder.startRecord();
            if (part == null) {
                return;
            }

            // 如果使用MediaRecorderSystem，不能在中途切换前后摄像头，否则有问题
            if (mMediaRecorder instanceof MediaRecorderSystem) {
                mIb_shot_cameraswitch.setVisibility(View.INVISIBLE);
            }
            mRecord_progress.setData(mMediaObject);
        }

        mRebuild = true;
        mPressedStatus = true;
        mCiv_shot_video_shot.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.drawable_shotbtn_sel));

        if (mHandler != null) {
            mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
            mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);

            mHandler.removeMessages(HANDLE_STOP_RECORD);
            mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD,
                    RECORD_TIME_MAX - mMediaObject.getDuration());
        }
        mCt_shot_delete.setVisibility(View.GONE);
        mIb_shot_cameraswitch.setEnabled(false);
        mIb_shot_flashtollgate.setEnabled(false);
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        mPressedStatus = false;
        mCiv_shot_video_shot.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.drawable_shotbtn));

        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
        }
        mIb_shot_cameraswitch.setVisibility(View.VISIBLE);
        mCt_shot_delete.setVisibility(View.VISIBLE);
        mIb_shot_cameraswitch.setEnabled(true);
        mIb_shot_flashtollgate.setEnabled(true);

        mHandler.removeMessages(HANDLE_STOP_RECORD);
        checkStatus();
    }

    /**
     * 检查录制时间，显示/隐藏下一步按钮
     */
    private int checkStatus() {
        int duration = 0;
        if (!isFinishing() && mMediaObject != null) {
            duration = mMediaObject.getDuration();
            if (duration < RECORD_TIME_MIN) {
                if (duration == 0) {
                    mIb_shot_cameraswitch.setVisibility(View.VISIBLE);
                    mCt_shot_delete.setVisibility(View.GONE);
                } else {
                }
                // TODO:下一步
                // //视频必须大于3秒
                mIbtn_shot_complete.setVisibility(View.INVISIBLE);
            } else {
                // //下一步
                mIbtn_shot_complete.setVisibility(View.VISIBLE);
            }
        }
        return duration;
    }

    /**
     * 取消回删
     */
    private boolean cancelDelete() {
        if (mMediaObject != null) {
            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
            if (part != null && part.remove) {
                part.remove = false;
                mCt_shot_delete.setChecked(false);

                if (mRecord_progress != null)
                    mRecord_progress.invalidate();

                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();

        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mIb_shot_flashtollgate.setSelected(false);
            mMediaRecorder.prepare();
            mRecord_progress.setData(mMediaObject);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecord();
        UtilityAdapter.freeFilterParser();
        if (!mReleased) {
            if (mMediaRecorder != null)
                mMediaRecorder.release();
        }
        mReleased = false;
    }

    /**
     * 手动对焦
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean checkCameraFocus(MotionEvent event) {
        // mFocusImage.setVisibility(View.GONE);
        // float x = event.getX();
        // float y = event.getY();
        // float touchMajor = event.getTouchMajor();
        // float touchMinor = event.getTouchMinor();
        //
        // Rect touchRect = new Rect((int) (x - touchMajor / 2),
        // (int) (y - touchMinor / 2), (int) (x + touchMajor / 2),
        // (int) (y + touchMinor / 2));
        // // The direction is relative to the sensor orientation, that is, what
        // // the sensor sees. The direction is not affected by the rotation or
        // // mirroring of setDisplayOrientation(int). Coordinates of the
        // rectangle
        // // range from -1000 to 1000. (-1000, -1000) is the upper left point.
        // // (1000, 1000) is the lower right point. The width and height of
        // focus
        // // areas cannot be 0 or negative.
        // // No matter what the zoom level is, (-1000,-1000) represents the top
        // of
        // // the currently visible camera frame
        // if (touchRect.right > 1000)
        // touchRect.right = 1000;
        // if (touchRect.bottom > 1000)
        // touchRect.bottom = 1000;
        // if (touchRect.left < 0)
        // touchRect.left = 0;
        // if (touchRect.right < 0)
        // touchRect.right = 0;
        //
        // if (touchRect.left >= touchRect.right
        // || touchRect.top >= touchRect.bottom)
        // return false;
        //
        // ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        // focusAreas.add(new Camera.Area(touchRect, 1000));
        // if (!mMediaRecorder.manualFocus(new Camera.AutoFocusCallback() {
        //
        // @Override
        // public void onAutoFocus(boolean success, Camera camera) {
        // // if (success) {
        // mFocusImage.setVisibility(View.GONE);
        // // }
        // }
        // }, focusAreas)) {
        // mFocusImage.setVisibility(View.GONE);
        // }
        //
        // RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
        // mFocusImage
        // .getLayoutParams();
        // int left = touchRect.left - (mFocusWidth / 2);// (int) x -
        // // (focusingImage.getWidth()
        // // / 2);
        // int top = touchRect.top - (mFocusWidth / 2);// (int) y -
        // // (focusingImage.getHeight()
        // // / 2);
        // if (left < 0)
        // left = 0;
        // else if (left + mFocusWidth > mWindowWidth)
        // left = mWindowWidth - mFocusWidth;
        // if (top + mFocusWidth > mWindowWidth)
        // top = mWindowWidth - mFocusWidth;
        //
        // lp.leftMargin = left;
        // lp.topMargin = top;
        // mFocusImage.setLayoutParams(lp);
        // mFocusImage.setVisibility(View.VISIBLE);
        //
        // if (mFocusAnimation == null)
        // mFocusAnimation = AnimationUtils.loadAnimation(this,
        // R.anim.record_focus);
        //
        // mFocusImage.startAnimation(mFocusAnimation);
        //
        // mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);//
        // 最多3.5秒也要消失
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (DeviceUtils.hasHoneycomb()) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
            mHandler.removeMessages(HANDLE_STOP_RECORD);
        }

        if (vid == R.id.ib_shot_close) {
            // 取消拍摄，回到上一个界面去
            mMediaObject.delete();
            this.finish();
        }

        // 处理开启回删后其他点击操作
        if (vid != R.id.ct_shot_delete) {
            if (mMediaObject != null) {
                MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                if (part != null) {
                    if (part.remove) {
                        part.remove = false;
                        mCt_shot_delete.setChecked(false);
                        if (mRecord_progress != null)
                            mRecord_progress.invalidate();
                    }
                }
            }
        }


        if (vid == R.id.civ_shot_video_shot) {
        }

        if (vid == R.id.ib_shot_cameraswitch) {
            // 切换摄像头
            if (mIb_shot_flashtollgate.isSelected()) {
                if (mMediaRecorder != null) {
                    mMediaRecorder.toggleFlashMode();
                }
                mIb_shot_flashtollgate.setSelected(false);
            }

            if (mMediaRecorder != null) {
                mMediaRecorder.switchCamera();
            }

            if (mMediaRecorder.isFrontCamera()) {
                mIb_shot_flashtollgate.setEnabled(false);
            } else {
                mIb_shot_flashtollgate.setEnabled(true);
            }
        }

        if (vid == R.id.ib_shot_flashtollgate) {
            // 开启前置摄像头以后不支持开启闪光灯
            if (mMediaRecorder != null) {
                if (mMediaRecorder.isFrontCamera()) {
                    return;
                }
            }

            if (mMediaRecorder != null) {
                mMediaRecorder.toggleFlashMode();
            }
        }

        if (vid == R.id.ct_shot_delete) {
            // 取消回删
            if (mMediaObject != null) {
                MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                if (part != null) {
                    if (part.remove) {
                        mRebuild = true;
                        part.remove = false;
                        mMediaObject.removePart(part, true);
                        mCt_shot_delete.setChecked(false);
                    } else {
                        part.remove = true;
                        mCt_shot_delete.setChecked(true);
                    }
                }
                if (mRecord_progress != null)
                    mRecord_progress.invalidate();

                // 检测按钮状态
                checkStatus();
            }
        }
        if (vid == R.id.ibtn_shot_complete) {
            mMediaRecorder.startEncoding();
        }
    }

    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVideoError(int what, int extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAudioError(int what, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEncodeStart() {
        // TODO Auto-generated method stub
        showProgress("", "装在拼接");
    }

    @Override
    public void onEncodeProgress(int progress) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEncodeComplete() {
        // TODO Auto-generated method stub
        hideProgress();
        // Toast.makeText(getApplicationContext(),
        // "拼接完成:" + mMediaObject.getOutputTempVideoPath(),
        // Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        //复制临时文件到结果路径，删掉临时文件
        FileUtils.fileCopy(mMediaObject.getOutputTempVideoPath(), mMediaObject.getOutputVideoPath());
        mMediaObject.delete();
        bundle.putString(INTENT_RESULT_EXTRA_PATH,
                mMediaObject.getOutputVideoPath());
        bundle.putBoolean("Rebuild", mRebuild);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        mRebuild = false;

    }

    @Override
    public void onEncodeError() {
        // TODO Auto-generated method stub

    }

    protected ProgressDialog mProgressDialog;

    public ProgressDialog showProgress(String title, String message) {
        return showProgress(title, message, -1);
    }

    public ProgressDialog showProgress(String title, String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!StringUtils.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
