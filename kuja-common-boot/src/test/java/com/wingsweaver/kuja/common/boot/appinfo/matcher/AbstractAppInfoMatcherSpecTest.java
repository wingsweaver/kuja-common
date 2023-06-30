package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractAppInfoMatcherSpecTest {
    @Test
    void test() {
        CustomAppInfoMatcherSpec spec = new CustomAppInfoMatcherSpec();
        assertEquals(spec.getClass().getTypeName(), spec.getSpecType());
        assertFalse(spec.isRevert());

        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);
        assertEquals(1, map.size());
        assertEquals("false", map.get("revert"));

        map.put("revert", "true");
        spec.loadConfig(map);
        assertTrue(spec.isRevert());

        spec.setRevert(false);
        assertFalse(spec.isRevert());

        AppInfoMatcher matcher = spec.createAppInfoMatcher();
        assertTrue(matcher instanceof CustomAppInfoMatcher);

        CustomAppInfoMatcher customMatcher = (CustomAppInfoMatcher) matcher;
        assertSame(spec, customMatcher.getSpec());
        assertEquals(spec.isRevert(), customMatcher.isRevert());
        assertFalse(customMatcher.matches(null));

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        assertNotNull(json);

        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertTrue(spec2 instanceof CustomAppInfoMatcherSpec);
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());

        CustomAppInfoMatcherSpec customSpec = (CustomAppInfoMatcherSpec) spec2;
        assertEquals(spec.isRevert(), customSpec.isRevert());
    }

    static class CustomAppInfoMatcherSpec extends AbstractAppInfoMatcherSpec {

        @Override
        public AppInfoMatcher createAppInfoMatcher() {
            CustomAppInfoMatcher matcher = new CustomAppInfoMatcher();
            matcher.initialize(this);
            matcher.validate();
            return matcher;
        }
    }

    static class CustomAppInfoMatcher extends AbstractAppInfoMatcher<CustomAppInfoMatcherSpec> {
        @Override
        protected boolean matchesInternal(AppInfo target) {
            return false;
        }
    }
}