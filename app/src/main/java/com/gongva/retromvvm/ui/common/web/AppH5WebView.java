package com.gongva.retromvvm.ui.common.web;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.gongva.library.app.ui.web.H5WebView;
import com.gongva.library.app.ui.web.H5WebViewClientCallBack;
import com.gongva.retromvvm.library.plugs.arouter.ARouterDispatcher;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;

/**
 * @author gongwei
 * @time 2020/03/26
 * @mail shmily__vivi@163.com
 */
@Deprecated //尽量使用X5WebView，某个版本以后webkit内核的WebView将不再维护
public class AppH5WebView extends H5WebView {

    //再嫁接一层CallBack，是因为一个App中可能多个地方使用WebView，他们会有一些公共需要处理的逻辑，如：唤起某个应用，则需要在此嫁接这一层处理
    protected H5WebViewClientCallBack _webViewClientCallBack;

    public AppH5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppH5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AppH5WebView(Context context) {
        super(context);
    }

    public void setH5WebViewClientCallBack(H5WebViewClientCallBack _webViewClientCallBack) {
        this._webViewClientCallBack = _webViewClientCallBack;
    }

    @Override
    protected H5WebViewClientCallBack getH5WebViewClientCallBack() {
        return new H5WebViewClientCallBack() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (_webViewClientCallBack != null) {
                    _webViewClientCallBack.onReceivedError(view, errorCode, description, failingUrl);
                }
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
        };
    }
}
