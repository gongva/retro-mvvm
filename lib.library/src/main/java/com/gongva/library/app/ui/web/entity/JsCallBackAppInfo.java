package com.gongva.library.app.ui.web.entity;

/**
 * H5向App请求的参数:app设备相关信息
 *
 * @author gongwei
 * @date 2019/8/8
 */
public class JsCallBackAppInfo {
    private String version;//：app版本号
    private String osType;//：系统类型Android、iOS
    private String osVersion;//: 系统版本号
    private String deviceBrand;//设备品牌
    private String resolution;//系统分辨率,如：1280x800

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}