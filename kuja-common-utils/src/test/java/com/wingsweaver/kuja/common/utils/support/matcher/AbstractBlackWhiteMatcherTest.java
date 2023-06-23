package com.wingsweaver.kuja.common.utils.support.matcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractBlackWhiteMatcherTest {
    @Test
    void test() {
        CustomBlackWhiteMatcher matcher = new CustomBlackWhiteMatcher();
        assertNull(matcher.getWhiteMatcher());
        assertNull(matcher.getBlackMatcher());
        assertFalse(matcher.getDefaultResult());
        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(1));
        assertFalse(matcher.matches(2));

        matcher.setWhiteMatcher(AbstractBlackWhiteMatcherTest::isEven);
        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(1));
        assertTrue(matcher.matches(2));

        matcher.setWhiteMatcher(AbstractBlackWhiteMatcherTest::divideBy3);
        assertTrue(matcher.matches(null));
        assertTrue(matcher.matches(0));
        assertFalse(matcher.matches(1));
        assertFalse(matcher.matches(2));
        assertTrue(matcher.matches(3));
        assertTrue(matcher.matches(12));

        matcher.setBlackMatcher(AbstractBlackWhiteMatcherTest::divideBy4);
        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(0));
        assertFalse(matcher.matches(1));
        assertFalse(matcher.matches(2));
        assertTrue(matcher.matches(3));
        assertFalse(matcher.matches(12));

        matcher.setWhiteMatcher(null);
        matcher.setDefaultResult(true);
        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(0));
        assertTrue(matcher.matches(1));
        assertTrue(matcher.matches(2));
        assertTrue(matcher.matches(3));
        assertFalse(matcher.matches(12));
    }

    static BlackWhiteMatcher.MatchResult divideBy4(Integer target) {
        int value = target != null ? target : 0;
        switch (value % 4) {
            case 0:
                return BlackWhiteMatcher.MatchResult.MATCHED;
            case 1:
                return BlackWhiteMatcher.MatchResult.UNMATCHED;
            default:
                return BlackWhiteMatcher.MatchResult.UNDETERMINED;
        }
    }

    static BlackWhiteMatcher.MatchResult divideBy3(Integer target) {
        int value = target != null ? target : 0;
        switch (value % 3) {
            case 0:
                return BlackWhiteMatcher.MatchResult.MATCHED;
            case 1:
                return BlackWhiteMatcher.MatchResult.UNMATCHED;
            default:
                return BlackWhiteMatcher.MatchResult.UNDETERMINED;
        }
    }

    static BlackWhiteMatcher.MatchResult isEven(Integer target) {
        if (target == null) {
            return BlackWhiteMatcher.MatchResult.UNDETERMINED;
        }
        if (target % 2 == 1) {
            return BlackWhiteMatcher.MatchResult.UNMATCHED;
        } else {
            return BlackWhiteMatcher.MatchResult.MATCHED;
        }
    }

    static class CustomBlackWhiteMatcher extends AbstractBlackWhiteMatcher<Integer> {
        @Override
        public void setBlackMatcher(SubMatcher<Integer> blackMatcher) {
            super.setBlackMatcher(blackMatcher);
        }

        @Override
        public void setWhiteMatcher(SubMatcher<Integer> whiteMatcher) {
            super.setWhiteMatcher(whiteMatcher);
        }
    }
}