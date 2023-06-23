package com.wingsweaver.kuja.common.utils.diag;

import com.wingsweaver.kuja.common.utils.support.EmptyChecker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertStateTest {
    @Test
    void test() {
        AssertState.isTrue(true, "should be true");
        assertThrows(IllegalStateException.class, () -> AssertState.isTrue(false, "should be true"));

        AssertState.isTrue(true, () -> "should be true");
        assertThrows(IllegalStateException.class, () -> AssertState.isTrue(false, () -> "should be true"));

        AssertState.isFalse(false, "should be false");
        assertThrows(IllegalStateException.class, () -> AssertState.isFalse(true, "should be false"));

        AssertState.isFalse(false, () -> "should be false");
        assertThrows(IllegalStateException.class, () -> AssertState.isFalse(true, () -> "should be false"));

        AssertState.isNull(null, "should be null");
        assertThrows(IllegalStateException.class, () -> AssertState.isNull(new Object(), "should be null"));

        AssertState.isNull(null, () -> "should be null");
        assertThrows(IllegalStateException.class, () -> AssertState.isNull(new Object(), () -> "should be null"));

        AssertState.notNull(new Object(), "should not be null");
        assertThrows(IllegalStateException.class, () -> AssertState.notNull(null, "should not be null"));

        AssertState.notNull(new Object(), () -> "should not be null");
        assertThrows(IllegalStateException.class, () -> AssertState.notNull(null, () -> "should not be null"));

        AssertState.isEmpty(null, "null should be empty");
        AssertState.isEmpty("", "empty string should be empty");
        AssertState.isEmpty(new Object[0], "empty array should be empty");
        AssertState.isEmpty(Collections.emptyList(), "empty collection should be empty");
        AssertState.isEmpty(Collections.emptyMap(), "empty map should be empty");
        AssertState.isEmpty(CustomEmpty.EMPTY, "empty checker with true should be empty");
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(" ", "blank text should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(new Object[1], "non-empty array should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(Collections.singletonList("a"), "non-empty collection should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(Collections.singletonMap("a", "b"), "non-empty map should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(CustomEmpty.NOT_EMPTY, "empty checker with false should not be empty"));

        AssertState.isEmpty(null, () -> "null should be empty");
        AssertState.isEmpty("", () -> "empty string should be empty");
        AssertState.isEmpty(new Object[0], () -> "empty array should be empty");
        AssertState.isEmpty(Collections.emptyList(), () -> "empty collection should be empty");
        AssertState.isEmpty(Collections.emptyMap(), () -> "empty map should be empty");
        AssertState.isEmpty(CustomEmpty.EMPTY, () -> "empty checker with true should be empty");
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(" ", () -> "blank text should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(new Object[1], () -> "non-empty array should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(Collections.singletonList("a"), () -> "non-empty collection should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(Collections.singletonMap("a", "b"), () -> "non-empty map should not be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.isEmpty(CustomEmpty.NOT_EMPTY, () -> "empty checker with false should not be empty"));

        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(null, "null should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty("", "empty string should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(new Object[0], "empty array should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(Collections.emptyList(), "empty collection should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(Collections.emptyMap(), "empty map should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(CustomEmpty.EMPTY, "empty checker with true should be empty"));
        AssertState.notEmpty(" ", "blank text should not be empty");
        AssertState.notEmpty(new Object[1], "non-empty array should not be empty");
        AssertState.notEmpty(Collections.singletonList("a"), "non-empty collection should not be empty");
        AssertState.notEmpty(Collections.singletonMap("a", "b"), "non-empty map should not be empty");
        AssertState.notEmpty(CustomEmpty.NOT_EMPTY, "empty checker with false should not be empty");

        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(null, () -> "null should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty("", () -> "empty string should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(new Object[0], () -> "empty array should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(Collections.emptyList(), () -> "empty collection should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(Collections.emptyMap(), () -> "empty map should be empty"));
        assertThrows(IllegalStateException.class, () -> AssertState.notEmpty(CustomEmpty.EMPTY, () -> "empty checker with true should be empty"));
        AssertState.notEmpty(" ", () -> "blank text should not be empty");
        AssertState.notEmpty(new Object[1], () -> "non-empty array should not be empty");
        AssertState.notEmpty(Collections.singletonList("a"), () -> "non-empty collection should not be empty");
        AssertState.notEmpty(Collections.singletonMap("a", "b"), () -> "non-empty map should not be empty");
        AssertState.notEmpty(CustomEmpty.NOT_EMPTY, () -> "empty checker with false should not be empty");
    }

    @Test
    void testNamed() {
        AssertState.Named.isTrue("isTrue-1", true);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isTrue("isTrue-2", false));

        AssertState.Named.isFalse("isFalse-1", false);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isFalse("isFalse-2", true));

        AssertState.Named.isNull("isNull-1", null);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isNull("isNull-2", new Object()));

        AssertState.Named.notNull("notNull-1", new Object());
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notNull("notNull-2", null));

        AssertState.Named.isEmpty("isEmpty-1", null);
        AssertState.Named.isEmpty("isEmpty-2", "");
        AssertState.Named.isEmpty("isEmpty-3", new Object[0]);
        AssertState.Named.isEmpty("isEmpty-4", Collections.emptyList());
        AssertState.Named.isEmpty("isEmpty-5", Collections.emptyMap());
        AssertState.Named.isEmpty("isEmpty-6", CustomEmpty.EMPTY);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isEmpty("isEmpty-7", " "));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isEmpty("isEmpty-8", new Object[1]));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isEmpty("isEmpty-9", Collections.singletonList("a")));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isEmpty("isEmpty-10", Collections.singletonMap("a", "b")));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isEmpty("isEmpty-11", CustomEmpty.NOT_EMPTY));

        AssertState.Named.notEmpty("notEmpty-1", " ");
        AssertState.Named.notEmpty("notEmpty-2", new Object[1]);
        AssertState.Named.notEmpty("notEmpty-3", Collections.singletonList("a"));
        AssertState.Named.notEmpty("notEmpty-4", Collections.singletonMap("a", "b"));
        AssertState.Named.notEmpty("notEmpty-5", CustomEmpty.NOT_EMPTY);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-6", null));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-7", ""));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-8", new Object[0]));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-9", Collections.emptyList()));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-10", Collections.emptyMap()));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notEmpty("notEmpty-11", CustomEmpty.EMPTY));

        AssertState.Named.isBlank("isBlank-1", null);
        AssertState.Named.isBlank("isBlank-2", "");
        AssertState.Named.isBlank("isBlank-3", " \t");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.isBlank("isBlank-4", "a"));

        AssertState.Named.notBlank("notBlank-1", "a");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notBlank("notBlank-2", null));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notBlank("notBlank-3", ""));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.notBlank("notBlank-4", " \t"));

        AssertState.Named.startsWith("startsWith-1", "abc", "a");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.startsWith("startsWith-2", "abc", "b"));

        AssertState.Named.endsWith("endsWith-1", "abc", "c");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.endsWith("endsWith-2", "abc", "b"));

        AssertState.Named.contains("contains-1", "abc", "b");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.contains("contains-2", "abc", "d"));

        AssertState.Named.likes("notContains-1", "abc", "a(.)+");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.likes("notContains-2", "abc", "b(.)+"));

        AssertState.Named.likes("notContains-1", "abc", Pattern.compile("a(.)+"));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.likes("notContains-2", "abc", Pattern.compile("b(.)+")));

        AssertState.Named.equals("equals-1", "abc", "abc");
        assertThrows(IllegalStateException.class, () -> AssertState.Named.equals("equals-2", "abc", "def"));

        AssertState.Named.lessThan("lessThan-1", 1, 2);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.lessThan("lessThan-2", 2, 1));

        AssertState.Named.greaterThan("greaterThan-1", 2, 1);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.greaterThan("greaterThan-2", 1, 2));

        AssertState.Named.noLessThan("noLessThan-1", 2, 1);
        AssertState.Named.noLessThan("noLessThan-2", 1, 1);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.noLessThan("noLessThan-3", 1, 2));

        AssertState.Named.noGreaterThan("noGreaterThan-1", 1, 2);
        AssertState.Named.noGreaterThan("noGreaterThan-2", 1, 1);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.noGreaterThan("noGreaterThan-3", 2, 1));

        AssertState.Named.between("between-1", 2, 1, 3);
        AssertState.Named.between("between-1", 2, 3, 1);
        AssertState.Named.between("between-2", 1, 1, 3);
        AssertState.Named.between("between-3", 3, 1, 3);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.between("between-4", 4, 3, 1));

        AssertState.Named.in("in-1", 1, 1, 2, 3);
        assertThrows(IllegalStateException.class, () -> AssertState.Named.in("in-4", 4, 1, 2, 3));

        AssertState.Named.in("in-1", 1, Arrays.asList(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> AssertState.Named.in("in-4", 4, Arrays.asList(1, 2, 3)));
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