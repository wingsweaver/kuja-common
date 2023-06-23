package com.wingsweaver.kuja.common.messaging;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-messaging 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonMessagingJakarta {
    private KujaCommonMessagingJakarta() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonMessagingJakarta.class);

    /**
     * 获取 kuja-common-messaging 的版本信息。
     *
     * @return 版本信息
     */
    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
