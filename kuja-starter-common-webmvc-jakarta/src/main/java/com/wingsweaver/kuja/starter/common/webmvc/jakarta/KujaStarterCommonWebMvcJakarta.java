package com.wingsweaver.kuja.starter.common.webmvc.jakarta;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-starter-common-webmvc-jakarta 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaStarterCommonWebMvcJakarta {
    private KujaStarterCommonWebMvcJakarta() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaStarterCommonWebMvcJakarta.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
