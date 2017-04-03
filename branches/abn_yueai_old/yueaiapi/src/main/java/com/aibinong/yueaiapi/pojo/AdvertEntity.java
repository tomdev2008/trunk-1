package com.aibinong.yueaiapi.pojo;


import java.io.Serializable;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/7/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|
public class AdvertEntity implements Serializable {

    /**
     * img : http://pic75.nipic.com/file/20150731/21366897_134718145301_2.jpg
     * adId : 1
     * cacheTime : 10
     * event : {"messageType":"9","data":{"url":"www.baidu.com"}}
     */

    public String img;
    public String adId;
    public int cacheTime;
    public PushMessage event;
    public long lastAdvertShowTime;
}
