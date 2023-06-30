package com.wingsweaver.kuja.common.utils.support.idgen;

/**
 * 生成 Long 型 ID 的 {@link IdGenerator} 的接口定义。
 *
 * @author wingsweaver
 */
public interface LongIdGenerator extends IdGenerator<Long> {
    /**
     * ID 的最大 BIT 数。
     */
    int MAX_ID_BITS = 63;

    /**
     * 没有更多的 ID。<br>
     * 在 {@link #nextId()} 返回该值时，表示没有更多的 ID 可以生成了。
     */
    Long NO_MORE_ID = null;

    /**
     * 默认的 Long 型 ID 生成器。
     */
    LongIdGenerator FALLBACK = LocalLongIdGenerator.INSTANCE;
}
