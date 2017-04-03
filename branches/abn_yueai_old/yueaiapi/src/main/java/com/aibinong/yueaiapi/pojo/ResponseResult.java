package com.aibinong.yueaiapi.pojo;

/**
 * Created by yourfriendyang on 16/6/22.
 */

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @author yourfriendyang
 *         <p/>
 *         0 成功
 *         <p/>
 *         1001 服务器繁忙，请稍后重试或与服务商联系
 *         <p/>
 *         1002 非法操作，通常是没有权限使用此项服务
 *         <p/>
 *         1003 client_id不存在或者非法
 *         <p/>
 *         1004 请求的接口无效
 *         <p/>
 *         1005 您操作太频繁了，请先停下来喝杯茶，稍后再继续吧！
 *         <p/>
 *         1006 无数据
 *         <p/>
 *         2001 必填参数为空
 *         <p/>
 *         2002 请求参数无效
 *         <p/>
 *         2003 查询的数量超过最大限制
 *         <p/>
 *         2005 客户端未认证
 *         <p/>
 *         2008 用户会话无效，重新登录
 *         <p/>
 *         2004 服务端错误
 */
public class ResponseResult extends RuntimeException {
    public static final String INFO_CLIENTAUTH_FAILED = "客户端认证失败";
    public static final String INFO_INVALID_CLIENTTOKEN = "客户端未认证";

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_INVALID_CLIENTTOKEN = 2005;
    public static final int CODE_INVALID_AUTH = 2008;

    private int code = -1;
    private String info = "";
    private int sonCode = -1;
    private int expireTime;

    public static ResponseResult fromThrowable(Throwable e) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setInfo(e.getMessage());
        responseResult.initCause(e);
        return responseResult;
    }

    public ResponseResult() {

    }

    public ResponseResult(int code, String info) {
        super(info);
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return getMessage();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSonCode() {
        return sonCode;
    }

    public void setSonCode(int sonCode) {
        this.sonCode = sonCode;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String getMessage() {
        Throwable cause = getCause();
        if (cause != null) {
            if (cause instanceof SocketTimeoutException) {
                return "连接超时";
            } else if (cause instanceof UnknownHostException) {
                return "请检查网络设置";
            } else if (cause instanceof ConnectException || cause instanceof SocketException) {
                return "无法连接到服务器";
            } else if (cause instanceof HttpException) {
                HttpException exception = (HttpException) cause;
                return String.format("服务器故障,正在抢修,错误码:%d", exception.code());
            }
        }
        return info;
    }
}
