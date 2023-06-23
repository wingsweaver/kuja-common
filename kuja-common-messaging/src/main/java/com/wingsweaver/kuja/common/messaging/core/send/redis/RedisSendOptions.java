package com.wingsweaver.kuja.common.messaging.core.send.redis;

import lombok.Getter;
import lombok.Setter;

/**
 * 适用于 Redis Streams 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RedisSendOptions {
    /**
     * 消息的 Topic。
     */
    private String topic;

    /**
     * 消息内容。
     */
    private Object payload;
}
