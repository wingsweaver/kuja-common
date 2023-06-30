package com.wingsweaver.kuja.starter.common.webmvc.jee;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-starter-common-webmvc-jee 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaStarterCommonWebMvcJee {
    private KujaStarterCommonWebMvcJee() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaStarterCommonWebMvcJee.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
