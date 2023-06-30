package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;

import java.util.Map;

/**
 * {@link AppInfoMatcher} 的规范定义的接口。
 *
 * @author wingsweaver
 */
public interface AppInfoMatcherSpec {
    /**
     * 生成使用本规范的 {@link AppInfoMatcher} 实例。
     *
     * @return AppInfoMatcher 实例
     */
    AppInfoMatcher createAppInfoMatcher();

    /**
     * 获取本规范的类型。
     *
     * @return 规范类型
     */
    default String getSpecType() {
        return this.getClass().getTypeName();
    }

    /**
     * 导出规范的配置到字典中。
     *
     * @param map 保存配置的字典
     */
    void exportConfig(Map<String, String> map);

    /**
     * 从字典中加载规范的配置。
     *
     * @param map 保存配置的字典
     */
    void loadConfig(Map<String, String> map);

    /**
     * 匹配所有的 AppInfo 的 {@link AppInfoMatcherSpec} 实现类。
     */
    AppInfoMatcherSpec MATCHES_ALL = AllAppInfoMatcherSpec.INSTANCE;
}
