package com.johan.jnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Files注解
 * 配合Upload，注解fileKey和file
 * location ：方法参数
 * Usage1 : @Files("fileKey") String fileName
 * Usage2 : @Files("fileKey") List<String> fileNameList
 * Created by Johan on 2016/10/8.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Files {
    String value();
}
