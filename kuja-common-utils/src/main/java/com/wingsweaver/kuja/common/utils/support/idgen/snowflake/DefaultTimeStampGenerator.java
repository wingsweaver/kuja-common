package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import lombok.Getter;
import lombok.Setter;

/**
 * 时间戳部分的生成器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultTimeStampGenerator extends AbstractPartGenerator implements TimeStampGenerator {
    /**
     * 开始的时间戳。
     */
    private long startEpoch;

    /**
     * 时间戳的单位。
     */
    private TimeStampUnit unit;

    public DefaultTimeStampGenerator(int bits) {
        super(bits);
    }

    /**
     * 生成指定时间戳对应的 TimeStamp 数值。
     *
     * @param epochUtc UTC EPOCH (毫秒)
     * @return TimeStamp 数值
     */
    @Override
    public long toTimeStamp(long epochUtc) {
        long timestamp = (epochUtc - this.startEpoch) / this.unit.getMultiplier();
        if (timestamp > this.maxValue()) {
            long maxEpochUtc = toEpoch(this.maxValue());
            throw new IllegalArgumentException("epochUtc should be no more than " + maxEpochUtc + ", current is " + epochUtc);
        }
        return timestamp;
    }

    /**
     * 将生成的时间戳转换成 Epoch。
     *
     * @param timestamp TimeStamp 数值
     * @return Epoch
     */
    @Override
    public long toEpoch(long timestamp) {
        AssertArgs.Named.noGreaterThan("timestamp", timestamp, this.maxValue());
        return timestamp * this.unit.getMultiplier() + this.startEpoch;
    }

}
