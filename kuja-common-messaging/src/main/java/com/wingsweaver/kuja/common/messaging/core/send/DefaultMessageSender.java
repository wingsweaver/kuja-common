package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 默认的 {@link MessageSender} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultMessageSender extends AbstractComponent implements MessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageSender.class);

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

    /**
     * 用于发送消息的 Executor。<br>
     * 如果设置的话，消息发送将会在该 Executor 中异步执行，否则将会在当前线程中同步执行。
     */
    private Executor sendExecutor;

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
        context.setId(this.contextIdGenerator.nextId());
        context.setOriginalMessage(message);

        // 使用 MessageSendContextAccessor 进行设置
        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);
        contextAccessor.setSendThread(Thread.currentThread());
        contextAccessor.setMessageSender(this);
        contextAccessor.setMessageSendService(sendService);
        contextAccessor.setMessageDestination(destination);

        // 初始化上下文
        this.initSendContext(context);

        // 执行发送
        this.doSend(context, sendService, callback);
    }

    /**
     * 执行发送处理。
     *
     * @param context     消息发送上下文
     * @param sendService 消息发送服务
     * @param callback    消息发送回调
     */
    @SuppressWarnings("PMD.GuardLogStatement")
    protected void doSend(MessageSendContext context, MessageSendService sendService, MessageSendCallback callback) {
        if (this.sendExecutor != null) {
            this.sendExecutor.execute(() -> {
                LogUtil.trace(LOGGER, "Sending message asynchronously, send service: {}, context: {}", sendService, context.getId());
                sendService.send(context, callback);
            });
        } else {
            LogUtil.trace(LOGGER, "Sending message synchronously, send service: {}, context: {}", sendService, context.getId());
            sendService.send(context, callback);
        }
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
            this.contextIdGenerator = this.getBean(StringIdGenerator.class, () -> StringIdGenerator.FALLBACK);
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
