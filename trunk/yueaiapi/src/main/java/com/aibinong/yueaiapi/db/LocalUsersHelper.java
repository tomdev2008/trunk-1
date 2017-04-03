package com.aibinong.yueaiapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.aibinong.yueaiapi.pojo.DbUserEntity;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.fatalsignal.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by yourfriendyang on 16/3/26.
 */
public class LocalUsersHelper extends SQLiteOpenHelper {
    private static final String db_name = "cached_users";
    private static final String TABLE_NAME_USER = "users";
    private static final String TABLE_NAME_SAYHI = "sayhi";
    private static final String COLUMN_localId = "userId";
    private static final String COLUMN_saiedHi = "saiedHi";//是否打过招呼
    private static final String COLUMN_entityJsonStr = "entityJsonStr";
    private static final String COLUMN_idmatch = "idmatch";  //打招呼字符串拼接

    private static final String CREATE_TABLE__USER_STR = "create table if not exists "
            + TABLE_NAME_USER + "("
            + COLUMN_localId + " TEXT PRIMARY KEY,"
            + COLUMN_saiedHi + " INTEGER,"
            + COLUMN_entityJsonStr + " TEXT"
            + ")";
    private static final String CREATE_TABLE__SAYHI_STR = "create table if not exists "
            + TABLE_NAME_SAYHI + "("
            + COLUMN_localId + " TEXT PRIMARY KEY,"
            + COLUMN_saiedHi + " INTEGER,"
            + COLUMN_idmatch + " TEXT"
            + ")";
    private Gson gson;

    public LocalUsersHelper(Context context) {
        super(context, db_name, null, 4);
        gson = new GsonBuilder().create();
    }

    public synchronized long deleteUser(String userId) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            result = db.delete(TABLE_NAME_USER, COLUMN_localId + "=?", new String[]{userId});
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public synchronized long saveUser(UserEntity userEntity) {
        DbUserEntity dbUserEntity = getDbUsers(userEntity.id);

        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, userEntity.id);
            cvs.put(COLUMN_entityJsonStr, gson.toJson(userEntity));
            if (dbUserEntity != null) {
                cvs.put(COLUMN_saiedHi, dbUserEntity.saiedHi);
            }

