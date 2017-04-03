package com.aibinong.yueaiapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.aibinong.yueaiapi.pojo.UserEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by yourfriendyang on 16/3/26.
 */
public class LocalUsersHelper extends SQLiteOpenHelper {
    private static final String db_name = "cached_users";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_localId = "userId";
    private static final String COLUMN_clickedFromLikeMe = "clickedFromLikeMe";//是否曾经在喜欢列表点过
    private static final String COLUMN_clickedFromPairList = "clickedFromPairList";//是否曾经在配对列表点过
    private static final String COLUMN_entityJsonStr = "entityJsonStr";


    private static final String CREATE_TABLE_STR = "create table if not exists "
            + TABLE_NAME + "("
            + COLUMN_localId + " TEXT PRIMARY KEY,"
            + COLUMN_clickedFromLikeMe + " INTEGER,"
            + COLUMN_clickedFromPairList + " INTEGER,"
            + COLUMN_entityJsonStr + " TEXT"
            + ")";
    private Gson gson;

    public LocalUsersHelper(Context context) {
        super(context, db_name, null, 2);
        gson = new GsonBuilder().create();
    }

    public long deleteUser(String userId) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            result = db.delete(TABLE_NAME, COLUMN_localId + "=?", new String[]{userId});
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public long saveUser(UserEntity userEntity) {
        boolean markAsClick = isClickedFromLikeMe(userEntity.id);
        boolean markAsClickFromPairList = isClickedFromPairList(userEntity.id);
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, userEntity.id);
            cvs.put(COLUMN_entityJsonStr, gson.toJson(userEntity));
            if (markAsClick) {
                cvs.put(COLUMN_clickedFromLikeMe, 1);
            }
            if (markAsClickFromPairList) {
                cvs.put(COLUMN_clickedFromPairList, 1);
            }
            result = db.replace(TABLE_NAME, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public UserEntity getUsers(String uid) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        UserEntity UserEntity = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
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

    public boolean isClickedFromLikeMe(String uid) {
        boolean everTalked = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
            while (cursor.moveToNext()) {
                int everTalkInt = cursor.getInt(cursor.getColumnIndex(COLUMN_clickedFromLikeMe));
                everTalked = (everTalkInt == 1 ? true : false);
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
        return everTalked;
    }

    public long markClikedFromLikeMe(String uid) {
//        boolean markAsClick = isClickedFromLikeMe(uid);
        boolean markAsClickFromPairList = isClickedFromPairList(uid);

        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, uid);
            cvs.put(COLUMN_clickedFromLikeMe, 1);
            cvs.put(COLUMN_clickedFromPairList, markAsClickFromPairList ? 1 : 0);
            result = db.replace(TABLE_NAME, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public boolean isClickedFromPairList(String uid) {
        boolean everTalked = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, COLUMN_localId + " = ?", new String[]{uid}, null, null, null);
            while (cursor.moveToNext()) {
                int everTalkInt = cursor.getInt(cursor.getColumnIndex(COLUMN_clickedFromPairList));
                everTalked = (everTalkInt == 1 ? true : false);
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
        return everTalked;
    }

    public long markClikedFromPairList(String uid) {
        boolean markAsClick = isClickedFromLikeMe(uid);
//        boolean markAsClickFromPairList = isClickedFromPairList(uid);
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues cvs = new ContentValues();
            cvs.put(COLUMN_localId, uid);
            cvs.put(COLUMN_clickedFromPairList, 1);
            cvs.put(COLUMN_clickedFromLikeMe, markAsClick ? 1 : 0);
            result = db.replace(TABLE_NAME, "null", cvs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public ArrayList<UserEntity> getAllUsers() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<UserEntity> goodsScenicArrayList = new ArrayList<>();
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_localId + " DESC");
            while (cursor.moveToNext()) {
                String jsonstr = cursor.getString(cursor.getColumnIndex(COLUMN_entityJsonStr));
                UserEntity goods = gson.fromJson(jsonstr, UserEntity.class);
                goodsScenicArrayList.add(goods);
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(CREATE_TABLE_STR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table " + TABLE_NAME);
        db.execSQL(CREATE_TABLE_STR);
    }
}
