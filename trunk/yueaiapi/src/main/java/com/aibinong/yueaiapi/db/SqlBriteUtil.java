package com.aibinong.yueaiapi.db;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/23.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;

public class SqlBriteUtil {
    private static SqlBriteUtil _smSqlBriteUtil=new SqlBriteUtil();
    private Context mContext;
    private LocalUsersHelper mLocalUsersHelper;
    public static SqlBriteUtil getInstance() {
        return _smSqlBriteUtil;
    }

    private SqlBriteUtil() {
    }

    public void init(Context context) {
        mContext = context;
    }

    public LocalUsersHelper getUserDb() {
        if (mLocalUsersHelper == null) {
            mLocalUsersHelper = new LocalUsersHelper(mContext);
        }
        return mLocalUsersHelper;
    }

}
