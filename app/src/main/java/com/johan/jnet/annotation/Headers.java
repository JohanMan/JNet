package com.johan.jnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Headers注解
 * 功能与Header相同
 * location : 方法
 * Usage : @Header({"Accept-Charset:utf-8"})
 * 注意 : ":"符号一定要有，否则解析错误
 * Created by Johan on 2016/10/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Headers {
    String[] values();
}
