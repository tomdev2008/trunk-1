package com.fatalsignal.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yourfriendyang on 16/1/5.
 */
public class MyToast {
    private static Toast mToast;
    private static Context mContext;

    public static Toast makeToast(Context context, String msg) {
        if (MyToast.mContext == context) {
            mToast.cancel();
            mToast.setText(msg);
        } else {
            MyToast.mContext = context;
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        return mToast;
    }
}
