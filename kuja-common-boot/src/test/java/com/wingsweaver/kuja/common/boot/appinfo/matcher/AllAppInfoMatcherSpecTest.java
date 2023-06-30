package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.boot.appinfo.DefaultAppInfo;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllAppInfoMatcherSpecTest {
    @Test
    void test() {
        AllAppInfoMatcherSpec spec = AllAppInfoMatcherSpec.INSTANCE;
        assertEquals("all", spec.getSpecType());

        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);
        assertTrue(map.isEmpty());

        map.put("id", "1234");
        spec.loadConfig(map);

        AppInfoMatcher matcher = spec.createAppInfoMatcher();
        assertSame(spec, matcher);
        assertTrue(matcher.matches(null));
        assertTrue(matcher.matches(new DefaultAppInfo()));

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        assertNotNull(json);

        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertSame(spec, spec2);
    }
}