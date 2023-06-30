package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeAppInfoMatcherSpecTest {
    @Test
    void test() {
        CompositeAppInfoMatcherSpec spec = new CompositeAppInfoMatcherSpec();
        assertEquals("composite", spec.getSpecType());

        spec.setMode(CompositeMatchMode.ALL);
        assertEquals(CompositeMatchMode.ALL, spec.getMode());

        assertNull(spec.getMatchers());

        AppInfoMatcher matcher = spec.createAppInfoMatcher();
        assertNotNull(matcher);
        assertFalse(matcher.matches(null));
    }

    @Test
    void test2() {
        HasAppInfoValueMatcherSpec subSpec1 = new HasAppInfoValueMatcherSpec();
        subSpec1.setKey("service.group");

        AppInfoValueEqualsMatcherSpec subSpec2 = new AppInfoValueEqualsMatcherSpec();
        subSpec2.setKey("service.name");
        subSpec2.setTarget("test-app-1");

        CompositeAppInfoMatcherSpec spec = new CompositeAppInfoMatcherSpec(subSpec1, subSpec2);
        assertIterableEquals(CollectionUtil.listOf(subSpec1, subSpec2), spec.getMatchers());

        spec.setMode(CompositeMatchMode.ANY);
        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);

        CompositeAppInfoMatcherSpec spec2 = new CompositeAppInfoMatcherSpec();
        spec2.loadConfig(map);
        assertEquals(CompositeMatchMode.ANY, spec2.getMode());
        assertIterableEquals(CollectionUtil.listOf(subSpec1, subSpec2), spec2.getMatchers());
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        AppInfoMatcherSpec spec3 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertNotNull(spec3);
        assertEquals(spec, spec3);

        CompositeAppInfoMatcher matcher = (CompositeAppInfoMatcher) spec3.createAppInfoMatcher();
        assertFalse(matcher.matches(null));

        AppInfo appInfo = AppInfo.create();
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("service.group", "test");
        assertTrue(matcher.matches(appInfo));

        matcher.setMode(CompositeMatchMode.ALL);
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("service.name", "test-app-1");
        assertTrue(matcher.matches(appInfo));
    }
}