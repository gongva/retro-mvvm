package com.gongva.library.app.ui.web.protocol;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.hik.core.android.api.GsonUtil;
import com.gongva.library.app.ui.web.entity.JsOpenUrlScheme;
import com.gongva.library.app.ui.web.entity.JsUrl;

/**
 * JsBridge 的协议解析器
 *
 * @author gongwei
 * @date 2019/3/6
 */
public abstract class JsBridgeProtocolHandler {

    /**
     * JS->Native
     */
    //数据类Handler Name
    public static final String HN_JS_2_NATIVE_DATA = "com.gongva.native_echo.data";//todo company's data echo
    //动作类Handler Name
    public static final String HN_JS_2_NATIVE_ACTION = "com.gongva.native_echo.action";//todo company's action echo
    /**
     * Native->JS
     */
    //数据类Handler Name
    public static final String HN_NATIVE_2_JS_DATA = "com.gongva.javascript_echo.data";//todo company's data echo
    //动作类Handler Name
    public static final String HN_NATIVE_2_JS_ACTION = "com.gongva.javascript_echo.action";//todo company's action echo

    //----------JS调Native----------
    /**
     * type:获取用户信息
     * param:null
     */
    public static final String DATA_USER_INFO = "userInfo";
    /**
     * type:获取app设备相关信息
     * param:null
     */
    public static final String DATA_APP_INFO = "appInfo";
    /**
     * type:原生页面跳转
     * param:"{routeUrl: ""}"
     */
    public static final String ACTION_ROUTE = "route";
    /**
     * type:关闭原生界面
     * param:null
     */
    public static final String ACTION_CLOSE_NATIVE_PAGE = "closeNativePage";
    /**
     * type:JS发现用户登录状态（token）不可用
     * param:"{"msg": "登录不可用的具体信息"}"
     */
    public static final String ACTION_TOKEN_INVALID = "tokenInvalidHandler";
    /**
     * type:JS发现用户被强制下线
     * param:"{"msg": "被踢提示消息"}"
     */
    public static final String ACTION_FORCED_OFFLINE = "forcedOfflineHandler";
    /**
     * type:JS发现用户账号被冻结
     * param:"{"msg": "冻结提示信息"}"
     */
    public static final String ACTION_FROZEN = "frozenAccountHandler";
    /**
     * type:保存图片
     * param:"{"base64": "图片Base64"}"
     */
    public static final String ACTION_SAVE_IMAGE = "saveImage";
    /**
     * type:跳转到第三方应用，通过URL Scheme协议
     * param:"{"urlScheme":"第三方的URL Scheme协议，例如 weixin://"}"
     */
    public static final String ACTION_OPEN_URL_SCHEME = "openUrlScheme";
    /**
     * type:文件下载
     * param:"{"url":"文件下载地址"}"
     */
    public static final String ACTION_DOWNLOAD_FILE = "downloadFile";


    //----------Native调JS----------
    /**
     * 原生点击了界面退出按钮，告知JS做相应的处理，原生不做退出界面的处理（只在JS通过nativeBackControl协议告知原生需要做返回拦截的情况下调用）
     */
    public static final String ACTION_NATIVE_BACK_CLICK = "nativeBackClick";


    protected static JsBridgeProtocolHandler INSTANCE = null;

    public static JsBridgeProtocolHandler getInstance() {
        return INSTANCE;
    }

    public static void setInstance(JsBridgeProtocolHandler instance) {
        INSTANCE = instance;
    }

    /**
     * 处理协议
     *
     * @param param
     * @return
     */
    public void handleActionProtocol(Activity activity, JsBridgeParam param, JsBridgeProtocolCallBack callBack) {
        if (INSTANCE == null || param == null || TextUtils.isEmpty(param.getType())) {
            return;
        }
        switch (param.getType()) {
            case DATA_USER_INFO:
                INSTANCE.handleUserInfo(callBack);
                break;
            case DATA_APP_INFO:
                INSTANCE.handleAppInfo(callBack);
                break;
            case ACTION_ROUTE:
                INSTANCE.handleRouter(activity, param.getParams());
                break;
            case ACTION_CLOSE_NATIVE_PAGE:
                INSTANCE.closeNativePage(activity);
                break;
            case ACTION_TOKEN_INVALID:
                INSTANCE.tokenInvalidHandler(param.getParams());
                break;
            case ACTION_FORCED_OFFLINE:
                INSTANCE.forcedOfflineHandler(param.getParams());
                break;
            case ACTION_FROZEN:
                INSTANCE.frozenHandler(param.getParams());
                break;
            case ACTION_SAVE_IMAGE:
                INSTANCE.saveImage(activity, param.getParams());
                break;
            case ACTION_OPEN_URL_SCHEME:
                INSTANCE.openUrlScheme(activity, param.getParams());
                break;
            case ACTION_DOWNLOAD_FILE:
                INSTANCE.downloadFile(activity, param.getParams());
                break;
        }
    }

    protected abstract void handleUserInfo(JsBridgeProtocolCallBack callBack);

    protected abstract void handleAppInfo(JsBridgeProtocolCallBack callBack);

    protected abstract void handleRouter(Activity activity, String params);

    protected abstract void closeNativePage(Activity activity);

    protected abstract void tokenInvalidHandler(String params);

    protected abstract void forcedOfflineHandler(String params);

    protected abstract void frozenHandler(String params);

    protected abstract void saveImage(Activity activity, String params);

    /**
     * 通过Scheme唤起第三方应用
     *
     * @param activity
     * @param params
     */
    protected void openUrlScheme(Activity activity, String params) {
        JsOpenUrlScheme jsOpenUrlScheme = GsonUtil.jsonDeserializer(params, JsOpenUrlScheme.class);
        if (activity != null && jsOpenUrlScheme != null && !TextUtils.isEmpty(jsOpenUrlScheme.getUrlScheme())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(jsOpenUrlScheme.getUrlScheme()));
            activity.startActivity(intent);
        }
    }

    /**
     * 文件下载，交给系统浏览器处理
     *
     * @param activity
     * @param params
     */
    protected void downloadFile(Activity activity, String params) {
        JsUrl jsUrl = GsonUtil.jsonDeserializer(params, JsUrl.class);
        if (activity != null && jsUrl != null && !TextUtils.isEmpty(jsUrl.getUrl())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(jsUrl.getUrl()));
            activity.startActivity(intent);
        }
    }

    /**
     * Native->Js 协议类型：action
     * 原生点击了界面退出按钮，告知JS做相应的处理，原生不做退出界面的处理（只在JS通过nativeBackControl协议告知原生需要做返回拦截的情况下调用）
     *
     * @return
     */
    public JsBridgeParam getNativeBackClickParam() {
        JsBridgeParam param = new JsBridgeParam();
        param.setType(ACTION_NATIVE_BACK_CLICK);
        return param;
    }

    public interface JsBridgeProtocolCallBack {
        void call(String result);
    }
}
