package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/10.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.LoginRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.ProfileService;

import rx.Subscriber;

public class UpdateUserPresenter extends PresenterBase {
    public interface IUpdateUser {
        void onUpdateUserFailed(ResponseResult e);

        void onUpdateUserSuccess(UserEntity userEntity);

    }

    private IUpdateUser mIUpdateUser;

    public UpdateUserPresenter(IUpdateUser iUpdateUser) {
        mIUpdateUser = iUpdateUser;
    }

    public void updateUser(Iterable<String> pictureList, String nickname, String birthdate, String occupation, Iterable<String> tags, String declaration) {
        String uploadPicStr = null;
        StringBuilder pictureBuilder = new StringBuilder();
        if (pictureList != null) {
            for (String url : pictureList) {
                pictureBuilder.append(url).append(',');
            }
            if (pictureBuilder.length() > 0) {
                pictureBuilder.deleteCharAt(pictureBuilder.length() - 1);
            }
            uploadPicStr = pictureBuilder.toString();
        }

        String tagsStr = null;
        StringBuilder tagsStrBuilder = new StringBuilder();
        if (tags != null) {
            for (String url : tags) {
                tagsStrBuilder.append(url).append(',');
            }
            if (tagsStrBuilder.length() > 0) {
                tagsStrBuilder.deleteCharAt(tagsStrBuilder.length() - 1);
            }
            tagsStr = tagsStrBuilder.toString();
        }

        addToCycle(ApiHelper.getInstance()
                .create(ProfileService.class)
                .update_user(uploadPicStr, nickname, birthdate, occupation, tagsStr, declaration)
                .compose(ApiHelper.<JsonRetEntity<LoginRetEntity>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<LoginRetEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIUpdateUser.onUpdateUserFailed(ResponseResult.fromThrowable(e));
                    }

                    @Override
                    public void onNext(JsonRetEntity<LoginRetEntity> loginRetEntityJsonRetEntity) {
                        mIUpdateUser.onUpdateUserSuccess(loginRetEntityJsonRetEntity.getData().user);
                    }
                })
        );
    }

}
