package com.aibinong.yueaiapi.api;

import android.content.Context;

import com.aibinong.yueaiapi.utils.LocalStorage;
import com.aibinong.yueaiapi.utils.LocalStorageKey;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.MetaDataUtil;
import com.fatalsignal.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author 杨升迁(yourfriendyang@163.com)
 *         <p/>
 *         上午11:24:02 2015年5月20日
 *         <p/>
 *         比如用来生成公共参数
 */
public class ParamsHelper {
    public static final String HEADER_WITH_CLIENTTOKEN = "WITH_CLIENTTOKEN";
    public static final String PARAMS_STUB = "params_stub";
    private static String TAG = ParamsHelper.class.getSimpleName();
    private WeakReference<Context> contextRef;
    private HashMap<String, String> globalParams;
    private static String _smEnvironment;
    private static String _smClientId;

    private ParamsHelper() {
    }

    //	private static class InstanceHolder {
//		static LiuLianAPIHelper instance = new LiuLianAPIHelper();
//	}
    static ParamsHelper instance = new ParamsHelper();

    public static ParamsHelper getInstance() {
        return instance;
    }

    public void init(Context appContext, String clientId, String environment) {
        contextRef = new WeakReference<Context>(appContext);
        _smEnvironment = environment;
        _smClientId = clientId;
        if (globalParams == null) {
            synchronized (ParamsHelper.class) {
                if (globalParams == null) {
                    globalParams = new HashMap<String, String>();

                    globalParams.put("channel", MetaDataUtil.getString(appContext, "UMENG_CHANNEL"));

                    globalParams.put(
                            "device_name",
                            DeviceUtils.getManufacturer() + "-"
                                    + DeviceUtils.getDeviceModel());
                    globalParams.put("size",
                            DeviceUtils.getScreenWidth(appContext) + "x"
                                    + DeviceUtils.getScreenHeight(appContext));
                    globalParams.put("os", "android");
                    globalParams.put("system_version",
                            DeviceUtils.getReleaseVersion());
                    globalParams.put("idfa", DeviceUtils.getDeviceId(appContext));
                    globalParams.put("version", DeviceUtils.getVersionName(appContext) + "");
                    globalParams.put("app_version", DeviceUtils.getVersionCode(appContext) + "");
                    globalParams.put("environment", _smEnvironment);
                    globalParams.put("clientId", _smClientId);
                    globalParams.put("channel", "aliulian");

                    if (DeviceUtils.isNetwotEnable(appContext) == 0) {
                        globalParams.put("app_network", "mobile");// 网络类型 每次不一样
                    } else if (DeviceUtils.isNetwotEnable(appContext) == 1) {
                        globalParams.put("app_network", "wifi");// 网络类型 每次不一样
                    }

                }
            }
        }
    }


    public Map<String, String> getGlobalParamsMap() {
        if (globalParams == null) {
            throw new IllegalAccessError("没有调用 init 方法初始化必要参数！！！");
        }
        globalParams.put("t", System.currentTimeMillis()
                + "");

        Context appContext = contextRef.get();
        if (appContext != null) {
            if (DeviceUtils.isNetwotEnable(appContext) == 0) {
                globalParams.put("app_network", "mobile");// 网络类型 每次不一样
            } else if (DeviceUtils.isNetwotEnable(appContext) == 1) {
                globalParams.put("app_network", "wifi");// 网络类型 每次不一样
            }
        }
        String access_token = UserUtil.getAccessToken();
        if (access_token != null) {
            globalParams.put("accessToken", access_token);
        } else {
            globalParams.remove("accessToken");
        }


        TreeMap<String, String> map = new TreeMap<String, String>(globalParams);
        //过滤空参数
        Iterator<String> keySet = globalParams.keySet().iterator();
        while (keySet.hasNext()) {
            String key = keySet.next();
            if (StringUtils.isEmpty(map.get(key))) {
                map.remove(key);
            }
        }
        return map;
    }

    public void removeCommonParams(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            Map<String, String> commonParams = getGlobalParamsMap();
            Iterator<String> keySet = commonParams.keySet().iterator();
            while (keySet.hasNext()) {
                String key = keySet.next();
                if (!"accessToken".equals(key)) {
                    if (params.containsKey(key)) {
                        params.remove(key);
                    }
                }
            }
            if (params.containsKey("asign")) {
                params.remove("asign");
            }
        }
    }

    /**
     * 获取或生成ticket，如果没有，就生成一个随机字符串。退出登录的时候把它删掉
     *
     * @return
     */
    private String getSavedTicket() {
        String ticket = null;
        ticket = LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_SAVED_TICKET, null);
        if (ticket == null || ticket.length() <= 0) {
            //随机生成一个
            UUID uuid = UUID.randomUUID();
            ticket = uuid.toString();
            LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_SAVED_TICKET, ticket);
        }
        return ticket;
    }


}
