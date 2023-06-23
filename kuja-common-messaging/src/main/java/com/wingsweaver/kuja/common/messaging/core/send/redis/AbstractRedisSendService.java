package com.wingsweaver.kuja.common.messaging.core.send.redis;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.messaging.core.send.common.AbstractMessageSendService;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于 Redis 的 {@link AbstractMessageSendService} 实现类的基类。
 *
 * @author wingsweaver
 */
@SuppressWarnings("DuplicatedCode")
@Getter
@Setter
public abstract class AbstractRedisSendService extends AbstractMessageSendService<RedisSendOptions> {
    @Override
    protected RedisSendOptions createSendOptions() {
        return new RedisSendOptions();
    }

    @Override
    protected void initSendOptions(MessageSendContext context, RedisSendOptions options) {
        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);

        // 计算 Topic
        String topic = this.resolveTopic(context);
        options.setTopic(topic);
        contextAccessor.setMessageDestination(topic);

        // 计算 Payload
        Object payload = this.resolvePayload(context);
        options.setPayload(payload);
        contextAccessor.setPayload(payload);
    }

    /**
     * 计算消息的 Topic。
     *
     * @param context 消息发送上下文
     * @return 消息的 Topic
     */
    protected String resolveTopic(MessageSendContext context) {
        Object destination = this.resolveDestination(context);
        if (destination instanceof CharSequence) {
            return destination.toString();
        } else {
            return null;
        }
    }
}
