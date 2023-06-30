package com.wingsweaver.kuja.common.messaging.broadcast.send;

import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoMatcherSpecUtil;
import com.wingsweaver.kuja.common.messaging.broadcast.common.BroadcastPayload;
import com.wingsweaver.kuja.common.messaging.common.AbstractSenderInfoOwner;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.common.PayloadResolver;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.Tags;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.json.JsonCodecUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于将 {@link BroadcastMessage} 转换成 {@link BroadcastPayload} 的 {@link PayloadResolver} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BroadcastMessagePayloadResolver extends AbstractSenderInfoOwner implements PayloadResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastMessagePayloadResolver.class);

    /**
     * 消息 ID 的生成器。
     */
    private StringIdGenerator idGenerator;

    @Override
    public Tuple2<Boolean, Object> resolvePayload(MessageSendContext context) {
        BroadcastMessage message = ObjectUtil.cast(context.getOriginalMessage(), BroadcastMessage.class);
        if (message == null) {
            return UNHANDLED;
        }

        BroadcastPayload payload = this.resolvePayload(context, message);
        return Tuple2.of(true, payload);
    }

    /**
     * 生成需要发送的消息内容 ({@link BroadcastPayload})。
     *
     * @param context 消息发送上下文
     * @param message 广播消息
     * @return 消息内容
     */
    protected BroadcastPayload resolvePayload(MessageSendContext context, BroadcastMessage message) {
        // 补全数据
        this.normalizeMessage(context, message);

        // 生成 Payload
        BroadcastPayload payload = new BroadcastPayload();
        payload.setId(message.getId());
        payload.setCreationTime(message.getCreationTime());
        payload.setMessageType(message.getMessageType());

        // 生成消息发送的内容
        if (message instanceof BroadcastMessageT) {
            Object content = ((BroadcastMessageT<?>) message).getContent();
            if (content != null) {
                payload.setContentClassName(content.getClass().getName());
                payload.setContent(JsonCodecUtil.ensureJsonCodec().toJsonString(content));
            }
        }

        // 生成发送者的信息
        Map<String, String> sender = this.resolveSender(context);
        payload.setSender(sender);

        // 生成接收者的信息
        String target = this.resolveTarget(message);
        payload.setTarget(target);

        // 生成附加信息
        Map<String, String> tags = this.resolveTags(message);
        payload.setTags(tags);

        // 返回
        return payload;
    }

    /**
     * 生成发送者的信息。
     *
     * @param context 消息发送上下文
     * @return 发送者的信息
     */
    protected Map<String, String> resolveSender(MessageSendContext context) {
        return this.ensureSenderInfo(false);
    }

    /**
     * 生成消息的附加信息。
     *
     * @param message 广播消息
     * @return 附加信息
     */
    @SuppressWarnings({"unchecked", "PMD.ReturnEmptyCollectionRatherThanNull"})
    protected Map<String, String> resolveTags(BroadcastMessage message) {
        Tags tags = message.getTags();
        if (tags == null) {
            return null;
        }

        int size = tags.size();
        if (size < 1) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new HashMap<>(size + 1, MapUtil.FULL_LOAD_FACTOR);
        tags.forEach((key, value) -> {
            String realKey = key.getKey();
            String realValue = ((TagKey<Object>) key).saveAsText(value);
            map.put(realKey, realValue);
        });
        return map;
    }

    /**
     * 生成消息的接收者信息。
     *
     * @param message 广播消息
     * @return 接收者信息
     */
    protected String resolveTarget(BroadcastMessage message) {
        return AppInfoMatcherSpecUtil.toJsonString(message.getTarget());
    }

    /**
     * 补全消息内容。
     *
     * @param context 消息发送上下文
     * @param message 广播消息
     */
    protected void normalizeMessage(MessageSendContext context, BroadcastMessage message) {
        // 补全 timestamp
        if (message.getCreationTime() <= 0) {
            message.setCreationTime(context.getCreationTime());
        }

        // 补全 messageId
        if (StringUtil.isEmpty(message.getId())) {
            message.setId(context.getId());
        }
        if (StringUtil.isEmpty(message.getMessageType())) {
            message.setMessageType(this.idGenerator.nextId());
        }

        // 补全 messageType
        if (StringUtil.isEmpty(message.getMessageType())) {
            message.setMessageType(message.getClass().getTypeName());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 idGenerator
        this.initIdGenerator();
    }

    /**
     * 初始化 idGenerator。
     */
    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.getBean(StringIdGenerator.class);
        }
    }
}
