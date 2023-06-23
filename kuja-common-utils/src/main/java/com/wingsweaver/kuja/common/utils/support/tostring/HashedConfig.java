package com.wingsweaver.kuja.common.utils.support.tostring;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link HashedConverter} 的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class HashedConfig {
    /**
     * 默认的哈希算法。
     */
    public static final String DEFAULT_ALGORITHM = "MD5";

    /**
     * 默认的编码格式。
     */
    public static final String DEFAULT_CODEC = "base64";

    /**
     * 哈希算法。
     */
    private String algorithm = DEFAULT_ALGORITHM;

    /**
     * 编码格式。
     */
    private String codec = DEFAULT_CODEC;
}
