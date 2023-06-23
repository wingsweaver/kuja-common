package com.wingsweaver.kuja.common.utils.constants;

/**
 * 数据大小的定义。
 *
 * @author wingsweaver
 */
@SuppressWarnings("PMD.ConstantsInInterface")
public interface BufferSizes {
    /**
     * KB。
     */
    int KB = 1024;

    /**
     * MB。
     */
    int MB = KB * KB;

    /**
     * GB。
     */
    int GB = MB * KB;

    /**
     * TB。
     */
    long TB = (long) GB * KB;

    /**
     * PB。
     */
    long PB = TB * KB;

    /**
     * 极小。
     */
    int TINY = 16;

    /**
     * 较小。
     */
    int SMALL = 256;

    /**
     * 中等大小。
     */
    int MEDIUM = KB;

    /**
     * 较大。
     */
    int LARGE = KB * 16;

    /**
     * 极大。
     */
    int HUGE = KB * 64;
}
