package com.gongva.library.app.ui.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.gongva.library.app.base.PermissionRequestListener;
import com.gongva.library.app.ui.web.protocol.JsBridgeParam;
import com.gongva.library.app.ui.web.protocol.JsBridgeProtocolHandler;
import com.hik.core.android.api.GsonUtil;
import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.io.FileProvider7;
import com.hik.core.android.api.io.FileUtil;
import com.hik.core.android.view.ViewUtil;
import com.gongva.library.app.base.ActivitySupport;
import com.gongva.library.app.base.IPermissionRequestListener;
import com.gongva.library.app.base.PermissionRequestListenerImpl;
import com.gongva.library.app.config.AppConfig;
import com.gongva.library.app.config.AppContext;
import com.gongva.library.app.ui.UI;
import com.gongva.library.app.ui.web.jsbridge.IJSBridgeManager;
import com.gongva.library.app.ui.web.jsbridge.JSBridgeManager;
import com.gongva.library.app.ui.web.jsbridge.X5BridgeWebViewClient;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * X5内核WebView
 *
 * @author gongwei
 * @time 2020/03/24
 * @mail shmily__vivi@163.com
 */
public abstract class X5WebView extends WebView {

    protected ProgressBar progressBar;
    private JSBridgeManager mJSBridgeManager;

