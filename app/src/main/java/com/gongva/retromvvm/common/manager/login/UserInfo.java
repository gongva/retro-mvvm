package com.gongva.retromvvm.common.manager.login;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo implements Serializable {
    public String id;
    public String name;//**彪
    public String avatar;//mock
    public String phone;//155****3868

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
