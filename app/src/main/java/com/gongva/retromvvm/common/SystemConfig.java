package com.gongva.retromvvm.common;

import com.gongva.library.plugin.netbase.RequestObserverCallbackAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.retromvvm.repository.Injection;
import com.hik.core.android.api.GsonUtil;
import com.hik.core.android.api.io.SharePreferencesUtil;

/**
 * 系统配置信息
 *
 * @author gongwei 2019.1.5
 */
public class SystemConfig {

    private static SystemConfig instance;

    //本地时间
    private long localTime;
    //服务器时间
    private long systemTimestamp;

    public static SystemConfig getInstance() {
        if (instance == null) {
            synchronized (SystemConfig.class) {
                if (instance == null) {
                    instance = new SystemConfig();
                    initConfigPref();
                }
            }
        }
        return instance;
    }

    private static void initConfigPref() {
        instance.systemTimestamp = System.currentTimeMillis();
        instance.localTime = System.currentTimeMillis();
        String jsonConfig = SharePreferencesUtil.getString(GvContext.SPF_SYSTEM_CONFIG, null);
        if (jsonConfig != null) {
            SystemConfig systemConfig = GsonUtil.jsonDeserializer(jsonConfig, SystemConfig.class);
            if (systemConfig != null) {
                instance.updateConfig(systemConfig);
            }
        }
    }

    /**
     * 更新配置到文件
     *
     * @param config
     */
    public void updateConfig(SystemConfig config) {
        systemTimestamp = config.systemTimestamp;
        localTime = config.localTime;
    }

    private void save() {
        SharePreferencesUtil.getString(GvContext.SPF_SYSTEM_CONFIG, GsonUtil.jsonSerializer(this));
    }


    /**
     * 更新系统配置
     */
    public void updateConfig() {
        Injection.provideGeneralRemoteDataSource().getSysConfig(new RequestObserverCallbackAdapter<ResponseResult<SystemConfig>>() {
            @Override
            public void loadData(ResponseResult<SystemConfig> result) {
                super.loadData(result);
                if (result != null && result.getData() != null) {
                    SystemConfig config = result.getData();
                    config.localTime = System.currentTimeMillis();
                    getInstance().updateConfig(config);
                    getInstance().save();
                }
            }
        });
    }

    /**
     * 利用服务器和本地时间的差值，计算当前的获取服务器时间
     *
     * @return long
     */
    public long getServerTimeLong() {
        return systemTimestamp + System.currentTimeMillis() - localTime;
    }
}
