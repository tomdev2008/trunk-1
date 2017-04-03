package com.aibinong.yueaiapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yourfriendyang on 16/6/20.
 * 后台返回数据解析时使用哪个字段来取数据，默认为空，表示直接使用data内的结构
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DataKey {
    String value();
}
