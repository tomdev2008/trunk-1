package com.aibinong.tantan.presenter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.util.Pair;

import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.ProfileService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class PMListPresenter extends PresenterBase {
    public interface IPMListPresenter {
        void onListFailed(Throwable e);

        void onListSuccess(ArrayList<UserEntity> userEntities, Page page);
    }

    private IPMListPresenter mIpmListPresenter;

    public void setIpmListPresenter(IPMListPresenter ipmListPresenter) {
        this.mIpmListPresenter = ipmListPresenter;
    }

    private int mPage_PMList = 1;

    public void loadPMList(boolean refresh) {
        Observable
                .create(new Observable.OnSubscribe<List<EMConversation>>() {
                    @Override
                    public void call(Subscriber<? super List<EMConversation>> subscriber) {
                        List<EMConversation> allConversations = loadConversationList();
                        subscriber.onNext(allConversations);
                        subscriber.onCompleted();
                    }
                })
                .compose(ApiHelper.<List<EMConversation>>doIoObserveMain())
                .subscribe(new Subscriber<List<EMConversation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<EMConversation> emConversations) {
                        if (emConversations == null || emConversations.size() <= 0) {
                            mIpmListPresenter.onListFailed(new ResponseResult(-1, "暂时没有任何数据"));
                        } else {

                            StringBuilder idsBuilder = new StringBuilder();
                            for (EMConversation conversation : emConversations) {
                                idsBuilder.append(conversation.getUserName()).append(',');
                            }
                            if (idsBuilder.length() > 1) {
                                idsBuilder.deleteCharAt(idsBuilder.length() - 1);
                            }
                            addToCycle(
                                    ApiHelper
                                            .getInstance()
                                            .create(ProfileService.class)
                                            .get_users(idsBuilder.toString())
                                            .compose(ApiHelper.<JsonRetEntity<ArrayList<UserEntity>>>doIoObserveMain())
                                            .subscribe(
                                                    new Subscriber<JsonRetEntity<ArrayList<UserEntity>>>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            mIpmListPresenter.onListFailed(ResponseResult.fromThrowable(e));
                                                        }

                                                        @Override
                                                        public void onNext(JsonRetEntity<ArrayList<UserEntity>> arrayListJsonRetEntity) {
                                                            mIpmListPresenter.onListSuccess(arrayListJsonRetEntity.getData(), arrayListJsonRetEntity.getPage());
                                                            for (UserEntity userEntity : arrayListJsonRetEntity.getData()) {
                                                                SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
                                                            }
                                                        }
                                                    }
                                            )
                            );
                        }

                    }
                })
        ;
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
