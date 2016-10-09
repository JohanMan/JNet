package com.johan.jnet.http;

import java.util.List;
import java.util.Map;

/**
 * Http请求的结果类
 * Created by Johan on 2016/10/9.
 */
public class Response <T> {

    private int code;

    private T body;

    private Map<String, List<String>> header;

    public Response(int code, T body, Map<String, List<String>> header) {
        this.code = code;
        this.body = body;
        this.header = header;
    }

    public int getCode() {
        return code;
    }

    public T getBody() {
        return body;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public List<String> getHeaders(String key) {
        return header.get(key);
    }

}
