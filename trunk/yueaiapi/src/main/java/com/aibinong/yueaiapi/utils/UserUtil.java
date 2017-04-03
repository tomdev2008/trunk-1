package com.aibinong.yueaiapi.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.db.SqlBriteUtil;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.user.LoginService;
import com.aibinong.yueaiapi.services.user.ProfileService;
import com.fatalsignal.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by yourfriendyang on 16/7/11.
 */
public class UserUtil {
    /**
     * home页是否展示过引导页
     * @param isguide
     */
    public static void saveHomeGudieShow(boolean isguide) {
        LocalStorage.getInstance().putBoolean(
                LocalStorageKey.KEY_STORAGE_HOME_GUIDE_SHOW, isguide);
    }

    /**
     * 关注页是否展示过引导页
     * @param isguide
     */
    public static void saveFllowGudieShow(boolean isguide) {
        LocalStorage.getInstance().putBoolean(
                LocalStorageKey.KEY_STORAGE_FLLOW_GUIDE_SHOW, isguide);
    }
    /**
     * 礼物页是否展示过引导页
     * @param isguide
     */
    public static void saveGiftGudieShow(boolean isguide) {
        LocalStorage.getInstance().putBoolean(
                LocalStorageKey.KEY_STORAGE_GIFT_GUIDE_SHOW, isguide);
    }
    /**
     * 打招呼页是否展示过引导页
     * @param isguide
     */
    public static void saveSayHiGudieShow(boolean isguide) {
        LocalStorage.getInstance().putBoolean(
                LocalStorageKey.KEY_STORAGE_SAYHI_GUIDE_SHOW, isguide);
    }
    /**
     * 用户登录过
     * @param isLogin
     */
    public static void saveLoginState(boolean isLogin) {
        LocalStorage.getInstance().putBoolean(
                LocalStorageKey.KEY_STORAGE_LOGIN_STATE, isLogin);
    }
    /**
     * 用户登录过
     * @param
     */
    public static boolean getLoginState() {
        boolean aBoolean = LocalStorage.getInstance().getBoolean(
                LocalStorageKey.KEY_STORAGE_LOGIN_STATE, false);
        return aBoolean;

    }
    public static void saveUserInfo(UserEntity info) {
        Gson gson = new GsonBuilder().create();
        String userStr = gson.toJson(info);
        LocalStorage.getInstance().putString(
                LocalStorageKey.KEY_STORAGE_SAVED_USERINFO, userStr);
    }

    public static UserEntity getSavedUserInfoNotNull() {
        UserEntity info = null;
        info = getSavedUserInfo();
        if (info == null) {
            info = new UserEntity();
        }
        return info;
    }

    public static UserEntity getSavedUserInfo() {
        String userStr = LocalStorage.getInstance().getString(
                LocalStorageKey.KEY_STORAGE_SAVED_USERINFO, null);
        UserEntity info = null;
        if (userStr != null) {
            Gson gson = new GsonBuilder().create();
            info = gson.fromJson(userStr, UserEntity.class);
        }
        return info;
    }

    public static boolean hasSayHiToVipPer() {
        boolean per = false;
        UserEntity userEntity = getSavedUserInfoNotNull();
        if (userEntity.sex == UserEntity.SEX_FEMALE) {
            per = true;
        } else {
            if (userEntity.memberLevel >= 3) {
                per = true;
            }
        }
        return per;
    }

    public static boolean hasChatToVipPer() {
        boolean per = false;
        UserEntity userEntity = getSavedUserInfoNotNull();
        if (userEntity.sex == UserEntity.SEX_FEMALE) {
            per = true;
        } else {
            if (userEntity.memberLevel > 0) {
                per = true;
            }
        }
        return per;
    }

    public static void deleteSavedUserInfo() {
        LocalStorage.getInstance().remove(
                LocalStorageKey.KEY_STORAGE_SAVED_USERINFO);
    }


