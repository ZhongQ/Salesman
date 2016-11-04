package com.salesman.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * 网络数据解析工具类
 * Created by LiHuai on 2016/1/21
 */
public class GsonUtils {
    public static <T> T json2Bean(String json, Class<T> clazz) {
        if (!json.contains("{") || !json.contains(":") || !json.contains("}")) {
            return null;
        }

        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T json2Object(String jsonStr, Type type) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T t = null;
        try {
            t = gson.fromJson(jsonStr, type);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }
}
