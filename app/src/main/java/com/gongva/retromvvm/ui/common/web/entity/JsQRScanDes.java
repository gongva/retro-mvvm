package com.gongva.retromvvm.ui.common.web.entity;

/**
 * JsBridge params:二维码扫描文案描述
 *
 * @author gongwei
 * @time 2019/12/20
 * @mail shmily__vivi@163.com
 */
public class JsQRScanDes {
    private String introduction;//二维码扫描文案说明
    private String title;//二维码扫描标题,可选，客户端默认二维码扫描

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "JsQRScanDes{" +
                "introduction='" + introduction + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
