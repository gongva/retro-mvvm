package com.gongva.retromvvm.base.application;

import android.app.Application;
import android.graphics.Color;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.ui.UI;
import com.gongva.library.app.ui.login.LoginManager;
import com.gongva.library.app.ui.web.JsBridgeProtocolHandler;
import com.gongva.library.plugin.netbase.RetrofitManager;
import com.gongva.library.plugin.netbase.SslUtils;
import com.gongva.retromvvm.BuildConfig;
import com.gongva.retromvvm.common.GvContext;
import com.gongva.retromvvm.common.SystemConfig;
import com.gongva.retromvvm.common.manager.login.GvLoginManager;
import com.gongva.retromvvm.library.utils.AppExecutors;
import com.gongva.retromvvm.library.utils.GvUI;
import com.gongva.retromvvm.repository.common.remote.CustomerResponseBodyConvert;
import com.gongva.retromvvm.repository.common.remote.InterceptorFactory;
import com.gongva.retromvvm.repository.common.remote.interceptor.NetInterceptorConfig;
import com.gongva.retromvvm.ui.common.web.bridge.GvJsBridgeProtocolHandler;
import com.hik.core.android.api.LogCat;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * 将程序所有初始化操作全部移动到这里
 *
 * @author wangtao
 * @date 2019/07/04
 * @mail wangtao55@hikcreate.com
 */
public class GvApplicationCreate extends TinkerApplicationCreate {

    protected static GvApplicationCreate instance;
    //初始化线程池
    private AppExecutors mAppExecutors;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColors(Color.parseColor("#F8F8F8"), Color.parseColor("#FF5A5A5A"));
            return new ClassicsHeader(context).setEnableLastTime(false).setTextSizeTitle(12).setDrawableArrowSize(15).setDrawableProgressSize(15);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
                new ClassicsFooter(context).setDrawableSize(20).setTextSizeTitle(12).setDrawableArrowSize(15).setDrawableProgressSize(15));
    }


    @Override
    protected void initApplicationInMainProcess(Application application) {
        TinkerApplicationCreate.setInstance(getInstance());
        super.initApplicationInMainProcess(application);
        initARouter();
        //相关操作初始化操作放到线程池中进行
        //mAppExecutors = new AppExecutors();
        //mAppExecutors.getTaskThread().execute(this::initUmeng); //eg:initUmeng
    }

    @Override
    protected void initApplication(Application application) {
        super.initApplication(application);
        LogCat.setDebug(BuildConfig.SHOW_LOG);
        //eg: initAliPush
    }

    /**
     * 初始化网络请求
     */
    @Override
    public void initNetRequest() {
        //根据是否需要校验https证书，构建Retrofit
        if (GvContext.openSslCheck) {
            OkHttpClient.Builder builder = null;
            try {
                SSLContext sslContext = SslUtils.getSslContextForCertificateFile(mApplication, GvContext.SSL_CERTIFICATE_FILE);

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                builder = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                        .hostnameVerifier((hostname, session) -> true);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            RetrofitManager.getInstance()
                    .setHttpClientBuilder(builder)
                    .setInterceptorList(InterceptorFactory.getInstance().getInterceptorList())
                    .setResponseConstructor(CustomerResponseBodyConvert.INSTANCE)
                    .setNetInterceptorConfig(NetInterceptorConfig.getInstance())
                    .rebuild();
        } else {
            RetrofitManager.getInstance()
                    .setInterceptorList(InterceptorFactory.getInstance().getInterceptorList())
                    .setResponseConstructor(CustomerResponseBodyConvert.INSTANCE)
                    .setNetInterceptorConfig(NetInterceptorConfig.getInstance())
                    .rebuild();
        }
        //定义需要拦截的业务code
        initSSOInterceptor();
    }

    /**
     * 初始化业务异常拦截器
     */
    private void initSSOInterceptor() {
        InterceptorFactory.getInstance().setKickOutListener((int code, String msg) -> {
            switch (code) {
                //业务异常： 账号被挤下线
                case GvContext.ERROR_CODE_LOGOFF:
                    //todo do something
                    break;
                // 业务异常： 登录失效
                case GvContext.ERROR_CODE_LOGIN_ERROR:
                    //todo do something
                    break;
            }
        });
    }

    @Override
    public void initUI() {
        UI.setUI(new GvUI());
    }

    @Override
    public void initJsBridgeProtocolHandler() {
        JsBridgeProtocolHandler.setInstance(new GvJsBridgeProtocolHandler());
    }

    @Override
    public void initLoginManager() {
        LoginManager.setLoginManager(GvLoginManager.getInstance());
    }

    /**
     * 初始化ARouter
     */
    private void initARouter() {
        if (BuildConfig.SHOW_LOG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(mApplication);
    }

    @Override
    protected void onAppEnterBackground() {
        super.onAppEnterBackground();
    }

    @Override
    protected void onAppEnterForeground() {
        super.onAppEnterForeground();
        //refresh system config
        SystemConfig.getInstance().updateConfig();
    }

    public static GvApplicationCreate getInstance() {
        if (null == instance) {
            synchronized (GvApplicationCreate.class) {
                if (null == instance) {
                    instance = new GvApplicationCreate();
                }
            }
        }
        return instance;
    }
}
