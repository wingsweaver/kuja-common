package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * 各部分的数值的生成器的接口定义。
 *
 * @author wingsweaver
 */
public interface PartGenerator {
    /**
     * 获取本部分的 BIT 数。
     *
     * @return BIT 数
     */
    int bits();

    /**
     * 获取本部分的最大值。
     *
     * @return 最大值
     */
    long maxValue();
}
