package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.vdurmont.semver4j.Semver;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppInfoValueVersionMatcherSpecTest {
    @Test
    void test() {
        AppInfoValueVersionMatcherSpec spec = new AppInfoValueVersionMatcherSpec();
        assertEquals("version", spec.getSpecType());

        spec.setKey("version");
        assertEquals("version", spec.getKey());

        spec.setVersionType(Semver.SemverType.NPM);
        assertEquals(Semver.SemverType.NPM, spec.getVersionType());

        spec.setRequirement("1.*");
        assertEquals("1.*", spec.getRequirement());

        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);
        assertEquals("version", map.get("key"));
        assertEquals("NPM", map.get("versionType"));
        assertEquals("1.*", map.get("requirement"));

        map.put("requirement", "2.*");
        spec.loadConfig(map);
        assertEquals("version", spec.getKey());
        assertEquals(Semver.SemverType.NPM, spec.getVersionType());
        assertEquals("2.*", spec.getRequirement());

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertTrue(spec2 instanceof AppInfoValueVersionMatcherSpec);
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());

        AppInfoMatcher matcher = spec.createAppInfoMatcher();
        assertNotNull(matcher);

        assertFalse(matcher.matches(null));

        AppInfo appInfo = AppInfo.create();
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("version", "1.2");
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("version", "2.3");
        assertTrue(matcher.matches(appInfo));

        appInfo.setAttribute("version", "3.4");
        assertFalse(matcher.matches(appInfo));
    }
}