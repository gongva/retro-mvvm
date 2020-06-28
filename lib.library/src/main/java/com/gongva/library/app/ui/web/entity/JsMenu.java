package com.gongva.library.app.ui.web.entity;

/**
 * JsBridge params:设置原生界面标题栏右侧的菜单按钮，menu为空则隐藏
 *
 * @author gongwei
 * @date 2019/10/29
 * @mail gongwei5@hikcreate.com
 */
public class JsMenu {
    private String menu;//菜单文字(不超过4个字)
    private String routeUrl;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }
}
