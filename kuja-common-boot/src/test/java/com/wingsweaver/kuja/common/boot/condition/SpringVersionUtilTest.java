package com.wingsweaver.kuja.common.boot.condition;

import com.vdurmont.semver4j.Semver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringVersionUtilTest {
    @Test
    void test() {
        assertNotNull(SpringVersionUtil.getSpringVersion());
        assertTrue(SpringVersionUtil.matches("5.x"));
        assertTrue(SpringVersionUtil.matches("5.x", Semver.SemverType.NPM));
    }
}