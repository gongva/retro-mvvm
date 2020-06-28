package com.gongva.library.plugin.netbase.entity;

/**
 * 网络请求统一格式
 *
 * @data 2019/3/8
 */
public class ResponseResult<T> extends ABaseResult<T> {

    private int code;
    private String msg;
    private boolean success;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return isDataEmpty(getData());
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
