package com.gongva.retromvvm.library.plugs.arouter;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类存在的意义：
 * 所有ARouterPath中配置的RouteUrl，任何流程中的【非入口页面】都不应该被外部(Deep Link)RouterUrl直接访问
 * 若出现此情况，与SchemeFilterActivity配合使用，改启动首页
 *
 * @author gongwei
 * @date 2019/6/3
 * @mail shmily__vivi@163.com
 */
public class ARouterPathProcess {

    private static final List<String> routeUrlProcess = new ArrayList<>();

    static {
        //例如：重置密码页 @gongwei
        //routeUrlProcess.add(ARouterPath.ResetPassword);
    }

    /**
     * 判断是否为任何业务流程中的非入口页面
     *
     * @param routeUrl
     * @return true流程中的非入口页面，false不在任何流程里或为流程的入口页面
     */
    public static boolean isRouteUrlProcess(String routeUrl) {
        if (!TextUtils.isEmpty(routeUrl)) {
            Uri uri = Uri.parse(routeUrl);
            if (uri != null) {
                String path = uri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    return routeUrlProcess.contains(path);
                }
            }
        }
        return false;
    }
}
