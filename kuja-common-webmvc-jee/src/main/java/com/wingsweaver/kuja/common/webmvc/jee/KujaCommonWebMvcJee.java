package com.wingsweaver.kuja.common.webmvc.jee;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-webmvc-jee 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonWebMvcJee {
    private KujaCommonWebMvcJee() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonWebMvcJee.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
