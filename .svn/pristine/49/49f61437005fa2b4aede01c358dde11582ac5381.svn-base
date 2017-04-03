package com.aibinong.yueaiapi.pojo;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/5.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import java.io.Serializable;
import java.util.List;

public class ActivityListEntity implements Serializable {


    /**
     * page : {"toPage":"0","totalPage":"1","totalCount":"1"}
     * list : [{"id":"1","icon":"http://image.aliulian.com/heads/default/touxiang-1.jpg","title":"测试","content":"内容内容内容","event":{"messageType":"9","data":{"targetId":"","url":"http://www.baidu.com"}},"sendTime":"1483587900000"}]
     */

    public Page page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 1
         * icon : http://image.aliulian.com/heads/default/touxiang-1.jpg
         * title : 测试
         * content : 内容内容内容
         * event : {"messageType":"9","data":{"targetId":"","url":"http://www.baidu.com"}}
         * sendTime : 1483587900000
         */

        public String id;
        public String icon;
        public String title;
        public String content;
        public EventBean event;
        public String sendTime;


    }

    public static class EventBean {
        /**
         * messageType : 9
         * data : {"targetId":"","url":"http://www.baidu.com"}
         */

        public String messageType;
        public DataBean data;

        public static class DataBean {
            /**
             * targetId :
             * url : http://www.baidu.com
             */
            public String targetId;
            public String url;

        }
    }


}
