package com.wingsweaver.kuja.common.messaging.core.send.rabbit;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.AbstractMessagingTemplateSendService;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;

import java.util.List;

/**
 * 基于 spring-rabbit 的 {@link RabbitMessagingTemplate} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RabbitMessagingTemplateSendService extends
        AbstractMessagingTemplateSendService<String, RabbitMessagingTemplate, RabbitMessageSendOptions> {
    /**
     * 默认的 routingKey。
     */
    private String defaultRoutingKey;

    /**
     * RoutingKeyResolver 的列表。
     */
    private List<RoutingKeyResolver> routingKeyResolvers;

    @Override
    protected RabbitMessageSendOptions createSendOptions() {
        return new RabbitMessageSendOptions();
    }

    @Override
    protected void initSendOptions(MessageSendContext context, RabbitMessageSendOptions options) {
        super.initSendOptions(context, options);

        RabbitSendContextAccessor contextAccessor = new RabbitSendContextAccessor(context);

        // 计算 routingKey
        String routingKey = this.resolveRoutingKey(context);
        options.setRoutingKey(routingKey);
        contextAccessor.setRoutingKey(routingKey);
    }

    /**
     * 计算 routingKey。
     *
     * @param context 消息发送的上下文
     * @return routingKey
     */
    protected String resolveRoutingKey(MessageSendContext context) {
        // 检查 Context 中的 routingKey
        RabbitSendContextAccessor contextAccessor = new RabbitSendContextAccessor(context);
        String routingKey = contextAccessor.getRoutingKey();
        if (StringUtil.isNotEmpty(routingKey)) {
            return routingKey;
        }

        // 解析 routingKey
        if (this.routingKeyResolvers != null) {
            for (RoutingKeyResolver resolver : this.routingKeyResolvers) {
                routingKey = resolver.resolveRoutingKey(context);
                if (routingKey != null) {
                    return routingKey;
                }
            }
        }

        // 返回默认值
        return this.defaultRoutingKey;
    }

    @Override
    protected void sendMessage(MessageSendContext context, RabbitMessageSendOptions options, MessageSendCallback callback) {
        Object destination = ObjectUtil.notNullOr(options.getDestination(), this.getMessagingTemplate()::getDefaultDestination);
        String routeKey = StringUtil.notEmptyOr(options.getRoutingKey(), this.defaultRoutingKey);
        this.getMessagingTemplate().convertAndSend(destination.toString(), routeKey, options.getPayload(), options.getHeaders(),
                options.getPostProcessor());
    }

    @Override
    public Class<RabbitMessagingTemplate> getMessagingTemplateType() {
        return RabbitMessagingTemplate.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 routingKeyResolvers
        this.initRoutingKeyResolvers();
    }

    /**
     * 初始化 routingKeyResolvers。
     */
    protected void initRoutingKeyResolvers() {
        if (this.routingKeyResolvers == null) {
            this.routingKeyResolvers = this.getBeansOrdered(RoutingKeyResolver.class);
        }
    }
}
