package com.hik.core.android.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

/**
 * Gson/Json 工具库
 *
 * @author gongwei
 * @time 2019/10/11
 * @mail shmily__vivi@163.com
 */
public class GsonUtil {

    public GsonUtil() {
    }

    public static String jsonSerializer(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T jsonDeserializer(String json, Class<T> clazz) {
        T result = null;

        try {
            Gson gson = new Gson();
            result = gson.fromJson(json, clazz);
        } catch (Exception var4) {
            LogCat.e("JsonDeserializer: " + var4.getMessage());
        }

        return result;
    }

    public static <T> T jsonDeserializer(Reader json, Class<T> clazz) {
        T result = null;

        try {
            Gson gson = new Gson();
            result = gson.fromJson(json, clazz);
        } catch (Exception var4) {
            LogCat.e("JsonDeserializer: " + var4.getMessage());
        }

        return result;
    }

    public static <T> T jsonDeserializer(String json, TypeToken<T> typeToken) {
        T result = null;

        try {
            Gson gson = new Gson();
            result = gson.fromJson(json, typeToken.getType());
        } catch (Exception var4) {
            LogCat.e("jsonDeserializerCollection: " + var4.getMessage());
        }

        return result;
    }

    public static <T> T jsonDeserializer(Reader json, TypeToken<T> typeToken) {
        T result = null;

        try {
            Gson gson = new Gson();
            result = gson.fromJson(json, typeToken.getType());
        } catch (Exception var4) {
            LogCat.e("jsonDeserializerCollection: " + var4.getMessage());
        }

        return result;
    }

    /**
     * 是Json不？
     *
     * @param jsonInString
     * @return
     */
    public final static boolean isJsonForGsonValid(String jsonInString) {
        if (TextUtils.isEmpty(jsonInString)) {
            return false;
        }
        try {
            new Gson().fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}
