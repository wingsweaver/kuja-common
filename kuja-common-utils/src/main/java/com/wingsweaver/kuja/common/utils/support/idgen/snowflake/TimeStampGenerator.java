package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * 时间戳部分的生成器的接口定义。
 *
 * @author wingsweaver
 */
public interface TimeStampGenerator extends PartGenerator {
    /**
     * 将指定的 EPOCH (毫秒) 转换成 TimeStamp 数值。
     *
     * @param epoch EPOCH (毫秒)
     * @return TimeStamp 数值
     */
    long toTimeStamp(long epoch);

    /**
     * 将生成的 TimeStamp 数值还原成对应的 EPOCH (毫秒)。
     *
     * @param timestamp TimeStamp 数值
     * @return EPOCH (毫秒)
     */
    long toEpoch(long timestamp);
}
