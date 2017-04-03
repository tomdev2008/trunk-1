package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/3.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.services.SysService;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import rx.Subscriber;

public class SysPresenter extends PresenterBase {
    public interface ISysPresenter {
        void onUploadFileSuccess(String url);

        void onUploadFileFailed(Throwable e);

        void onUploadFileProgress(File file, long readedSize, long totalSize);

        void onGetConfigFailed(Throwable e);

        void onGetConfigSuccess(ConfigEntity configEntity);
    }

    private ISysPresenter mISysPresenter;

    public SysPresenter(ISysPresenter iSysPresenter) {
        this.mISysPresenter = iSysPresenter;
    }

    public void uploadFile(final File file) {
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
                        mISysPresenter.onUploadFileProgress(file, readedCount, totalSize);
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
                        mISysPresenter.onUploadFileFailed(ResponseResult.fromThrowable(e));
                    }

                    @Override
                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                        mISysPresenter.onUploadFileSuccess(stringJsonRetEntity.getData());
                    }
                })
        );
    }

    public void performSelectOccupation() {
        addToCycle(
                ConfigUtil
                        .getInstance()
                        .getOrRequireConfig()
                        .subscribe(new Subscriber<ConfigEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mISysPresenter.onGetConfigFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(ConfigEntity configEntity) {
                                mISysPresenter.onGetConfigSuccess(configEntity);
                            }
                        })
        );
    }
/*
    public void config() {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(SysService.class)
                        .config(null)
                        .compose(ApiHelper.<JsonRetEntity<ConfigEntity>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<ConfigEntity>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mISysPresenter.onGetConfigFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ConfigEntity> configEntityJsonRetEntity) {
                                        mISysPresenter.onGetConfigSuccess(configEntityJsonRetEntity.getData());
                                    }
                                }
                        )
        );
    }*/
}
