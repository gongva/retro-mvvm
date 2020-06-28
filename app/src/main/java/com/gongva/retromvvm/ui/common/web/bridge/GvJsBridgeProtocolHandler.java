package com.gongva.retromvvm.ui.common.web.bridge;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.base.ActivitySupport;
import com.gongva.library.app.base.PermissionRequestListener;
import com.gongva.library.app.ui.UI;
import com.gongva.library.app.ui.web.protocol.JsBridgeParam;
import com.gongva.library.app.ui.web.protocol.JsBridgeProtocolHandler;
import com.gongva.library.app.ui.web.entity.JsCallBackAppInfo;
import com.gongva.library.app.ui.web.entity.JsCallBackUserInfo;
import com.gongva.library.app.ui.web.entity.JsBase64;
import com.gongva.library.app.ui.web.entity.JsMsg;
import com.gongva.library.app.ui.web.entity.JsRoute;
import com.gongva.retromvvm.common.GvConfig;
import com.gongva.retromvvm.common.manager.location.LocationManager;
import com.gongva.retromvvm.library.plugs.arouter.ARouterDispatcher;
import com.gongva.retromvvm.library.utils.Tools;
import com.gongva.retromvvm.repository.common.remote.HeaderUtils;
import com.gongva.retromvvm.ui.common.web.entity.H5CallBackLocation;
import com.gongva.retromvvm.ui.common.web.entity.JsGetLocation;
import com.gongva.retromvvm.ui.common.web.entity.JsQRScanDes;
import com.hik.core.android.api.DeviceUtil;
import com.hik.core.android.api.GsonUtil;
import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.ScreenUtil;
import com.hik.core.android.api.io.FileUtil;
import com.hik.core.java.security.Base64;

/**
 * JsBridge 的协议解析处理器
 *
 * @author gongwei
 * @date 2019/4/18
 * @mail shmily__vivi@163.com
 */
public class GvJsBridgeProtocolHandler extends JsBridgeProtocolHandler {
    /**
     * type:获取用户当前实时的位置信息
     * param:"{"needSettingGuide": true}"//如果用户没开GPS相关权限，是否需要Native弹提示框引导用户打开?
     */
    public static final String ACTION_GET_LOCATION = "getLocation";

    /**
     * 扫描地址二维码或者其它二维码
     */
    public static final String ACTION_SCAN_QR_CODE = "scanQRCode";

    private JsCallBackAppInfo mAppInfo;

    public GvJsBridgeProtocolHandler() {
        //与设备有关的不变的信息，只需构造一次
        mAppInfo = new JsCallBackAppInfo();
        LogCat.i("1");
        mAppInfo.setVersion(GvConfig.getAppVersionName());
        LogCat.i("3");
        mAppInfo.setOsType(GvConfig.PHONE_TYPE_ANDROID);
        LogCat.i("4");
        mAppInfo.setOsVersion(String.valueOf(Build.VERSION.SDK_INT));
        LogCat.i("5");
        DisplayMetrics metrics = ScreenUtil.getDisplayMetrics(TinkerApplicationCreate.getApplication());
        LogCat.i("13");
        mAppInfo.setResolution(metrics.heightPixels + "x" + metrics.widthPixels);
        LogCat.i("14");
        mAppInfo.setDeviceBrand(Build.BRAND);
        LogCat.i("15");
    }

    @Override
    public void handleActionProtocol(Activity activity, JsBridgeParam param, JsBridgeProtocolCallBack callBack) {
        if (param == null || TextUtils.isEmpty(param.getType())) {
            return;
        }
        switch (param.getType()) {
            //Gv: App独有的JsBridge协议在这里扩展
            case ACTION_GET_LOCATION:
                getLocation(param.getParams(), callBack);
                break;
            case ACTION_SCAN_QR_CODE:
                callQRScan(activity, param.getParams(), callBack);
                break;
            default:
                super.handleActionProtocol(activity, param, callBack);
        }
    }

    /**
     * 获取用户当前位置信息
     * return:H5CallBackLocation
     */
    protected void getLocation(String params, JsBridgeProtocolCallBack callBack) {
        if (TextUtils.isEmpty(params) || callBack == null) return;
        JsGetLocation jsGetLocation = GsonUtil.jsonDeserializer(params, JsGetLocation.class);
        if (jsGetLocation != null) {
            LocationManager.startLocationOnline(jsGetLocation.isNeedSettingGuide(), bdLocation -> {
                String result = null;
                if (bdLocation != null) {
                    H5CallBackLocation location = new H5CallBackLocation();
                    location.setLng(String.valueOf(bdLocation.getLongitude()));
                    location.setLat(String.valueOf(bdLocation.getLatitude()));
                    location.setProvinceName(bdLocation.getProvince());
                    location.setCityCode(bdLocation.getAdCode());
                    location.setCityName(bdLocation.getCity());
                    location.setCountyName(bdLocation.getDistrict());
                    location.setStreet(bdLocation.getStreet());
                    location.setAddress(bdLocation.getAddrStr());
                    result = GsonUtil.jsonSerializer(location);
                }
                callBack.call(result);
            });
        }
    }

