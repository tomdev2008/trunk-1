package com.aibinong.tantan.presenter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.QuestionEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.ChatService;
import com.aibinong.yueaiapi.services.SearchService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;

import rx.Subscriber;

public class PairListPresenter extends PresenterBase {
    public interface IPairList {
        void onMachingListStart();

        void onMachingListFailed(Throwable e);

        void onMachingListSuccess(ArrayList<UserEntity> userEntities, Page page);

        void onCancelPairStart();

        void onCancelPairSuccess(String uuid);

        void onCancelPairFailed(Throwable e, String uuid);

        void onReportSuccess();

        void onReportFailed(ResponseResult e);
    }

    private IPairList mIPairList;
    private int mToPage;

    public PairListPresenter(IPairList iPairList) {
        mIPairList = iPairList;
    }

    public void machingList(boolean refresh) {
        if (refresh) {
            mToPage = 1;
        }
        mIPairList.onMachingListStart();
        addToCycle(
                ApiHelper.getInstance()
                        .create(SearchService.class)
                        .matching_list(mToPage)
                        .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIPairList.onMachingListFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                        mIPairList.onMachingListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                                        mToPage++;
                                        for (UserEntity userEntity : arrayListJsonRetEntity.getData()) {
                                            SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
                                        }
                                    }
                                }
                        )
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

    public void cancelPair(final String uuid) {
        mIPairList.onCancelPairStart();
        addToCycle(
                ApiHelper.getInstance()
                        .create(SearchService.class)
                        .sever(uuid)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIPairList.onCancelPairFailed(ResponseResult.fromThrowable(e), uuid);
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIPairList.onCancelPairSuccess(uuid);
                                        sendCancelPairMsg(uuid);
                                    }
                                }
                        )
        );
    }


    public void report(String userId, QuestionEntity.OptionsEntity reson) {
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(ChatService.class)
                        .report(userId, reson.id)
                        .compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain())
                        .subscribe(
                                new Subscriber<JsonRetEntity<String>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mIPairList.onReportFailed(ResponseResult.fromThrowable(e));
                                    }

                                    @Override
                                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {
                                        mIPairList.onReportSuccess();
                                    }
                                }
                        )
        );
    }
}
