package com.wingsweaver.kuja.common.utils.support.matcher.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AntPathStringBlackWhiteMatcherTest {
    @Test
    void test() {
        AntPathBlackWhiteMatcher matcher = new AntPathBlackWhiteMatcher(true);
        assertTrue(matcher.isCaseSensitive());
        assertNotNull(matcher.getWhiteMatcher());
        assertNotNull(matcher.getBlackMatcher());
        assertFalse(matcher.getDefaultResult());
        assertFalse(matcher.matches("test.html"));

        matcher.getWhiteMatcher().getPatterns(true).add("*.html");
        assertTrue(matcher.matches("test.html"));
        assertFalse(matcher.matches("test.htm"));
        assertFalse(matcher.matches("test.js"));
        assertTrue(matcher.matches("index.html"));
        assertFalse(matcher.matches("index.htm"));

        matcher.getBlackMatcher().getPatterns(true).add("test.*");
        assertFalse(matcher.matches("test.html"));
        assertFalse(matcher.matches("test.htm"));
        assertFalse(matcher.matches("test.js"));
        assertTrue(matcher.matches("index.html"));
        assertFalse(matcher.matches("index.htm"));

    }
}