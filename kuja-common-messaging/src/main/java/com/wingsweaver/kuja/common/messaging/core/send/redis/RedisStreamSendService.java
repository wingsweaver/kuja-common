package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 基于 spring-redis 的 {@link RedisTemplate} 的实现类。<br>
 * 使用了 Redis Stream 操作，需要 Redis 5.0 以上的版本。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RedisStreamSendService extends AbstractRedisStreamSendService {
    /**
     * RedisTemplate 实例。
     */
    private StringRedisTemplate redisTemplate;

    @Override
    protected void sendMessage(MessageSendContext context, RedisSendOptions options, MessageSendCallback callback) {
        ObjectRecord<String, Object> record = this.resolveRecord(context, options);
        RecordId recordId = this.redisTemplate.opsForStream().add(record);
        this.setRecordId(context, recordId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 RedisTemplate
        this.initRedisTemplate();
    }

    /**
     * 初始化 RedisTemplate。
     */
    protected void initRedisTemplate() {
        if (this.redisTemplate == null) {
            this.redisTemplate = this.getApplicationContext().getBean(StringRedisTemplate.class);
        }
    }
}
