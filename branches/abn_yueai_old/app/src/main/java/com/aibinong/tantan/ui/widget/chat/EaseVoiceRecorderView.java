package com.aibinong.tantan.ui.widget.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aibinong.tantan.R;
import com.aibinong.tantan.util.message.ChatVoicePlayerHelper;
import com.aibinong.tantan.util.message.EaseVoiceRecorder;
import com.fatalsignal.util.DeviceUtils;
import com.hyphenate.EMError;


/**
 * Voice recorder view
 */
public class EaseVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected EaseVoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    public EaseVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.abn_yueai_widget_chat_recordview, this);

        micImage = (ImageView) findViewById(R.id.iv_abn_yueai_chat_record_mic);
        recordingHint = (TextView) findViewById(R.id.tv_abn_yueai_chat_record_hint);

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

        // animation resources, used for recording
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14),};

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        this.setVisibility(View.INVISIBLE);
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    if (ChatVoicePlayerHelper.getInstance().isPlaying()) {
                        ChatVoicePlayerHelper.getInstance().stopPlayVoice();
                    }
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(context, R.string.abn_yueai_chat_record_nopermission, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.abn_yueai_chat_record_too_short, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.abn_yueai_chat_record_send_failed, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!DeviceUtils.isSdcardExist()) {
            Toast.makeText(context, R.string.abn_yueai_chat_record_need_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            recordingHint.setText(context.getString(R.string.abn_yueai_chat_record_move_to_cancel));
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, R.string.abn_yueai_chat_record_failed, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText(context.getString(R.string.abn_yueai_chat_record_release_to_cancel));
        recordingHint.setBackgroundResource(R.drawable.abn_yueai_selector_round_bordergray);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText(context.getString(R.string.abn_yueai_chat_record_move_to_cancel));
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
