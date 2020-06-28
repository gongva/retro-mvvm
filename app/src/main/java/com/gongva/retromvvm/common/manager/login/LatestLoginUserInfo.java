package com.gongva.retromvvm.common.manager.login;

import java.io.Serializable;

/**
 * 最后一次登录用户的基本信息
 *
 * @author gongwei
 * @date 2019/4/25
 * @mail gongwei5@hikcreate.com
 */
public class LatestLoginUserInfo implements Serializable {
    private String phone;//手机号
    private String avatar;//头像

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
