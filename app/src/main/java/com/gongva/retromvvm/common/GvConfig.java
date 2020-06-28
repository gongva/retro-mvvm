package com.gongva.retromvvm.common;

import android.app.Activity;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.config.AppConfig;
import com.gongva.retromvvm.R;


public class GvConfig extends AppConfig {

    public static final String APP_NAME = "GvDemo";
    public static final String APP_NAME_ENGLISH = "retro-mvvm";//todo app name

    public static final String PHONE_TYPE_ANDROID = "Android";//客户端类型：安卓

    public static final String MAP_COORD_TYPE = "gcj02";//地图坐标的坐标系

    public static final int PASSWORD_MIN_LENGTH = 8;//密码最短长度

    public static final int PASSWORD_MAX_LENGTH = 16;//密码最长长度

    public static final int SECOND_RESEND_CAPTCHA = 60;//重新获取验证码的时间倒计时，单位：s

    public static final int HTTP_DEFAULT_SIZE = 20;//分页接口请求的每页条数，默认20

    // 用于tinker bugly 配置
    public static final String BUGLY_APP_ID = "12345abcde";//todo company's bugly appId
    public static final int BUGLY_TAG_DEBUG = 123456;//todo company's bugly debug tag
    public static final int BUGLY_TAG_RELEASE = 123456;//todo company's bugly release tag
    public static final String BUGLY_CHANNEL_DEFAULT = "channel_debug";//bugly debug channel
    public static final int BUGLY_CHANNEL_DEBUG = 123000;//todo company's bugly channels
    public static final int BUGLY_CHANNEL_BAIDU = 123001;//todo company's bugly channels
    public static final int BUGLY_CHANNEL_HUAWEI = 123002;//todo company's bugly channels
    public static final int BUGLY_CHANNEL_XIAOMI = 123003;//todo company's bugly channels and so on

    /**
     * 获取App Name
     *
     * @return
     */
    public static String getAppName() {
        final Activity topActivity = TinkerApplicationCreate.getInstance().getActivityTop();
        if (topActivity != null) {
            return topActivity.getResources().getString(R.string.app_name);
        }
        return APP_NAME;
    }
}
