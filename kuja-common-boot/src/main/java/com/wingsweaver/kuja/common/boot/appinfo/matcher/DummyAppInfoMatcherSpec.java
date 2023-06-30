package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
class DummyAppInfoMatcherSpec implements AppInfoMatcherSpec, AppInfoMatcher {
    /**
     * 匹配结果。
     */
    private boolean matched;

    /**
     * 规范类型。
     */
    private String specType;

    /**
     * 规范配置。
     */
    private Map<String, String> config;

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        return this;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        MapUtil.copy(this.config, map, true);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        if (map == null || map == this.config) {
            return;
        }
        if (this.config == null) {
            this.config = new HashMap<>(map);
        } else {
            MapUtil.copy(map, this.config, true);
        }
    }

    @Override
    public boolean matches(AppInfo target) {
        return this.matched;
    }
}
