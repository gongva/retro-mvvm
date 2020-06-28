package com.gongva.retromvvm.common.manager.login;

import java.io.Serializable;

/**
 * 登录返回的信息
 */
public class LoginInfo implements Serializable {
    public String token;
    public UserInfo userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
