package com.wingsweaver.kuja.common.messaging.core.send.kafka;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendCallback;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContextAccessor;
import com.wingsweaver.kuja.common.messaging.core.send.common.AbstractMessageSendService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 基于 spring-kafka 的 {@link KafkaTemplate} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings("DuplicatedCode")
public class KafkaTemplateSendService extends AbstractMessageSendService<KafkaMessageSendOptions> {
    /**
     * KafkaTemplate 实例。
     */
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean isAsyncMode() {
        return true;
    }

    @Override
    protected void sendMessage(MessageSendContext context, KafkaMessageSendOptions options, MessageSendCallback callback) {
        // 启动发送 Future
        ListenableFuture<SendResult<String, Object>> future;
        String topic = options.getTopic();
        if (topic != null) {
            future = this.kafkaTemplate.send(topic, options.getPayload());
        } else {
            future = this.kafkaTemplate.sendDefault(options.getPayload());
        }

        // 添加发送完成回调
        future.addCallback(result -> this.onSuccess(context, callback),
                error -> this.onError(context, error, callback));
    }

    @Override
    protected KafkaMessageSendOptions createSendOptions() {
        return new KafkaMessageSendOptions();
    }

    @Override
    protected void initSendOptions(MessageSendContext context, KafkaMessageSendOptions options) {
        MessageSendContextAccessor contextAccessor = new MessageSendContextAccessor(context);
        contextAccessor.setSendOptions(options);

        // 计算 Topic
        String topic = this.resolveTopic(context);
        options.setTopic(topic);
        contextAccessor.setMessageDestination(topic);

        // 计算 Payload
        Object payload = this.resolvePayload(context);
        options.setPayload(payload);
        contextAccessor.setPayload(payload);
    }

    /**
     * 计算消息的 Topic。
     *
     * @param context 消息发送上下文
     * @return 消息的 Topic
     */
    protected String resolveTopic(MessageSendContext context) {
        Object destination = this.resolveDestination(context);
        if (destination instanceof CharSequence) {
            return destination.toString();
        } else {
            return this.kafkaTemplate.getDefaultTopic();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 KafkaTemplate
        this.initKafkaTemplate();
    }

    /**
     * 初始化 KafkaTemplate。
     */
    @SuppressWarnings("unchecked")
    protected void initKafkaTemplate() {
        if (this.kafkaTemplate == null) {
            this.kafkaTemplate = this.getApplicationContext().getBean(KafkaTemplate.class);
        }
    }
}
