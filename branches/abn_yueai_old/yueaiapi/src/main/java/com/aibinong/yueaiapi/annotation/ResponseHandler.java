package com.aibinong.yueaiapi.annotation;


import com.aibinong.yueaiapi.api.handler.IResultHandler;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/7/25.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|
public @interface ResponseHandler {
    Class<? extends IResultHandler> value();
}
