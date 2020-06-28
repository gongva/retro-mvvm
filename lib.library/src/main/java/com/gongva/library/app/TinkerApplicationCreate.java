package com.gongva.library.app;

import android.app.Activity;
import android.app.Application;

import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.ProcessUtil;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;
import java.util.List;

/**
 * app初始化基础类
 *
 * @author gongwei
 * @date 2019/07/04
 * @mail shmily__vivi@163.com
 */
public abstract class TinkerApplicationCreate {

    protected static TinkerApplicationCreate instance;
    protected static Application mApplication;
    protected APPStatus appStatus;

    public static TinkerApplicationCreate getInstance() {
        return instance;
    }

    public static void setInstance(TinkerApplicationCreate instance) {
        TinkerApplicationCreate.instance = instance;
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static void setApplication(Application mApplication) {
        LogCat.i("Gv: set application.");
        TinkerApplicationCreate.mApplication = mApplication;
    }

    public void initInAppCreate() {
        if (mApplication == null) {
            LogCat.i("initInAppCreate but application is null");
            throw new RuntimeException("The application is null. Plz call setApplication() in to set the application first. ");
        }
        try {
            LogCat.i("initInAppCreate");
            if (ProcessUtil.isMainProcess(mApplication.getApplicationContext())) {
                initAppStatus();
                initX5();
                initApplicationInMainProcess(mApplication);
            }
            initApplication(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initApplicationInMainProcess(Application application) {
        initNetRequest();
        initUI();
        initJsBridgeProtocolHandler();
        initLoginManager();
    }

    protected void initApplication(Application application) {
    }

    /**
     * 初始化自制AppStatus控制器
     */
    private void initAppStatus() {
        appStatus = new APPStatus();
        mApplication.registerActivityLifecycleCallbacks(appStatus);
        appStatus.setStatusCallback(new APPStatus.AppStatusCallback() {
            @Override
            public void appEnterBackground(APPStatus status) {
                onAppEnterBackground();
            }

            @Override
            public void appEnterForeground(APPStatus status) {
                onAppEnterForeground();
            }
        });
    }

    protected void onAppEnterBackground() {

    }

    protected void onAppEnterForeground() {

    }

    public APPStatus getAppStatus() {
        return appStatus;
    }

    public List<Activity> getActivityList() {
        return appStatus.getActivityList();
    }

    public Activity getActivityTop() {
        return appStatus.getCurrentActivity();
    }

    public void exit() {
        appStatus.exit();
    }

    /**
     * 初始化X5内核
     */
    private void initX5() {
        QbSdk.initX5Environment(getApplication(), null);

        HashMap<String, Object> map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
    }

    /**
     * 初始化Retrofit，参考：下面的Demo Code：initNetRequest
     */
    public abstract void initNetRequest();

    /**
     * 初始化UI实例，参考：UI.setUI(new UI.java的子类);
     */
    public abstract void initUI();

    /**
     * 初始化JsBridge协议Handler，参考：JsBridgeProtocolHandler.setInstance(new JsBridgeProtocolHandler.java的子类)
     */
    public abstract void initJsBridgeProtocolHandler();

    /**
     * 初始化登录管理器，参考：LoginManager.setLoginManager(new LoginManager.java的子类)
     */
    public abstract void initLoginManager();
}
