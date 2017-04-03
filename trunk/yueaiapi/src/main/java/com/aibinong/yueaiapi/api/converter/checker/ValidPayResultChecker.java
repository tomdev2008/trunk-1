package com.aibinong.yueaiapi.api.converter.checker;


import com.aibinong.yueaiapi.pojo.PayResultEntity;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class ValidPayResultChecker implements IDataChecker<PayResultEntity> {
    private static ValidPayResultChecker checker;

    private ValidPayResultChecker() {
    }

    public static ValidPayResultChecker getInstance() {
        if (checker == null) {
            checker = new ValidPayResultChecker();
        }
        return checker;
    }

    @Override
    public boolean isDataValid(PayResultEntity data) {
        if (data != null && data.order != null) {
            return true;
        }
        return false;
    }
}