    protected boolean initSettingCompleted = false;//WebSetting设置是否完成
    protected X5WebViewClientCallBack _clientCallBack;
    protected X5WebChromeClient _h5WebChromeClient;
    protected boolean scroll = true; //页面是否滚动,，内嵌到ScrollView中时考虑设置
    protected boolean wideViewPort = true; //是否完整适配屏幕，会导致字体变小，内嵌到ScrollView中时考虑设置
    protected int cacheMode = android.webkit.WebSettings.LOAD_DEFAULT; //缓存策略

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._clientCallBack = getX5H5WebViewClientCallBack();
        initJSManager(this);
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._clientCallBack = getX5H5WebViewClientCallBack();
        initJSManager(this);
    }

    public X5WebView(Context context) {
        super(context);
        this._clientCallBack = getX5H5WebViewClientCallBack();
        initJSManager(this);
    }

    private void initJSManager(X5WebView webView){
        mJSBridgeManager = new JSBridgeManager(webView);
    }

    /**
     * 子类请按需override
     *
     * @return
     */
    protected abstract X5WebViewClientCallBack getX5H5WebViewClientCallBack();

    @Override
    public void loadUrl(String url) {
        initWebSetting();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        initWebSetting();
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        initWebSetting();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadData(String data, @Nullable String mimeType, @Nullable String encoding) {
        initWebSetting();
        super.loadData(data, mimeType, encoding);
    }

    /**
     * 设置ProgressBar
     *
     * @param progressBar
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * 页面是否滚动,，内嵌到ScrollView中时考虑设置
     *
     * @param scroll
     */
    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    /**
     * 是否完整适配屏幕，会导致字体变小，内嵌到ScrollView中时考虑设置
     *
     * @param wideViewPort
     */
    public void setWideViewPort(boolean wideViewPort) {
        this.wideViewPort = wideViewPort;
        getSettings().setUseWideViewPort(wideViewPort);
    }

    /**
     * 设置缓存策略
     *
     * @param cacheMode
     */
    public void setCacheMode(int cacheMode) {
        this.cacheMode = cacheMode;
        getSettings().setCacheMode(cacheMode);
    }

    public void initWebSetting() {
        if (!initSettingCompleted) {
            initSettingCompleted = true;

            Activity activityHost = ViewUtil.getActivityFromView(this);

            setLayerType(View.LAYER_TYPE_NONE, null);
            setBackgroundColor(Color.TRANSPARENT);//设置背景色
            setWebChromeClient(_h5WebChromeClient = new X5WebChromeClient());
            setWebViewClient(new X5WebViewClient(mJSBridgeManager));

            WebSettings webSettings = getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 设置图片宽度不超过屏幕
            webSettings.setSavePassword(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBlockNetworkImage(false);
            webSettings.setUseWideViewPort(wideViewPort);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true);//设置可以访问文件
            webSettings.setCacheMode(cacheMode);
            webSettings.setUserAgentString(String.format("%s%s", webSettings.getUserAgentString(), AppConfig.WEB_USER_AGENT_MARK));//在UserAgent增加标识，用以web页面判断是否在我们的App中打开
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(0);//android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {// 遇到下载任务，交给系统浏览器处理
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                activityHost.startActivity(intent);
            });

            //check js bridge
            addJsBridgeHandler();
        }
    }

    /**
     * 添加Js Bridge
     */
    public void addJsBridgeHandler() {
        //数据类Handler
        mJSBridgeManager.registerHandler(JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA, (data, function) -> {
            LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA + "' 入参：" + data);
            if (TextUtils.isEmpty(data)) return;
            JsBridgeParam h5Param = GsonUtil.jsonDeserializer(data, JsBridgeParam.class);
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            if (h5Param != null && activityHost != null) {
                JsBridgeProtocolHandler.getInstance().handleActionProtocol(activityHost, h5Param, (String result) -> {
                    function.onCallBack(result);
                    LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA + "' 返回：" + result);
                });
            }
        });
        //动作类Handler
        mJSBridgeManager.registerHandler(JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION, (data, function) -> {
            LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION + "' 入参：" + data);
            if (TextUtils.isEmpty(data)) return;
            JsBridgeParam h5Param = GsonUtil.jsonDeserializer(data, JsBridgeParam.class);
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            if (h5Param != null && activityHost != null) {
                JsBridgeProtocolHandler.getInstance().handleActionProtocol(activityHost, h5Param, (String result) -> {
                    function.onCallBack(result);
                    LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION + "' 返回：" + result);
                });
            }
        });
    }

    /**
     * 发送数据给Js
     *
     * @param dataToJs
     */
    public void sendDataToJs(JsBridgeParam dataToJs) {
        String dataString = GsonUtil.jsonSerializer(dataToJs);
        mJSBridgeManager.callHandler(JsBridgeProtocolHandler.HN_NATIVE_2_JS_DATA, dataString, null);
        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_NATIVE_2_JS_DATA + "' ：" + dataString);
    }

    /**
     * 发送指令给Js
     *
     * @param dataToJs
     */
    public void sendActionToJs(JsBridgeParam dataToJs) {
        String dataString = GsonUtil.jsonSerializer(dataToJs);
        mJSBridgeManager.callHandler(JsBridgeProtocolHandler.HN_NATIVE_2_JS_ACTION, dataString, null);
        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_NATIVE_2_JS_ACTION + "' ：" + dataString);
    }

    /**
     * 通知H5所选图片，选择图片后调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public boolean onImageChooserCallBack(int requestCode, int resultCode, Intent data) {
        if (_h5WebChromeClient != null) {
            return _h5WebChromeClient.onImageChooserCallBack(requestCode, resultCode, data);
        }
        return false;
    }

    /**
     * 添加Js Interface
     */
    @SuppressLint("JavascriptInterface")
    public void addJsInterface(Object obj, String interfaceName) {
        if (getContext() instanceof Activity) {
            addJavascriptInterface(obj, interfaceName);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!scroll) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * X5 Web Chrome Client
     * 提供：
     * 1.加载进度控制
     * 2.拍照、相册与文件选择
     */
    public class X5WebChromeClient extends WebChromeClient implements IPermissionRequestListener {

        private Activity activityHost;
        private ViewGroup activityRootView;

        private ValueCallback<Uri> mUploadMessageAndroid5Below;
        private ValueCallback<Uri[]> mUploadMessageAndroid5Above;
        private PermissionRequestListenerImpl permissionRequestListener;
        private Uri imageUri;//拍照的照片的Uri，当ActivityResult ResultOK时，若data==null，则该Uri必然有值
        private String imageUriPath = FileUtil.getImageCacheFilePath();
        private boolean isCapture;//是否只拍照

        private View mCustomView;
        private FrameLayout mCustomViewLayout;
        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

        public X5WebChromeClient(){
            permissionRequestListener = new PermissionRequestListenerImpl();
            activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            activityRootView = (ViewGroup) ((ViewGroup) activityHost.findViewById(android.R.id.content)).getChildAt(0);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressBar != null) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
            if (_clientCallBack != null) {
                _clientCallBack.updateProgress(newProgress);
            }
        }

        // For Lollipop 5.0+ Devices
        @Override
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadMessageAndroid5Above = filePathCallback;
            isCapture = fileChooserParams.isCaptureEnabled();
            openSystemChooser();
            return true;
        }

        /**
         * 打开5.0以上的系统默认图片选择器
         */
        private void openSystemChooser() {
            ActivitySupport.requestRunPermission(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, this);
        }

        /**
         * 权限请求成功
         */
        @Override
        public void onGranted() {
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            imageUri = FileProvider7.getUriForFile(activityHost, new File(imageUriPath));

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            if (isCapture) {
                activityHost.startActivityForResult(captureIntent, AppContext.REQUEST_CODE_WEB_SELECT_FILE);
            } else {
                Intent photo = new Intent(Intent.ACTION_PICK);//采用相册和文件的选择方式，切近用户习惯
                //Intent photo = new Intent(Intent.ACTION_GET_CONTENT);//采用内容选择器的方式，选择方式更多，功能更强大
                photo.setType("image/*");

                Intent chooserIntent = Intent.createChooser(photo, "选择方式");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                activityHost.startActivityForResult(chooserIntent, AppContext.REQUEST_CODE_WEB_SELECT_FILE);
            }
        }

        /**
         * 权限请求失败
         */
        @Override
        public void onDenied(Activity activity, List<String> deniedPermission) {
            permissionRequestListener.onDenied(activity, deniedPermission);
            if (mUploadMessageAndroid5Above != null) {
                mUploadMessageAndroid5Above.onReceiveValue(null);
                mUploadMessageAndroid5Above = null;
            }
            if (mUploadMessageAndroid5Below != null) {
                mUploadMessageAndroid5Below.onReceiveValue(null);
                mUploadMessageAndroid5Below = null;
            }
        }

        /**
         * 通知H5所选图片，Activity Result的时候调用
         *
         * @param requestCode
         * @param resultCode
         * @param data
         */
        public boolean onImageChooserCallBack(int requestCode, int resultCode, Intent data) {
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AppContext.REQUEST_CODE_WEB_SELECT_FILE) {
                    if (mUploadMessageAndroid5Above != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Uri[] urisFromIntent = FileChooserParams.parseResult(resultCode, data);
                        Uri result = data == null ? imageUri : (urisFromIntent != null && urisFromIntent.length > 0) ? urisFromIntent[0] : null;
                        //修正图片可能存在的角度旋转问题
                        Uri rotateImage = amendRotateImage(activityHost, data == null ? imageUriPath : FileUtil.getImagePathFromUri(activityHost, result));
                        if (rotateImage != null) {
                            mUploadMessageAndroid5Above.onReceiveValue(new Uri[]{rotateImage});
                        } else if (result != null){
                            mUploadMessageAndroid5Above.onReceiveValue(new Uri[]{result});
                        } else {
                            mUploadMessageAndroid5Above.onReceiveValue(null);
                        }
                        mUploadMessageAndroid5Above = null;
                    }
                    return true;
                }
            } else {
                if (mUploadMessageAndroid5Above != null) {
                    mUploadMessageAndroid5Above.onReceiveValue(null);
                    mUploadMessageAndroid5Above = null;
                }
                if (mUploadMessageAndroid5Below != null) {
                    mUploadMessageAndroid5Below.onReceiveValue(null);
                    mUploadMessageAndroid5Below = null;
                }
            }
            return false;
        }

        /**
         * 修正图片可能的错误旋转角度，并返回新的Uri
         *
         * @param activity
         * @param path 原始图片路径
         */
        private Uri amendRotateImage(Activity activity, String path) {
            if (activity == null || TextUtils.isEmpty(path)) return null;

            // 得到修正后的Bitmap对象
            Bitmap originBitmap = FileUtil.getImage(path);
            // 保存修复后的图片
            String resultPath = FileUtil.getDCIMBasePath() + System.currentTimeMillis() + ".jpg";
            FileUtil.saveImageToSDCard(originBitmap, resultPath);
            // 通过图片路径生成新的Uri
            Uri uriByImagePath = FileUtil.makeUriByImagePath(activity, resultPath);
            return uriByImagePath;

        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            super.onShowCustomView(view, customViewCallback);
            showCustomView(view, customViewCallback);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
            super.onHideCustomView();
        }

        @SuppressLint("SourceLockedOrientationActivity")
        private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            if (mCustomViewLayout == null) {
                mCustomViewLayout = new FrameLayout(activityHost);
                mCustomViewLayout.setBackgroundColor(Color.parseColor("#FF000000"));
                mCustomViewLayout.addView(mCustomView);
            }
            activityRootView.addView(mCustomViewLayout);
            mCustomViewCallback = callback;
            X5WebView.this.setVisibility(View.GONE);

            activityHost.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            activityHost.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activityHost.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        @SuppressLint("SourceLockedOrientationActivity")
        private void hideCustomView() {
            X5WebView.this.setVisibility(View.VISIBLE);
            if (mCustomViewLayout == null || mCustomView == null) {
                return;
            }
            mCustomViewLayout.setVisibility(View.GONE);
            mCustomView.setVisibility(View.GONE);
            activityRootView.removeView(mCustomViewLayout);
            mCustomViewCallback.onCustomViewHidden();
            mCustomViewLayout = null;
            mCustomView = null;

            activityHost.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            activityHost.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activityHost.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    /**
     * X5 WebView Client
     * 提供：
     * 1.WebView加载生命周期回调；
     * 2.加载的Url拦截；
     */
    public class X5WebViewClient extends X5BridgeWebViewClient {

        protected static final int PERMISSIONS_REQUEST_PHONE = 209;

        public X5WebViewClient(IJSBridgeManager jsBridgeManager) {
            super(jsBridgeManager);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (_clientCallBack != null) {
                _clientCallBack.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (_clientCallBack != null) {
                _clientCallBack.onPageFinished(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (_clientCallBack != null) {
                _clientCallBack.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //拨打电话拦截
            if (parseSchemeTel(url)) {
                startCallTask(url);
                return true;
            }
            //支付宝支付拦截
            if (parseSchemeAliPay(url)) {
                startCustomTask(url, "未检测到支付宝客户端");
                return true;
            }
            //微信支付拦截
            if (parseSchemeWx(url)) {
                startCustomTask(url, "未检测到微信客户端");
                return true;
            }

            boolean handled = false;
            if (_clientCallBack != null) {
                handled = _clientCallBack.shouldOverrideUrlLoading(view, url);
            }
            if (!handled) {
                String replacedUrl = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");//把url中单独的%字符换成%25
                handled = super.shouldOverrideUrlLoading(view, replacedUrl);
            }
            return handled;
        }

        /**
         * 校验是否为拨打电话的Uri
         *
         * @param url
         * @return
         */
        private boolean parseSchemeTel(String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            } else {
                return url.startsWith("tel:");
            }
        }

        /**
         * 校验是否为支付宝的Uri
         * 兼容旧版支付宝网页支付
         *
         * 支付宝网页支付是先后调用如下网页
         * 1.https://mobilecodec.alipay.com/client_download.htm?qrcode=bax05351pgjhc4yegd2y2084
         * 2.https://ds.alipay.com/from=mobilecodec&scheme=alipayqr%3A%2F%2Fplatformapi%2Fstartapp%3FsaId%3D10000007%26clientVersion%3D3.7.0.0718%26qrcode%3Dhttps%253A%252F%252Fqr.alipay.com%252Fbax05351pgjhc4yegd2y2084%253F_s%253Dweb-other
         * 之后返回一个意图，也是用这个意图来打开支付宝app
         * intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fbax05351pgjhc4yegd2y2084%3F_s%3Dwebother&_t=1474448799004#Intent;scheme=alipayqr;package=com.eg.android.AlipayGphone;end
         * <p>
         * 基于以上而做的拦截
         *
         * @param url
         * @return
         */
        public boolean parseSchemeAliPay(String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            String urlTemp = url.toLowerCase();
            return urlTemp.startsWith("alipays://") || urlTemp.contains("platformapi/startapp");
        }

        /**
         * 校验是否为微信的Uri
         *
         * @param url
         * @return
         */
        public boolean parseSchemeWx(String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            } else {
                return url.startsWith("weixin://");
            }
        }

        /**
         * 唤起拨打电话应用
         *
         * @param url
         */
        private void startCallTask(String url) {
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            if (activityHost == null) {
                return;
            }
            ActivitySupport.requestRunPermission(activityHost, new String[]{Manifest.permission.CALL_PHONE}, new PermissionRequestListener() {
                @Override
                public void onGranted() {
                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityHost.startActivity(intentCall);
                }
            });
        }

        /**
         * 唤起第三方App
         *
         * @param url
         * @param errorMsg 唤起失败时的文案
         */
        private void startCustomTask(String url, String errorMsg) {
            Activity activityHost = ViewUtil.getActivityFromView(X5WebView.this);
            try {
                activityHost.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                UI.showConfirmDialog(activityHost, errorMsg, "知道了", null);
            }
        }
    }
}
