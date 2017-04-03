package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/21.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.services.SysService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import rx.Subscriber;

public class AlbumEditPresenter extends PresenterBase {
    public interface UploadCallback {
        void onProgress(File file, long readedSize, long totalSize);

        void onError(Throwable e);

        void onComplete(String url);
    }

    public void uploadFile(final File file, final UploadCallback callback) {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MultipartBody.FORM;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {


                InputStream is = null;
                try {
                    is = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    long totalSize = file.length();
                    int readedCount = 0;
                    int currCount;
                    while ((currCount = is.read(buffer)) > 0) {
                        sink.write(buffer, 0, currCount);
                        readedCount += currCount;
                        //更新进度
                        if (callback != null) {
                            callback.onProgress(file, readedCount, totalSize);
                        }
                    }
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        };

        addToCycle(ApiHelper.getInstance()
                .create(SysService.class)
                .upload_file(MultipartBody.Part.createFormData(SysService.upload_file_key, file.getName().endsWith(".jpg") ? file.getName() : (file.getName() + ".jpg"), requestBody)/*, SysService.UPLOAD_TYPE_AVATAR*/)
                .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                        callback.onComplete(stringJsonRetEntity.getData());
                    }
                })
        );
    }

}
