package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.BlackWhiteMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AntPathStringMatcherTest {
    @Test
    void test() {
        AntPathSubMatcher matcher = new AntPathSubMatcher(true);
        assertTrue(matcher.isCaseSensitive());
        assertNull(matcher.getPatterns());
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("test.html"));

        matcher.getPatterns(true).add("*.html");
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("test.html"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("test.Html"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("test.htm"));
    }

    @Test
    void test2() {
        AntPathSubMatcher matcher = new AntPathSubMatcher(false);
        assertFalse(matcher.isCaseSensitive());

        matcher.getPatterns(true).add("*.html");
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("test.Html"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("test.htm"));

        matcher.getPatterns().add("test.*");
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("test.htm"));

        matcher.setPatterns(null);
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("test.html"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("test.htm"));
    }
}