package com.wingsweaver.kuja.common.messaging.core.send.rocketmq;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.AbstractMessagingTemplateSendService;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 基于 rocketmq-spring 的 {@link RocketMQTemplate} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings("PMD.GuardLogStatement")
public class RocketMqTemplateSendService extends AbstractMessagingTemplateSendService<String, RocketMQTemplate, RocketMqMessageSendOptions> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqTemplateSendService.class);

    /**
     * 是否是异步模式。
     */
    private boolean asyncMode;

    /**
     * 消息发送超时的解析器列表。
     */
    private List<OrderHashKeyResolver> orderHashKeyResolvers;

    /**
     * 消息发送超时的解析器列表。
     */
    private List<TimeOutResolver> timeOutResolvers;

    @Override
    protected void sendMessage(MessageSendContext context, RocketMqMessageSendOptions options, MessageSendCallback callback) {
        if (this.isAsyncMode()) {
            this.sendMessageAsync(context, options, callback);
        } else {
            this.sendMessageSync(context, options, callback);
        }
    }

    /**
     * 异步发送消息。
     *
     * @param context  消息发送上下文
     * @param options  发送选项
     * @param callback 消息发送的回调处理
     */
    protected void sendMessageAsync(MessageSendContext context, RocketMqMessageSendOptions options, MessageSendCallback callback) {
        String destination = String.valueOf(options.getDestination());
        Object payload = options.getPayload();
        String orderHashKey = options.getOrderHashKey();
        long timeout = options.getTimeout();

        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                RocketMqTemplateSendService.this.updateContext(context, sendResult);
                RocketMqTemplateSendService.this.onSuccess(context, callback);
            }

            @Override
            public void onException(Throwable e) {
                RocketMqTemplateSendService.this.onError(context, e, callback);
            }
        };

        if (this.shouldSendOrderly(orderHashKey)) {
            // 使用顺序发送
            LogUtil.trace(LOGGER, "Send RocketMQ message asynchronously & orderly to destination: {}, order hash key: {}, context id = {}",
                    destination, orderHashKey, context.getId());
            if (timeout > 0) {
                this.getMessagingTemplate().asyncSendOrderly(destination, payload, orderHashKey, sendCallback, timeout);
            } else {
                this.getMessagingTemplate().asyncSendOrderly(destination, payload, orderHashKey, sendCallback);
            }
        } else {
            // 不使用顺序发送
            LogUtil.trace(LOGGER, "Send RocketMQ message asynchronously to destination: {}, context id = {}", destination, context.getId());
            if (timeout > 0) {
                this.getMessagingTemplate().asyncSend(destination, payload, sendCallback, timeout);
            } else {
                this.getMessagingTemplate().asyncSend(destination, payload, sendCallback);
            }
        }
    }

    /**
     * 更新消息发送上下文。
     *
     * @param context    消息发送上下文
     * @param sendResult 消息发送结果
     */
    protected void updateContext(MessageSendContext context, SendResult sendResult) {
        RocketMqSendContextAccessor contextAccessor = new RocketMqSendContextAccessor(context);
        contextAccessor.setSendStatus(sendResult.getSendStatus());
        contextAccessor.setMsgId(sendResult.getMsgId());
        contextAccessor.setMessageQueue(sendResult.getMessageQueue());
        contextAccessor.setTransactionId(sendResult.getTransactionId());
        contextAccessor.setRegionId(sendResult.getRegionId());
    }

    /**
     * 同步发送消息。
     *
     * @param context  消息发送上下文
     * @param options  发送选项
     * @param callback 消息发送的回调处理
     */
    protected void sendMessageSync(MessageSendContext context, RocketMqMessageSendOptions options, MessageSendCallback callback) {
        String destination = String.valueOf(options.getDestination());
        Object payload = options.getPayload();
        String orderHashKey = options.getOrderHashKey();
        long timeout = options.getTimeout();

        if (this.shouldSendOrderly(orderHashKey)) {
            // 使用顺序发送
            LogUtil.trace(LOGGER, "Send RocketMQ message orderly to destination: {}, order hash key: {}, context id = {}",
                    destination, orderHashKey, context.getId());
            if (timeout > 0) {
                this.getMessagingTemplate().syncSendOrderly(destination, payload, orderHashKey, timeout);
            } else {
                this.getMessagingTemplate().syncSendOrderly(destination, payload, orderHashKey);
            }
        } else {
            // 不使用顺序发送
            LogUtil.trace(LOGGER, "Send RocketMQ message to destination: {}, context id = {}", destination, context.getId());
            if (timeout > 0) {
                this.getMessagingTemplate().syncSend(destination, payload, timeout);
            } else {
                this.getMessagingTemplate().syncSend(destination, payload);
            }
        }
    }

    /**
     * 检查是否使用顺序发送。
     *
     * @param orderHashKey 顺序发送的 hash key
     * @return 如果使用顺序发送，则返回 true；否则返回 false。
     */
    protected boolean shouldSendOrderly(String orderHashKey) {
        return StringUtil.isNotEmpty(orderHashKey);
    }

    @Override
    protected RocketMqMessageSendOptions createSendOptions() {
        return new RocketMqMessageSendOptions();
    }

    @Override
    protected void initSendOptions(MessageSendContext context, RocketMqMessageSendOptions options) {
        super.initSendOptions(context, options);
        RocketMqSendContextAccessor contextAccessor = new RocketMqSendContextAccessor(context);

        // 计算 orderHashKey
        String orderHashKey = this.resolveOrderHashKey(context);
        options.setOrderHashKey(orderHashKey);
        contextAccessor.setOrderHashKey(orderHashKey);

        // 计算 timeout
        long timeout = this.resolveTimeout(context);
        options.setTimeout(timeout);
        contextAccessor.setTimeout(timeout);
    }

    /**
     * 计算消息发送超时时间。
     *
     * @param context 消息发送上下文
     * @return 消息发送超时时间
     */
    protected long resolveTimeout(MessageSendContext context) {
        // 检查 context 中的 timeout
        RocketMqSendContextAccessor contextAccessor = new RocketMqSendContextAccessor(context);
        long timeout = contextAccessor.getTimeout();
        if (timeout > 0) {
            return timeout;
        }

        // 使用 timeOutResolvers 计算 timeout
        for (TimeOutResolver timeOutResolver : this.timeOutResolvers) {
            timeout = timeOutResolver.resolveTimeout(context);
            if (timeout > 0) {
                return timeout;
            }
        }

        // 默认返回 0
        return 0;
    }

    /**
     * 计算顺序发送的 HashKey。
     *
     * @param context 消息发送上下文
     * @return 顺序发送的 HashKey
     */
    protected String resolveOrderHashKey(MessageSendContext context) {
        // 检查 context 中的 orderHashKey
        RocketMqSendContextAccessor contextAccessor = new RocketMqSendContextAccessor(context);
        String orderHashKey = contextAccessor.getOrderHashKey();
        if (orderHashKey != null) {
            return orderHashKey;
        }

        // 使用 orderHashKeyResolvers 计算 orderHashKey
        for (OrderHashKeyResolver orderHashKeyResolver : this.orderHashKeyResolvers) {
            orderHashKey = orderHashKeyResolver.resolveOrderHashKey(context);
            if (orderHashKey != null) {
                return orderHashKey;
            }
        }

        // 无效的话，返回 null
        return null;
    }

    @Override
    public Class<RocketMQTemplate> getMessagingTemplateType() {
        return RocketMQTemplate.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 orderHashKeyResolvers
        this.initOrderHashKeyResolvers();

        // 初始化 timeoutResolvers
        this.initTimeOutResolvers();
    }

    /**
     * 初始化 orderHashKeyResolvers。
     */
    protected void initOrderHashKeyResolvers() {
        if (this.orderHashKeyResolvers == null) {
            this.orderHashKeyResolvers = this.getBeansOrdered(OrderHashKeyResolver.class);
        }
    }

    /**
     * 初始化 timeoutResolvers。
     */
    protected void initTimeOutResolvers() {
        if (this.timeOutResolvers == null) {
            this.timeOutResolvers = this.getBeansOrdered(TimeOutResolver.class);
        }
    }
}
