package com.wingsweaver.kuja.common.utils.constants;

import org.springframework.core.Ordered;

/**
 * Bean Order 定义。
 *
 * @author wingsweaver
 */
public interface Orders {
    /**
     * 最高优先度。
     */
    int HIGHEST_PRECEDENCE = Ordered.HIGHEST_PRECEDENCE;

    /**
     * 最低优先度。
     */
    int LOWEST_PRECEDENCE = Ordered.LOWEST_PRECEDENCE;

    /**
     * 默认优先度。
     */
    int DEFAULT_PRECEDENCE = 0;

    /**
     * 极小的步长。
     */
    int STEP_TINY = 256;

    /**
     * 较小的步长。
     */
    int STEP_SMALL = STEP_TINY * 16;

    /**
     * 中等的步长。
     */
    int STEP_MEDIUM = STEP_SMALL * 16;

    /**
     * 较大的步长。
     */
    int STEP_LARGE = STEP_MEDIUM * 16;

    /**
     * 极大的步长。
     */
    int STEP_HUGE = STEP_LARGE * 16;

    /**
     * 计算更高优先级的数值。
     *
     * @param base  基数
     * @param delta 增量
     * @return 更高优先级的数值
     */
    static int higher(int base, int delta) {
        return base - delta;
    }

    /**
     * 计算更低优先级的数值。
     *
     * @param base  基数
     * @param delta 增量
     * @return 更低优先级的数值
     */
    static int lower(int base, int delta) {
        return base + delta;
    }

    /**
     * 获取指定实例的优先级。
     *
     * @param object 实例
     * @return 优先级
     */
    static int getOrder(Object object) {
        if (object instanceof Ordered) {
            return ((Ordered) object).getOrder();
        } else {
            return 0;
        }
    }

    /**
     * 比较两个 {@link Ordered} 实例的优先级。
     *
     * @param t1 第一个 {@link Ordered} 实例
     * @param t2 第二个 {@link Ordered} 实例
     * @return 比较结果
     */
    static int compare(Object t1, Object t2) {
        return Integer.compare(getOrder(t1), getOrder(t2));
    }

    /**
     * 比较两个 {@link Ordered} 实例的优先级，反转比较结果。
     *
     * @param t1 第一个 {@link Ordered} 实例
     * @param t2 第二个 {@link Ordered} 实例
     * @return 比较结果
     */
    static int compareReversed(Object t1, Object t2) {
        return Integer.compare(getOrder(t2), getOrder(t1));
    }
}
