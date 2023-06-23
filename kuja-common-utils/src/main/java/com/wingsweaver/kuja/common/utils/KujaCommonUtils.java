package com.wingsweaver.kuja.common.utils;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-utils 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonUtils {
    private KujaCommonUtils() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonUtils.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
