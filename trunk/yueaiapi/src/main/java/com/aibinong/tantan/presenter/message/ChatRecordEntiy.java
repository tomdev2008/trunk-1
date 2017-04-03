package com.aibinong.tantan.presenter.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hubin on 2017/3/15.
 */

public class ChatRecordEntiy implements Serializable {

    /**
     * type : chatmessage
     * from : zw123
     * msg_id : 5I02W-16-8278a
     * chat_type : chat
     * timestamp : 1403099033211
     * to : 1402541206787
     */

    private String type;
    private String from;
    private String msg_id;
    private String chat_type;
    private long timestamp;
    private String to;
    private PayLoad payLoad;
    public PayLoad getPayLoad() {
        return payLoad;
    }
    public void setPayLoad(PayLoad payLoad) {
        this.payLoad = payLoad;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    private class PayLoad {

        /**
         * bodies : [{"msg":"hhhhhh","type":"txt","length":3,"url":"","filename":"22.png","secret":"pCY80PdfEeO4Jh9URCOfMQWU9QYsJytynu4n-yhtvAhmt1g9","lat":39.983805,"lng":116.307417,"addr":"北京市海淀区北四环西路66号"}]
         * ext : {"key1":"value1"}
         */

        private ExtBean ext;
        private List<BodiesBean> bodies;

        public ExtBean getExt() {
            return ext;
        }

        public void setExt(ExtBean ext) {
            this.ext = ext;
        }

        public List<BodiesBean> getBodies() {
            return bodies;
        }

        public void setBodies(List<BodiesBean> bodies) {
            this.bodies = bodies;
        }

        public class ExtBean {
            /**
             * key1 : value1
             */

            private String key1;

            public String getKey1() {
                return key1;
            }

            public void setKey1(String key1) {
                this.key1 = key1;
            }
        }

        public class BodiesBean {
            /**
             * msg : hhhhhh
             * type : txt
             * length : 3
             * url :
             * filename : 22.png
             * secret : pCY80PdfEeO4Jh9URCOfMQWU9QYsJytynu4n-yhtvAhmt1g9
             * lat : 39.983805
             * lng : 116.307417
             * addr : 北京市海淀区北四环西路66号
             */

            private String msg;
            private String type;
            private int length;
            private String url;
            private String filename;
            private String secret;
            private double lat;
            private double lng;
            private String addr;

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }
        }
    }
}
