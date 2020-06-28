package com.gongva.library.plugin.netbase.exception;

import android.text.TextUtils;

/**
 * 服务器产生的Excption，比如404，503等等服务器返回的Excption。
 *
 * @data 2019/3/8
 */
public class ApiException extends RuntimeException {
    private int code;
    private String responseJson;
    private String displayMessage;
    private long threadId;

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(Throwable throwable, int code) {
        this(throwable, code, -1);
    }

    public ApiException(Throwable throwable, int code, long threadId) {
        this(throwable);
        this.code = code;
        this.threadId = threadId;
        if (throwable != null) {
            this.displayMessage = throwable.getMessage();
        }
    }

    public ApiException(int code, String displayMessage) {
        this(code, null, displayMessage, -1);
    }

    public ApiException(int code, String message, String displayMessage) {
        this(code, message, displayMessage, -1);
    }

    public ApiException(int code, String message, String displayMessage, long threadId) {
        super(message);
        this.code = code;
        this.threadId = threadId;
        this.displayMessage = displayMessage;
    }

    public ApiException(String responseJson, int code, String displayMessage) {
        this(responseJson, code, null, displayMessage, -1);
    }

    public ApiException(String responseJson, int code, String message, String displayMessage) {
        this(responseJson, code, message, displayMessage, -1);
    }

    public ApiException(String responseJson, int code, String message, String displayMessage, long threadId) {
        super(message);
        this.code = code;
        this.threadId = threadId;
        this.displayMessage = displayMessage;
        this.responseJson = responseJson;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(getDisplayMessage())) {
            return getDisplayMessage();
        } else {
            return super.getMessage();
        }
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                ", responseJson='" + responseJson + '\'' +
                ", displayMessage='" + displayMessage + '\'' +
                ", threadId=" + threadId +
                '}';
    }
}