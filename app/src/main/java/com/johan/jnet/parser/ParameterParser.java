package com.johan.jnet.parser;

import com.johan.jnet.http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 方法参数的解析器
 * Created by Johan on 2016/10/8.
 */
public abstract class ParameterParser extends MethodParser {

    @Override
    public ParseResult parser(ParseResult parseResult, Method method) {
        if (parseResult.getMethod() == Request.NO_METHOD) {
            throw new IllegalArgumentException("ParameterParser Error : request method must not NO_METHOD");
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return parseNext(parseResult, method);
        }
        int parameterLength = parameterAnnotations.length;
        for (int index = 0; index < parameterLength; index++) {
            Annotation[] annotations = parameterAnnotations[index];
            for (Annotation annotation : annotations) {
                handleAnnotation(parseResult, annotation, index);
            }
        }
        return parseNext(parseResult, method);
    }

    protected abstract void handleAnnotation(ParseResult parseResult, Annotation annotation, int index);

}
