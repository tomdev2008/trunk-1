package com.aibinong.yueaiapi.api.converter.paser;//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易

import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.handler.IResultHandler;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by yourfriendyang on 16/7/16.
 * yourfriendyang@163.com
 */
public class DirectDataPaser<R> implements IDataParser {
    private static DirectDataPaser parser;

    private DirectDataPaser() {
    }

    public static DirectDataPaser getInstance() {
        if (parser == null) {
            parser = new DirectDataPaser();
        }
        return parser;
    }

    @Override
    public R parse(Gson gson, String jsonStr, String dataKey, Type dataType, IDataChecker mDataChecker, IResultHandler resultHandler, boolean autoShowLogin) {
        R result = gson.fromJson(jsonStr, dataType);
        if (result != null) {
            if (mDataChecker != null) {
                if (!mDataChecker.isDataValid(result)) {
                    throw new ResponseResult(-1, "无效的数据");
                }
            }
        } else {
            throw new ResponseResult(-1, "无效的数据");
        }
        return result;
    }
}
