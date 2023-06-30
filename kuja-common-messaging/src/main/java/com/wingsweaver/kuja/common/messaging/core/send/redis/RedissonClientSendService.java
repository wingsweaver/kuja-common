package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RFuture;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RShardedTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于 Redisson 的 {@link RedissonClient} 的消息发送服务。
 *
 * @author wingswewaver
 */
@Getter
@Setter
@SuppressWarnings("PMD.GuardLogStatement")
public class RedissonClientSendService extends AbstractRedisSendService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonClientSendService.class);

    /**
     * RedissonClient 实例。
     */
    private RedissonClient redissonClient;

    /**
     * 消息的编解码器。
     */
    private Codec codec;

    /**
     * TOPIC 的模式。
     */
    private RedissonTopicMode topicMode = RedissonTopicMode.TOPIC;

    @Override
    protected void sendMessage(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        switch (this.topicMode) {
            case SHARED_TOPIC:
                this.sendAsSharedTopic(context, options, callback);
                break;
            case RELIABLE_TOPIC:
                this.sendAsReliableTopic(context, options, callback);
                break;
            case TOPIC:
            default:
                this.sendAsTopic(context, options, callback);
                break;
        }
    }

    /**
     * 使用 {@link RReliableTopic} 进行发送。
     *
     * @param context  消息发送上下文
     * @param options  消息发送设置
     * @param callback 消息发送回调函数
     */
    protected void sendAsReliableTopic(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        String topic = options.getTopic();
        RReliableTopic shardedTopic = (this.codec != null)
                ? this.redissonClient.getReliableTopic(topic, this.codec) : this.redissonClient.getReliableTopic(topic);
        if (this.isAsyncMode()) {
            LogUtil.trace(LOGGER, "Send Redisson message asynchronously to reliable topic {}, context id = {}", topic, context.getId());
            this.callbackFuture(context, options, callback, shardedTopic.publishAsync(options.getPayload()));
        } else {
            LogUtil.trace(LOGGER, "Send Redisson message to reliable topic {}, context id = {}", topic, context.getId());
            shardedTopic.publish(options.getPayload());
        }
    }

    /**
     * 使用 {@link RShardedTopic} 进行发送。
     *
     * @param context  消息发送上下文
     * @param options  消息发送设置
     * @param callback 消息发送回调函数
     */
    protected void sendAsSharedTopic(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        String topic = options.getTopic();
        RShardedTopic shardedTopic = (this.codec != null)
                ? this.redissonClient.getShardedTopic(topic, this.codec) : this.redissonClient.getShardedTopic(topic);
        if (this.isAsyncMode()) {
            LogUtil.trace(LOGGER, "Send Redisson message asynchronously to shared topic {}, context id = {}", topic, context.getId());
            this.callbackFuture(context, options, callback, shardedTopic.publishAsync(options.getPayload()));
        } else {
            LogUtil.trace(LOGGER, "Send Redisson message to shared topic {}, context id = {}", topic, context.getId());
            shardedTopic.publish(options.getPayload());
        }
    }

    /**
     * 使用 {@link RTopic} 进行发送。
     *
     * @param context  消息发送上下文
     * @param options  消息发送设置
     * @param callback 消息发送回调函数
     */
    protected void sendAsTopic(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        String topic = options.getTopic();
        RTopic rtopic = (this.codec != null)
                ? this.redissonClient.getTopic(topic, this.codec) : this.redissonClient.getTopic(topic);
        if (this.isAsyncMode()) {
            LogUtil.trace(LOGGER, "Send Redisson message asynchronously to topic {}, context id = {}", topic, context.getId());
            this.callbackFuture(context, options, callback, rtopic.publishAsync(options.getPayload()));
        } else {
            LogUtil.trace(LOGGER, "Send Redisson message to topic {}, context id = {}", topic, context.getId());
            rtopic.publish(options.getPayload());
        }
    }

    /**
     * 异步处理 {@link RFuture} 任务。
     *
     * @param context  消息发送上下文
     * @param options  消息发送设置
     * @param callback 消息发送回调函数
     * @param future   异步处理任务
     * @param <T>      异步任务的返回值的类型
     */
    protected <T> void callbackFuture(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback,
                                      RFuture<T> future) {
        future.whenComplete((result, error) -> {
            if (error == null) {
                this.onSuccess(context, callback);
            } else {
                this.onError(context, error, callback);
            }
        });
    }

    @Override
    protected String resolveTopic(MessageSendContext context) {
        String topic = super.resolveTopic(context);
        if (StringUtil.isEmpty(topic)) {
            topic = String.valueOf(this.getDefaultDestination());
        }
        return topic;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 RedissonClient
        this.initRedissonClient();

        // 初始化 codec
        this.initCodec();
    }

    /**
     * 初始化 codec。
     */
    protected void initCodec() {
        // 什么也不做
    }

    /**
     * 初始化 RedissonClient。
     */
    protected void initRedissonClient() {
        if (this.redissonClient == null) {
            this.redissonClient = this.getBean(RedissonClient.class);
        }
    }
}
