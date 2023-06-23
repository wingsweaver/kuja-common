package com.wingsweaver.kuja.common.utils.support.matcher.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexStringBlackWhiteMatcherTest {
    @Test
    void test() {
        RegexBlackWhiteMatcher matcher = new RegexBlackWhiteMatcher(false);
        assertFalse(matcher.isCaseSensitive());
        assertNotNull(matcher.getWhiteMatcher());
        assertNotNull(matcher.getBlackMatcher());

        matcher.getWhiteMatcher().addString("(.)*an(.)*");
        assertTrue(matcher.matches("an"));
        assertTrue(matcher.matches("An"));
        assertTrue(matcher.matches("Android"));
        assertTrue(matcher.matches("plan"));
        assertTrue(matcher.matches("dance"));
        assertFalse(matcher.matches("afn"));

        matcher.getBlackMatcher().addString("(.)*an(.)+");
        assertTrue(matcher.matches("an"));
        assertTrue(matcher.matches("An"));
        assertFalse(matcher.matches("Android"));
        assertTrue(matcher.matches("plan"));
        assertFalse(matcher.matches("dance"));
        assertFalse(matcher.matches("afn"));
    }
}