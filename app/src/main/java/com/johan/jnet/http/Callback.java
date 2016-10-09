package com.johan.jnet.http;

/**
 * Http结果回调接口
 * Created by Johan on 2016/10/8.
 */
public interface Callback <T> {

    void onResponse(Response<T> respond);

    void onFailure(String reason);

}
