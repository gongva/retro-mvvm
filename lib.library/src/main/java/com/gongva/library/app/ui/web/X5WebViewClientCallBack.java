package com.gongva.library.app.ui.web;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;

/**
 * X5WebView的回调
 * 将WebView加载生命周期方法和加载进度控制，暴露给需要的Activity
 *
 * @author gongwei
 * @time 2020/06/28
 * @mail shmily__vivi@163.com
 */
public abstract class X5WebViewClientCallBack {

    public abstract void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    public void onPageFinished(WebView view, String url) {
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public void updateProgress(int progress) {
    }
}
