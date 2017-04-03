package com.aibinong.yueaiapi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fatalsignal.util.PreferenceUtils;

import java.util.Set;


public class LocalStorage {
    private static final String TAG = LocalStorage.class.getSimpleName();


    private Context mContext;

    private LocalStorage() {
    }

    private static class InstanceHolder {
        static LocalStorage instance = new LocalStorage();
    }

    public static LocalStorage getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context appContext) {
        if (mContext == null) {
            synchronized (LocalStorage.class) {
                if (mContext == null) {
                    mContext = appContext;
                }
            }
        }
    }


    public String getString(String key, String defaultValue) {
        try {
            return PreferenceUtils.getPreferences(mContext).getString(key, defaultValue);
        } catch (Exception e) {
            PreferenceUtils.getPreferences(mContext).edit().remove(key).commit();
            return null;
        }
    }

    public Set<String> getStringSet(String key) {
        try {
            return PreferenceUtils.getPreferences(mContext).getStringSet(key, null);
        } catch (Exception e) {
            PreferenceUtils.getPreferences(mContext).edit().remove(key).commit();
            return null;
        }
    }

    public void putStringSet(String key, Set<String> set) {
        getEditor(mContext).putStringSet(key, set).apply();
    }

//	    public  Set<String> getStringSet(String key, Set<String> defaultValue) {
//	        return PreferenceUtils.getPreferences(mContext).getStringSet(key, defaultValue);
//	    }

    public int getInt(String key, int defaultValue) {
        return PreferenceUtils.getPreferences(mContext).getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return PreferenceUtils.getPreferences(mContext).getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return PreferenceUtils.getPreferences(mContext).getFloat(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return PreferenceUtils.getPreferences(mContext).getBoolean(key, defaultValue);
    }

    public SharedPreferences.Editor getEditor(Context context) {
        return PreferenceUtils.getPreferences(mContext).edit();
    }

    public void putString(String key, String value) {
        getEditor(mContext).putString(key, value).apply();
    }

//	    public  void putStringSet(String key, Set<String> value) {
//	        getEditor(mContext).putStringSet(key, value).apply();
//	    }

    public void putInt(String key, int value) {
        getEditor(mContext).putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        getEditor(mContext).putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        getEditor(mContext).putFloat(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        getEditor(mContext).putBoolean(key, value).apply();
    }

    public void remove(String key) {
        getEditor(mContext).remove(key).apply();
    }

    public void clear(Context context) {
        getEditor(mContext).clear().apply();
    }

}
