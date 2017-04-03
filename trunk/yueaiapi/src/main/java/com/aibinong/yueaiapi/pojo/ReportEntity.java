package com.aibinong.yueaiapi.pojo;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/5.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import java.io.Serializable;

public class ReportEntity implements Serializable {
    /**
     /**
     * id : 1
     * content : 虚假资料
     */

    public String id;
    public String content;

    @Override
    public String toString() {
        return "ReportEntity{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
