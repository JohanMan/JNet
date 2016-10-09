package com.johan.jnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Upload注解
 * 声明Http方法为Post方法，并指明是上传文件
 * location : 方法
 * Usage : @Upload("/hello/world") /hello/world is remote host path
 * Created by Johan on 2016/10/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Upload {
    String value();
}
