package com.gongva.retromvvm.common.manager.login;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo implements Serializable {
    public String id;
    public String name;
    public String avatar;
    public String phone;

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static UserInfo createTemp() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId("id123");
        userInfo.setName("nameABC");
        userInfo.setAvatar(null);
        userInfo.setPhone("13800001111");
        return userInfo;
    }
}
