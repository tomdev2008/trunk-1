package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.util.ArrayList;

import rx.Subscriber;

public class TagsEditPresenter extends PresenterBase {
    public interface ITagsEdit {
        void onLoadAllTagsFailed(ResponseResult e);

        void onLoadAllTagsSuccess(ArrayList<String> tags);
    }

    private ITagsEdit mITagsEdit;
    private int mSex;

    public TagsEditPresenter(ITagsEdit iTagsEdit, int sex) {
        mITagsEdit = iTagsEdit;
        mSex = sex;
    }

    public void loadAllTags(final Iterable<String> excluedTags) {
        addToCycle(
                ConfigUtil.getInstance().getOrRequireConfig().subscribe(
                        new Subscriber<ConfigEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mITagsEdit.onLoadAllTagsFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(ConfigEntity configEntity) {
                                ArrayList<String> allTags = new ArrayList<String>();
                                if (configEntity.tags != null) {
                                    if (configEntity.tags.common != null) {
                                        allTags.addAll(configEntity.tags.common);
                                    }
                                    if (mSex == UserEntity.SEX_FEMALE) {
                                        if (configEntity.tags.female != null) {
                                            allTags.addAll(configEntity.tags.female);
                                        }
                                    } else {
                                        if (configEntity.tags.male != null) {
                                            allTags.addAll(configEntity.tags.male);
                                        }
                                    }
                                }

                                if (allTags != null && allTags.size() > 0) {
                                    ArrayList<String> tags2Delete = new ArrayList<String>(allTags.size());
                                    for (String tag : allTags) {
                                        for (String excludeTag : excluedTags) {
                                            if (tag.equals(excludeTag)) {
                                                tags2Delete.add(tag);
                                            }
                                        }
                                    }
                                    allTags.removeAll(tags2Delete);
                                }

                                mITagsEdit.onLoadAllTagsSuccess(allTags);
                            }
                        }
                )
        );
    }
}
