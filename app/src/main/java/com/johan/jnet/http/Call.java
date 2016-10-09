package com.johan.jnet.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 访问Http的过渡类
 * Created by Johan on 2016/10/8.
 */
public class Call<T> {

    private Request request;
    private Type respondType;

    public Call(Request request, ParameterizedType type) {
        this.request = request;
        this.respondType = Utils.getRespondType(type);
    }

    public void call(Callback<T> callback) {
        OkHttpExecutor.INSTANCE.executeRequest(request, callback, respondType);
    }

}
