package com.gongva.library.app.ui.web.entity;

import java.io.Serializable;

/**
 * JsBridge params:分享params
 *
 * @author gongwei
 * @date 2019/4/26
 * @mail shmily__vivi@163.com
 */
public class JsShareContent implements Serializable {

    private String title;
    private String summary;
    private String imageUrl;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
