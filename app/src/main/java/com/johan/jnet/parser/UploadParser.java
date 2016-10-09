package com.johan.jnet.parser;

import com.johan.jnet.annotation.Upload;
import com.johan.jnet.http.Request;
import com.johan.jnet.http.Utils;

import java.lang.reflect.Method;

/**
 * @Upload解析器
 * Created by Johan on 2016/10/8.
 */
public class UploadParser extends MethodParser {

    @Override
    public ParseResult parser(ParseResult parseResult, Method method) {
        if (method.isAnnotationPresent(Upload.class)) {
            Upload get = method.getAnnotation(Upload.class);
            String url = get.value();
            if (Utils.isEmptyString(url)) {
                throw new IllegalArgumentException("UploadParser Error : Upload value() must no empty");
            }
            parseResult.setMethod(Request.UPLOAD);
            parseResult.setUrl(url);
            return parseNext(parseResult, method);
        } else {
            return parseNext(parseResult, method);
        }
    }

}
