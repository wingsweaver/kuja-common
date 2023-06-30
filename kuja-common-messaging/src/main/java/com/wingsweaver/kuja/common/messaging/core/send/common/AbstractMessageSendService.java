package com.wingsweaver.kuja.common.messaging.core.send.common;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendService;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessagingSendContextAccessor;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@link MessageSendService} 现类的基类。
 *
 * @param <O> 发送选项的类型
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractMessageSendService<O> extends AbstractComponent implements MessageSendService {
    /**
     * 优先级。
     */
    private int order;

    /**
     * 检查是否支持指定消息的处理函数。
     */
    private MessagePredicate messagePredicate;

    /**
     * 消息内容解析器的列表。
     */
    private List<PayloadResolver> payloadResolvers;

    /**
     * 消息目标解析器的列表。
     */
    private List<DestinationResolver> destinationResolvers;

    /**
     * 默认的消息目的地。
     */
    private Object defaultDestination;

    @Override
    public boolean supportDestination(Object destination) {
        return destination == null || destination instanceof CharSequence;
    }

    @Override
    public void send(MessageSendContext context, MessageSendCallback callback) {
        if (this.isAsyncMode()) {
            this.sendAsync(context, callback);
        } else {
            this.sendSync(context, callback);
        }
    }

    /**
     * 以同步模式进行发送。
     *
     * @param context  消息发送上下文
     * @param callback 消息发送回调
     */
    protected void sendSync(MessageSendContext context, MessageSendCallback callback) {
        // 开始发送
        this.onStart(context, callback);

        // 进行发送
        Throwable error = null;
        try {
            // 准备发送
            O options = this.resolveSendOptions(context);

            // 执行发送
            this.sendMessage(context, options, callback);
        } catch (Throwable ex) {
            // 结束发送
            error = ex;
        }

        // 完成发送
        if (error != null) {
            this.onError(context, error, callback);
        } else {
            this.onSuccess(context, callback);
        }
    }

    /**
     * 以异步模式进行发送。
     *
     * @param context  消息发送上下文
     * @param callback 消息发送回调
     */
    protected void sendAsync(MessageSendContext context, MessageSendCallback callback) {
        // 开始发送
        this.onStart(context, callback);

        try {
            // 准备发送
            O options = this.resolveSendOptions(context);

            // 执行发送
            this.sendMessage(context, options, callback);
        } catch (Exception error) {
            this.onError(context, error, callback);
        }
    }

    /**
     * 计算发送设置。
     *
     * @param context 消息发送上下文
     * @return 发送设置
     */
    protected O resolveSendOptions(MessageSendContext context) {
        O options = this.createSendOptions();

        MessageSendContextAccessor contextAccessor = new MessagingSendContextAccessor(context);
        contextAccessor.setSendOptions(options);

        this.initSendOptions(context, options);
        return options;
    }

    /**
     * 创建消息发送设置。
     *
     * @return 消息发送设置
     */
    protected abstract O createSendOptions();

    /**
     * 初始化消息发送设置。
     *
     * @param context 消息发送上下文
     * @param options 消息发送设置
     */
    protected abstract void initSendOptions(MessageSendContext context, O options);

    /**
     * 检查是否是异步模式。
     *
     * @return 是否是异步模式
     */
    public boolean isAsyncMode() {
        return false;
    }

    /**
     * 计算要发送的消息的实际内容。
     *
     * @param context 消息发送上下文
     * @return 消息的实际内容
     */
    protected Object resolvePayload(MessageSendContext context) {
        // 检查 Context 中的 payload
        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);
        Object payload = contextAccessor.getPayload();
        if (payload != null) {
            return payload;
        }

        // 先尝试使用 PayloadResolver 进行转换
        for (PayloadResolver payloadResolver : this.payloadResolvers) {
            Tuple2<Boolean, Object> result = payloadResolver.resolvePayload(context);
            if (Boolean.TRUE.equals(result.getT1())) {
                return result.getT2();
            }
        }

        // 无法转换的话，直接返回原始消息
        return context.getOriginalMessage();
    }

    /**
     * 计算要发送的消息的目标。
     *
     * @param context 消息发送上下文
     * @return 消息的实际目标
     */
    protected Object resolveDestination(MessageSendContext context) {
        return this.resolveDestination(context, true);
    }

    /**
     * 计算要发送的消息的目标。
     *
     * @param context    消息发送上下文
     * @param useDefault 是否使用默认的目标
     * @return 消息的实际目标
     */
    protected Object resolveDestination(MessageSendContext context, boolean useDefault) {
        // 检查 Context 中的 destination
        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);
        Object destination = contextAccessor.getMessageDestination();
        if (destination != null) {
            return destination;
        }

        // 先尝试使用 PayloadResolver 进行转换
        for (DestinationResolver destinationResolver : this.destinationResolvers) {
            Tuple2<Boolean, Object> result = destinationResolver.resolveDestination(context);
            if (Boolean.TRUE.equals(result.getT1())) {
                return result.getT2();
            }
        }

        // 无法转换的话，直接返回 null
        return useDefault ? this.getDefaultDestination() : null;
    }

    /**
     * 执行消息的发送。
     *
     * @param context  消息发送上下文
     * @param options  发送选项
     * @param callback 消息发送的回调处理
     */
    protected abstract void sendMessage(MessageSendContext context, O options, MessageSendCallback callback);

    @Override
    public boolean supportMessage(Object message) {
        return this.messagePredicate == null || this.messagePredicate.supportMessage(message);
    }

    /**
     * 触发消息发送开始的回调。
     *
     * @param context  消息发送上下文
     * @param callback 消息发送的回调处理
     */
    protected final void onStart(MessageSendContext context, MessageSendCallback callback) {
        if (callback != null) {
            callback.onStart(context);
        }
    }

    /**
     * 触发消息发送成功的回调。
     *
     * @param context  消息发送上下文
     * @param callback 消息发送的回调处理
     */
    protected final void onSuccess(MessageSendContext context, MessageSendCallback callback) {
        if (callback != null) {
            callback.onSuccess(context);
        }
    }

    /**
     * 触发消息发送失败的回调。
     *
     * @param context  消息发送上下文
     * @param error    发送失败的异常
     * @param callback 消息发送的回调处理
     */
    protected final void onError(MessageSendContext context, Throwable error, MessageSendCallback callback) {
        if (callback != null) {
            callback.onFail(context, error);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 messagePredicate
        this.initMessagePredicate();

        // 初始化 PayloadResolver
        this.initPayloadResolvers();

        // 初始化 DestinationResolver
        this.initDestinationResolvers();
    }

    /**
     * 初始化 messagePredicate。
     */
    protected void initMessagePredicate() {
        // 什么都不做
    }

    /**
     * 初始化 destinationResolvers。
     */
    protected void initDestinationResolvers() {
        if (this.destinationResolvers == null) {
            this.destinationResolvers = this.getBeansOrdered(DestinationResolver.class);
        }
    }

    /**
     * 初始化 payloadResolvers。
     */
    protected void initPayloadResolvers() {
        if (this.payloadResolvers == null) {
            this.payloadResolvers = this.getBeansOrdered(PayloadResolver.class);
        }
    }
}