    /**
     * 二维码回调
     */
    protected void callQRScan(Activity activity, String params, JsBridgeProtocolCallBack callBack) {
        JsQRScanDes jsQRScanDes = GsonUtil.jsonDeserializer(params, JsQRScanDes.class);
        if (jsQRScanDes != null) {
            // todo
            // start company's qr code capture Activity
            // and then
            // callBack.call(scanResult);
        }
    }

    /**
     * 获取用户信息
     * return:H5CallBackUserInfo
     */
    @Override
    protected void handleUserInfo(JsBridgeProtocolCallBack callBack) {
        if (callBack != null) {
            JsCallBackUserInfo userInfoParam = new JsCallBackUserInfo();
            userInfoParam.setToken(HeaderUtils.getHeaderToken());
            callBack.call(GsonUtil.jsonSerializer(userInfoParam));
        }
    }

    /**
     * 获取app设备相关信息
     * return:H5CallBackUserInfo
     */
    @Override
    protected void handleAppInfo(JsBridgeProtocolCallBack callBack) {
        if (callBack != null) {
            callBack.call(GsonUtil.jsonSerializer(mAppInfo));
        }
    }

    /**
     * 原生页面跳转
     * return:null
     */
    @Override
    protected void handleRouter(Activity activity, String params) {
        if (!TextUtils.isEmpty(params)) {
            JsRoute jsRoute = GsonUtil.jsonDeserializer(params, JsRoute.class);
            if (jsRoute != null) {
                if (jsRoute.closeNativePageAfterRoute()) {
                    ARouterDispatcher.dispatch(jsRoute.getRouteUrl());
                    if (activity != null) {
                        activity.finish();
                    }
                } else if (jsRoute.closeNativePageBeforeRoute()) {
                    if (activity != null) {
                        activity.finish();
                    }
                    ARouterDispatcher.dispatch(jsRoute.getRouteUrl());
                } else {
                    ARouterDispatcher.dispatch(jsRoute.getRouteUrl());
                }
            }
        }
    }

    /**
     * 关闭当前页面
     *
     * @param activity
     * @return
     */
    @Override
    protected void closeNativePage(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * JS发现用户登录状态（token）不可用
     *
     * @param params
     */
    @Override
    protected void tokenInvalidHandler(String params) {
        String msg = "登录过期";
        if (!TextUtils.isEmpty(params)) {
            JsMsg jsMsg = GsonUtil.jsonDeserializer(params, JsMsg.class);
            if (jsMsg != null) {
                msg = jsMsg.getMsg();
            }
        }
        //todo to something with 'msg'
    }

    /**
     * JS发现用户被强制下线
     *
     * @param params
     */
    @Override
    protected void forcedOfflineHandler(String params) {
        String msg = "您的账号在另一台手机登录，如非本人操作，密码可能已泄露，建议修改密码后重新登录";
        if (!TextUtils.isEmpty(params)) {
            JsMsg jsMsg = GsonUtil.jsonDeserializer(params, JsMsg.class);
            if (jsMsg != null) {
                msg = jsMsg.getMsg();
            }
        }
        //todo to something with 'msg'
    }

    /**
     * JS发现用户账号被冻结
     *
     * @param params
     */
    @Override
    protected void frozenHandler(String params) {
        String msg = "您的账号因违规操作被管理员冻结";
        if (!TextUtils.isEmpty(params)) {
            JsMsg jsMsg = GsonUtil.jsonDeserializer(params, JsMsg.class);
            if (jsMsg != null) {
                msg = jsMsg.getMsg();
            }
        }
        //todo to something with 'msg'
    }

    /**
     * 保存图片
     *
     * @param activity
     * @param params
     */
    @Override
    protected void saveImage(Activity activity, String params) {
        JsBase64 jsBase64 = GsonUtil.jsonDeserializer(params, JsBase64.class);
        if (jsBase64 != null && !TextUtils.isEmpty(jsBase64.getBase64())) {
            ActivitySupport.requestRunPermission(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionRequestListener() {
                @Override
                public void onGranted() {
                    Bitmap bitmap = Base64.base64ToBitmap(jsBase64.getBase64());
                    String path = Tools.newPicPath();
                    boolean success = FileUtil.saveImageToSDCard(bitmap, path);
                    if (success) {
                        Tools.notifyFileToMedia(path);
                        UI.showToast(String.format("已保存至%s", path));
                    } else {
                        UI.showToast("图片有异常，保存失败");
                    }
                }
            });
        }
    }
}