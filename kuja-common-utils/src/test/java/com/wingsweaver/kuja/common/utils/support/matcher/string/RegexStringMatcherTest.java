package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.BlackWhiteMatcher;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexStringMatcherTest {
    @Test
    void test() {
        RegexSubMatcher matcher = new RegexSubMatcher(true);
        assertTrue(matcher.isCaseSensitive());
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("plant"));

        matcher.addStrings("an", "Answer");
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("plant"));

        matcher.addStrings(CollectionUtil.listOf("(.)+an(.)*"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("plant"));

        matcher.removeStrings("an");
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("an"));

        matcher.removeStrings(CollectionUtil.listOf("Answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("plant"));
    }

    @Test
    void test2() {
        RegexSubMatcher matcher = new RegexSubMatcher(false);
        assertFalse(matcher.isCaseSensitive());
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNDETERMINED, matcher.matches("plant"));

        matcher.addStrings("an", "Answer");
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches("plant"));

        matcher.addStrings(CollectionUtil.listOf("(.)*An(.)*"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("plant"));

        matcher.removeStrings("an");
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("an"));

        matcher.removeStrings(CollectionUtil.listOf("Answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.UNMATCHED, matcher.matches(null));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("an"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("answer"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("japan"));
        assertEquals(BlackWhiteMatcher.MatchResult.MATCHED, matcher.matches("plant"));
    }
}