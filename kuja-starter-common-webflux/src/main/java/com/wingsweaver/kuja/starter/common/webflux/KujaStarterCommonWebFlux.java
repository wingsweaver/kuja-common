package com.wingsweaver.kuja.starter.common.webflux;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-starter-common-webflux 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaStarterCommonWebFlux {
    private KujaStarterCommonWebFlux() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaStarterCommonWebFlux.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
