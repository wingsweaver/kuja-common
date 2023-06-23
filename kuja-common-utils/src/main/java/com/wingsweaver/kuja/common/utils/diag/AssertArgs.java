package com.wingsweaver.kuja.common.utils.diag;

import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 抛出 {@link IllegalArgumentException} 异常的断言工具类。
 *
 * @author wingsweaver
 */
public final class AssertArgs {
    private AssertArgs() {
        // 禁止实例化
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * 断言指定的表达式为 true。
     *
     * @param condition 表达式
     * @param message   异常消息
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的表达式为 true。
     *
     * @param condition       表达式
     * @param messageSupplier 异常消息的生成函数
     */
    public static void isTrue(boolean condition, Supplier<String> messageSupplier) {
        if (!condition) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言指定的表达式为 false。
     *
     * @param condition 表达式
     * @param message   异常消息
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的表达式为 false。
     *
     * @param condition       表达式
     * @param messageSupplier 异常消息的生成函数
     */
    public static void isFalse(boolean condition, Supplier<String> messageSupplier) {
        if (condition) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言指定的对象为 null。
     *
     * @param object  指定的对象
     * @param message 异常消息
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的对象为 null。
     *
     * @param object          指定的对象
     * @param messageSupplier 异常消息的生成函数
     */
    public static void isNull(Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言指定的对象不为 null。
     *
     * @param object  指定的对象
     * @param message 异常消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的对象不为 null。
     *
     * @param object          指定的对象
     * @param messageSupplier 异常消息的生成函数
     */
    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言指定的对象为空。
     *
     * @param object  指定的对象
     * @param message 异常消息
     */
    public static void isEmpty(Object object, String message) {
        if (ObjectUtil.isNotEmpty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的对象为空。
     *
     * @param object          指定的对象
     * @param messageSupplier 异常消息的生成函数
     */
    public static void isEmpty(Object object, Supplier<String> messageSupplier) {
        if (ObjectUtil.isNotEmpty(object)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言指定的对象不为空。
     *
     * @param object  指定的对象
     * @param message 异常消息
     */
    public static void notEmpty(Object object, String message) {
        if (ObjectUtil.isEmpty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言指定的对象不为空。
     *
     * @param object          指定的对象
     * @param messageSupplier 异常消息的生成函数
     */
    public static void notEmpty(Object object, Supplier<String> messageSupplier) {
        if (ObjectUtil.isEmpty(object)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * 带名称的 {@link AssertArgs} 工具类。<br>
     * 在断言失败时，根据名称生成错误消息，而无需指定错误消息。
     */
    public static final class Named {
        private Named() {
            // 禁止实例化
        }

        /**
         * 断言指定的表达式为 true。
         *
         * @param name      名称
         * @param condition 表达式
         */
        public static void isTrue(String name, boolean condition) {
            AssertArgs.isTrue(condition, () -> MessageFormat.format("[{0}] should be true", name));
        }

        /**
         * 断言指定的表达式为 false。
         *
         * @param name      名称
         * @param condition 表达式
         */
        public static void isFalse(String name, boolean condition) {
            AssertArgs.isFalse(condition, () -> MessageFormat.format("[{0}] should be false", name));
        }

        /**
         * 断言指定的对象为 null。
         *
         * @param name   名称
         * @param object 指定的对象
         */
        public static void isNull(String name, Object object) {
            AssertArgs.isNull(object, () -> MessageFormat.format("[{0}] should be null", name));
        }

        /**
         * 断言指定的对象不为 null。
         *
         * @param name   名称
         * @param object 指定的对象
         */
        public static void notNull(String name, Object object) {
            AssertArgs.notNull(object, () -> MessageFormat.format("[{0}] should not be null", name));
        }

        /**
         * 断言指定的对象为空。
         *
         * @param name   名称
         * @param object 指定的对象
         */
        public static void isEmpty(String name, Object object) {
            AssertArgs.isEmpty(object, () -> MessageFormat.format("[{0}] should be empty", name));
        }

        /**
         * 断言指定的对象不为空。
         *
         * @param name   名称
         * @param object 指定的对象
         */
        public static void notEmpty(String name, Object object) {
            AssertArgs.notEmpty(object, () -> MessageFormat.format("[{0}] should not be empty", name));
        }

        /**
         * 断言指定的字符串为空白。
         *
         * @param name 名称
         * @param text 指定的字符串
         */
        public static void isBlank(String name, String text) {
            AssertArgs.isTrue(StringUtils.isBlank(text), () -> MessageFormat.format(
                    "[{0}] should be blank string, actual text is", name, text));
        }

        /**
         * 断言指定的字符串不为空白。
         *
         * @param name 名称
         * @param text 指定的字符串
         */
        public static void notBlank(String name, String text) {
            AssertArgs.isTrue(!StringUtils.isBlank(text), () -> MessageFormat.format(
                    "[{0}] should not be blank string", name));
        }

        /**
         * 断言指定的字符串以指定的前缀开头。
         *
         * @param name   名称
         * @param text   指定的字符串
         * @param prefix 指定的前缀
         */
        public static void startsWith(String name, String text, String prefix) {
            AssertArgs.isTrue(StringUtils.startsWith(text, prefix), () -> MessageFormat.format(
                    "[{0}] should starts with [{1}], actual text is [{2}]", name, prefix, text));
        }

        /**
         * 断言指定的字符串以指定的后缀结尾。
         *
         * @param name   名称
         * @param text   指定的字符串
         * @param suffix 指定的后缀
         */
        public static void endsWith(String name, String text, String suffix) {
            AssertArgs.isTrue(StringUtils.endsWith(text, suffix), () -> MessageFormat.format(
                    "[{0}] should ends with [{1}], actual text is [{2}]", name, suffix, text));
        }

        /**
         * 断言指定的字符串包含指定的子串。
         *
         * @param name      名称
         * @param text      指定的字符串
         * @param substring 指定的子串
         */
        public static void contains(String name, String text, String substring) {
            AssertArgs.isTrue(StringUtils.contains(text, substring), () -> MessageFormat.format(
                    "[{0}] should contains [{1}], actual text is [{2}]", name, substring, text));
        }

        /**
         * 断言指定的字符串匹配指定的正则表达式。
         *
         * @param name  名称
         * @param text  指定的字符串
         * @param regex 正则表达式
         */
        public static void likes(String name, String text, String regex) {
            AssertArgs.isTrue(text != null && text.matches(regex), () -> MessageFormat.format(
                    "[{0}] should matches regex pattern [{1}], actual text is [{2}]", name, regex, text));
        }

        /**
         * 断言指定的字符串匹配指定的正则表达式。
         *
         * @param name    名称
         * @param text    指定的字符串
         * @param pattern 正则表达式
         */
        public static void likes(String name, String text, Pattern pattern) {
            AssertArgs.isTrue(text != null && pattern.matcher(text).matches(), () -> MessageFormat.format(
                    "[{0}] should matches regex pattern [{1}], actual text is [{2}]", name, pattern, text));
        }

        /**
         * 断言指定的对象等于目标对象。
         *
         * @param name   名称
         * @param value  指定的对象
         * @param target 目标对象
         */
        public static void equals(String name, Object value, Object target) {
            AssertArgs.isTrue(value.equals(target), () -> MessageFormat.format(
                    "[{0}] should equals [{1}], actual value is [{2}]", name, target, value));
        }

        /**
         * 断言指定的对象小于目标对象。
         *
         * @param name   名称
         * @param value  指定的对象
         * @param target 目标对象
         * @param <T>    对象类型
         */
        public static <T extends Comparable<T>> void lessThan(String name, T value, T target) {
            AssertArgs.isTrue(value.compareTo(target) < 0, () -> MessageFormat.format(
                    "[{0}] should be less than [{1}], actual value is [{2}]", name, target, value));
        }

        /**
         * 断言指定的对象大于目标对象。
         *
         * @param name   名称
         * @param value  指定的对象
         * @param target 目标对象
         * @param <T>    对象类型
         */
        public static <T extends Comparable<T>> void greaterThan(String name, T value, T target) {
            AssertArgs.isTrue(value.compareTo(target) > 0, () -> MessageFormat.format(
                    "[{0}] should be greater than [{1}], actual value is [{2}]", name, target, value));
        }

        /**
         * 断言指定的对象不小于目标对象。
         *
         * @param name   名称
         * @param value  指定的对象
         * @param target 目标对象
         * @param <T>    对象类型
         */
        public static <T extends Comparable<T>> void noLessThan(String name, T value, T target) {
            AssertArgs.isTrue(value.compareTo(target) >= 0, () -> MessageFormat.format(
                    "[{0}] should be greater than or equal to [{1}], actual value is [{2}]", name, target, value));
        }

        /**
         * 断言指定的对象不大于目标对象。
         *
         * @param name   名称
         * @param value  指定的对象
         * @param target 目标对象
         * @param <T>    对象类型
         */
        public static <T extends Comparable<T>> void noGreaterThan(String name, T value, T target) {
            AssertArgs.isTrue(value.compareTo(target) <= 0, () -> MessageFormat.format(
                    "[{0}] should be less than or equal to [{1}], actual value is [{2}]", name, target, value));
        }

        /**
         * 断言指定的对象在指定的范围内。
         *
         * @param name  名称
         * @param value 指定的对象
         * @param min   最小值
         * @param max   最大值
         * @param <T>   对象类型
         */
        public static <T extends Comparable<T>> void between(String name, T value, T min, T max) {
            AssertArgs.isTrue(ObjectUtil.between(value, min, max), () -> MessageFormat.format(
                    "[{0}] should be between [{1}] and [{2}], actual value is [{3}]", name, min, max, value));
        }

        /**
         * 断言指定的对象包含在指定的数组之内。
         *
         * @param name    名称
         * @param value   指定的对象
         * @param targets 指定的数组
         */
        public static void in(String name, Object value, Object... targets) {
            AssertArgs.isTrue(ObjectUtil.in(value, targets), () -> MessageFormat.format(
                    "[{0}] should be in array [{1}], actual value is [{2}]", name, ToStringBuilder.toString(targets), value));
        }

        /**
         * 断言指定的对象包含在指定的集合之内。
         *
         * @param name    名称
         * @param value   指定的对象
         * @param targets 指定的集合
         */
        public static void in(String name, Object value, Collection<?> targets) {
            AssertArgs.isTrue(ObjectUtil.in(value, targets), () -> MessageFormat.format(
                    "[{0}] should be in collection [{1}], actual value is [{2}]", name, ToStringBuilder.toString(targets), value));
        }
    }
}
