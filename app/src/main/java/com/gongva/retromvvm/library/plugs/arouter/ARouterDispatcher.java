package com.gongva.retromvvm.library.plugs.arouter;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.retromvvm.common.manager.login.GvLoginManager;
import com.gongva.retromvvm.library.utils.GvUI;
import com.hik.core.android.api.LogCat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Router地址分发器
 *
 * @author gongwei
 * @date 2019/4/13
 * @mail gongwei5@hikcreate.com
 */
public class ARouterDispatcher {
    //记录最近跳转的地址与其时间戳
    private static RouteUrlLRUCache routeUrlCache = new RouteUrlLRUCache();
    //两次相同routeUrl跳转允许的timestamp间隔，单位：ms
    private static final long TIMESTAMP_STEP = 500;

    /**
     * 关闭Activity后分发Router地址
     * 先关自己再跳转，避免下一个页面因为拦截器而取到当前Activity来弹框，瞬间页面又关了
     *
     * @param activity
     * @param routeUrl
     */
    public static boolean dispatchAfterFinish(Activity activity, String routeUrl) {
        if (activity != null) {
            activity.finish();
        }
        return dispatch(routeUrl);
    }

    /**
     * 分发Router地址，失败时带Toast提示
     *
     * @param routeUrl
     */
    public static boolean dispatch(String routeUrl) {
        LogCat.i("ARouterDispatcher dispatch -> " + routeUrl);
        if (ARouterPath.isDeepLinkScheme(routeUrl)) {
            Uri ulLink = Uri.parse(routeUrl);
            routeUrl = ulLink.getQueryParameter(SchemeFilterActivity.DEEP_LINK_QUERY_KEY);
        }
        if (isLegalRoutePath(routeUrl)) {
            return checkRepeat(routeUrl);
        }
        return false;
    }

    /**
     * 是否为合法的RouteUrl地址
     *
     * @param routeUrl
     * @return true合法，false不合法
     */
    private static boolean isLegalRoutePath(String routeUrl) {
        if (!TextUtils.isEmpty(routeUrl)) {
            Pattern pattern = Pattern.compile(ARouterPath.ROUTE_SCHEME_HOST + "/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
            Matcher m = pattern.matcher(routeUrl);
            if (m.matches()) {
                return true;
            }
            GvUI.showToast("无效的目标页面");
        }
        return false;
    }

    /**
     * 防连续重复启动
     *
     * @param routeUrl
     */
    private static boolean checkRepeat(String routeUrl) {
        if (!routeUrlCache.containsKey(routeUrl) || System.currentTimeMillis() - routeUrlCache.get(routeUrl) > TIMESTAMP_STEP) {
            Uri uri = Uri.parse(routeUrl);
            if (uri != null) {
                navigation(routeUrl);
                routeUrlCache.put(routeUrl, System.currentTimeMillis());
                return true;
            }
        }
        routeUrlCache.put(routeUrl, System.currentTimeMillis());
        return false;
    }

    /**
     * 页面跳转，需解析permitType校验权限
     * 允许routeUrl中的permitType权限高于App对于该页面配置的权限
     *
     * @param routeUrl
     */
    private static void navigation(String routeUrl) {
        Uri uri = Uri.parse(routeUrl);
        if (uri != null) {
            String permitType = uri.getQueryParameter(ARouterPath.ROUTE_PERMIT_TYPE);
            try {
                int permitTypeInt = Integer.parseInt(permitType);
                switch (permitTypeInt) {
                    case ARouterPath.PERMIT_TYPE_LOGIN:
                        if (GvLoginManager.checkLoginPermission(routeUrl)) {
                            ARouter.getInstance().build(uri).navigation();
                        }
                        break;
                    default:
                        ARouter.getInstance().build(uri).navigation();
                        break;
                }
            } catch (Exception e) {
                ARouter.getInstance().build(uri).navigation();
            }
        }
    }
}
