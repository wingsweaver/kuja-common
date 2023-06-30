package com.wingsweaver.kuja.starter.common.messaging.jakarta;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-starter-common-messaging-jakarta 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaStarterCommonMessagingJakarta {
    private KujaStarterCommonMessagingJakarta() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaStarterCommonMessagingJakarta.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
