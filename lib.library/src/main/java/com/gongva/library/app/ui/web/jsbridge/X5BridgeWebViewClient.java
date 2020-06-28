package com.gongva.library.app.ui.web.jsbridge;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class X5BridgeWebViewClient extends WebViewClient {
    private IJSBridgeManager mJsBridgeManager;

    public X5BridgeWebViewClient(IJSBridgeManager webView) {
        this.mJsBridgeManager = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            mJsBridgeManager.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            mJsBridgeManager.flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        webViewLoadLocalJs(view);
        if (mJsBridgeManager.getStartupMessage() != null) {
            for (Message m : mJsBridgeManager.getStartupMessage()) {
                mJsBridgeManager.dispatchMessage(m);
            }
            mJsBridgeManager.setStartupMessage(null);
        }
    }

    private void webViewLoadLocalJs(WebView view) {
        String jsContent = BridgeUtil.assetFile2Str(view.getContext(), BridgeWebView.toLoadJs);
        view.loadUrl("javascript:" + jsContent);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}