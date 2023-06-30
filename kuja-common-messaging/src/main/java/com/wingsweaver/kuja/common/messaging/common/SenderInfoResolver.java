package com.wingsweaver.kuja.common.messaging.common;

import java.util.Map;

/**
 * 发送者信息解析器的接口定义。
 *
 * @author wingsweaver
 */
public interface SenderInfoResolver {
    /**
     * 解析发送者信息。
     *
     * @param senderInfo 发送者信息
     */
    void resolveSenderInfo(Map<String, String> senderInfo);
}
