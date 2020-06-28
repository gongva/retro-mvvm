package com.gongva.library.app.ui.web.entity;

import java.util.ArrayList;

/**
 * JsBridge params:浏览大图
 *
 * @author gongwei
 * @date 2019/5/11
 * @mail shmily__vivi@163.com
 */
public class JsImageBrowser {
    private ArrayList<String> imageList;//图片全路径数组
    private int currentIndex;//当前图片索引，用于指明打开大图浏览器后默认显示哪张图片

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