            result = db.replace(TABLE_NAME_USER, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public synchronized UserEntity getUsers(String uid) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        UserEntity UserEntity = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME_USER, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
            while (cursor.moveToNext()) {
                String jsonstr = cursor.getString(cursor.getColumnIndex(COLUMN_entityJsonStr));
                UserEntity = gson.fromJson(jsonstr, UserEntity.class);
                break;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return UserEntity;
    }

    public synchronized DbUserEntity getDbUsers(String uid) {
        if (uid == null) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        DbUserEntity dbUserEntity = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME_USER, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
            while (cursor.moveToNext()) {
                dbUserEntity = new DbUserEntity();
                dbUserEntity.userId = uid;

                int saiedHi = cursor.getInt(cursor.getColumnIndex(COLUMN_saiedHi));
                String jsonstr = cursor.getString(cursor.getColumnIndex(COLUMN_entityJsonStr));
                dbUserEntity.saiedHi = saiedHi;
                dbUserEntity.userEntity = gson.fromJson(jsonstr, UserEntity.class);
                break;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return dbUserEntity;
    }

    public synchronized long saveDbUser(DbUserEntity userEntity) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, userEntity.userId);
            cvs.put(COLUMN_entityJsonStr, gson.toJson(userEntity.userEntity));
            cvs.put(COLUMN_saiedHi, userEntity.saiedHi);
            result = db.replace(TABLE_NAME_USER, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }
//
//    public boolean isClickedFromLikeMe(String uid) {
//        DbUserEntity dbUserEntity = getDbUsers(uid);
//        if (dbUserEntity != null && dbUserEntity.clickedFrmLikeMe == 1) {
//            return true;
//        }
//        return false;
//    }

    public synchronized boolean isSaiedHi(String uid) {
        DbUserEntity dbUserEntity = getNewDbUsers(uid);
        if (dbUserEntity != null && dbUserEntity.saiedHi == 1 ) {
            return true;
        }
        return false;
    }
//
//    public boolean isClickedFromPairList(String uid) {
//        DbUserEntity dbUserEntity = getDbUsers(uid);
//        if (dbUserEntity != null && dbUserEntity.clickedFromPairList == 1) {
//            return true;
//        }
//        return false;
//    }
//
//    public long markClikedFromLikeMe(String uid) {
//        DbUserEntity dbUserEntity = getDbUsers(uid);
//        if (dbUserEntity == null) {
//            dbUserEntity = new DbUserEntity();
//            dbUserEntity.userId = uid;
//        }
//        dbUserEntity.clickedFrmLikeMe = 1;
//        long result = saveDbUser(dbUserEntity);
//        return result;
//    }
//
//
//    public long markClikedFromPairList(String uid) {
//        DbUserEntity dbUserEntity = getDbUsers(uid);
//        if (dbUserEntity == null) {
//            dbUserEntity = new DbUserEntity();
//            dbUserEntity.userId = uid;
//        }
//        dbUserEntity.clickedFromPairList = 1;
//        long result = saveDbUser(dbUserEntity);
//        return result;
//    }

    public synchronized long markSaiedHi(String uid) {
        DbUserEntity dbUserEntity = getDbUsers(uid);
        if (dbUserEntity == null) {
            dbUserEntity = new DbUserEntity();
            dbUserEntity.userId = uid;
        }
        dbUserEntity.saiedHi = 1;
        long result = saveDbUser(dbUserEntity);
        return result;
    }
    public synchronized long markNoSaiedHi(String uid) {
        DbUserEntity dbUserEntity = getDbUsers(uid);
        if (dbUserEntity == null) {
            dbUserEntity = new DbUserEntity();
            dbUserEntity.userId = uid;
        }
        dbUserEntity.saiedHi = 0;
        long result = saveDbUser(dbUserEntity);
        return result;
    }
    public synchronized ArrayList<UserEntity> getAllUsers() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<UserEntity> goodsScenicArrayList = new ArrayList<>();
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME_USER, null, null, null, null, null, COLUMN_localId + " DESC");
            while (cursor.moveToNext()) {
                String jsonstr = cursor.getString(cursor.getColumnIndex(COLUMN_entityJsonStr));
                if (!StringUtils.isEmpty(jsonstr)) {
                    UserEntity goods = gson.fromJson(jsonstr, UserEntity.class);
                    if (!StringUtils.isEmpty(goods.id)) {
                        goodsScenicArrayList.add(goods);
                    }
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return goodsScenicArrayList;
    }








    public synchronized DbUserEntity getNewDbUsers(String uid) {
        if (uid == null) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        DbUserEntity dbUserEntity = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME_SAYHI, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
            while (cursor.moveToNext()) {
                dbUserEntity = new DbUserEntity();
                dbUserEntity.userId = uid;
                int saiedHi = cursor.getInt(cursor.getColumnIndex(COLUMN_saiedHi));
                dbUserEntity.saiedHi = saiedHi;
                String idmatch = cursor.getString(cursor.getColumnIndex(COLUMN_idmatch));
                if (idmatch.equals(uid+ UserUtil.getSavedUUID())){
                    dbUserEntity.matchid = idmatch;
                    return dbUserEntity;
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public synchronized long saveNewDbUser(DbUserEntity userEntity) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, userEntity.userId);
            cvs.put(COLUMN_saiedHi, userEntity.saiedHi);
            cvs.put(COLUMN_idmatch, userEntity.userId+UserUtil.getSavedUUID());
            result = db.replace(TABLE_NAME_SAYHI, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }



    public synchronized long markNewSaiedHi(String uid) {
        DbUserEntity dbUserEntity = getNewDbUsers(uid);
        if (dbUserEntity == null) {
            dbUserEntity = new DbUserEntity();
            dbUserEntity.userId = uid;
            dbUserEntity.matchid=uid+UserUtil.getSavedUUID();
        }
        dbUserEntity.saiedHi = 1;
        long result = saveNewDbUser(dbUserEntity);
        return result;
    }

    public synchronized long markNewNoSaiedHi(String uid) {
        DbUserEntity dbUserEntity = getNewDbUsers(uid);
        if (dbUserEntity == null) {
            dbUserEntity = new DbUserEntity();
            dbUserEntity.userId = uid;
            dbUserEntity.matchid=uid+UserUtil.getSavedUUID();
        }
        dbUserEntity.saiedHi = 0;
        long result = saveNewDbUser(dbUserEntity);
        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(CREATE_TABLE__USER_STR);

        db.execSQL(CREATE_TABLE__SAYHI_STR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table " + TABLE_NAME_USER);
        db.execSQL(CREATE_TABLE__USER_STR);

        db.execSQL("Drop table " + TABLE_NAME_SAYHI);
        db.execSQL(CREATE_TABLE__SAYHI_STR);
    }
}
