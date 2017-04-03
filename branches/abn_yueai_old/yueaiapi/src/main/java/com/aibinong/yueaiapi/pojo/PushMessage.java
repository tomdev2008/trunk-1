package com.aibinong.yueaiapi.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class PushMessage implements Serializable {
    public static final int MessageType_BeenLiked = 1;
    public static final int MessageType_PairSuccess = 2;
    public static final int MessageType_CommonMsgSingle = 3;
    public static final int MessageType_CommonMsgGroup = 4;

    public static final int MessageType_LaunchApp = 6;
    public static final int MessageType_Web = 9;
    public static final int MessageType_Pay = 10;
    public static final int MessageType_WhoLikeMe = 11;

    public static PushMessage createVipBuyMessage(String url, String targetId) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.messageType = MessageType_Web;
        pushMessage.data = new CommonData();
        pushMessage.data.url = url;
        pushMessage.data.targetId = targetId;
        pushMessage.isBuyVipEvent = true;
        return pushMessage;
    }

    public static class CommonData implements Serializable {
        public String title;
        public String message;
        public String url;
        public String targetId;
        public String type;
        public UserEntity user;
        public float payMoney;
        public int payCount;
        public ArrayList<PayTypeEntity> payTypes;
    }

    public int messageType;
    public int needLogin;
    public boolean isBuyVipEvent = false;
    public PushMessage.CommonData data;
}