    public static void bindPushId() {

        final String pushId = ConfigUtil.getInstance().getClientID();

        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        //登录成功,绑定pushid
        ApiHelper.getInstance()
                .create(LoginService.class)
                .bind_push_id(pushId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonRetEntity<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JsonRetEntity<String> stringJsonRetEntity) {

                    }
                });
    }

    public static void saveAccessToken(String access_token) {
        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_ACCESS_TOKEN, access_token);
    }

    /*清除登录信息*/
    public static void clearLoginInfo() {
        deleteAccessToken();
        deleteSavedHuanxinPwd();
        deleteSavedUUID();
        NotificationManager notificationManager = (NotificationManager) ApiHelper.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static Observable<JsonRetEntity<String>> logout() {
        return ApiHelper.getInstance().create(ProfileService.class).logout(null).compose(ApiHelper.<JsonRetEntity<String>>doIoObserveMain());
    }

    /*uuid,huanxinPwd,accessToken缺一不可*/
    public static String isLoginValid(boolean autoClear) {
        String accessToken = getAccessToken();
        String uuid = getSavedUUID();
        String huanxinPwd = getSavedHuanxinPwd();
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(uuid) || StringUtils.isEmpty(huanxinPwd)) {
            if (autoClear) {
                clearLoginInfo();
            }
            return null;
        }
        return accessToken;
    }

    public static String getAccessToken() {
        String access_token = LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_ACCESS_TOKEN, null);
        if (StringUtils.isEmpty(access_token)) {
            deleteAccessToken();
            return null;
        }
        return access_token;
    }

    public static void deleteAccessToken() {
        LocalStorage.getInstance().remove(LocalStorageKey.KEY_STORAGE_ACCESS_TOKEN);
    }

    public static void saveUUID(String uuid) {
        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_UUID, uuid);
    }

    public static String getSavedUUID() {
        String uuid = LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_UUID, null);
        return uuid;
    }

    public static void deleteSavedUUID() {
        LocalStorage.getInstance().remove(LocalStorageKey.KEY_STORAGE_UUID);
    }


    public static void saveHuanxinPwd(String uuid) {
        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_HUANXIN_PWD, uuid);
    }

    public static String getSavedHuanxinPwd() {
        String uuid = LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_HUANXIN_PWD, null);
        return uuid;
    }

    public static void deleteSavedHuanxinPwd() {
        LocalStorage.getInstance().remove(LocalStorageKey.KEY_STORAGE_HUANXIN_PWD);
    }


    private static Runnable ensureLoginRunnable;
    public static final int REQUEST_CODE_ENSURE_LOGIN = 0x1024;

    /**
     * 是否已经登录，不是的话跳转到登录界面
     *
     * @param activity
     * @param runnable
     * @return
     */
    public static boolean ensureLoginAuth(final Activity activity,
                                          final Runnable runnable, final boolean autoContinue) {

        if (isLoginValid(true) != null) {
            if (runnable != null) {
                runnable.run();
            }
            ensureLoginRunnable = null;
            return true;
        } else {
            // 显示对话框
            if (activity != null) {

                Intent intent = new Intent("com.aibinong.yueai.activity.login");
                if (autoContinue) {
                    ensureLoginRunnable = runnable;
                    activity.startActivityForResult(
                            intent,
                            REQUEST_CODE_ENSURE_LOGIN);
                } else {
                    ensureLoginRunnable = null;
                    activity.startActivity(intent);
                }
            }
        }

        return false;
    }

    public static void handleLoginActivityResult(Activity activity, int requestCode,
                                                 int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ENSURE_LOGIN) {
                // 检查一下登录结果
                if (getAccessToken() != null) {
                    // 登录成功
                    ensureLoginAuth(activity, ensureLoginRunnable, false);
                    return;
                }
            }
        }
        ensureLoginRunnable = null;
    }

    public static void saveUsers(final ArrayList<UserEntity> userEntities) {
        if (userEntities == null || userEntities.size() <= 0) {
            return;
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                for (UserEntity userEntity : userEntities) {
                    SqlBriteUtil.getInstance().getUserDb().saveUser(userEntity);
                }
            }
        }).compose(ApiHelper.doIoObserveMain()).subscribe(
                new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }
        );

    }

    public static boolean isHelper(UserEntity userEntity) {
        boolean isHelper = false;
        if (ConfigUtil.getInstance().getConfig() != null && userEntity != null && userEntity.id != null&&ConfigUtil.getInstance().getConfig().helper!=null) {
            if (userEntity.id.equals(ConfigUtil.getInstance().getConfig().helper.id)) {
                isHelper = true;
            }
        }
        return isHelper;
    }

    public static void saveUserPhoe(String mobile, String verifyCode) {
        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_USER_MOBILE, mobile);
    }
}
