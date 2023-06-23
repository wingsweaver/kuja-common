package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.common.AbstractMessageSendService;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.core.MessagePostProcessor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于 spring-messaging 的 {@link AbstractMessageSendingTemplate} 的 {@link AbstractMessageSendService} 实现类。
 *
 * @param <D> 消息目的地的类型
 * @param <T> 消息发送模板的类型
 * @param <O> 消息发送的配置项的类型
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractMessagingTemplateSendService<D, T extends AbstractMessageSendingTemplate<D>,
        O extends MessageSendingTemplateOptions>
        extends AbstractMessageSendService<O> {
    /**
     * 消息发送模板。
     */
    private T messagingTemplate;

    /**
     * 消息头解析器的列表。
     */
    private List<HeadersResolver> headersResolvers;

    /**
     * 消息后处理器解析器的列表。
     */
    private List<PostProcessorResolver> postProcessorResolvers;

    /**
     * 初始化消息发送设置。
     *
     * @param context 消息发送上下文
     * @param options 消息发送设置
     */
    @Override
    protected void initSendOptions(MessageSendContext context, O options) {
        MessagingSendContextAccessor contextAccessor = new MessagingSendContextAccessor(context);

        // 解析要发送的消息内容
        Object payload = this.resolvePayload(context);
        options.setPayload(payload);
        contextAccessor.setPayload(payload);

        // 解析消息接收的目的地
        Object destination = this.resolveDestination(context);
        options.setDestination(destination);
        contextAccessor.setMessageDestination(destination);

        // 解析消息头
        Map<String, Object> headers = this.resolveHeaders(context);
        options.setHeaders(headers);
        contextAccessor.setHeaders(headers);

        // 解析消息处理器
        MessagePostProcessor postProcessor = this.resolveMessagePostProcessor(context);
        options.setPostProcessor(postProcessor);
        contextAccessor.setPostProcessor(postProcessor);
    }

    /**
     * 计算消息处理函数。
     *
     * @param context 消息发送上下文
     * @return 消息处理函数
     */
    protected MessagePostProcessor resolveMessagePostProcessor(MessageSendContext context) {
        // 检查 Context 中自带的 MessagePostProcessor
        MessagingSendContextAccessor contextAccessor = new MessagingSendContextAccessor(context);
        MessagePostProcessor postProcessor = contextAccessor.getPostProcessor();
        if (postProcessor != null) {
            return postProcessor;
        }

        // 生成新的 MessagePostProcessor
        List<MessagePostProcessor> postProcessors = new LinkedList<>();
        this.postProcessorResolvers.forEach(resolver -> resolver.resolvePostProcessors(context, postProcessors));
        postProcessors.sort(Orders::compare);
        postProcessors.add(fallbackMessagePostProcessor(context));
        return new CompositeMessagePostProcessor(postProcessors);
    }

    /**
     * 计算消息头。
     *
     * @param context 消息发送上下文
     * @return 消息头
     */
    protected Map<String, Object> resolveHeaders(MessageSendContext context) {
        // 检查 Context 中自带的 Headers
        MessagingSendContextAccessor contextAccessor = new MessagingSendContextAccessor(context);
        Map<String, Object> headers = contextAccessor.getHeaders();

        // 检查参数
        if (this.headersResolvers.isEmpty()) {
            return headers;
        }

        // 生成 Headers
        if (headers == null) {
            headers = new HashMap<>(BufferSizes.SMALL);
        }

        // 初始化 Headers
        for (HeadersResolver headersResolver : this.headersResolvers) {
            headersResolver.resolveHeaders(context, headers);
        }

        // 返回
        return headers;
    }

    /**
     * 计算消息接收的目的地。
     *
     * @param context 消息发送上下文
     * @return 消息接收的目的地
     */
    @Override
    protected Object resolveDestination(MessageSendContext context) {
        // 使用标准的解析处理
        Object destination = super.resolveDestination(context, false);

        // 无法解析的话，使用默认的目的地
        if (destination == null) {
            destination = ObjectUtil.notNullOr(this.messagingTemplate.getDefaultDestination(), this.getDefaultDestination());
        }

        // 返回
        return destination;
    }

    /**
     * 兜底的用于更新消息发送上下文的消息实体的后处理器。
     *
     * @param context 消息发送上下文
     * @return 消息实体的后处理器
     */
    private MessagePostProcessor fallbackMessagePostProcessor(MessageSendContext context) {
        return message -> {
            MessagingSendContextAccessor contextAccessor = new MessagingSendContextAccessor(context);
            contextAccessor.setCoreMessage(message);
            return message;
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 messagingTemplate
        this.initMessagingTemplate();

        // 初始化 headersResolver
        this.initHeadersResolvers();

        // 初始化 postProcessorResolvers
        this.initPostProcessorResolvers();
    }

    /**
     * 初始化 postProcessorResolvers。
     */
    protected void initPostProcessorResolvers() {
        if (this.postProcessorResolvers == null) {
            this.postProcessorResolvers = this.getBeansOrdered(PostProcessorResolver.class);
        }
    }

    /**
     * 初始化 headersResolvers。
     */
    protected void initHeadersResolvers() {
        if (this.headersResolvers == null) {
            this.headersResolvers = this.getBeansOrdered(HeadersResolver.class);
        }
    }

    /**
     * 初始化 messagingTemplate。
     */
    protected void initMessagingTemplate() {
        if (this.messagingTemplate == null) {
            this.messagingTemplate = this.getApplicationContext().getBean(this.getMessagingTemplateType());
        }
    }

    /**
     * 获取消息发送模板的类型。
     *
     * @return 消息发送模板的类型
     */
    public abstract Class<T> getMessagingTemplateType();
}
