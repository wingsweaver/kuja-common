package com.wingsweaver.kuja.common.utils.support.idgen;

/**
 * 生成字符串型 ID 的 {@link IdGenerator} 的接口定义。
 *
 * @author wingsweaver
 */
public interface StringIdGenerator extends IdGenerator<String> {
    /**
     * 默认的字符串型 ID 生成器。
     */
    StringIdGenerator FALLBACK = UuidStringIdGenerator.DEFAULT;
}
