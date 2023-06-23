package com.wingsweaver.kuja.common.utils.support.idgen;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 基于 {@link UUID} 的 {@link StringIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
public class UuidStringIdGenerator implements StringIdGenerator {
    /**
     * 是否保留连字符。
     */
    private boolean withHyphens = true;

    @Override
    public String nextId() {
        String uuid = UUID.randomUUID().toString();
        if (!this.withHyphens) {
            uuid = StringUtils.remove(uuid, '-');
        }
        return uuid;
    }

    public boolean isWithHyphens() {
        return withHyphens;
    }

    public void setWithHyphens(boolean withHyphens) {
        this.withHyphens = withHyphens;
    }
}
