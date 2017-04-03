package com.aibinong.yueaiapi.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class PushMessage implements Serializable {
    public static final int MessageType_UserDetail = 1;//个人详情页
    public static final int MessageType_FansList = 2;//谁关注了我
    public static final int MessageType_BrowserList = 3;//谁看过我

    public static final int MessageType_LaunchApp = 6;
    public static final int MessageType_Web = 9;
    public static final int MessageType_Pay = 10;

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

        @Override
        public String toString() {
            return "CommonData{" +
                    "title='" + title + '\'' +
                    ", message='" + message + '\'' +
                    ", url='" + url + '\'' +
                    ", targetId='" + targetId + '\'' +
                    ", type='" + type + '\'' +
                    ", user=" + user +
                    ", payMoney=" + payMoney +
                    ", payCount=" + payCount +
                    ", payTypes=" + payTypes +
                    '}';
        }
    }

    public int messageType;
    public int needLogin;
    public boolean isBuyVipEvent = false;
    public PushMessage.CommonData data;

    @Override
    public String toString() {
        return "PushMessage{" +
                "messageType=" + messageType +
                ", needLogin=" + needLogin +
                ", isBuyVipEvent=" + isBuyVipEvent +
                ", data=" + data +
                '}';
    }
}
