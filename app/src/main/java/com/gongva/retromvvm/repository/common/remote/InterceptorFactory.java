package com.gongva.retromvvm.repository.common.remote;

import android.support.annotation.NonNull;

import com.gongva.library.plugin.netbase.interceptor.DownloadProgressInterceptor;
import com.gongva.library.plugin.netbase.interceptor.HeaderInterceptor;
import com.gongva.retromvvm.repository.common.remote.interceptor.LogInterceptor;
import com.gongva.retromvvm.repository.common.remote.interceptor.SSOInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * 网络拦截器工厂
 * 包含各类拦截器
 *
 * @data 2019/3/13
 */
public class InterceptorFactory {

    private static volatile InterceptorFactory INSTANCE;
    private List<Interceptor> mInterceptorList;

    private HeaderInterceptor mHeaderInterceptor;
    private SSOInterceptor mSSOInterceptor;
    private LogInterceptor mLogInterceptor;
    private DownloadProgressInterceptor mProgressInterceptor;

    public static InterceptorFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (InterceptorFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InterceptorFactory();
                }
            }
        }
        return INSTANCE;
    }

    public InterceptorFactory() {
        mInterceptorList = new ArrayList<>();
        mHeaderInterceptor = new HeaderInterceptor();
        mSSOInterceptor = new SSOInterceptor();
        mLogInterceptor = new LogInterceptor();
        mProgressInterceptor = new DownloadProgressInterceptor();

        mInterceptorList.add(mHeaderInterceptor);
        mInterceptorList.add(mSSOInterceptor);
        mInterceptorList.add(mLogInterceptor);
        mInterceptorList.add(mProgressInterceptor);
    }

    public void setKickOutListener(SSOInterceptor.KickOutListener kickoutListener) {
        mSSOInterceptor.setKickOutListener(kickoutListener);
    }

    public List<Interceptor> getInterceptorList() {
        return mInterceptorList;
    }

    /**
     * 添加下载进度跟踪
     *
     * @param urlPath          下载路径
     * @param progressListener 跟踪回调
     */
    public void addProgressListener(@NonNull String urlPath, @NonNull DownloadProgressInterceptor.DownloadProgressListener progressListener) {
        if (progressListener != null && mProgressInterceptor != null) {
            mProgressInterceptor.addProgressListener(urlPath, progressListener);
        }
    }

    /**
     * 删除添加的下载进度跟踪
     *
     * @param urlPath 下载路径
     */
    public void removeProgressListener(@NonNull String urlPath) {
        if (mProgressInterceptor != null) {
            mProgressInterceptor.removeProgressListener(urlPath);
        }
    }
}
