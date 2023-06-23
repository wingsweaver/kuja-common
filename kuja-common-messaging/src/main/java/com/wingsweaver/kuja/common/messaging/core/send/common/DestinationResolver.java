package com.wingsweaver.kuja.common.messaging.core.send.common;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 计算消息目标的处理逻辑的接口定义。
 *
 * @author wingsweaver
 */
public interface DestinationResolver extends DefaultOrdered {
    /**
     * 未处理的结果。
     */
    Tuple2<Boolean, Object> UNHANDLED = Tuple2.of(false, null);

    /**
     * 解析消息目标。
     *
     * @param context 消息发送的上下文
     * @return 解析结果。第一个元素表示是否已处理，第二个元素表示解析结果。
     */
    Tuple2<Boolean, Object> resolveDestination(MessageSendContext context);
}
