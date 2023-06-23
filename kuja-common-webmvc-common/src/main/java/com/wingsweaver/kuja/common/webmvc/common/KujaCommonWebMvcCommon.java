package com.wingsweaver.kuja.common.webmvc.common;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-webmvc-common 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonWebMvcCommon {
    private KujaCommonWebMvcCommon() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonWebMvcCommon.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
