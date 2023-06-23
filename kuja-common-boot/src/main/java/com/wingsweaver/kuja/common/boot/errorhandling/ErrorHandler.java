package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 错误处理器的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorHandler extends DefaultOrdered {
    /**
     * 处理错误。
     *
     * @param context 错误处理上下文
     * @return 是否处理成功，无需继续处理（以便快速完成断路处理）
     */
    boolean handle(ErrorHandlerContext context);
}
