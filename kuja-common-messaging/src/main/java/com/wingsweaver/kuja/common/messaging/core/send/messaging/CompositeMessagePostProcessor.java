package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;

import java.util.LinkedList;
import java.util.List;

/**
 * 由多个 {@link MessagePostProcessor} 组合而成的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompositeMessagePostProcessor implements MessagePostProcessor {
    /**
     * {@link MessagePostProcessor} 实例的列表。
     */
    private List<MessagePostProcessor> postProcessors;

    @SuppressWarnings("NullableProblems")
    @Override
    public Message<?> postProcessMessage(Message<?> message) {
        if (this.postProcessors != null) {
            for (MessagePostProcessor postProcessor : this.postProcessors) {
                message = postProcessor.postProcessMessage(message);
            }
        }
        return message;
    }

    /**
     * 获取 {@link MessagePostProcessor} 实例的列表。
     *
     * @param createIfAbsent 如果为 {@code true}，则在列表不存在的情况下创建一个新的列表
     * @return {@link MessagePostProcessor} 实例的列表
     */
    public List<MessagePostProcessor> getPostProcessors(boolean createIfAbsent) {
        if (this.postProcessors == null && createIfAbsent) {
            this.postProcessors = new LinkedList<>();
        }
        return this.postProcessors;
    }
}
