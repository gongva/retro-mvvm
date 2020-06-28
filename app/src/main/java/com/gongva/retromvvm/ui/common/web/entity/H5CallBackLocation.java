package com.gongva.retromvvm.ui.common.web.entity;

import java.io.Serializable;

/**
 * H5向App请求的参数:定位信息
 *
 * @author gongwei
 * @time 2019/12/20
 * @mail shmily__vivi@163.com
 */
public class H5CallBackLocation implements Serializable {
    private String lng;//"经度",
    private String lat;//"纬度",
    private String provinceName;//"省份名称",
    private String cityCode;//"城市编码".
    private String cityName;//"城市名称",
    private String countyName;//"区县名称",
    private String street;//"街道详情",
    private String address;//"全路径地址信息"

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
