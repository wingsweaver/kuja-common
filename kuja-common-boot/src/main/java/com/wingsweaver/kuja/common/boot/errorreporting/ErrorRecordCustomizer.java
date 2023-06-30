package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 错误记录的定制/生成工具的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorRecordCustomizer extends DefaultOrdered {
    /**
     * 定制/生成错误记录。
     *
     * @param context 错误处理上下文
     * @param record  错误记录
     * @return 定制/生成后的错误记录
     */
    ErrorRecord customize(ErrorHandlerContext context, ErrorRecord record);
}
