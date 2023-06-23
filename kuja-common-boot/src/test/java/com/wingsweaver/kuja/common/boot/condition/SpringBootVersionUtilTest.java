package com.wingsweaver.kuja.common.boot.condition;

import com.vdurmont.semver4j.Semver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringBootVersionUtilTest {
    @Test
    void test() {
        assertNotNull(SpringBootVersionUtil.getSpringBootVersion());
        assertTrue(SpringBootVersionUtil.matches("2.x"));
        assertTrue(SpringBootVersionUtil.matches("2.x", Semver.SemverType.NPM));
    }
}