package com.gongva.retromvvm.repository.common.remote.interceptor;

import com.hik.core.android.api.LogCat;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * log定制拦截器
 *
 * @data 2019/3/13
 */
public class LogInterceptor implements Interceptor {

    private HttpLoggingInterceptor mHttpLoggingInterceptor;

    public LogInterceptor() {
        mHttpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
            LogCat.d("LogInterceptor", "message:" + message);
        });
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return mHttpLoggingInterceptor.intercept(chain);
        /*Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        LogCat.d("hik_net", requestStartMessage);
        return chain.proceed(request);*/
    }
}
