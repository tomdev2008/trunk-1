package com.aibinong.yueaiapi.api.converter.paser;


import android.util.Log;

import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.pojo.DanMuEntity;
import com.aibinong.yueaiapi.pojo.DanMusEntity;
import com.aibinong.yueaiapi.pojo.GiftDanMuEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.PairDanMuEntity;
import com.aibinong.yueaiapi.pojo.RechargeDanMuEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by yourfriendyang on 16/1/31.
 */
public class DanmuDataParser implements IDataParser {
    private static DanmuDataParser parser;

    private DanmuDataParser() {
    }

    public static DanmuDataParser getInstance() {
        if (parser == null) {
            parser = new DanmuDataParser();
        }
        return parser;
    }

    @Override
    public JsonRetEntity<DanMusEntity> parse(Gson gson, String response, String mDataKey, Type mDataType, IDataChecker mDataChecker, IResultHandler resultHandler, boolean autoShowLogin) {
        JsonRetEntity retDo = new JsonRetEntity<DanMusEntity>();
        ResponseResult responseResult = gson.fromJson(response, ResponseResult.class);
        if (resultHandler != null) {
            resultHandler.handleResponse(responseResult, autoShowLogin);
        }
        if (responseResult != null && responseResult.getCode() != ResponseResult.CODE_SUCCESS) {
            throw responseResult;
        }
        Page page = new Page();
        retDo.setPage(page);

        try {
            JSONObject jsonObj = new JSONObject(response);
            //解析expireTime
            JSONObject dataObj = jsonObj.optJSONObject("data");
            if (dataObj != null) {

                DanMusEntity danMusEntity = new DanMusEntity();

                danMusEntity.intervals = dataObj.optDouble("intervals");
                danMusEntity.polltime = dataObj.optDouble("polltime");
                danMusEntity.strips = dataObj.optInt("strips");

                JSONArray listJson = dataObj.optJSONArray("list");
                if (listJson != null && listJson.length() > 0) {
                    danMusEntity.list = new ArrayList<>(listJson.length());
                    for (int i = 0; i < listJson.length(); i++) {
                        JSONObject itemObj = listJson.getJSONObject(i);
                        DanMuEntity danMuEntity = null;
                        int type = itemObj.optInt("type");
                        if (type == DanMuEntity.type_pair) {
                            danMuEntity = gson.fromJson(listJson.getString(i), PairDanMuEntity.class);
                        } else if (type == DanMuEntity.type_gift) {
                            danMuEntity = gson.fromJson(listJson.getString(i), GiftDanMuEntity.class);
                        } else if (type == DanMuEntity.type_recharge) {
                            danMuEntity = gson.fromJson(listJson.getString(i), RechargeDanMuEntity.class);
                        }
                        if (danMuEntity != null) {
                            danMusEntity.list.add(danMuEntity);
                        }
                    }
                }
                retDo.setData(danMusEntity);
            }
            retDo.setPage(page);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        if (responseResult != null && responseResult.getCode() == 0) {
            if (mDataChecker != null) {
                if (!mDataChecker.isDataValid(retDo.getData())) {
                    responseResult.setCode(-1);
                    responseResult.setInfo("数据无效！");
                    throw responseResult;
                }
            }
        } else {
            throw responseResult;
        }
        retDo.setError(responseResult);
        return retDo;
    }
}
