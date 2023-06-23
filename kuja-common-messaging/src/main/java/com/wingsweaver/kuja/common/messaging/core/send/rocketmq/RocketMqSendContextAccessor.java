package com.wingsweaver.kuja.common.messaging.core.send.rocketmq;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessagingSendContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * 用于 RocketMQ 场景的 {@link MessageSendContextAccessor} 实现类。
 *
 * @author wingsweaver
 */
public class RocketMqSendContextAccessor extends MessagingSendContextAccessor {
    public RocketMqSendContextAccessor(MessageSendContext context) {
        super(context);
    }

    public static final String KEY_ORDER_HASHKEY = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "order-hashkey");

    public String getOrderHashKey() {
        return this.getContext().getTempValue(KEY_ORDER_HASHKEY);
    }

    public void setOrderHashKey(String orderHashKey) {
        this.getContext().setTempValue(KEY_ORDER_HASHKEY, orderHashKey);
    }

    public static final String KEY_TIMEOUT = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "timeout");

    public long getTimeout() {
        return this.getContext().getTempValue(KEY_TIMEOUT, 0L);
    }

    public void setTimeout(Long timeout) {
        this.getContext().setTempValue(KEY_TIMEOUT, timeout);
    }

    public static final String KEY_SEND_STATUS = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "send-status");

    public SendStatus getSendStatus() {
        return this.getContext().getTempValue(KEY_SEND_STATUS);
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.getContext().setTempValue(KEY_SEND_STATUS, sendStatus);
    }

    public static final String KEY_MSG_ID = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "msg-id");

    public String getMsgId() {
        return this.getContext().getTempValue(KEY_MSG_ID);
    }

    public void setMsgId(String msgId) {
        this.getContext().setTempValue(KEY_MSG_ID, msgId);
    }

    public static final String KEY_MESSAGE_QUEUE = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "message-queue");

    public MessageQueue getMessageQueue() {
        return this.getContext().getTempValue(KEY_MESSAGE_QUEUE);
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.getContext().setTempValue(KEY_MESSAGE_QUEUE, messageQueue);
    }

    public static final String KEY_TRANSACTION_ID = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "transaction-id");

    public String getTransactionId() {
        return this.getContext().getTempValue(KEY_TRANSACTION_ID);
    }

    public void setTransactionId(String transactionId) {
        this.getContext().setTempValue(KEY_TRANSACTION_ID, transactionId);
    }

    public static final String KEY_REGION_ID = ClassUtil.resolveKey(RocketMqSendContextAccessor.class, "region-id");

    public String getRegionId() {
        return this.getContext().getTempValue(KEY_REGION_ID);
    }

    public void setRegionId(String regionId) {
        this.getContext().setTempValue(KEY_REGION_ID, regionId);
    }
}
