package com.gongva.library.app.ui.view.patternlock;


/**
 * 手势设置回调接口
 */
public interface PatternLockViewSetListener {

    /**
     * 设置完成
     */
    void onComplete(String password);

    /**
     * 第一次设置完成
     */
    void onCompleteFirstSet(String password);

    /**
     * 与上一次绘制不一致
     */
    void onDifference(String password);
}