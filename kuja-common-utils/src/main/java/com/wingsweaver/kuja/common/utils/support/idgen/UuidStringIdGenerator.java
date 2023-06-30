package com.wingsweaver.kuja.common.utils.support.idgen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 基于 {@link UUID} 的 {@link StringIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UuidStringIdGenerator implements StringIdGenerator {
    /**
     * 没有连字符的 UUID 生成器。
     */
    public static final UuidStringIdGenerator WITHOUT_HYPHEN = new UuidStringIdGenerator(false);

    /**
     * 含有连字符的 UUID 生成器。
     */
    public static final UuidStringIdGenerator WITH_HYPHEN = new UuidStringIdGenerator(true);

    /**
     * 默认的 UUID 生成器。
     */
    public static final UuidStringIdGenerator DEFAULT = WITHOUT_HYPHEN;

    /**
     * 是否保留连字符。
     */
    private boolean withHyphens = false;

    @Override
    public String nextId() {
        String uuid = UUID.randomUUID().toString();
        if (!this.withHyphens) {
            uuid = StringUtils.remove(uuid, '-');
        }
        return uuid;
    }
}
