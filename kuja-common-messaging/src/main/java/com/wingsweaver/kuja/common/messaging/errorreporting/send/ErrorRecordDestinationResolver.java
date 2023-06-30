package com.wingsweaver.kuja.common.messaging.errorreporting.send;

import com.wingsweaver.kuja.common.boot.errorreporting.ErrorRecord;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.common.DestinationResolver;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 用于设置错误记录目的地的 {@link DestinationResolver} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ErrorRecordDestinationResolver implements DestinationResolver {
    /**
     * 默认的消息目的地。
     */
    private String defaultDestination;

    @Override
    public Tuple2<Boolean, Object> resolveDestination(MessageSendContext context) {
        // 检查是否有默认的目的地。
        if (StringUtil.isEmpty(this.defaultDestination)) {
            return UNHANDLED;
        }

        // 获取错误记录
        ErrorRecord errorRecord = ObjectUtil.cast(context.getOriginalMessage(), ErrorRecord.class);
        if (errorRecord == null) {
            return UNHANDLED;
        }

        // 返回默认目的地
        return Tuple2.of(true, this.defaultDestination);
    }
}
