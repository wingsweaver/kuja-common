package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;

/**
 * 基于 spring-redis 的实现类。<br>
 * 使用了 Redis Stream 操作，需要 Redis 5.0 以上的版本。
 *
 * @author wingsweaver
 */
public abstract class AbstractRedisStreamSendService extends AbstractRedisSendService {
    /**
     * 设置消息发送上下文中的 Record Id。
     *
     * @param context  消息发送上下文
     * @param recordId Record Id
     */
    protected void setRecordId(MessageSendContext context, RecordId recordId) {
        RedisStreamSendContextAccessor contextAccessor = new RedisStreamSendContextAccessor(context);
        contextAccessor.setRecordId(recordId);
    }

    /**
     * 计算要发送的消息的 Record。
     *
     * @param context 消息发送上下文
     * @param options 消息发送设置
     * @return 要发送的消息的 Record
     */
    protected ObjectRecord<String, Object> resolveRecord(MessageSendContext context, RedisSendOptions options) {
        String streamKey = this.resolveStreamKey(options);
        return StreamRecords.newRecord()
                .ofObject(options.getPayload())
                .withStreamKey(streamKey);
    }

    /**
     * 计算消息的 Stream Key。
     *
     * @param options 消息发送设置
     * @return 消息的 Record Id
     */
    protected String resolveStreamKey(RedisSendOptions options) {
        String topic = options.getTopic();
        if (StringUtil.isNotEmpty(topic)) {
            return topic;
        }
        return String.valueOf(this.getDefaultDestination());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }
}
