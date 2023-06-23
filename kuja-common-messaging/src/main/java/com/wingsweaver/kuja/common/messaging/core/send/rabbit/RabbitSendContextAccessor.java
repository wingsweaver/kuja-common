package com.wingsweaver.kuja.common.messaging.core.send.rabbit;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessagingSendContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;

/**
 * 用于 spring-rabbit 场景的 {@link MessageSendContextAccessor} 实现类。
 *
 * @author wingsweaver
 */
public class RabbitSendContextAccessor extends MessagingSendContextAccessor {
    public RabbitSendContextAccessor(MessageSendContext context) {
        super(context);
    }

    /**
     * Routing Key。
     */
    public static final String KEY_ROUTING_KEY = ClassUtil.resolveKey(RabbitSendContextAccessor.class, "routing-key");

    public String getRoutingKey() {
        return this.getContext().getTempValue(KEY_ROUTING_KEY);
    }

    public void setRoutingKey(String routingKey) {
        this.getContext().setTempValue(KEY_ROUTING_KEY, routingKey);
    }
}
