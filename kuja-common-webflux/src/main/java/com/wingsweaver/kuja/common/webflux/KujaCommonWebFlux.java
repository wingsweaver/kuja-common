package com.wingsweaver.kuja.common.webflux;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-webflux 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonWebFlux {
    private KujaCommonWebFlux() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonWebFlux.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
