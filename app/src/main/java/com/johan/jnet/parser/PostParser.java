package com.johan.jnet.parser;

import com.johan.jnet.annotation.Post;
import com.johan.jnet.http.Request;
import com.johan.jnet.http.Utils;

import java.lang.reflect.Method;

/**
 * @Post解析器
 * Created by Johan on 2016/10/8.
 */
public class PostParser extends MethodParser {

    @Override
    public ParseResult parser(ParseResult parseResult, Method method) {
        if (method.isAnnotationPresent(Post.class)) {
            Post get = method.getAnnotation(Post.class);
            String url = get.value();
            if (Utils.isEmptyString(url)) {
                throw new IllegalArgumentException("PostParser Error : Post value() must no empty");
            }
            parseResult.setMethod(Request.POST);
            parseResult.setUrl(url);
            return parseNext(parseResult, method);
        } else {
            return parseNext(parseResult, method);
        }
    }

}
