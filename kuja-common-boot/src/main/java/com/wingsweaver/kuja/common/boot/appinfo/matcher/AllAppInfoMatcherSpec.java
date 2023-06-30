package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;

import java.util.Map;

/**
 * 匹配所有的 {@link AppInfo} 的 {@link AppInfoMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
final class AllAppInfoMatcherSpec implements AppInfoMatcherSpec, AppInfoMatcher {
    /**
     * 单例实例。
     */
    public static final AllAppInfoMatcherSpec INSTANCE = new AllAppInfoMatcherSpec();

    /**
     * 规格的类型。
     */
    public static final String SPEC_TYPE = "all";

    private AllAppInfoMatcherSpec() {
        // 禁止外部生成实例
    }

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        return this;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        // 什么也不做
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        // 什么也不做
    }

    @Override
    public boolean matches(AppInfo target) {
        return true;
    }
}
