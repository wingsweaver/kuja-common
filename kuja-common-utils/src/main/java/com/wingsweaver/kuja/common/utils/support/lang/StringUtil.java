package com.wingsweaver.kuja.common.utils.support.lang;

import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * 字符串工具类。
 *
 * @author wingsweaver
 */
public final class StringUtil {
    private StringUtil() {
        // 禁止实例化
    }

    /**
     * 检测字符串是否为空。
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 检测字符串是否不为空。
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 将字符串进行裁剪。
     *
     * @param str 字符串
     * @return 裁剪后的字符串
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 将字符串进行裁剪。
     *
     * @param str 字符串
     * @return 裁剪后的字符串
     */
    public static String trimToNull(String str) {
        String trimmed = trimToEmpty(str);
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 检查字符串是否非空。如果空的话，使用回退值。
     *
     * @param str           字符串
     * @param fallbackValue 回退值
     * @return 处理结果
     */
    public static String notEmptyOrElse(String str, String fallbackValue) {
        return isEmpty(str) ? fallbackValue : str;
    }

    /**
     * 检查字符串是否非空。如果空的话，使用回退值。
     *
     * @param str                   字符串
     * @param fallbackValueSupplier 回退值的计算函数
     * @return 处理结果
     */
    public static String notEmptyOrElse(String str, Supplier<String> fallbackValueSupplier) {
        return isEmpty(str) ? fallbackValueSupplier.get() : str;
    }

    /**
     * 从函数中计算字符串。如果函数为 null 的话，使用回退值。
     *
     * @param supplier      函数
     * @param fallbackValue 回退值
     * @return 处理结果
     */
    public static String fromSupplierOrDefault(Supplier<String> supplier, String fallbackValue) {
        return supplier != null ? supplier.get() : fallbackValue;
    }

    /**
     * 从函数中计算字符串。如果函数为 null 的话，使用空字符串。
     *
     * @param supplier 函数
     * @return 处理结果
     */
    public static String fromSupplierOrEmpty(Supplier<String> supplier) {
        return supplier != null ? supplier.get() : "";
    }

    /**
     * 从函数中计算字符串。如果函数为 null 的话，使用 null。
     *
     * @param supplier 函数
     * @return 处理结果
     */
    public static String fromSupplierOrNull(Supplier<String> supplier) {
        return supplier != null ? supplier.get() : null;
    }

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * @param str        字符串
     * @param str2       字符串2
     * @param ignorecase 是否忽略大小写
     * @return 比较结果
     */
    public static int compare(String str, String str2, boolean ignorecase) {
        if (str == null) {
            return str2 == null ? 0 : -1;
        } else if (str2 == null) {
            return 1;
        } else {
            return ignorecase ? str.compareToIgnoreCase(str2) : str.compareTo(str2);
        }
    }

    /**
     * 如果字符串为 null 的话，使用回退值；否则返回指定字符串。
     *
     * @param str           字符串
     * @param fallbackValue 回退值
     * @return 处理结果
     */
    public static String notNullOr(String str, String fallbackValue) {
        return str != null ? str : fallbackValue;
    }

    /**
     * 如果字符串为 null 的话，使用回退值；否则返回指定字符串。
     *
     * @param str                   字符串
     * @param fallbackValueSupplier 回退值的计算函数
     * @return 处理结果
     */
    public static String notNullOr(String str, Supplier<String> fallbackValueSupplier) {
        return str != null ? str : fallbackValueSupplier.get();
    }

    /**
     * 如果字符串为空的话，使用回退值；否则返回指定字符串。
     *
     * @param str           字符串
     * @param fallbackValue 回退值
     * @return 处理结果
     */
    public static String notEmptyOr(String str, String fallbackValue) {
        return str != null && !str.isEmpty() ? str : fallbackValue;
    }

    /**
     * 如果字符串为空的话，使用回退值；否则返回指定字符串。
     *
     * @param str                   字符串
     * @param fallbackValueSupplier 回退值的计算函数
     * @return 处理结果
     */
    public static String notEmptyOr(String str, Supplier<String> fallbackValueSupplier) {
        return str != null && !str.isEmpty() ? str : fallbackValueSupplier.get();
    }

    /**
     * 如果字符串为空白的话，使用回退值；否则返回指定字符串。
     *
     * @param str           字符串
     * @param fallbackValue 回退值
     * @return 处理结果
     */
    public static String notBlankOr(String str, String fallbackValue) {
        return StringUtils.hasText(str) ? str : fallbackValue;
    }

    /**
     * 如果字符串为空白的话，使用回退值；否则返回指定字符串。
     *
     * @param str                   字符串
     * @param fallbackValueSupplier 回退值的计算函数
     * @return 处理结果
     */
    public static String notBlankOr(String str, Supplier<String> fallbackValueSupplier) {
        return StringUtils.hasText(str) ? str : fallbackValueSupplier.get();
    }
}
