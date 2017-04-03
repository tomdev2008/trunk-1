package com.aibinong.yueaiapi.api.handler;


import com.aibinong.yueaiapi.pojo.ResponseResult;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/7/25.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|
public interface IResultHandler {
    boolean handleResponse(ResponseResult result, boolean autoShowLogin);
}
