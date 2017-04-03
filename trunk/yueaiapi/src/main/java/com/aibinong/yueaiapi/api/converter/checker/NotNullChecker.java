package com.aibinong.yueaiapi.api.converter.checker;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class NotNullChecker<T> implements IDataChecker<T> {
    private static NotNullChecker checker;

    private NotNullChecker() {
    }

    public static NotNullChecker getInstance() {
        if (checker == null) {
            checker = new NotNullChecker();
        }
        return checker;
    }

    @Override
    public boolean isDataValid(T data) {
        if (data != null) {
            return true;
        }
        return false;
    }
}
