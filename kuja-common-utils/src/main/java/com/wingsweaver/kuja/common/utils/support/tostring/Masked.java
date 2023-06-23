package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于屏蔽指定数据的注解。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ToStringWith(MaskedConverter.class)
public @interface Masked {
    /**
     * 屏蔽用字符。
     *
     * @return 屏蔽用字符
     */
    char value() default '*';

    /**
     * 屏蔽后的最大长度。
     *
     * @return 屏蔽后的长度
     */
    int maxLength() default 0;

    /**
     * 屏蔽范围。<br>
     * 默认屏蔽所有字符串。
     *
     * @return 屏蔽范围
     */
    Range[] ranges() default {};

    /**
     * 设置屏蔽范围的注解。<br>
     * 范围是一个左闭右开的区间，即 [{@code start}, {@code end})。<br>
     * start = 0, end = 0 表示屏蔽整个字符串。
     */
    @interface Range {
        /**
         * 开始坐标。<br>
         * 小于 0 的话，从字符串末尾开始计数。<br>
         * 如: -1 表示 length - 1 的位置。
         *
         * @return 开始坐标
         */
        int start() default 0;

        /**
         * 结束坐标。<br>
         * 小于等于 0 的话，从字符串末尾开始计数。<br>
         * 如: -1 表示 length - 1 的位置。
         *
         * @return 结束坐标
         */
        int end() default 0;
    }
}
