package com.aibinong.yueaiapi.api.converter.paser;


import android.text.TextUtils;
import android.util.Log;

import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.pojo.AdvertEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.Page;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by yourfriendyang on 16/1/31.
 */
public class DefaultDataParser<R> implements IDataParser {
    private static DefaultDataParser parser;

    private DefaultDataParser() {
    }

    public static DefaultDataParser getInstance() {
        if (parser == null) {
            parser = new DefaultDataParser();
        }
        return parser;
    }

    @Override
    public JsonRetEntity<R> parse(Gson gson, String response, String mDataKey, Type mDataType, IDataChecker mDataChecker, IResultHandler resultHandler, boolean autoShowLogin) {
        JsonRetEntity retDo = new JsonRetEntity<R>();
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
            String advertStr = jsonObj.optString("advert");
            if (TextUtils.isEmpty(advertStr)) {
                advertStr = jsonObj.optString("payResultShare");
            }
            if (!TextUtils.isEmpty(advertStr)) {
                AdvertEntity advert = gson.fromJson(advertStr, AdvertEntity.class);
                retDo.advert = advert;
//                AdvertUtil.getInstance().handlerAdvert(advert);
            }

            if (dataObj != null) {
                JSONObject pageObj = dataObj.optJSONObject("page");
                if (pageObj != null) {
                    page.toPage = pageObj.optInt("toPage");
                    page.totalPage = pageObj.optInt("totalPage");
                    page.totalCount = pageObj.optInt("totalCount");
                }
                if (!TextUtils.isEmpty(mDataKey)) {
                    String settingStr = dataObj.optString(mDataKey);
                    if (mDataType.equals(String.class)) {
                        retDo.setData((R) settingStr);
                    } else {
                        if (settingStr != null) {
                            R setting = gson.fromJson(settingStr,
                                    mDataType);
                            retDo.setData(setting);
                        }
                    }

                } else if (mDataType.equals(String.class)) {
                    retDo.setData((R) dataObj.toString());
                } else {

                    R setting = gson.fromJson(dataObj.toString(),
                            mDataType);
                    retDo.setData(setting);
                }


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
