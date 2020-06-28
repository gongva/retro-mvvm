package com.gongva.retromvvm.repository.common.remote.interceptor;

import android.text.TextUtils;

import com.gongva.library.plugin.netbase.NetConstant;
import com.gongva.retromvvm.repository.common.remote.HeaderUtils;
import com.hik.core.android.api.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求header拦截器
 *
 * @data 2019/3/13
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Map<String, String> headers = HeaderUtils.getHeaders();
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("mimeType", "application/json")
                .method(original.method(), original.body());

        for (String key : headers.keySet()) {
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(headers.get(key))) {
                requestBuilder.header(key, headers.get(key));
            }
        }
        Request request = requestBuilder.build();
        Response response = chain.proceed(request);//进行网络请求并获得返回结果
        ResponseBody body = response.body();
        if (body != null) {
            MediaType mediaType = body.contentType();//获取返回结果的type
            if (mediaType == null
                    || NetConstant.MiniType.IMAGE.equals(mediaType.type())
                    || NetConstant.MiniType.AUDIO.equals(mediaType.type())
                    || NetConstant.MiniType.VIDEO.equals(mediaType.type())) {//媒体数据直接跳过
                return response;
            } else if (NetConstant.MiniType.APPLICATION.equals(mediaType.type())
                    && NetConstant.MiniType.APPLICATION_JSON.equals(mediaType.subtype())) {//json数据转换
                String content = response.body().string();//拿到返回结果，进行分析
                return response.newBuilder()//生成新的response返回，网络请求的response如果取出之后，直接返回将会抛出异常
                        .body(ResponseBody.create(mediaType, convertResult(response.headers(), content)))
                        .build();
            } else {
                return response;
            }
        } else {
            return response;
        }
    }

    /**
     * 可以选择性的二次构造一下body
     *
     * @param headers
     * @param content
     * @return
     */
    private String convertResult(Headers headers, String content) {
        if (GsonUtil.isJsonForGsonValid(content)) {
            try {
                JSONObject kindJson = new JSONObject(content);//将string转为jsonobject
                //kindJson.put("testKey", "testValue");
                return kindJson.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
}
