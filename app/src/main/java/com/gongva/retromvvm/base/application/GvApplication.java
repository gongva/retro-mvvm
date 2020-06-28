package com.gongva.retromvvm.base.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 在tinker-support.gradle中enableProxyApplication = false 时的接入方式
 * <p>
 * 这是Tinker推荐的接入方式，一定程度上会增加接入成本，但具有更好的兼容性
 * 这个类集成TinkerApplication类，这里面不做任何操作，所有Application的代码都会放到ApplicationLike继承类当中
 *
 * @date 2019/07/04
 */
public class GvApplication extends TinkerApplication {
    public GvApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,
                "com.gongva.retromvvm.base.application.TinkerAppLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}