package com.aibinong.yueaiapi.annotation;


import com.aibinong.yueaiapi.api.converter.checker.IDataChecker;
import com.aibinong.yueaiapi.api.converter.checker.NotNullChecker;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yourfriendyang on 16/6/20.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DataChecker {
    Class<? extends IDataChecker> value() default NotNullChecker.class;
}
