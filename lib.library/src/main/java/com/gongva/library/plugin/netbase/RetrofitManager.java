package com.gongva.library.plugin.netbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gongva.library.plugin.netbase.entity.INetInterceptorConfig;
import com.gongva.library.plugin.netbase.entity.NetInterceptorConfigAdapter;
import com.gongva.library.plugin.netbase.transformer.AGsonRequestBodyConverter;
import com.gongva.library.plugin.netbase.transformer.AGsonResponseBodyConverter;
import com.gongva.library.plugin.netbase.transformer.MineGsonConverterFactory;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * retrofit 统一配置管理器
 *
 * @data 2019/5/6
 */
public class RetrofitManager {

    private static volatile RetrofitManager INSTANCE;

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitManager();
                }
            }
        }
        return INSTANCE;
    }

    private List<Interceptor> mInterceptorList;//各种拦截器集合
    private INetInterceptorConfig mNetInterceptorConfig;//网络拦截适配器
    private AGsonResponseBodyConverter mResponseConstructor;//response 转换器
    private AGsonRequestBodyConverter mRequestBodyConverter;//request 转换器
    private Gson mGson;
    private OkHttpClient.Builder mHttpClientBuilder;
    private boolean mHasRebuild;

    public RetrofitManager() {
        mGson = new GsonBuilder()
                .setLenient()
                .create();
        mHttpClientBuilder = new OkHttpClient.Builder();
//                .connectTimeout(60, TimeUnit.SECONDS) // 默认10s
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS);
    }

    public boolean isHasRebuild() {
        return mHasRebuild;
    }

    public void setHasRebuild(boolean hasRebuild) {
        mHasRebuild = hasRebuild;
    }

    public Gson getGson() {
        return mGson;
    }

    public RetrofitManager setGson(Gson gson) {
        mGson = gson;
        return this;
    }

    public OkHttpClient.Builder getHttpClientBuilder() {
        return mHttpClientBuilder;
    }

    public RetrofitManager setHttpClientBuilder(OkHttpClient.Builder httpClientBuilder) {
        mHttpClientBuilder = httpClientBuilder;
        return this;
    }

    public List<Interceptor> getInterceptorList() {
        return mInterceptorList;
    }

    public RetrofitManager setInterceptorList(List<Interceptor> interceptorList) {
        mInterceptorList = interceptorList;
        return this;
    }

    public INetInterceptorConfig getNetInterceptorConfig() {
        if (mNetInterceptorConfig == null) {
            mNetInterceptorConfig = NetInterceptorConfigAdapter.INSTANCE;
        }
        return mNetInterceptorConfig;
    }

    public RetrofitManager setNetInterceptorConfig(INetInterceptorConfig netInterceptorConfig) {
        mNetInterceptorConfig = netInterceptorConfig;
        return this;
    }

    public AGsonResponseBodyConverter getResponseConstructor() {
        return mResponseConstructor;
    }

    public RetrofitManager setResponseConstructor(AGsonResponseBodyConverter responseConstructor) {
        mResponseConstructor = responseConstructor;
        return this;
    }

    public AGsonRequestBodyConverter getRequestBodyConverter() {
        return mRequestBodyConverter;
    }

    public RetrofitManager setRequestBodyConverter(AGsonRequestBodyConverter requestBodyConverter) {
        mRequestBodyConverter = requestBodyConverter;
        return this;
    }

    public void rebuild() {
        mHasRebuild = true;
    }

    public <S> S generatorService(Class<S> serviceClass, String url) {
        return ServiceGenerator.createService(serviceClass, url);
    }

    public MineGsonConverterFactory generatorFactory() {
        if (mRequestBodyConverter == null && mResponseConstructor == null) {
            return MineGsonConverterFactory.create(mGson);
        } else {
            return MineGsonConverterFactory.create(mGson, mRequestBodyConverter, mResponseConstructor);
        }
    }
}
