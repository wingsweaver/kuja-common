package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppInfoValueRegexMatcherSpecTest {
    @Test
    void test() {
        AppInfoValueRegexMatcherSpec spec = new AppInfoValueRegexMatcherSpec();
        assertEquals("regex", spec.getSpecType());

        assertEquals(0, spec.getRegexFlags());
        spec.setRegexFlags(0);

        spec.setKey("name");
        assertEquals("name", spec.getKey());

        spec.setPattern("(.)+y");
        assertEquals("(.)+y", spec.getPattern());

        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);
        assertEquals("0", map.get("regexFlags"));
        assertEquals("name", map.get("key"));
        assertEquals("(.)+y", map.get("pattern"));

        map.put("key", "id");
        spec.loadConfig(map);
        assertEquals("0", map.get("regexFlags"));
        assertEquals("id", map.get("key"));
        assertEquals("(.)+y", map.get("pattern"));

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());
        assertTrue(spec2 instanceof AppInfoValueRegexMatcherSpec);

        AppInfoValueRegexMatcher matcher = (AppInfoValueRegexMatcher) spec.createAppInfoMatcher();
        assertNotNull(matcher);

        assertFalse(matcher.matches(null));

        AppInfo appInfo = AppInfo.create();
        appInfo.setAttribute("id", "sam");
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("id", "sammy");
        assertTrue(matcher.matches(appInfo));
    }
}