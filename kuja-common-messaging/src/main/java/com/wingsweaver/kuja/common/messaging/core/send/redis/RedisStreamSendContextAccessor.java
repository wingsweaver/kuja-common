package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.springframework.data.redis.connection.stream.RecordId;

/**
 * 用于 spring-redis 中 Redis Stream 场景的 {@link MessageSendContextAccessor} 实现类。
 *
 * @author wingsweaver
 */
public class RedisStreamSendContextAccessor extends MessageSendContextAccessor {
    public RedisStreamSendContextAccessor(MessageSendContext context) {
        super(context);
    }

    public static final String KEY_RECORD_ID = ClassUtil.resolveKey(RedisStreamSendContextAccessor.class, "record-id");

    public RecordId getRecordId() {
        return this.getContext().getTempValue(KEY_RECORD_ID);
    }

    public void setRecordId(RecordId recordId) {
        this.getContext().setTempValue(KEY_RECORD_ID, recordId);
    }
}
