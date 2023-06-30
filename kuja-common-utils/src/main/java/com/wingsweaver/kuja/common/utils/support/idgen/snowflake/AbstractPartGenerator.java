package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;

/**
 * {@link PartGenerator} 的实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractPartGenerator extends AbstractComponent implements PartGenerator {
    /**
     * BIT 数。
     */
    private final int bits;

    /**
     * 最大值。
     */
    private final long maxValue;

    public AbstractPartGenerator(int bits) {
        this.bits = bits;
        this.maxValue = (1L << bits) - 1;
    }

    @Override
    public int bits() {
        return this.bits;
    }

    @Override
    public long maxValue() {
        return this.maxValue;
    }
}
