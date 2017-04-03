package com.aibinong.yueaiapi.pojo;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/15.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import java.io.Serializable;

public class GiftDanMuData implements Serializable {
    public GiftEntity gift;
    public UserEntity user;
    public UserEntity target;

    @Override
    public String toString() {
        return "GiftDanMuData{" +
                "gift=" + gift +
                ", user=" + user +
                ", target=" + target +
                '}';
    }
}
