package com.gongva.retromvvm.repository.common.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Request 参数工具
 *
 * @data 2019/3/20
 */
public class RequestUtils {

    private static final String MEDIA_TYPE = "application/json; charset=utf-8";

    /**
     * 将java bean对象通过json转换然后封装包装为RequestBody
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> RequestBody convertFromObject(T obj) {
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(obj);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        return FormBody.create(MediaType.parse(MEDIA_TYPE), json);
    }

    /**
     * 将JsonObject对象封装包装为RequestBody
     *
     * @param obj
     * @return
     */
    public static RequestBody convertFromJsonObject(JsonObject obj) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), obj.toString());
    }

    /**
     * 将JsonObject对象封装包装为RequestBody
     *
     * @param jsonString
     * @return
     */
    public static RequestBody convertFromJsonObject(String jsonString) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), jsonString);
    }
}
