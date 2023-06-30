package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.boot.appinfo.DefaultAppInfo;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractAppInfoValueMatcherSpecTest {
    @Test
    void test() {
        CustomMatcherSpec spec = new CustomMatcherSpec();
        assertNull(spec.getKey());
        assertNull(spec.getDefaultValue());

        spec.setKey("id");
        assertEquals("id", spec.getKey());

        spec.setDefaultValue("1234");
        assertEquals("1234", spec.getDefaultValue());

        Map<String, String> map = new HashMap<>(BufferSizes.TINY);
        spec.exportConfig(map);
        assertEquals("id", map.get("key"));
        assertEquals("1234", map.get("defaultValue"));

        map.put("key", "name");
        map.put("defaultValue", "jerry");
        spec.loadConfig(map);
        assertEquals("name", spec.getKey());
        assertEquals("jerry", spec.getDefaultValue());

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        assertNotNull(json);

        CustomMatcherSpec spec2 = (CustomMatcherSpec) AppInfoMatcherSpecUtil.fromJsonString(json);
        assertNotNull(spec2);
        assertEquals("name", spec2.getKey());
        assertEquals("jerry", spec2.getDefaultValue());
        assertEquals(spec, spec2);
        assertEquals(spec.hashCode(), spec2.hashCode());

        spec2.setDefaultValue(null);

        AppInfoMatcher matcher = spec2.createAppInfoMatcher();
        assertNotNull(matcher);

        assertFalse(matcher.matches(null));

        AppInfo appInfo = new DefaultAppInfo();
        appInfo.setAttribute("no-name", 1234);
        assertFalse(matcher.matches(appInfo));

        appInfo.setAttribute("name", "tom");
        assertTrue(matcher.matches(appInfo));
    }

    static class CustomMatcherSpec extends AbstractAppInfoValueMatcherSpec {
        @Override
        public AppInfoMatcher createAppInfoMatcher() {
            CustomMatcher matcher = new CustomMatcher();
            matcher.initialize(this);
            matcher.validate();
            return matcher;
        }
    }

    static class CustomMatcher extends AbstractAppInfoValueMatcher<CustomMatcherSpec> {
        @Override
        protected boolean matchesValue(Object value) {
            return value != null;
        }
    }
}