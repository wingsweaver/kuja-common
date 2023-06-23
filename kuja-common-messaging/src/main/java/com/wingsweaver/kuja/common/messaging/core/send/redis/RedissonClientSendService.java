package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;

/**
 * 基于 Redisson 的 {@link RedissonClient} 的消息发送服务。
 *
 * @author wingswewaver
 */
@Getter
@Setter
public class RedissonClientSendService extends AbstractRedisSendService {
    /**
     * RedissonClient 实例。
     */
    private RedissonClient redissonClient;

    /**
     * 消息的编解码器。
     */
    private Codec codec;

    @Override
    protected void sendMessage(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        String topic = options.getTopic();
        RTopic rtopic = (this.codec != null) ? this.redissonClient.getTopic(topic, this.codec) : this.redissonClient.getTopic(topic);
        rtopic.publish(options.getPayload());
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
            this.redissonClient = this.getApplicationContext().getBean(RedissonClient.class);
        }
    }
}
