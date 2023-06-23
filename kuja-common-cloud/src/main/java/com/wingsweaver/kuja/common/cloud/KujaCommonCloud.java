package com.wingsweaver.kuja.common.cloud;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-cloud 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonCloud {
    private KujaCommonCloud() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonCloud.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
