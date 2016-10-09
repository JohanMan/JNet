package com.johan.jnet.parser;

import com.johan.jnet.annotation.Get;
import com.johan.jnet.http.Request;
import com.johan.jnet.http.Utils;

import java.lang.reflect.Method;

/**
 * @Get的解析器
 * Created by Johan on 2016/10/8.
 */
public class GetParser extends MethodParser {

    @Override
    public ParseResult parser(ParseResult parseResult, Method method) {
        if (method.isAnnotationPresent(Get.class)) {
            Get get = method.getAnnotation(Get.class);
            String url = get.value();
            if (Utils.isEmptyString(url)) {
                throw new IllegalArgumentException("GetParser Error : Get value() must no empty");
            }
            parseResult.setMethod(Request.GET);
            parseResult.setUrl(url);
            return parseNext(parseResult, method);
        } else {
            return parseNext(parseResult, method);
        }
    }

}
