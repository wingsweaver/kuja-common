package com.wingsweaver.kuja.common.utils.support.json;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * JSON 序列化器工厂的接口定义。
 *
 * @author wingsweaver
 */
public interface JsonCodecFactory extends DefaultOrdered {
    /**
     * 创建 JSON 序列化器。
     *
     * @return JSON 序列化器
     */
    JsonCodec createJsonCodec();
}
