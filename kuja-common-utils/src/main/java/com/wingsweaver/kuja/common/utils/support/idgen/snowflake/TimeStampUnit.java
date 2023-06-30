package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * 时间戳的单位。
 *
 * @author wingsweaver
 */
public enum TimeStampUnit {
    /**
     * 毫秒。
     */
    MILLISECONDS(1),

    /**
     * 10 毫秒。
     */
    TEN_MILLISECONDS(10),

    /**
     * 100 毫秒。
     */
    HUNDRED_MILLISECONDS(100),

    /**
     * 秒。
     */
    SECONDS(1000);

    private final int multiplier;

    TimeStampUnit(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return this.multiplier;
    }
}
