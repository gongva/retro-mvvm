package com.gongva.library.app.ui.web.entity;

/**
 * JS调用原生router
 *
 * @author gongwei
 * @time 2019/11/20
 * @mail shmily__vivi@163.com
 */
public class JsRoute {

    //0：不关闭当前页面
    //1：先路由再关闭当前页面
    //2：先关闭当前页面再路由
    private static final int CLOSE_NATIVE_PAGE_AFTER_ROUTE = 1;
    private static final int CLOSE_NATIVE_PAGE_BEFORE_ROUTE = 2;

    private String routeUrl;

    private int closeNativePage; // @see static final int ↑

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }

    public int getCloseNativePage() {
        return closeNativePage;
    }

    public void setCloseNativePage(int closeNativePage) {
        this.closeNativePage = closeNativePage;
    }

    /**
     * 先路由再关页面么？
     *
     * @return
     */
    public boolean closeNativePageAfterRoute() {
        return closeNativePage == CLOSE_NATIVE_PAGE_AFTER_ROUTE;
    }

    /**
     * 先关页面再路由么？
     *
     * @return
     */
    public boolean closeNativePageBeforeRoute() {
        return closeNativePage == CLOSE_NATIVE_PAGE_BEFORE_ROUTE;
    }
}
