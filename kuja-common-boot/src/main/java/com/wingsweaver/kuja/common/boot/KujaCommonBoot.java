package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-boot 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonBoot {
    private KujaCommonBoot() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonBoot.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
