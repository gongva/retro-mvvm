package com.gongva.retromvvm.common.event.login;

import com.gongva.library.plugin.eventbus.Event;

/**
 * 应用状态
 *
 * @author yslei
 * @date 2019/4/1
 * @mail leiyongsheng@hikcreate.com
 */
public class LoginEvent extends Event {

    private Status mStatus;
    private String mTipMsg;

    public LoginEvent() {
        this(Status.IDLE);
    }

    public LoginEvent(Status status) {
        this(status,null);
    }

    public LoginEvent(Status status, String tipMsg) {
        mStatus = status;
        mTipMsg = tipMsg;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public String getTipMsg() {
        return mTipMsg;
    }

    public void setTipMsg(String tipMsg) {
        mTipMsg = tipMsg;
    }

    public enum Status {
        IDLE,//闲置状态，未登录
        LOGIN,//登录状态
        ACTIVE_CREDIT//激活信用状态
    }
}
