package com.wingsweaver.kuja.common.messaging.core.send.redis;

/**
 * Redisson Topic 的模式定义。
 *
 * @author wingsweaver
 */
public enum RedissonTopicMode {
    /**
     * 普通的 {@link org.redisson.api.RTopic}。
     */
    TOPIC,

    /**
     * 适用于集群的 {@link org.redisson.api.RShardedTopic}。
     */
    SHARED_TOPIC,

    /**
     * 基于 Redis Stream 的 {@link org.redisson.api.RReliableTopic}。
     */
    RELIABLE_TOPIC
}
