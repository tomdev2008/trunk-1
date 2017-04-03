package com.aibinong.yueaiapi.pojo;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionEntity implements Serializable {

    /**
     * content : 是否接受婚前性行为
     * options : [{"id":"1","content":"能"},{"id":"2","content":"不能"}]
     */

    public String content;
    /**
     * id : 1
     * content : 能
     */

    public ArrayList<OptionsEntity> options;
    public String questionId;

    public static class OptionsEntity implements Serializable {
        public String id;
        public String content;
    }
}
