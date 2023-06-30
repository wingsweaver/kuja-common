package com.wingsweaver.kuja.common.messaging.core.send.jms;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.AbstractMessagingTemplateSendService;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Destination;

/**
 * 基于 spring-jms 的 {@link JmsMessagingTemplate} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings("PMD.GuardLogStatement")
public class JmsJeeMessagingTemplateSendService extends
        AbstractMessagingTemplateSendService<Destination, JmsMessagingTemplate, JmsMessageSendOptions> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsJeeMessagingTemplateSendService.class);

    /**
     * 是否从 bean 中解析消息目的地。
     */
    private boolean resolveDestinationFromBean = false;

    @Override
    protected Object resolveDestination(MessageSendContext context) {
        Object destination = super.resolveDestination(context);

        // 如果是字符串的话，尝试从 Bean 中获取
        if (destination instanceof CharSequence && this.resolveDestinationFromBean) {
            try {
                destination = this.getBean(destination.toString(), Destination.class, true);
            } catch (Exception ignored) {
                // 忽略此错误
            }
        }

        // 返回
        return destination;
    }

    @Override
    protected JmsMessageSendOptions createSendOptions() {
        return new JmsMessageSendOptions();
    }

    @Override
    protected void sendMessage(MessageSendContext context, JmsMessageSendOptions options, MessageSendCallback callback) {
        Object destination = ObjectUtil.notNullOr(options.getDestination(), this.getMessagingTemplate()::getDefaultDestination);
        if (destination instanceof Destination) {
            LogUtil.trace(LOGGER, "Send JMS message to destination: {}, context id = {}", destination, context.getId());
            this.getMessagingTemplate().convertAndSend((Destination) destination, options.getPayload(), options.getHeaders(),
                    options.getPostProcessor());
        } else if (destination instanceof String) {
            LogUtil.trace(LOGGER, "Send JMS message to destination name: {}, context id = {}", destination, context.getId());
            this.getMessagingTemplate().convertAndSend((String) destination, options.getPayload(), options.getHeaders(),
                    options.getPostProcessor());
        } else {
            throw new ExtendedRuntimeException("Unsupported destination type: " + destination)
                    .withExtendedAttribute("destination", destination);
        }
    }

    @Override
    public Class<JmsMessagingTemplate> getMessagingTemplateType() {
        return JmsMessagingTemplate.class;
    }
}
