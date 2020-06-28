package com.gongva.library.app.ui.web.jsbridge;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class JSBridgeManager implements WebViewJavascriptBridge, IJSBridgeManager {

    private final String TAG = "BridgeWebView";

    private android.webkit.WebView mLocalWebView;
    private com.tencent.smtt.sdk.WebView mTencentWebView;
    public static final String toLoadJs = "WebViewJavascriptBridge.js";
    Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
    Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
    BridgeHandler defaultHandler = new DefaultHandler();

    private List<Message> startupMessage = new ArrayList<Message>();

    private long uniqueId = 0;

    public JSBridgeManager(@NonNull android.webkit.WebView localWebView) {
        this.mLocalWebView = localWebView;
        init();
    }

    public JSBridgeManager(@NonNull com.tencent.smtt.sdk.WebView tencentWebView) {
        this.mTencentWebView = tencentWebView;
        init();
    }

    @Override
    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    @Override
    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        if (mLocalWebView != null) {
            mLocalWebView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        } else if (mTencentWebView != null) {
            mTencentWebView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        if (mLocalWebView != null) {
            mLocalWebView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        } else if (mTencentWebView != null) {
            mTencentWebView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }

    @Override
    public void setJavaScriptEnabled(boolean flag) {
        if (mLocalWebView != null) {
            mLocalWebView.getSettings().setJavaScriptEnabled(flag);
        } else if (mTencentWebView != null) {
            mTencentWebView.getSettings().setJavaScriptEnabled(flag);
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }

    @Override
    public void setWebContentsDebuggingEnabled(boolean enabled) {
        if (mLocalWebView != null) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(enabled);
        } else if (mTencentWebView != null) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(enabled);
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }

    @Override
    public void setWebViewClient() {
        if (mLocalWebView != null) {
            mLocalWebView.setWebViewClient(new LocalBridgeWebViewClient(this));
        } else if (mTencentWebView != null) {
            mTencentWebView.setWebViewClient(new X5BridgeWebViewClient(this));
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }

    @Override
    public void loadUrl(String url) {
        if (mLocalWebView != null) {
            mLocalWebView.loadUrl(url);
        } else if (mTencentWebView != null) {
            mTencentWebView.loadUrl(url);
        } else {
            throw new IllegalStateException("WebView is empty ,you must inject WebView instance first");
        }
    }


    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     *                if js message has handler name, it will be handled by named handlers registered by native
     */
    public void setDefaultHandler(BridgeHandler handler) {
        this.defaultHandler = handler;
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
        setWebViewClient();
    }

    @Override
    public void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        CallBackFunction f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.onCallBack(data);
            responseCallbacks.remove(functionName);
            return;
        }
    }

    @Override
    public void send(String data) {
        send(data, null);
    }

    @Override
    public void send(String data, CallBackFunction responseCallback) {
        doSend(null, data, responseCallback);
    }

    private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
        Message m = new Message();
        if (!TextUtils.isEmpty(data)) {
            m.setData(data);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            responseCallbacks.put(callbackStr, responseCallback);
            m.setCallbackId(callbackStr);
        }
        if (!TextUtils.isEmpty(handlerName)) {
            m.setHandlerName(handlerName);
        }
        queueMessage(m);
    }

    private void queueMessage(Message m) {
        if (startupMessage != null) {
            startupMessage.add(m);
        } else {
            dispatchMessage(m);
        }
    }

    @Override
    public void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.loadUrl(javascriptCommand);
        }
    }

    @Override
    public void flushMessageQueue() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                    // deserializeMessage
                    List<Message> list = null;
                    try {
                        list = Message.toArrayList(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Message m = list.get(i);
                        String responseId = m.getResponseId();
                        // 是否是response
                        if (!TextUtils.isEmpty(responseId)) {
                            CallBackFunction function = responseCallbacks.get(responseId);
                            String responseData = m.getResponseData();
                            function.onCallBack(responseData);
                            responseCallbacks.remove(responseId);
                        } else {
                            CallBackFunction responseFunction = null;
                            // if had callbackId
                            final String callbackId = m.getCallbackId();
                            if (!TextUtils.isEmpty(callbackId)) {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void onCallBack(String data) {
                                        Message responseMsg = new Message();
                                        responseMsg.setResponseId(callbackId);
                                        responseMsg.setResponseData(data);
                                        queueMessage(responseMsg);
                                    }
                                };
                            } else {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void onCallBack(String data) {
                                        // do nothing
                                    }
                                };
                            }
                            BridgeHandler handler;
                            if (!TextUtils.isEmpty(m.getHandlerName())) {
                                handler = messageHandlers.get(m.getHandlerName());
                            } else {
                                handler = defaultHandler;
                            }
                            if (handler != null) {
                                handler.handler(m.getData(), responseFunction);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
        this.loadUrl(jsUrl);
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
    }

    /**
     * register handler,so that javascript can call it
     *
     * @param handlerName
     * @param handler
     */
    @Override
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if (handler != null) {
            messageHandlers.put(handlerName, handler);
        }
    }

    /**
     * call javascript registered handler
     *
     * @param handlerName
     * @param data
     * @param callBack
     */
    @Override
    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
    }
}
