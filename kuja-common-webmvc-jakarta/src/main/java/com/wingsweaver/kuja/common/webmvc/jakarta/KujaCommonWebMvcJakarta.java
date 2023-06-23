package com.wingsweaver.kuja.common.webmvc.jakarta;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-webmvc-jakarta 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonWebMvcJakarta {
    private KujaCommonWebMvcJakarta() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonWebMvcJakarta.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
