package com.johan.jnet.parser;

import com.johan.jnet.annotation.Header;

import java.lang.annotation.Annotation;

/**
 * @Header的解析器
 * Created by Johan on 2016/10/8.
 */
public class HeaderParser extends ParameterParser {

    @Override
    protected void handleAnnotation(ParseResult parseResult, Annotation annotation, int index) {
        if (annotation instanceof Header) {
            Header header = (Header) annotation;
            String headerKey = header.value();
            parseResult.addTrack(ParseResult.TYPE_HEADER, headerKey, index);
        }
    }

}
