package com.gongva.library.plugin.netbase.entity;

/**
 * 业务异常基础bean
 * @author gongwei
 * @date 2020/1/14
 * @email shmily__vivi@163.com
 */
public class ExceptionBaseBean {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ExceptionBaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
