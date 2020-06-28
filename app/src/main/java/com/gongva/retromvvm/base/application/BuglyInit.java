package com.gongva.retromvvm.base.application;

import android.content.Context;
import android.text.TextUtils;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.config.AppConfig;
import com.gongva.retromvvm.BuildConfig;
import com.gongva.retromvvm.common.GvConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Bugly 相关初始化操作
 *
 * @date 2019/9/20
 */
class BuglyInit {

    static void initBugly(Context application) {

        String channel = com.meituan.android.walle.WalleChannelReader.getChannel(application);
        int tagValue = GvConfig.BUGLY_TAG_RELEASE;
        if (BuildConfig.DEBUG || TextUtils.isEmpty(channel)) {
            channel = GvConfig.BUGLY_CHANNEL_DEFAULT;
            tagValue = GvConfig.BUGLY_TAG_DEBUG;
        }
        Bugly.setIsDevelopmentDevice(application, BuildConfig.DEBUG);
        CrashReport.setUserSceneTag(application, AppConfig.getAppVersionCode());
        CrashReport.setUserSceneTag(application, tagValue);
        CrashReport.setUserSceneTag(application, getChannelTag(channel));
        CrashReport.setAppChannel(application, channel);
        CrashReport.initCrashReport(application, GvConfig.BUGLY_APP_ID, false);
        Bugly.init(application, GvConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
    }

    private static int getChannelTag(String channel) {
        switch (channel) {
            case "huawei":
                return GvConfig.BUGLY_CHANNEL_HUAWEI;//bugly后端配置的数据
            case "xiaomi":
                return GvConfig.BUGLY_CHANNEL_XIAOMI;
            case "baidu":
                return GvConfig.BUGLY_CHANNEL_BAIDU;
            case GvConfig.BUGLY_CHANNEL_DEFAULT:
                return GvConfig.BUGLY_CHANNEL_DEBUG;
        }
        return 0;
    }
}


