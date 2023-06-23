package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * {@link AppInfo} 的设置接口。
 *
 * @author wingsweaver
 */
public interface AppInfoCustomizer extends DefaultOrdered {
    /**
     * 设置 AppInfo。
     *
     * @param appInfo AppInfo 实例
     */
    void customize(AppInfo appInfo);
}
