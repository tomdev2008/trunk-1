package com.aibinong.tantan.pojo.chat;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/25.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.aibinong.yueaiapi.pojo.PushMessage;

import java.io.Serializable;

public class HightLightEntity implements Serializable {
    /**
     * text : aaaa
     * color : #ffffff
     * event : event结构
     */

    public String text;
    public String color;
    public PushMessage event;
}
