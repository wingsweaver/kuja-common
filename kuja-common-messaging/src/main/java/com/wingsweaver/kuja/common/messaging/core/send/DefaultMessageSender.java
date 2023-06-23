package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.boot.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.UuidStringIdGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 默认的 {@link MessageSender} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultMessageSender extends AbstractComponent implements MessageSender {
    /**
     * 默认的 {@link StringIdGenerator} 实例。
     */
    public static final StringIdGenerator DEFAULT_ID_GENERATOR = new UuidStringIdGenerator();

    /**
     * 消息发送服务的实例列表。
     */
    private List<MessageSendService> sendServices;

    /**
     * 消息上下文的唯一标识的生成器。
     */
    private StringIdGenerator contextIdGenerator;

    /**
     * 消息发送上下文的定制器的实例列表。
     */
    private List<MessageSendContextCustomizer> sendContextCustomizers;

    @Override
    public void send(Object message, MessageSendCallback callback) {
        this.send(null, message, callback);
    }

    @Override
    public void send(Object message) {
        this.send(null, message, null);
    }

    @Override
    public void send(Object destination, Object message, MessageSendCallback callback) {
        // 查找可用的 MessageSendService 实例
        MessageSendService sendService = this.findSendService(destination, message);
        if (sendService == null) {
            throw new ExtendedRuntimeException("No message sending strategy found for message: " + message)
                    .withExtendedAttribute("destination", destination)
                    .withExtendedAttribute("original-message", message);
        }

        // 创建消息发送的上下文
        MessageSendContext context = new MessageSendContext();
        context.setContextId(this.contextIdGenerator.nextId());
        context.setCreationTimeUtc(new Date());
        context.setOriginalMessage(message);

        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);
        contextAccessor.setSendThread(Thread.currentThread());
        contextAccessor.setMessageSender(this);
        contextAccessor.setMessageSendService(sendService);
        contextAccessor.setMessageDestination(destination);

        // 初始化上下文
        this.initSendContext(context);

        // 执行发送
        sendService.send(context, callback);
    }

    @Override
    public void send(Object destination, Object message) {
        this.send(destination, message, null);
    }

    /**
     * 初始化消息发送上下文。
     *
     * @param context 消息发送上下文
     */
    protected void initSendContext(MessageSendContext context) {
        this.sendContextCustomizers.forEach(customizer -> customizer.customize(context));
    }

    /**
     * 查找可以发送指定消息的 {@link MessageSendService} 实例。
     *
     * @param destination 消息的目的地
     * @param message     消息内容
     * @return 可用的 MessageSendService 实例
     */
    protected MessageSendService findSendService(Object destination, Object message) {
        for (MessageSendService sendService : this.sendServices) {
            if (sendService.supportDestination(destination) && sendService.supportMessage(message)) {
                return sendService;
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 sendServices
        this.initSendServices();

        // 初始化 contextIdGenerator
        this.initContextIdGenerator();

        // 初始化 sendContextCustomizers
        this.initSendContextCustomizers();
    }

    /**
     * 初始化 sendContextCustomizers。
     */
    protected void initSendContextCustomizers() {
        if (this.sendContextCustomizers == null) {
            this.sendContextCustomizers = this.getBeansOrdered(MessageSendContextCustomizer.class);
        }
    }

    /**
     * 初始化 contextIdGenerator。
     */
    protected void initContextIdGenerator() {
        if (this.contextIdGenerator == null) {
            this.contextIdGenerator = DEFAULT_ID_GENERATOR;
        }
    }

    /**
     * 初始化 sendServices。
     */
    protected void initSendServices() {
        if (this.sendServices == null) {
            this.sendServices = this.getBeansOrdered(MessageSendService.class);
        }
    }
}
