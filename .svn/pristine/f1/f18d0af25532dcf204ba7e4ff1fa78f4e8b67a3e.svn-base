package com.aibinong.tantan.presenter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.apiInterface.ISearchList;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;

import rx.Subscriber;

public class FollowListPresenter extends PresenterBase {

    private int mToPage;



    public void follow_list(final ISearchList iSearchList, boolean refresh) {
        if (refresh) {
            mToPage = 1;
        }
        addToCycle(
                ApiHelper
                        .getInstance()
                        .follow_list(mToPage)
                        .subscribe(new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                iSearchList.onListStart();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                iSearchList.onListFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                iSearchList.onListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                                UserUtil.saveUsers(arrayListJsonRetEntity.getData());
                                mToPage++;
                            }
                        })
        );
    }

    private void sendCancelPairMsg(String uuid) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        String action = "revocation";//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setReceipt(uuid);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute(EMessageConstant.CMD_EXT_TYPE, EMessageConstant.CMD_EXT_TYPE_CANCEL_PAIR);
        cmdMsg.setAttribute(EMessageConstant.KEY_CMD_EXT_targetId, uuid);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }
}
