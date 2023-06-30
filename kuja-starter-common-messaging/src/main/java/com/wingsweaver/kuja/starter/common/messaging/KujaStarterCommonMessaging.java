package com.wingsweaver.kuja.starter.common.messaging;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-starter-common-messaging 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaStarterCommonMessaging {
    private KujaStarterCommonMessaging() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaStarterCommonMessaging.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
