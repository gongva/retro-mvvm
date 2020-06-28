package com.gongva.library.plugin.netbase.exception;

/**
 * 统一对外定制错误
 *
 * @author yslei
 * @data 2019/3/12
 * @email leiyongsheng@hikcreate.com
 */
public class CustomerError {
    /**
     * 业务错误
     */
    public static final int BUSINESS_ERROR = 9;
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 2;
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 3;
    /**
     * http出错
     */
    public static final int HTTP_ERROR = 4;
    /**
     * 协议出错
     */
    public static final int CERTIFICATE_ERROR = 5;
    /**
     * 数据返回为空
     */
    public static final int CONTENT_JSON_NULL_ERROR = 6;
}
