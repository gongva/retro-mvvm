package com.gongva.retromvvm.base.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.gongva.retromvvm.common.GvConfig;
import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.io.FileUtil;
import com.hik.core.android.api.io.SharePreferencesUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.entry.DefaultApplicationLike;

/**
 * 实现tinker需要设置的类，在这里完成tinker，以及bugly的一些初始化操作
 * 以前所有在Application的实现必须要全部拷贝到这里
 * 这样做增加了集成成本，但具有更好的兼容性
 *
 * @date 2019/07/04
 */
public class TinkerAppLike extends DefaultApplicationLike {
    public TinkerAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        Beta.installTinker(this);
        GvApplicationCreate.setApplication(getApplication());
        SharePreferencesUtil.init(base);
        initAppRootFilePath();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BuglyInit.initBugly(getApplication());
        GvApplicationCreate.getInstance().initInAppCreate();
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }


    /**
     * 初始化App根路径
     */
    private void initAppRootFilePath() {
        try {
            String appRootFilePath;
            if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
                appRootFilePath = Environment.getExternalStorageDirectory().toString();
            } else {
                appRootFilePath = getApplication().getCacheDir().getAbsolutePath();
            }
            FileUtil.initCachePath(appRootFilePath, GvConfig.APP_NAME_ENGLISH);
        } catch (Exception e) {
            LogCat.e(e.getMessage());
        }
    }
}
