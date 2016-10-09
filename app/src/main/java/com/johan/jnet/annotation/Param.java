package com.johan.jnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Param注解
 * 提交Http键值对的正文内容
 * location : 方法参数
 * Usage : @Param("key") Object value
 * Created by Johan on 2016/10/8.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();
}
