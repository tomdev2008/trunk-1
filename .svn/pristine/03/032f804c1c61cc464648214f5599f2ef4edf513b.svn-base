package com.aibinong.tantan.util;

import android.content.Context;
import android.content.Intent;

import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.yueaiapi.api.ParamsHelper;
import com.aibinong.yueaiapi.api.interceptor.EncryptUtil;
import com.fatalsignal.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yourfriendyang on 2015/12/30.
 */
public class CommonUtils {

    //    public static boolean showFirstTips = false;
    private static int SEARCHLATELYTSUM = 10;

    public static HashMap<String, String> getArguments(String urlStr) {
        HashMap<String, String> args = new HashMap<>();
        try {
            URL url = new URL(urlStr);
            String query = url.getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (int i = 0; i < pairs.length; i++) {
                    int pos = pairs[i].indexOf('=');
                    if (pos == -1) {
                        continue;
                    }
                    String argname = pairs[i].substring(0, pos);
                    String value = (pairs[i].substring(pos + 1));
                    args.put(argname, value);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return args;
    }

    public static String getCommonPageUrl(String baseUrl, HashMap<String, String> extraParams) {
        HashMap<String, String> queryParams = getArguments(baseUrl);
        int queryStart = baseUrl.indexOf("?");
        String pureUrl = baseUrl;
        if (queryStart >= 0) {
            pureUrl = baseUrl.substring(0, queryStart);
        }
        Map<String, String> params = ParamsHelper.getInstance().getGlobalParamsMap();
        if (queryParams != null) {
            params.putAll(queryParams);
        }
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        EncryptUtil.encrypt(params);
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        StringBuilder sb = new StringBuilder(pureUrl);
        int count = 0;
        for (Map.Entry<String, String> entry : entrySet) {
            if (count != 0) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue()))
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            count++;
        }

        return sb.toString();
    }

    public static boolean showWebActivity(Context activity, String urlStr, boolean spliceParams, HashMap<String, String> extraParams) {
        return showWebActivity(activity, urlStr, spliceParams, extraParams, false);
    }

    public static boolean showWebActivity(Context activity, String urlStr, boolean spliceParams, HashMap<String, String> extraParams, boolean transparent) {
        if (!spliceParams && extraParams != null) {
            throw new IllegalArgumentException("不需要拼参数的时候传入额外参数是无效的");
        }
        if (!StringUtils.isEmpty(urlStr)) {
            try {
                URL url = null;
                if (spliceParams) {
                    url = new URL(getCommonPageUrl(urlStr, extraParams));
                } else {
                    url = new URL(urlStr);
                }
                Intent webIntent = null;
                if (transparent) {
//                    webIntent = new Intent(activity,
//                            TransparentWebActivity.class);
                } else {
                    webIntent = new Intent(activity,
                            CommonWebActivity.class);
                }
                webIntent.putExtra(IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA,
                        url.toString());
                activity.startActivity(webIntent);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
