package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import org.springframework.messaging.core.MessagePostProcessor;

import java.util.List;

/**
 * 用于计算消息后处理器 {@link MessagePostProcessor} 的处理逻辑的接口定义。
 *
 * @author wingsweaver
 */
public interface PostProcessorResolver extends DefaultOrdered {
    /**
     * 解析消息后处理器。
     *
     * @param context        消息发送的上下文
     * @param postProcessors 消息后处理器的列表
     */
    void resolvePostProcessors(MessageSendContext context, List<MessagePostProcessor> postProcessors);
}
