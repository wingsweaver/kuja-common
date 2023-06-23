package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;

import java.util.Map;

/**
 * 用于 spring-messaging 场景的 {@link MessageSendContextAccessor} 实现类。
 *
 * @author wingsweaver
 */
public class MessagingSendContextAccessor extends MessageSendContextAccessor {
    public MessagingSendContextAccessor(MessageSendContext context) {
        super(context);
    }

    /**
     * 消息头。
     */
    public static final String KEY_HEADERS = ClassUtil.resolveKey(MessagingSendContextAccessor.class, "headers");

    public Map<String, Object> getHeaders() {
        return this.getContext().getTempValue(KEY_HEADERS);
    }

    public void setHeaders(Map<String, Object> headers) {
        this.getContext().setTempValue(KEY_HEADERS, headers);
    }

    /**
     * 消息的处理器。
     */
    public static final String KEY_POST_PROCESSOR = ClassUtil.resolveKey(MessagingSendContextAccessor.class, "post-processor");

    public MessagePostProcessor getPostProcessor() {
        return this.getContext().getTempValue(KEY_POST_PROCESSOR);
    }

    public void setPostProcessor(MessagePostProcessor postProcessor) {
        this.getContext().setTempValue(KEY_POST_PROCESSOR, postProcessor);
    }

    /**
     * spring-messaging 中的消息实例。
     */
    public static final String KEY_CORE_MESSAGE = ClassUtil.resolveKey(MessagingSendContextAccessor.class, "core-message");

    public Message<?> getCoreMessage() {
        return this.getContext().getTempValue(KEY_CORE_MESSAGE);
    }

    public void setCoreMessage(Message<?> coreMessage) {
        this.getContext().setTempValue(KEY_CORE_MESSAGE, coreMessage);
    }
}
