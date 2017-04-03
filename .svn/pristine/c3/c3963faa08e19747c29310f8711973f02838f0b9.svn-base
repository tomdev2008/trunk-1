package com.aibinong.tantan.util;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/7/25.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Intent;

import com.aibinong.tantan.broadcast.GlobalLocalBroadCastManager;
import com.aibinong.tantan.push.BroadCastConst;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

/**
 * 通用返回码
 * <p/>
 * 值	含义
 * 0	响应成功
 * 1000	无效签名
 * 1001	token失效, 请重新登录
 * 1002	非法参数
 * 1006	验证码发送失败
 * 1007	手机号不正确
 * 1008	用户被锁定
 * 1009	手机号验证失败
 * 1010	登录失效
 * 1011	绑定失败
 * 2001	期次信息不存在
 * 2002	订单信息不存在
 * 2003	购买份数必须是默认份数的整数
 * 2004	红包不存在
 * 2005	红包已使用
 * 2006	红包已过期
 * 2007	产品详情不存在
 * 2008	预下单失败
 * 2009	订单信息不存在
 * 2010	账户余额不足
 * 2011	支付金额异常
 * 9999	系统异常
 */
public class CommonResultHandler implements IResultHandler {
    private static CommonResultHandler handler;

    private CommonResultHandler() {
    }

    public static CommonResultHandler getInstance() {
        if (handler == null) {
            handler = new CommonResultHandler();
        }
        return handler;
    }

    @Override
    public boolean handleResponse(ResponseResult result, boolean autoShowLogin) {
        if (result != null) {
            if (result.getCode() == 1001||result.getCode()==1009) {
                //token失效
                MobclickAgent.onProfileSignOff();
                UserUtil.clearLoginInfo();
                EMClient.getInstance().logout(false);
                if (autoShowLogin) {
                    GlobalLocalBroadCastManager.getInstance().getLocalBroadcastManager().sendBroadcast(new Intent(BroadCastConst.BROADCAST_OPEN_LOGIN));
                }
                return true;
            }
        }
        return false;
    }
}
