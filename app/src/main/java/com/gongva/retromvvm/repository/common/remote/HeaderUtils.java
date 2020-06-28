package com.gongva.retromvvm.repository.common.remote;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.retromvvm.common.GvConfig;
import com.gongva.retromvvm.common.SystemConfig;
import com.gongva.retromvvm.common.manager.login.GvLoginManager;
import com.hik.core.android.api.DeviceUtil;
import com.hik.core.android.api.ScreenUtil;
import com.hik.core.java.security.AESUtil;
import com.hik.core.java.security.SecurityFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Api请求的Header管理器
 *
 * @data 2019/3/11
 */
public class HeaderUtils {
    private static final String TAG = "HeaderUtils";
    private static Map<String, String> sHeaderMap;

    /**
     * 请求Header中的参数
     *
     * @return
     */
    public static Map<String, String> getHeaders() {
        if (sHeaderMap == null) {
            sHeaderMap = createHeaderMap();
        }
        sHeaderMap.put("Token", getHeaderToken());
        return sHeaderMap;
    }

    /**
     * 获取Header参数集，用于Http请求与WebView加载时传参
     *
     * @return
     */
    public static Map<String, String> createHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Version", GvConfig.getAppVersionName());
        headerMap.put("Device-Type", GvConfig.PHONE_TYPE_ANDROID);
        headerMap.put("Device-Code", DeviceUtil.getDeviceCode(TinkerApplicationCreate.getApplication()));
        headerMap.put("Device-Model", DeviceUtil.getDeviceModel());// Xiaomi 8 SE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR && BluetoothAdapter.getDefaultAdapter() != null) {
            headerMap.put("Device-Name", URLEncoder.encode(BluetoothAdapter.getDefaultAdapter().getName()));//设备名称（蓝牙名称）
        } else {
            headerMap.put("Device-Name", Build.DEVICE);
        }
        headerMap.put("Device-Brand", Build.BRAND);
        DisplayMetrics metrics = ScreenUtil.getDisplayMetrics(TinkerApplicationCreate.getApplication());
        headerMap.put("Resolution", metrics.heightPixels + "x" + metrics.widthPixels);
        headerMap.put("OS-Type", GvConfig.PHONE_TYPE_ANDROID);
        headerMap.put("OS-Version", String.valueOf(Build.VERSION.SDK_INT));
        return headerMap;
    }

    /**
     * Header中的公网token：登录token末尾拼接时间戳后AES加密
     * todo 每一次请求中的token需要带上时间戳并加密
     *
     * @return
     */
    public static String getHeaderToken() {
        String loginToken = GvLoginManager.getInstance().getToken();
        if (!TextUtils.isEmpty(loginToken)) {
            long serverTime = SystemConfig.getInstance().getServerTimeLong();
            return AESUtil.encrypt(loginToken + serverTime, SecurityFactory.getKeyFromNative(TinkerApplicationCreate.getApplication()));
        }
        return null;
    }
}