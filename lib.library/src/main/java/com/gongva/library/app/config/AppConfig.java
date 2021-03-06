package com.gongva.library.app.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.gongva.library.app.TinkerApplicationCreate;

/**
 * @author gongwei
 * @date 2019/3/6
 */
public class AppConfig {

    //webview需要在userAgent的末尾增加标识，给WebJs判断用
    public static final String WEB_USER_AGENT_MARK = ",com.gongva:android";//todo mark: company's user agent

    /**
     * 获取版本号Name
     *
     * @return
     */
    public static String getAppVersionName() {
        try {
            PackageInfo packageInfo = getPackageInfo(TinkerApplicationCreate.getApplication());
            return packageInfo.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取版本号Code
     *
     * @return
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo packageInfo = getPackageInfo(TinkerApplicationCreate.getApplication());
            return packageInfo.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getAppPackageName() {
        try {
            PackageInfo packageInfo = getPackageInfo(TinkerApplicationCreate.getApplication());
            return packageInfo.packageName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取PackageInfo
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getPackageInfo(context.getPackageName(), 0);
    }
}
