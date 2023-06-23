package com.wingsweaver.kuja.common.utils.support.codec.binary;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 二进制编解码器的服务提供者的接口定义。
 *
 * @author wingsweaver
 */
public interface BinaryCodecProvider extends DefaultOrdered {
    /**
     * 获取服务名称。
     *
     * @return 服务名称
     */
    String getName();

    /**
     * 将字节数组编码为字符串。
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    String encode(byte[] bytes);

    /**
     * 将字符串解码为字节数组。
     *
     * @param text 字符串
     * @return 字节数组
     */
    byte[] decode(String text);
}
