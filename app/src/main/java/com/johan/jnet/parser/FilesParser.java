package com.johan.jnet.parser;

import com.johan.jnet.annotation.Files;
import com.johan.jnet.http.Request;

import java.lang.annotation.Annotation;

/**
 * @Files的解析器
 * Created by Johan on 2016/10/8.
 */
public class FilesParser extends ParameterParser {

    @Override
    protected void handleAnnotation(ParseResult parseResult, Annotation annotation, int index) {
        if (annotation instanceof Files) {
            if (parseResult.getMethod() != Request.UPLOAD) {
                throw new IllegalArgumentException("FilesParser Error : request method must is UPLOAD");
            }
            Files files = (Files) annotation;
            String fileKey = files.value();
            parseResult.addTrack(ParseResult.TYPE_FILE, fileKey, index);
        }
    }

}
