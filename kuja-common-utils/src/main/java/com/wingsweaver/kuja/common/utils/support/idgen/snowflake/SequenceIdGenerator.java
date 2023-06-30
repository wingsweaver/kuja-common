package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * 序列 ID 生成器的接口定义。
 *
 * @author wingsweaver
 */
public interface SequenceIdGenerator extends PartGenerator {
    /**
     * 没有更多的序列 ID。
     */
    Long NO_MORE_SEQUENCE_ID = null;

    /**
     * 获取下一个 Sequence Id。
     *
     * @param timestamp 时间戳的数值
     * @return 下一个 Sequence Id。如果无法生成更多的 SequenceId，那么返回 null。
     */
    Long nextSequenceId(long timestamp);
}
