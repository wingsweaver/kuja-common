package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.utils.model.TagsTempsAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;

/**
 * {@link MessageSendContext} 的访问器。
 *
 * @author wingsweaver
 */
@Getter
public class MessageSendContextAccessor extends TagsTempsAccessor {
    /**
     * 消息发送上下文。
     */
    private final MessageSendContext context;

    public MessageSendContextAccessor(MessageSendContext context) {
        super(context);
        this.context = context;
    }

    /**
     * 发送设置。
     */
    public static final String KEY_SEND_OPTIONS = ClassUtil.resolveKey(MessageSendContextAccessor.class, "send-options");

    public Object getSendOptions() {
        return this.context.getTempValue(KEY_SEND_OPTIONS);
    }

    public void setSendOptions(Object sendOptions) {
        this.context.setTempValue(KEY_SEND_OPTIONS, sendOptions);
    }

    /**
     * 发送线程。
     */
    public static final String KEY_SEND_THREAD = ClassUtil.resolveKey(MessageSendContextAccessor.class, "send-thread");

    public Thread getSendThread() {
        return this.context.getTempValue(KEY_SEND_THREAD);
    }

    public void setSendThread(Thread sendThread) {
        this.context.setTempValue(KEY_SEND_THREAD, sendThread);
    }

    /**
     * {@link MessageSender} 实例。
     */
    public static final String KEY_MESSAGE_SENDER = ClassUtil.resolveKey(MessageSendContextAccessor.class, "message-sender");

    public MessageSender getMessageSender() {
        return this.context.getTempValue(KEY_MESSAGE_SENDER);
    }

    public void setMessageSender(MessageSender messageSender) {
        this.context.setTempValue(KEY_MESSAGE_SENDER, messageSender);
    }

    /**
     * {@link MessageSendService} 实例。
     */
    public static final String KEY_MESSAGE_SEND_SERVICE = ClassUtil.resolveKey(MessageSendContextAccessor.class, "message-send-service");

    public MessageSendService getMessageSendService() {
        return this.context.getTempValue(KEY_MESSAGE_SEND_SERVICE);
    }

    public void setMessageSendService(MessageSendService messageSendService) {
        this.context.setTempValue(KEY_MESSAGE_SEND_SERVICE, messageSendService);
    }

    /**
     * 消息目的地。
     */
    public static final String KEY_MESSAGE_DESTINATION = ClassUtil.resolveKey(MessageSendContextAccessor.class, "message-destination");

    public Object getMessageDestination() {
        return this.context.getTempValue(KEY_MESSAGE_DESTINATION);
    }

    public void setMessageDestination(Object messageDestination) {
        this.context.setTempValue(KEY_MESSAGE_DESTINATION, messageDestination);
    }

    /**
     * 消息内容。
     */
    public static final String KEY_PAYLOAD = ClassUtil.resolveKey(MessageSendContextAccessor.class, "payload");

    public Object getPayload() {
        return this.context.getTempValue(KEY_PAYLOAD);
    }

    public void setPayload(Object payload) {
        this.context.setTempValue(KEY_PAYLOAD, payload);
    }
}
