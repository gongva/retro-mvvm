package com.gongva.retromvvm.ui.common.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.gongva.library.app.ui.web.X5WebView;
import com.gongva.library.app.ui.web.X5WebViewClientCallBack;
import com.gongva.retromvvm.library.plugs.arouter.ARouterDispatcher;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * X5内核的WebView实例
 *
 * @author gongwei
 * @time 2020/03/27
 * @mail shmily__vivi@163.com
 */
public class AppX5WebView extends X5WebView {

    //再嫁接一层CallBack，是因为一个App中可能多个地方使用WebView，他们会有一些公共需要处理的逻辑，如：唤起某个应用，则需要在此嫁接这一层处理
    protected X5WebViewClientCallBack _webViewClientCallBack;

    public AppX5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppX5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AppX5WebView(Context context) {
        super(context);
    }

    @Override
    public void loadUrl(String url) {
        super.initWebSetting();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.initWebSetting();
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        super.initWebSetting();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadData(String data, @Nullable String mimeType, @Nullable String encoding) {
        super.initWebSetting();
        super.loadData(data, mimeType, encoding);
    }

    public void setWebViewClientCallBack(X5WebViewClientCallBack _webViewClientCallBack) {
        this._webViewClientCallBack = _webViewClientCallBack;
    }

    @Override
    protected X5WebViewClientCallBack getX5H5WebViewClientCallBack() {
        return new X5WebViewClientCallBack() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                if (_webViewClientCallBack != null) {
                    _webViewClientCallBack.onReceivedError(webView, i, s, s1);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (_webViewClientCallBack != null) {
                    _webViewClientCallBack.onPageStarted(view, url, favicon);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (_webViewClientCallBack != null) {
                    _webViewClientCallBack.onPageFinished(view, url);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 判断是否为本App的RouteUrl，是则拦截并路由；
                // 或者为唤起本公司其他App的Scheme，是则拦截并唤起；
                if (!TextUtils.isEmpty(url) && (url.startsWith(ARouterPath.ROUTE_SCHEME_HOST) || ARouterPath.isDeepLinkScheme(url))) {
                    ARouterDispatcher.dispatch(url);
                    return true;
                }
                if (_webViewClientCallBack != null) {
                    return _webViewClientCallBack.shouldOverrideUrlLoading(view, url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void updateProgress(int progress) {
                if (_webViewClientCallBack != null) {
                    _webViewClientCallBack.updateProgress(progress);
                }
                super.updateProgress(progress);
            }
        };
    }
}
