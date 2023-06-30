package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppInfoMatcherSpecUtilTest {
    @Test
    void test() {
        assertNotNull(AppInfoMatcherSpecUtil.getUnknownSpecTypeHandler());
        AppInfoMatcherSpecUtil.setUnknownSpecTypeHandler(AppInfoMatcherSpecUtil.REJECT_UNKNOWN_SPEC_TYPE);
        assertSame(AppInfoMatcherSpecUtil.REJECT_UNKNOWN_SPEC_TYPE, AppInfoMatcherSpecUtil.getUnknownSpecTypeHandler());

        HasAppInfoValueMatcherSpec spec = new HasAppInfoValueMatcherSpec();
        spec.setKey("name");
        spec.setDefaultValue("123456");
        spec.setRevert(true);

        String json = AppInfoMatcherSpecUtil.toJsonString(spec);
        assertNotNull(json);
        AppInfoMatcherSpec spec2 = AppInfoMatcherSpecUtil.fromJsonString(json);
        assertEquals(spec, spec2);

        assertThrows(ExtendedRuntimeException.class, () -> AppInfoMatcherSpecUtil.FAIL_ON_UNKNOWN_SPEC_TYPE.handle("test"));

        assertNull(AppInfoMatcherSpecUtil.IGNORE_UNKNOWN_SPEC_TYPE.handle("test"));

        AppInfoMatcherSpec spec3 = AppInfoMatcherSpecUtil.ACCEPT_UNKNOWN_SPEC_TYPE.handle("test");
        assertTrue(spec3.createAppInfoMatcher().matches(null));

        AppInfoMatcherSpec spec4 = AppInfoMatcherSpecUtil.REJECT_UNKNOWN_SPEC_TYPE.handle("test");
        assertFalse(spec4.createAppInfoMatcher().matches(null));
    }
}