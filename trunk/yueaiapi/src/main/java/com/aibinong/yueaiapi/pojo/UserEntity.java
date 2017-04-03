package com.aibinong.yueaiapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public class UserEntity implements Serializable {

    /**
     * id : a8366b30-9f44-11e6-aed4-65d31730540f
     * nickname : xpg
     * sex : 0
     * age : 25
     * constellation : 处女座
     * portrait : http://b.hiphotos.baidu.com/image/pic/item/d009b3de9c82d15825ffd75c840a19d8bd3e42da.jpg
     * pictureList : http://b.hiphotos.baidu.com/image/pic/item/d009b3de9c82d15825ffd75c840a19d8bd3e42da.jpg,http://a.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb3a78c787fdd33c895d0430c44.jpg,http://b.hiphotos.baidu.com/image/pic/item/a686c9177f3e670900d880193fc79f3df9dc5578.jpg,http://f.hiphotos.baidu.com/image/pic/item/3b87e950352ac65cad5ff279f9f2b21193138a66.jpg
     * pictureCount : 4
     * atomization : 0
     * messageLevel : 0
     * occupation : programmer
     * tags : 1,2,3
     * commonTags :
     * distance : 1km
     * activeTime : 2016-10-31 19:04:18
     */
    public static final int SEX_MALE = 1, SEX_FEMALE = 0;
    @SerializedName(value = "id", alternate = {"uuid"})
    public String id;
    public String password;
    public String nickname;
    public int sex;
    public int age;
    public String constellation;
    public ArrayList<String> pictureList;
    public String pictureCount;
    //    public int atomization;
//    public int messageLevel;
    public String occupation;
    public ArrayList<String> tags;
    public ArrayList<String> commons;
    public String distance;
    public String activeTime;
    public String followTime;
    //    public String matchingTime;
    public String birthdate;
    public int status;
//    public int helper;//是否小助手
    /**
     * memberLevel : 0
     * memberDate :
     */
    public int memberLevel;
    public String memberDate;
    //    public String signIcon;
    @SerializedName(value = "freeTimes", alternate = {"freeChat"})
    public int freeTimes = Integer.MAX_VALUE;
    public String declaration;

    /**
     * fans : 0
     */

    public int fans;
    public String area;
    public int follow;//是否已经关注
    public CertEntity cert;

    public boolean isVIP() {
        if (cert != null && "1".equals(cert.vip)) {
            return true;
        }
        return false;
    }

    public String getFirstPicture() {
        if (pictureList != null && pictureList.size() > 0) {
            return pictureList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", constellation='" + constellation + '\'' +
                ", pictureList=" + pictureList +
                ", pictureCount='" + pictureCount + '\'' +
                ", occupation='" + occupation + '\'' +
                ", tags=" + tags +
                ", commons=" + commons +
                ", distance='" + distance + '\'' +
                ", activeTime='" + activeTime + '\'' +
                ", followTime='" + followTime + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", status=" + status +
                ", memberLevel=" + memberLevel +
                ", memberDate='" + memberDate + '\'' +
                ", freeTimes=" + freeTimes +
                ", fans=" + fans +
                '}';
    }
}
