package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.DefaultAppInfo;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppInfoValueEqualsMatcherSpecTest {
    @Test
    void test() {
        AppInfoValueEqualsMatcherSpec spec = new AppInfoValueEqualsMatcherSpec();
        assertEquals("equals", spec.getSpecType());

        spec.setKey("name");
        spec.setTarget("tom");
        spec.setIgnoreCase(true);
        assertEquals("name", spec.getKey());
        assertEquals("tom", spec.getTarget());
        assertTrue(spec.isIgnoreCase());

        Map<String, String> map = new HashMap<>();
        spec.exportConfig(map);
        assertEquals("name", map.get("key"));
        assertEquals("tom", map.get("target"));
        assertEquals("true", map.get("ignoreCase"));

        map.put("target", "jerry");
        spec.loadConfig(map);
        assertEquals("name", spec.getKey());
        assertEquals("jerry", spec.getTarget());
        assertTrue(spec.isIgnoreCase());

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertTrue(spec2 instanceof AppInfoValueEqualsMatcherSpec);
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());

        AppInfoValueEqualsMatcher matcher = (AppInfoValueEqualsMatcher) spec.createAppInfoMatcher();
        assertNotNull(matcher);
        assertEquals(spec.getKey(), matcher.getKey());
        assertEquals(spec.getTarget(), matcher.getTarget());
        assertEquals(spec.isIgnoreCase(), matcher.isIgnoreCase());

        assertFalse(matcher.matches(null));

        AppInfo appInfo = new DefaultAppInfo();
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("name", "tom");
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("name", "Jerry");
        assertTrue(matcher.matches(appInfo));

        matcher.setIgnoreCase(false);
        assertFalse(matcher.matches(appInfo));
    }
}