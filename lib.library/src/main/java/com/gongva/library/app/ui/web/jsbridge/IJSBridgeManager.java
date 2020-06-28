package com.gongva.library.app.ui.web.jsbridge;

import java.util.List;

/**
 * JSBridgeManager接口
 *
 * @date 2020/3/30
 */
public interface IJSBridgeManager {
    /**
     * 对应webView的setVerticalScrollBarEnabled方法
     *
     * @param verticalScrollBarEnabled
     */
    void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled);

    /**
     * 对应webView的setHorizontalScrollBarEnabled方法
     *
     * @param horizontalScrollBarEnabled
     */
    void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled);

    /**
     * 对应webView.WebSettings的setJavaScriptEnabled方法
     *
     * @param flag
     */
    void setJavaScriptEnabled(boolean flag);

    /**
     * 对应webView的setWebContentsDebuggingEnabled方法
     *
     * @param enabled
     */
    void setWebContentsDebuggingEnabled(boolean enabled);

    /**
     * 对应webView的setWebViewClient方法，参数和调用者通过类型判定
     */
    void setWebViewClient();

    /**
     * 对应webView的setWebViewClient方法，调用者通过通过类型判定
     */
    void loadUrl(String url);

    /**
     * JSBridgeManager获取启动消息队列
     *
     * @return
     */
    List<Message> getStartupMessage();

    /**
     * JSBridgeManager 设置启动消息队列
     *
     * @return startupMessage
     */
    void setStartupMessage(List<Message> startupMessage);

    /**
     * JSBridgeManager 处理返回数据
     *
     * @param url
     */
    void handlerReturnData(String url);

    /**
     * JSBridgeManager 处理消息队列数据
     */
    void flushMessageQueue();

    /**
     * JSBridgeManager 分发消息
     *
     * @param m
     */
    void dispatchMessage(Message m);

    /**
     * JSBridgeManager js调用
     *
     * @param jsUrl
     * @param returnCallback
     */
    void loadUrl(String jsUrl, CallBackFunction returnCallback);

    /**
     * JSBridgeManager 注册native方法供js调用
     *
     * @param handlerName
     * @param handler
     */
    void registerHandler(String handlerName, BridgeHandler handler);

    /**
     * JSBridgeManager js调用native方法
     *
     * @param handlerName
     * @param data
     * @param callBack
     */
    void callHandler(String handlerName, String data, CallBackFunction callBack);
}
