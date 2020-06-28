package com.gongva.library.plugin.netbase.interceptor;

import android.text.TextUtils;

import com.gongva.library.plugin.netbase.NetConstant;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 进度下载拦截器
 *
 * @data 2019/3/8
 */
public class DownloadProgressInterceptor implements Interceptor {

    private ConcurrentHashMap<String, DownloadProgressListener> mProgressListenerMap;
    private String mCurrentDownUrl;

    public DownloadProgressInterceptor() {
        this(null, null);
    }

    public DownloadProgressInterceptor(String urlPath, DownloadProgressListener progressListener) {
        mProgressListenerMap = new ConcurrentHashMap<>();
        if (progressListener != null && !TextUtils.isEmpty(urlPath)) {
            mProgressListenerMap.put(urlPath, progressListener);
        }
    }

    public void addProgressListener(String urlPath, DownloadProgressListener progressListener) {
        if (mProgressListenerMap != null && progressListener != null && !TextUtils.isEmpty(urlPath)) {
            mProgressListenerMap.put(urlPath, progressListener);
        }
    }

    public void removeProgressListener(String urlPath) {
        if (mProgressListenerMap != null && !TextUtils.isEmpty(urlPath)) {
            mProgressListenerMap.remove(urlPath);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        mCurrentDownUrl = request.url().url().toString();
        Response originalResponse = chain.proceed(request);
        ResponseBody body = originalResponse.body();
        if (body != null && body.contentType() != null) {
            MediaType mediaType = body.contentType();//获取返回结果的type
            if (NetConstant.MiniType.APPLICATION.equals(mediaType.type())
                    && NetConstant.MiniType.APPLICATION_JSON.equals(mediaType.subtype())) {
                return originalResponse;
            } else {//Json外的数据默认都支持下载
                return originalResponse.newBuilder()
                        .body(new DownloadProgressResponseBody(originalResponse.body()))
                        .build();
            }
        } else {
            return originalResponse;
        }
    }

    private class DownloadProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;

        public DownloadProgressResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                    DownloadProgressListener progressInterceptor = mProgressListenerMap.get(mCurrentDownUrl);
                    if (null != progressInterceptor) {
                        progressInterceptor.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                        if (bytesRead == -1) {
                            removeProgressListener(mCurrentDownUrl);
                        }
                    }
                    return bytesRead;
                }
            };
        }
    }

    public interface DownloadProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
