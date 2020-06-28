package com.gongva.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Gson转换器
 *
 * @data 2019/5/5
 */
public class MineGsonConverterFactory extends Converter.Factory {

    public static MineGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MineGsonConverterFactory(gson);
    }

    public static MineGsonConverterFactory create(Gson gson, AGsonRequestBodyConverter requestBodyConverter, AGsonResponseBodyConverter responseBodyConverter) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MineGsonConverterFactory(gson, requestBodyConverter, responseBodyConverter);
    }

    private final Gson gson;
    private AGsonRequestBodyConverter mRequestBodyConverter;
    private AGsonResponseBodyConverter mResponseBodyConverter;

    private MineGsonConverterFactory(Gson gson) {
        this(gson, MineGsonRequestBodyConverter.INSTANCE, MineGsonResponseBodyConverter.INSTANCE);
    }

    private MineGsonConverterFactory(Gson gson, AGsonRequestBodyConverter requestBodyConverter, AGsonResponseBodyConverter responseBodyConverter) {
        this.gson = gson;
        this.mRequestBodyConverter = requestBodyConverter;
        this.mResponseBodyConverter = responseBodyConverter;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        AGsonResponseBodyConverter responseBodyConverter;
        try {
            if (mResponseBodyConverter == null) {
                responseBodyConverter = MineGsonResponseBodyConverter.INSTANCE.clone();
            } else {
                responseBodyConverter = mResponseBodyConverter.clone();
            }
            responseBodyConverter.initParams(gson, adapter);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            responseBodyConverter = MineGsonResponseBodyConverter.INSTANCE;
            responseBodyConverter.initParams(gson, adapter);
        }
        responseBodyConverter.initParams(gson, adapter);
        return responseBodyConverter;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        AGsonRequestBodyConverter requestBodyConverter;
        try {
            if (mRequestBodyConverter == null) {
                requestBodyConverter = MineGsonRequestBodyConverter.INSTANCE.clone();
            } else {
                requestBodyConverter = mRequestBodyConverter.clone();
            }
            requestBodyConverter.initParams(gson, adapter);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            requestBodyConverter = MineGsonRequestBodyConverter.INSTANCE;
            requestBodyConverter.initParams(gson, adapter);
        }
        requestBodyConverter.initParams(gson, adapter);
        return requestBodyConverter;
    }
}
