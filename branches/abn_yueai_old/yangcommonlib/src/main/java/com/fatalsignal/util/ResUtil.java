package com.fatalsignal.util;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

public class ResUtil {

    public static int getId(String resPackageName, String className, String name) {
        try {
            Class localClass = Class.forName(resPackageName + ".R$" + className);
            Field localField = localClass.getField(name);
            int i = Integer.parseInt(localField.get(localField.getName()).toString());
            return i;
        } catch (Exception localException) {
            Log.e("getIdByReflection error", localException.getMessage());
        }

        return 0;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }

    public static int dp2px(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale);
    }
}
