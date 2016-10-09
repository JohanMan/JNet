package com.johan.jnet;

import com.johan.jnet.http.Call;
import com.johan.jnet.http.OkHttpExecutor;
import com.johan.jnet.http.Request;
import com.johan.jnet.parser.ParseResult;
import com.johan.jnet.parser.Parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * JNet，主要类，动态代理所有接口
 * Created by Johan on 2016/10/8.
 */
public class JNet {

    private static HashMap<String, ParseResult> parseResultCache = new HashMap<>();

    public static <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class returnType = method.getReturnType();
                        Type genericReturnType = method.getGenericReturnType();
                        if (returnType.equals(Call.class) && genericReturnType instanceof ParameterizedType) {
                            ParseResult parseResult;
                            String cacheKey = service.getName() + "_" + method.getName();
                            if (parseResultCache.containsKey(cacheKey)) {
                                parseResult = parseResultCache.get(cacheKey);
                            } else {
                                Parser parser = new Parser();
                                parseResult = parser.parse(method);
                                parseResultCache.put(cacheKey, parseResult);
                            }
                            Request request = parseResult.buildRequest(args);
                            request.setTag(service.getName());
                            return new Call(request, (ParameterizedType) genericReturnType);
                        }
                        return method.invoke(this, args);
                    }
                });
    }

    public static void cancel(String tag) {
        OkHttpExecutor.INSTANCE.cancelRequest(tag);
    }

}
