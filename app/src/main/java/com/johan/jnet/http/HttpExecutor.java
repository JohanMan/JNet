package com.johan.jnet.http;

import java.lang.reflect.Type;

/**
 * 访问网络的接口
 * Created by Johan on 2016/10/9.
 */
public interface HttpExecutor {

    void executeRequest(Request request, Callback callback, Type respondType);

    void cancelRequest(String tag);

}
