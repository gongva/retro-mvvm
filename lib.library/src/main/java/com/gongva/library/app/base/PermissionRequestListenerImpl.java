package com.gongva.library.app.base;

/**
 * 空的PermissionRequestListener实现类
 * WebChromeClient里，不允许匿名内部类的回调使用，固有了这个类
 *
 * @author gongwei 2018.12.20
 */
public class PermissionRequestListenerImpl extends PermissionRequestListener {

    @Override
    public void onGranted() {

    }
}
