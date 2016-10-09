package com.johan.jnet.parser;

import com.johan.jnet.annotation.Path;

import java.lang.annotation.Annotation;

/**
 * @Path解析器
 * Created by Johan on 2016/10/8.
 */
public class PathParser extends ParameterParser {

    @Override
    protected void handleAnnotation(ParseResult parseResult, Annotation annotation, int index) {
        if (annotation instanceof Path) {
            Path path = (Path) annotation;
            String pathKey = path.value();
            parseResult.addTrack(ParseResult.TYPE_PATH, pathKey, index);
        }
    }

}
