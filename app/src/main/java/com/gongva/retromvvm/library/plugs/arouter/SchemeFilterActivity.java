package com.gongva.retromvvm.library.plugs.arouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.retromvvm.ui.common.MainPageActivity;
import com.hik.core.android.api.LogCat;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * App Link唤起程序
 * Scheme拦截之后分发给ARouter
 *
 * @author gongwei
 * @date 2019/3/4
 */
public class SchemeFilterActivity extends Activity {

    public static final String DEEP_LINK_QUERY_KEY = "routeUrl";
    public static final int ROUTER_DELAY_DISPATCH = 500;//route分发延迟启动，单位：毫秒

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Uri uri = getIntent().getData();//uri形如：banmacredit://route?routeUrl={整体URLEncode之后的routeUrl}
            if (uri != null) {
                String routeUrl = uri.getQueryParameter(DEEP_LINK_QUERY_KEY);
                LogCat.i("DeepLink Scheme=" + uri.getScheme());
                LogCat.i("DeepLink Host=" + uri.getHost());
                LogCat.i("DeepLink Query(url)=" + routeUrl);
                LogCat.i("DeepLink Stack=" + TinkerApplicationCreate.getInstance().getAppStatus().getActivityList());
                TinkerApplicationCreate.getInstance().getAppStatus().onActivityDestroyed(this);
                if (!MainPageActivity.hasMainPage()) {
                    MainPageActivity.start();
                    if (!TextUtils.isEmpty(routeUrl) && !ARouterPathProcess.isRouteUrlProcess(routeUrl)) {
                        Observable.timer(ROUTER_DELAY_DISPATCH, TimeUnit.MILLISECONDS)
                                .subscribe(aLong -> ARouterDispatcher.dispatch(routeUrl));
                    }
                } else {
                    if (!TextUtils.isEmpty(routeUrl) && !ARouterPathProcess.isRouteUrlProcess(routeUrl)) {
                        ARouterDispatcher.dispatch(routeUrl);
                    } else {
                        MainPageActivity.start(this);
                    }
                }
            } else {
                MainPageActivity.start(this);
            }
        } catch (Exception e) {
            MainPageActivity.start(this);
            LogCat.e("SchemeFilterActivity dispatch error:" + e.getMessage());
        }
        finish();
    }
}