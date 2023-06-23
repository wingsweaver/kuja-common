package com.wingsweaver.kuja.common.utils.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexUtilTest {
    @Test
    void isRegexPattern() {
        assertFalse(RegexUtil.isRegexPattern(null));
        assertFalse(RegexUtil.isRegexPattern(""));
        assertFalse(RegexUtil.isRegexPattern("abc"));
        assertTrue(RegexUtil.isRegexPattern("abc*"));
        assertFalse(RegexUtil.isRegexPattern("abc["));
        assertTrue(RegexUtil.isRegexPattern("abc[]"));
        assertFalse(RegexUtil.isRegexPattern("abc("));
        assertTrue(RegexUtil.isRegexPattern("abc()"));
    }
}