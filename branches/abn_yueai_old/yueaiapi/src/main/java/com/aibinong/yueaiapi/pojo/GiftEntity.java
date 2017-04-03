package com.aibinong.yueaiapi.pojo;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/5.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import java.io.Serializable;

public class GiftEntity implements Serializable {
    /**
     * id : 1
     * name : 别墅
     * img : http://huoban-social-upload.img-cn-beijing.aliyuncs.com/20161201EBQN8XTW.png
     */

    public String id;
    public String name;
    public String img;
    public int count;

    @Override
    public String toString() {
        return "GiftEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", count=" + count +
                '}';
    }
}
