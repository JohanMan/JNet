package com.johan.jnet.parser;

import java.lang.reflect.Method;

/**
 * 所有解析器的集成
 * Created by Johan on 2016/10/8.
 */
public class Parser {

    private MethodParser rootParser;

    public Parser() {
        rootParser = new GetParser();
        MethodParser postParser = new PostParser();
        rootParser.setNextParser(postParser);
        MethodParser uploadParser = new UploadParser();
        postParser.setNextParser(uploadParser);
        MethodParser pathParser = new PathParser();
        uploadParser.setNextParser(pathParser);
        MethodParser paramParser = new ParamParser();
        pathParser.setNextParser(paramParser);
        MethodParser headerParser = new HeaderParser();
        paramParser.setNextParser(headerParser);
        MethodParser headersParser = new HeadersParser();
        headerParser.setNextParser(headersParser);
        MethodParser filesParser = new FilesParser();
        headersParser.setNextParser(filesParser);
    }

    public ParseResult parse(Method method) {
        ParseResult result = new ParseResult();
        return rootParser.parser(result, method);
    }

}
