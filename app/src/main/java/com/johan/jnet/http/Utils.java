package com.johan.jnet.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 工具类
 * Created by Johan on 2016/10/9.
 */
public class Utils {

    public static boolean isEmptyString(String... strings) {
        for (String string : strings) {
            if (string == null || "".equals(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoEmptyString(String... strings) {
        return !isEmptyString(strings);
    }

    public static Type getRespondType(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        Type paramType = types[0];
        if (paramType instanceof ParameterizedType) {
            return getRespondType((ParameterizedType) paramType);
        }
        return paramType;
    }

    public static boolean isBaseDataType(Type type) {
        String className = type.toString();
        return className.endsWith(String.class.getName())  ||
               className.endsWith(Integer.class.getName()) ||  className.endsWith("int")     ||
               className.endsWith(Long.class.getName())    ||  className.endsWith("long")    ||
               className.endsWith(Double.class.getName())  ||  className.endsWith("double")  ||
               className.endsWith(Float.class.getName())   ||  className.endsWith("float")   ||
               className.endsWith(Boolean.class.getName()) ||  className.endsWith("boolean");
    }

}
