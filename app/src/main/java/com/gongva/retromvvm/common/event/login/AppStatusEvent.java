package com.gongva.retromvvm.common.event.login;

import com.gongva.library.plugin.eventbus.Event;

/**
 * 应用(登录)状态变更
 *
 * @date 2019/4/1
 */
public class AppStatusEvent extends Event {

    private Status mStatus;
    private String mTipMsg;

    public AppStatusEvent() {
        this(Status.IDLE);
    }

    public AppStatusEvent(Status status) {
        this(status, null);
    }

    public AppStatusEvent(Status status, String tipMsg) {
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
        LOGIN//登录状态
    }
}
