package com.aibinong.yueaiapi.pojo;


import java.io.Serializable;

/**
 * Created by yourfriendyang on 16/7/12.
 */
public class ConfigItemEntity implements Serializable {
    public static final String SORT_DESC = "desc", SORT_ASC = "asc";
    /**
     * id : 13
     * title : 手机
     * event : {"data":{"url":"http://front.aliulian.com/v1/special/1_speciallshow.html"},"needLogin":0,"messageType":9}
     * img : http://image.aliulian.com/20160708R1CDUME4.jpg
     */

    public String id;
    public String title;
    public String img;
    public PushMessage event;
    public int badage;
    public String description;
    public String sort;

    @Override
    public String toString() {
        return "ConfigItemEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", event=" + event +
                ", badage=" + badage +
                '}';
    }
}
