package com.aibinong.tantan.ui.adapter.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.view.View;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.widget.ListUserInfoView;
import com.aibinong.yueaiapi.pojo.UserEntity;

public class CommonUserInfoVH extends BaseCommonVH<UserEntity> {


    ListUserInfoView mListuseinfoItemPmlist;
    private UserEntity mUserEntity;

    public CommonUserInfoVH(View itemView) {
        super(itemView);
        mListuseinfoItemPmlist = (ListUserInfoView) itemView.findViewById(R.id.listuseinfo_item_pmlist);
    }

    @Override
    public void bindData(UserEntity data, int itemType) {
        mUserEntity = data;
        mListuseinfoItemPmlist.bindData(mUserEntity, itemType);
    }


}
