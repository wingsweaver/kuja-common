package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.utils.model.attributes.MutableAttributes;

import java.util.Map;

/**
 * 应用程序的信息。
 *
 * @author wingsweaver
 */
public interface AppInfo extends MutableAttributes<String> {
    /**
     * 生成新的 {@link AppInfo} 实例。
     *
     * @return {@link AppInfo} 实例
     */
    static AppInfo create() {
        return new DefaultAppInfo();
    }

    /**
     * 基于字典生成新的 {@link AppInfo} 实例。
     *
     * @param map 字典
     * @return {@link AppInfo} 实例
     */
    static AppInfo of(Map<String, ?> map) {
        return new DefaultAppInfo(map);
    }
}
