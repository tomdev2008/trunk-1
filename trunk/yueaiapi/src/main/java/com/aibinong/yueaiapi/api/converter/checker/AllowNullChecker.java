package com.aibinong.yueaiapi.api.converter.checker;

/**
 * Created by yourfriendyang on 16/6/20.
 */
public class AllowNullChecker<T> implements IDataChecker<T> {
    private static AllowNullChecker checker;

    private AllowNullChecker() {
    }

    public static AllowNullChecker getInstance() {
        if (checker == null) {
            checker = new AllowNullChecker();
        }
        return checker;
    }

    @Override
    public boolean isDataValid(T data) {
        return true;
    }
}
