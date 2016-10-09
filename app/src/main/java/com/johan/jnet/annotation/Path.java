package com.johan.jnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Path注解
 * 配合Get、Post和Upload使用，可以代替url的占位符
 * location : 方法参数
 * Usage : 如声明@Get("/index/{name}/{age}"), 那么在方法参数内加入 @Path("name") String name, @Path("age") String age
 * Created by Johan on 2016/10/8.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String value();
}
