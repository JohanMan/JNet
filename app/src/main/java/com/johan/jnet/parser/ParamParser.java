package com.johan.jnet.parser;

import com.johan.jnet.annotation.Param;

import java.lang.annotation.Annotation;

/**
 * @Param解析器
 * Created by Johan on 2016/10/8.
 */
public class ParamParser extends ParameterParser {

    @Override
    protected void handleAnnotation(ParseResult parseResult, Annotation annotation, int index) {
        if (annotation instanceof Param) {
            Param param = (Param) annotation;
            String paramKey = param.value();
            parseResult.addTrack(ParseResult.TYPE_PARAM, paramKey, index);
        }
    }

}
