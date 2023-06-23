package com.wingsweaver.kuja.common.utils.diag;

import com.wingsweaver.kuja.common.utils.support.EmptyChecker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertArgsTest {
    @Test
    void test() {
        AssertArgs.isTrue(true, "should be true");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isTrue(false, "should be true"));

        AssertArgs.isTrue(true, () -> "should be true");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isTrue(false, () -> "should be true"));

        AssertArgs.isFalse(false, "should be false");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isFalse(true, "should be false"));

        AssertArgs.isFalse(false, () -> "should be false");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isFalse(true, () -> "should be false"));

        AssertArgs.isNull(null, "should be null");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isNull(new Object(), "should be null"));

        AssertArgs.isNull(null, () -> "should be null");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isNull(new Object(), () -> "should be null"));

        AssertArgs.notNull(new Object(), "should not be null");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notNull(null, "should not be null"));

        AssertArgs.notNull(new Object(), () -> "should not be null");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notNull(null, () -> "should not be null"));

        AssertArgs.isEmpty(null, "null should be empty");
        AssertArgs.isEmpty("", "empty string should be empty");
        AssertArgs.isEmpty(new Object[0], "empty array should be empty");
        AssertArgs.isEmpty(Collections.emptyList(), "empty collection should be empty");
        AssertArgs.isEmpty(Collections.emptyMap(), "empty map should be empty");
        AssertArgs.isEmpty(CustomEmpty.EMPTY, "empty checker with true should be empty");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(" ", "blank text should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(new Object[1], "non-empty array should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(Collections.singletonList("a"), "non-empty collection should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(Collections.singletonMap("a", "b"), "non-empty map should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(CustomEmpty.NOT_EMPTY, "empty checker with false should not be empty"));

        AssertArgs.isEmpty(null, () -> "null should be empty");
        AssertArgs.isEmpty("", () -> "empty string should be empty");
        AssertArgs.isEmpty(new Object[0], () -> "empty array should be empty");
        AssertArgs.isEmpty(Collections.emptyList(), () -> "empty collection should be empty");
        AssertArgs.isEmpty(Collections.emptyMap(), () -> "empty map should be empty");
        AssertArgs.isEmpty(CustomEmpty.EMPTY, () -> "empty checker with true should be empty");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(" ", () -> "blank text should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(new Object[1], () -> "non-empty array should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(Collections.singletonList("a"), () -> "non-empty collection should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(Collections.singletonMap("a", "b"), () -> "non-empty map should not be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.isEmpty(CustomEmpty.NOT_EMPTY, () -> "empty checker with false should not be empty"));

        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(null, "null should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty("", "empty string should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(new Object[0], "empty array should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(Collections.emptyList(), "empty collection should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(Collections.emptyMap(), "empty map should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(CustomEmpty.EMPTY, "empty checker with true should be empty"));
        AssertArgs.notEmpty(" ", "blank text should not be empty");
        AssertArgs.notEmpty(new Object[1], "non-empty array should not be empty");
        AssertArgs.notEmpty(Collections.singletonList("a"), "non-empty collection should not be empty");
        AssertArgs.notEmpty(Collections.singletonMap("a", "b"), "non-empty map should not be empty");
        AssertArgs.notEmpty(CustomEmpty.NOT_EMPTY, "empty checker with false should not be empty");

        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(null, () -> "null should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty("", () -> "empty string should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(new Object[0], () -> "empty array should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(Collections.emptyList(), () -> "empty collection should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(Collections.emptyMap(), () -> "empty map should be empty"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.notEmpty(CustomEmpty.EMPTY, () -> "empty checker with true should be empty"));
        AssertArgs.notEmpty(" ", () -> "blank text should not be empty");
        AssertArgs.notEmpty(new Object[1], () -> "non-empty array should not be empty");
        AssertArgs.notEmpty(Collections.singletonList("a"), () -> "non-empty collection should not be empty");
        AssertArgs.notEmpty(Collections.singletonMap("a", "b"), () -> "non-empty map should not be empty");
        AssertArgs.notEmpty(CustomEmpty.NOT_EMPTY, () -> "empty checker with false should not be empty");
    }

    @Test
    void testNamed() {
        AssertArgs.Named.isTrue("isTrue-1", true);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isTrue("isTrue-2", false));

        AssertArgs.Named.isFalse("isFalse-1", false);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isFalse("isFalse-2", true));

        AssertArgs.Named.isNull("isNull-1", null);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isNull("isNull-2", new Object()));

        AssertArgs.Named.notNull("notNull-1", new Object());
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notNull("notNull-2", null));

        AssertArgs.Named.isEmpty("isEmpty-1", null);
        AssertArgs.Named.isEmpty("isEmpty-2", "");
        AssertArgs.Named.isEmpty("isEmpty-3", new Object[0]);
        AssertArgs.Named.isEmpty("isEmpty-4", Collections.emptyList());
        AssertArgs.Named.isEmpty("isEmpty-5", Collections.emptyMap());
        AssertArgs.Named.isEmpty("isEmpty-6", CustomEmpty.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isEmpty("isEmpty-7", " "));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isEmpty("isEmpty-8", new Object[1]));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isEmpty("isEmpty-9", Collections.singletonList("a")));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isEmpty("isEmpty-10", Collections.singletonMap("a", "b")));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isEmpty("isEmpty-11", CustomEmpty.NOT_EMPTY));

        AssertArgs.Named.notEmpty("notEmpty-1", " ");
        AssertArgs.Named.notEmpty("notEmpty-2", new Object[1]);
        AssertArgs.Named.notEmpty("notEmpty-3", Collections.singletonList("a"));
        AssertArgs.Named.notEmpty("notEmpty-4", Collections.singletonMap("a", "b"));
        AssertArgs.Named.notEmpty("notEmpty-5", CustomEmpty.NOT_EMPTY);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-6", null));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-7", ""));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-8", new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-9", Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-10", Collections.emptyMap()));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notEmpty("notEmpty-11", CustomEmpty.EMPTY));

        AssertArgs.Named.isBlank("isBlank-1", null);
        AssertArgs.Named.isBlank("isBlank-2", "");
        AssertArgs.Named.isBlank("isBlank-3", " \t");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.isBlank("isBlank-4", "a"));

        AssertArgs.Named.notBlank("notBlank-1", "a");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notBlank("notBlank-2", null));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notBlank("notBlank-3", ""));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.notBlank("notBlank-4", " \t"));

        AssertArgs.Named.startsWith("startsWith-1", "abc", "a");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.startsWith("startsWith-2", "abc", "b"));

        AssertArgs.Named.endsWith("endsWith-1", "abc", "c");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.endsWith("endsWith-2", "abc", "b"));

        AssertArgs.Named.contains("contains-1", "abc", "b");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.contains("contains-2", "abc", "d"));

        AssertArgs.Named.likes("notContains-1", "abc", "a(.)+");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.likes("notContains-2", "abc", "b(.)+"));

        AssertArgs.Named.likes("notContains-1", "abc", Pattern.compile("a(.)+"));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.likes("notContains-2", "abc", Pattern.compile("b(.)+")));

        AssertArgs.Named.equals("equals-1", "abc", "abc");
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.equals("equals-2", "abc", "def"));

        AssertArgs.Named.lessThan("lessThan-1", 1, 2);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.lessThan("lessThan-2", 2, 1));

        AssertArgs.Named.greaterThan("greaterThan-1", 2, 1);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.greaterThan("greaterThan-2", 1, 2));

        AssertArgs.Named.noLessThan("noLessThan-1", 2, 1);
        AssertArgs.Named.noLessThan("noLessThan-2", 1, 1);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.noLessThan("noLessThan-3", 1, 2));

        AssertArgs.Named.noGreaterThan("noGreaterThan-1", 1, 2);
        AssertArgs.Named.noGreaterThan("noGreaterThan-2", 1, 1);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.noGreaterThan("noGreaterThan-3", 2, 1));

        AssertArgs.Named.between("between-1", 2, 1, 3);
        AssertArgs.Named.between("between-1", 2, 3, 1);
        AssertArgs.Named.between("between-2", 1, 1, 3);
        AssertArgs.Named.between("between-3", 3, 1, 3);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.between("between-4", 4, 3, 1));

        AssertArgs.Named.in("in-1", 1, 1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.in("in-4", 4, 1, 2, 3));

        AssertArgs.Named.in("in-1", 1, Arrays.asList(1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> AssertArgs.Named.in("in-4", 4, Arrays.asList(1, 2, 3)));
    }

    static class CustomEmpty implements EmptyChecker {
        public static final CustomEmpty EMPTY = new CustomEmpty(true);
        public static final CustomEmpty NOT_EMPTY = new CustomEmpty(false);

        private final boolean empty;

        CustomEmpty(boolean empty) {
            this.empty = empty;
        }

        @Override
        public boolean isEmpty() {
            return this.empty;
        }
    }
}