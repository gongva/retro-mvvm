package com.gongva.retromvvm.ui.common.web.entity;

/**
 * JsBridge params:获取用户当前位置信息
 *
 * @author gongwei
 * @time 2019/12/20
 * @mail gongwei5@hikcreate.com
 */
public class JsGetLocation {
    private boolean needSettingGuide;//如果用户没开GPS相关权限，是否需要Native弹提示框引导用户打开?

    public boolean isNeedSettingGuide() {
        return needSettingGuide;
    }

    public void setNeedSettingGuide(boolean needSettingGuide) {
        this.needSettingGuide = needSettingGuide;
    }
}
