package com.johan.jnet.parser;

import java.lang.reflect.Method;

/**
 * 解析器
 * Created by Johan on 2016/10/8.
 */
public abstract class MethodParser {

    protected MethodParser nextParser;

    public void setNextParser(MethodParser nextParser) {
        this.nextParser = nextParser;
    }

    public abstract ParseResult parser(ParseResult parseResult, Method method);

    protected ParseResult parseNext(ParseResult parseResult, Method method) {
        if (nextParser != null) {
            return nextParser.parser(parseResult, method);
        } else {
            return parseResult;
        }
    }

}
