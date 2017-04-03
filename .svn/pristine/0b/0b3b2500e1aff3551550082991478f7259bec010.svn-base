package com.aibinong.tantan.ui.activity.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.activity.ActivityBase;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * show the video
 */
public class EaseShowVideoActivity extends ActivityBase {
    private static final String TAG = "ShowVideoActivity";

    private RelativeLayout loadingLayout;
    private ProgressBar progressBar;
    private String localFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_video_playactivity);
        requireTransStatusBar();
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        localFilePath = getIntent().getStringExtra("localpath");
        String remotepath = getIntent().getStringExtra("remotepath");
        String secret = getIntent().getStringExtra("secret");
        EMLog.d(TAG, "show video view file:" + localFilePath
                + " remotepath:" + remotepath + " secret:" + secret);
        if (localFilePath != null && new File(localFilePath).exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(localFilePath)),
                    "video/mp4");
            startActivity(intent);
            finish();
            /*List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
            if (resolveInfos != null && resolveInfos.size() > 0) {
                for (int i = 0; i < resolveInfos.size(); i++) {
                    ResolveInfo resolveInfo = resolveInfos.get(i);
                    intent.setPackage(resolveInfo.resolvePackageName);
                    startActivity(intent);
                    finish();
                    return;
                }
            }*/

        } else if (!TextUtils.isEmpty(remotepath) && !remotepath.equals("null")) {
            EMLog.d(TAG, "download remote video file");
            Map<String, String> maps = new HashMap<String, String>();
            if (!TextUtils.isEmpty(secret)) {
                maps.put("share-secret", secret);
            }
            downloadVideo(remotepath, maps);
        } else {

        }
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {

    }

    public String getLocalFilePath(String remoteUrl) {
        String localPath;
        if (remoteUrl.contains("/")) {
            localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
                    + "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1)
                    + ".mp4";
        } else {
            localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
                    + "/" + remoteUrl + ".mp4";
        }
        return localPath;
    }

    /**
     * show local video
     *
     * @param localPath -- local path of the video file
     */
    private void showLocalVideo(String localPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(localPath)),
                "video/mp4");
        try {

            startActivity(intent);
        } catch (Exception e) {
        }
        finish();
        /*List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            for (int i = 0; i < resolveInfos.size(); i++) {
                ResolveInfo resolveInfo = resolveInfos.get(i);
                intent.setPackage(resolveInfo.resolvePackageName);
                startActivity(intent);
                finish();
                return;
            }
        }*/

    }


    /**
     * download video file
     */
    private void downloadVideo(final String remoteUrl,
                               final Map<String, String> header) {

        if (TextUtils.isEmpty(localFilePath)) {
            localFilePath = getLocalFilePath(remoteUrl);
        }
        if (new File(localFilePath).exists()) {
            showLocalVideo(localFilePath);
            return;
        }
        loadingLayout.setVisibility(View.VISIBLE);

        EMCallBack callback = new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loadingLayout.setVisibility(View.GONE);
                        progressBar.setProgress(0);
                        showLocalVideo(localFilePath);
                    }
                });
            }

            @Override
            public void onProgress(final int progress, String status) {
                Log.d("ease", "video progress:" + progress);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                    }
                });

            }

            @Override
            public void onError(int error, String msg) {
                Log.e("###", "offline file transfer error:" + msg);
                File file = new File(localFilePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        };

        EMClient.getInstance().chatManager().downloadFile(remoteUrl, localFilePath, header, callback);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
