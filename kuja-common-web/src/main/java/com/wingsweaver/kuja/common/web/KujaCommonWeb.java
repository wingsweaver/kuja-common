package com.wingsweaver.kuja.common.web;

import com.wingsweaver.kuja.common.utils.support.ModuleVersion;

/**
 * kuja-common-web 的入口类。
 *
 * @author wingsweaver
 */
public final class KujaCommonWeb {
    private KujaCommonWeb() {
        // 禁止实例化
    }

    private static final ModuleVersion.Builder MODULE_VERSION_BUILDER = ModuleVersion.builder(KujaCommonWeb.class);

    public static ModuleVersion moduleVersion() {
        return MODULE_VERSION_BUILDER.build();
    }
}
