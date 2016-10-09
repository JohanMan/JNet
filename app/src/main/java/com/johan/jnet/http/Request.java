package com.johan.jnet.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Http请求类
 * Created by Johan on 2016/10/8.
 */
public class Request {

    public static final int NO_METHOD = 0;
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int UPLOAD = 3;

    private String tag;
    private String url;
    private int method = NO_METHOD;

    private HashMap<String, String> headers = new HashMap<>();
    private HashMap<String, String> params = new HashMap<>();

    // if method is upload
    private static final String DEFAULT_FILE_KEY = "files";
    private String fileKey = DEFAULT_FILE_KEY;
    private List<String> fileList = new ArrayList<>();

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getMethod() {
        return method;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void addFile(String file) {
        this.fileList.add(file);
    }

    public void addFileList(List<String> fileList) {
        this.fileList.addAll(fileList);
    }

    public List<String> getFileList() {
        return fileList;
    }

}
