package com.gongva.library.app.ui.web.entity;

/**
 * H5向App请求的参数:用户信息
 *
 * @author gongwei
 * @date 2019/2/22
 */
public class JsCallBackUserInfo {
    private String token;//公网token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}