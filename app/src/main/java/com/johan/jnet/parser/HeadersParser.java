package com.johan.jnet.parser;

import com.johan.jnet.annotation.Headers;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Headers的解析器
 * Created by Johan on 2016/10/8.
 */
public class HeadersParser extends MethodParser {

    @Override
    public ParseResult parser(ParseResult parseResult, Method method) {
        if (method.isAnnotationPresent(Headers.class)) {
            Headers headers = method.getAnnotation(Headers.class);
            String[] headerValues = headers.values();
            if (headerValues.length == 0) {
                return parseNext(parseResult, method);
            }
            HashMap<String, String> headerMap = new HashMap<>();
            for (String headerValue : headerValues) {
                if (headerValue.contains(":")) {
                    String[] headerArray = headerValue.split(":", 2);
                    if (headerArray.length != 2) {
                        throw new IllegalArgumentException("HeadersParser Error : header content parser err");
                    }
                    String headerContentKey = headerArray[0];
                    String headerContentValue = headerArray[1];
                    headerMap.put(headerContentKey, headerContentValue);
                } else {
                    throw new IllegalArgumentException("HeadersParser Error : header content must contains ':'");
                }
            }
            parseResult.setHeaders(headerMap);
            return parseNext(parseResult, method);
        } else {
            return parseNext(parseResult, method);
        }
    }

}
