package com.gongva.retromvvm.repository.common.remote.interceptor;

import android.text.TextUtils;

import com.gongva.library.plugin.netbase.NetConstant;
import com.gongva.library.plugin.netbase.RetrofitManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hik.core.android.api.LogCat;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 单点登录拦截器(可包含登录失效、账号冻结等)
 *
 * @data 2019/3/8
 */
public class SSOInterceptor implements Interceptor {

    private final String TAG = "SSOInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private KickOutListener mKickOutListener;

    public interface KickOutListener {
        void onKickOut(int code, String msg);
    }

    public void setKickOutListener(KickOutListener kickoutListener) {
        mKickOutListener = kickoutListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (!HttpHeaders.hasBody(response)) {
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            MediaType contentType = responseBody.contentType();
            if (contentType != null && NetConstant.MiniType.APPLICATION.equals(contentType.type())
                    && NetConstant.MiniType.APPLICATION_JSON.equals(contentType.subtype())) {//只拦截json数据
                Charset charset = contentType.charset(UTF8);
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                if (!isPlaintext(buffer)) {
                    logTip(request, contentType + " content(size=" + buffer.size() + ")");
                    return response;
                }

                if (contentLength != 0) {
                    String result = buffer.clone().readString(charset);
                    handleSSO(request, result, response);
                }
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private void handleSSO(Request request, String result, Response response) {
        try {
            logTip(request, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(result)) {
            return;
        }
        try {
            JsonObject jObj = new JsonParser().parse(result).getAsJsonObject();
            int code = jObj.has("code") ? jObj.get("code").getAsInt() : 0;
            String message = jObj.has("msg") ? jObj.get("msg").getAsString() : "";
            if (RetrofitManager.getInstance().getNetInterceptorConfig().isInterceptorCode(code)) {
                //Close the response
                //response.close();
                if (null != mKickOutListener) {
                    mKickOutListener.onKickOut(code, message);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            LogCat.w(TAG, "request url=" + request.url() + "\n   result=" + result + "\n   not a response structure:" + e.getMessage());
        }
    }

    private void logTip(Request request, String result) throws IOException {
        StringBuilder builder = new StringBuilder();
        if ("POST".equals(request.method())) {
            RequestBody requestBody = request.body();
            builder.append("POST request \n");
            builder.append("{\n");
            builder.append("  POST request url=" + request.url() + "\n");
            builder.append("  Token=" + request.headers().get("Token") + "\n");
            builder.append("  Pvt-Token=" + request.headers().get("Pvt-Token") + "\n");
            builder.append("  RequestParams length : " + requestBody.contentLength() + "\n");
            if (bodyHasUnknownEncoding(request.headers())) {
                builder.append("  RequestParams : (encoded body omitted) \n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (isPlaintext(buffer)) {
                    String temp = buffer.readString(charset);
                    if (!TextUtils.isEmpty(temp) && temp.length() > 2048) {
                        temp = temp.substring(0, 2048) + "...";
                    }
                    builder.append("  RequestParams : " + temp + "\n");
                } else {
                    builder.append("  RequestParams : (not plain request body omitted) \n");
                }
            }
            builder.append("  response:" + result + "\n");
            builder.append("}");
        } else {
            builder.append("GET request \n");
            builder.append("{\n");
            builder.append("  GET request url=" + request.url() + "\n");
            builder.append("  Token=" + request.headers().get("Token") + "\n");
            builder.append("  Pvt-Token=" + request.headers().get("Pvt-Token") + "\n");
            builder.append("  response:" + result + "\n");
            builder.append("}");
        }
        LogCat.d(TAG, builder.toString());
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}

