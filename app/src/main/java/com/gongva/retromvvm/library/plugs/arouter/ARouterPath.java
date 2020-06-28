package com.gongva.retromvvm.library.plugs.arouter;

import android.text.TextUtils;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.retromvvm.R;

/**
 * 所有页面的Router path
 *
 * @author gongwei
 * @date 2019/3/5
 */
public class ARouterPath {
    /*ARouter extra*/
    public static final int PERMIT_TYPE_LOGIN = 1;//访问权限：登录才可以访问

    public static final String ROUTE_SCHEME = "com.gongva.demo";//todo company's scheme
    public static final String ROUTE_HOST = "nativePage";
    public static final String ROUTE_SCHEME_HOST = String.format("%s://%s", ROUTE_SCHEME, ROUTE_HOST);
    public static final String ROUTE_SCHEME_BACKUP = "https://bmcjump.gongva.com"; //iOS UL的二级域名，Android也需要支持 //todo company's UL url
    public static final String ROUTE_PERMIT_TYPE = "permitType";//todo company's permit type key

    /*-------------Paths-------------*/
    public static final String MainPage = "/common/home";//App主页
    public static final String Login = "/common/login";//登录
    public static final String Web = "/common/web";//网页加载器

    /**
     * 构建完整的routeUrl路径
     *
     * @param pathAndQuery
     * @return
     */
    public static String getCompleteRouteUrl(String pathAndQuery) {
        return ROUTE_SCHEME_HOST + pathAndQuery;
    }

    /**
     * 判断url是否为Deep Link 的Scheme
     * ：不是RouteUrl的Scheme+Host，但却是Deep Link的Scheme
     *
     * @param url
     * @return
     */
    public static boolean isDeepLinkScheme(String url) {
        if (TextUtils.isEmpty(url)) return false;
        if (!url.startsWith(ARouterPath.ROUTE_SCHEME_HOST) &&
                (url.startsWith(ARouterPath.ROUTE_SCHEME_BACKUP)
                        || url.startsWith(TinkerApplicationCreate.getApplication().getString(R.string.link_scheme1) + "://")
                        || url.startsWith(TinkerApplicationCreate.getApplication().getString(R.string.link_scheme2) + "://"))) {
            return true;
        }
        return false;
    }
}
