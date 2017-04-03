package com.aibinong.tantan.events;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class BaseEvent<T> {
    public static final String ACTION_SAYHI_START = "ACTION_SAYHI_START";//打招呼开始
    public static final String ACTION_SAYHI_SUCCESS = "ACTION_SAYHI_SUCCESS";//打招呼成功
    public static final String ACTION_SAYHI_FAILED = "ACTION_SAYHI_FAILED";//打招呼失败
    public static final String ACTION_ASK_FOR_LOGIN = "ACTION_ASK_FOR_LOGIN";//询问是否登录
    public static final String ACTION_ASK_FOR_BUY_VIP = "ACTION_ASK_FOR_BUY_VIP";//询问是否登录


    public static final String ACTION_FOLLOW_START = "ACTION_FOLLOW_START";
    public static final String ACTION_FOLLOW_SUCCESS = "ACTION_FOLLOW_SUCCESS";
    public static final String ACTION_FOLLOW_FAILED = "ACTION_FOLLOW_FAILED";

    public static final String ACTION_UNFOLLOW_START = "ACTION_UNFOLLOW_START";
    public static final String ACTION_UNFOLLOW_SUCCESS = "ACTION_UNFOLLOW_SUCCESS";
    public static final String ACTION_UNFOLLOW_FAILED = "ACTION_UNFOLLOW_FAILED";

    public static final String ACTION_SYS_MESSAGE = "ACTION_SYS_MESSAGE";//系统消息

    public BaseEvent(String action, T data) {
        this.action = action;
        this.data = data;
    }

    public String action;
    public T data;
}
