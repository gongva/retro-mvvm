package com.hik.core.android.api;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

/**
 * 设备相关的Api库
 *
 * @author gongwei
 * @time 2019/10/11
 * @mail shmily__vivi@163.com
 */
public class DeviceUtil {
    /**
     * 获取设备机型
     *
     * @return
     */
    public static String getDeviceModel() {
        return String.format("%s %s", Build.BRAND, Build.MODEL); //手机厂商 手机型号
    }

    /**
     * 获得DeviceCode
     *
     * @return
     */
    public static String getDeviceCode(Application application) {
        String result = getAndroidId(application);
        if (TextUtils.isEmpty(result)) {
            result = getBuildSerial();
        }
        return result;
    }

    /**
     * 获取Android Id
     *
     * @return AndroidId or null
     */
    private static String getAndroidId(Application application) {
        String androidId = Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)) {
            try {
                UUID androidUuid = new UUID(androidId.hashCode(), androidId.hashCode() << 32 | androidId.hashCode());
                return androidUuid.toString().replace("-", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取Serial号
     * 失败时则通过硬件信息生成的伪设备码
     *
     * @return Build.SERIAL or Pseudo DeviceId
     */
    public static String getBuildSerial() {
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        String uuid = "";
        try {
            //API>=9 使用serial号
            uuid = new UUID(m_szDevIDShort.hashCode(), Build.SERIAL.hashCode()).toString();
        } catch (Exception exception) {
            //使用硬件信息构建出来的15位号码
            uuid = new UUID(m_szDevIDShort.hashCode(), "unknown".hashCode()).toString();
        }
        return uuid.replace("-", "");
    }
}
