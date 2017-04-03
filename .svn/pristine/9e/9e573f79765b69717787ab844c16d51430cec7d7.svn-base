package com.aibinong.tantan.util.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/15.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.fatalsignal.util.Log;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.EMLog;

import java.io.File;

import static com.hyphenate.chat.EMGCMListenerService.TAG;


public class ChatVoicePlayerHelper {
    public interface IVoicePlayerHelper {
        void onPlayVoiceStop(EMMessage msg);

        void onPlayVoiceStart(EMMessage msg);

        void onPlayVoiceFailed(EMMessage msg);

        void onVoiceNotLoaded(EMMessage msg);

    }

    private static ChatVoicePlayerHelper _smChatVoicePlayerHelper = new ChatVoicePlayerHelper();
    private Context mCurrentContext;
    private EMMessage mCurrentMessage;
    private boolean mIsPlaying = false;
    private String mPlayingMsgId;
    private IVoicePlayerHelper mIVoicePlayerHelper;
    private MediaPlayer mMediaPlayer = null;

    private ChatVoicePlayerHelper() {

    }

    public static ChatVoicePlayerHelper getInstance() {

        return _smChatVoicePlayerHelper;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public String getPlayingMsgId() {
        return mPlayingMsgId;
    }

    public void startPlay(EMMessage msg, Context context, IVoicePlayerHelper iVoicePlayerHelper) {
        //先停止正在播放的
        stopPlayVoice();

        mCurrentContext = context;
        mCurrentMessage = msg;
        mIVoicePlayerHelper = iVoicePlayerHelper;

        if (!(msg.getBody() instanceof EMVoiceMessageBody)) {
            return;
        }
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) mCurrentMessage.getBody();
        if (mCurrentMessage.direct() == EMMessage.Direct.SEND) {
            // 发出去的录音，直接播放本地就好了
            playVoice(voiceBody.getLocalUrl());
        } else {
            if (mCurrentMessage.status() == EMMessage.Status.SUCCESS) {
                File file = new File(voiceBody.getLocalUrl());
                if (file.exists() && file.isFile()) {
                    playVoice(voiceBody.getLocalUrl());
                } else {
                    EMLog.e(TAG, "file not exist");
                }
            } else if (mCurrentMessage.status() == EMMessage.Status.INPROGRESS) {
                mIVoicePlayerHelper.onVoiceNotLoaded(mCurrentMessage);
            } else if (mCurrentMessage.status() == EMMessage.Status.FAIL) {
                mIVoicePlayerHelper.onVoiceNotLoaded(mCurrentMessage);
            }
        }
    }

    public void stopPlayVoice() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mIsPlaying = false;
        mPlayingMsgId = null;

        if (mIVoicePlayerHelper != null) {
            mIVoicePlayerHelper.onPlayVoiceStop(mCurrentMessage);
        }
        mCurrentContext = null;
        mCurrentMessage = null;
        mIVoicePlayerHelper = null;
    }

    private void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        mPlayingMsgId = mCurrentMessage.getMsgId();
        AudioManager audioManager = (AudioManager) mCurrentContext.getSystemService(Context.AUDIO_SERVICE);

        mMediaPlayer = new MediaPlayer();
        if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("mMediaPlayer", "onCompletion");
                    stopPlayVoice();
                }

            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.i("mMediaPlayer", "onPrepared");
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    stopPlayVoice();
                    return false;
                }
            });
            mMediaPlayer.prepare();
            mIsPlaying = true;
            if (mIVoicePlayerHelper != null) {
                mIVoicePlayerHelper.onPlayVoiceStart(mCurrentMessage);
            }


        } catch (Exception e) {
            System.out.println();
        }

    }
}
