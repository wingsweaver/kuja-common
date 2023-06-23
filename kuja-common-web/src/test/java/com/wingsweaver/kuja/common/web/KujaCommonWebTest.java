package com.wingsweaver.kuja.common.web;

import com.wingsweaver.kuja.common.boot.KujaCommonBoot;
import org.junit.jupiter.api.Test;

class KujaCommonWebTest {
    @Test
    void moduleVersion() {
        KujaCommonBoot.moduleVersion();
    }
}