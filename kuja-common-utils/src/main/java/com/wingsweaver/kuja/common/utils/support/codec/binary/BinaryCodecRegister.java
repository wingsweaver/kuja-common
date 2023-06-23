package com.wingsweaver.kuja.common.utils.support.codec.binary;

import java.util.Collection;

/**
 * {@link BinaryCodecProvider} 注册器。
 *
 * @author wingsweaver
 */
public interface BinaryCodecRegister {
    /**
     * 注册 {@link BinaryCodecProvider} 到集合中。
     *
     * @param providers 集合
     */
    void register(Collection<BinaryCodecProvider> providers);
}
