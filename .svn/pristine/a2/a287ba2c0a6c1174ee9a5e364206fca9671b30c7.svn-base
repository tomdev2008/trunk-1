package com.aibinong.tantan.constant;

import android.content.Context;

import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.MD5Util;

import java.io.File;

/**
 * @author yourfriendyang
 */

/**
 * @author yourfriendyang
 */
public class Constant {

    private static final String TAG = "Constant";
    public static String cachePath;

    /**
     * 对Constant里面的对象初始化
     *
     * @param context
     */
    public static void init(Context context) {
        // 初始化文件和图片的存放路径
        long freeSize = DeviceUtils.readSDCard(0);
        if (freeSize == -1) {
            // Toast.makeText(context, "未检测到SD卡，将无法下载软件", Toast.LENGTH_SHORT)
            // .show();
        }
        // else
        {
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile == null) {
                cacheFile = context.getCacheDir();
            }
            if (cacheFile != null) {
                // if (freeSize < 50L) {
                // Toast.makeText(context, "SD可用空间不足50MB，请及时清理垃圾文件",
                // Toast.LENGTH_LONG).show();
                // }
                cachePath = cacheFile.getAbsolutePath() + "/";
                File cacheDir = new File(cachePath);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }

            }
        }
    }

    public static String url2Local(String url) {
        String ret = Constant.cachePath + MD5Util.MD5Encode(url, "utf-8")
                + url.substring(url.lastIndexOf("."));
        return ret;
    }

    public static String url2fileName(String url) {
        String ret = MD5Util.MD5Encode(url, "utf-8")
                + url.substring(url.lastIndexOf("."));
        return ret;
    }
    public static final String _sUrl_vip_buy = "http://yueai-app.oss-cn-beijing.aliyuncs.com/member/index.html";
    public static final String _sUrl_agreement = "http://h5.imchumo.com/lp-h5-msc/mj/service.html";
    public static final String _sUrl_help = "http://yueai-app.oss-cn-beijing.aliyuncs.com/lead/index.html";
    public static final String _sUrl_gift_buy = "http://yueai-app.oss-cn-beijing.aliyuncs.com/gift/index.html";
    public static final float IMAGE_CARD_RATIO_WH = 1.0f;
    public static final float IMAGE_MINE_RATIO_WH = 750.0f / 540.0f;

    public static String registerMsg;
    public static String newnovicePacks;
    public static final int maxShowAge = 50;
    public static final int minShowAge = 18;
}
