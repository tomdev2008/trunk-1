package com.aibinong.yueaiapi.api.interceptor;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/21.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import com.fatalsignal.util.MD5Util;

import java.util.Iterator;
import java.util.Map;

public class EncryptUtil {
    public static void encrypt(Map<String, String> params) {
        //过滤空参数
//        Iterator<String> keySet = params.keySet().iterator();
//        while (keySet.hasNext()) {
//            String key = keySet.next();
//            if (TextUtils.isEmpty(params.get(key))) {
//                params.remove(key);
//            }
//        }
        StringBuilder javaSignSb = new StringBuilder();
        Iterator<Map.Entry<String, String>> entryIterator1 = params.entrySet().iterator();
        while (entryIterator1.hasNext()) {
            Map.Entry<String, String> entry = entryIterator1.next();
            if (entry.getValue() != null && entry.getKey() != null) {
                javaSignSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        javaSignSb.append("snow2LDiaMsH");
        String javaSignOriStr = javaSignSb.toString();
        String javaSign = MD5Util.MD5Encode(javaSignSb.toString().getBytes());

        //对参数加密
//                EncryptUtil.getInstance().encryptParams(globalParams);
        params.put("asign", javaSign);
    }
}
