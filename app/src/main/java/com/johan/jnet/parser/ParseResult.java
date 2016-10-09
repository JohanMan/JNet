package com.johan.jnet.parser;

import com.johan.jnet.http.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Method的解析结果，用于创建一个Request
 * Created by Johan on 2016/10/8.
 */
public class ParseResult {

    private String url;
    private int method;
    private HashMap<String, String> headers;
    private List<ParamTrack> trackList = new ArrayList<>();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getMethod() {
        return method;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void addTrack(int type, String key, int valueIndex) {
        trackList.add(new ParamTrack(type, key, valueIndex));
    }

    public static final int TYPE_PATH = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_PARAM = 3;
    public static final int TYPE_FILE = 4;

    public class ParamTrack {
        public int type;
        public String key;
        public int valueIndex;
        public ParamTrack(int type, String key, int valueIndex) {
            this.type = type;
            this.key = key;
            this.valueIndex = valueIndex;
        }
    }

    public Request buildRequest(Object[] args) {
        Request request = new Request();
        String copyUrl = url;
        if (headers != null) {
            request.addHeaders(headers);
        }
        if (args == null) {
            request.setMethod(method);
            request.setUrl(copyUrl);
            return request;
        }
        if (args.length != trackList.size()) {
            throw new IllegalArgumentException("ParseResult buildRequest() Error : args length must equals track list size");
        }
        for (ParamTrack track : trackList) {
            String key = track.key;
            int index = track.valueIndex;
            if (index >= args.length) {
                throw new IllegalArgumentException("ParseResult buildRequest() Error : track value index is out of args array");
            }
            Object value = args[index];
            switch (track.type) {
                case TYPE_HEADER :
                    if (value instanceof String) {
                        request.addHeader(key, (String) value);
                    } else {
                        throw new IllegalArgumentException("ParseResult buildRequest() Error : header value must is String type");
                    }
                    break;
                case TYPE_PARAM :
                    String stringValue;
                    if (value instanceof String) {
                        stringValue = (String) value;
                    } else {
                        try {
                            stringValue = String.valueOf(value);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("ParseResult buildRequest() Error : param value can not convert String type");
                        }
                    }
                    request.addParam(key, stringValue);
                    break;
                case TYPE_PATH :
                    if (value instanceof String) {
                        copyUrl = copyUrl.replace("{" + key + "}", (String) value);
                    } else {
                        throw new IllegalArgumentException("ParseResult buildRequest() Error : path value must is String type");
                    }
                    break;
                case TYPE_FILE :
                    request.setFileKey(key);
                    if (value instanceof String) {
                        request.addFile((String) value);
                    } else if (value instanceof List) {
                        request.addFileList((List<String>) value);
                    } else {
                        throw new IllegalArgumentException("ParseResult buildRequest() Error : files value must is String or List type");
                    }
                    break;
            }
        }
        request.setMethod(method);
        request.setUrl(copyUrl);
        return request;
    }

}
