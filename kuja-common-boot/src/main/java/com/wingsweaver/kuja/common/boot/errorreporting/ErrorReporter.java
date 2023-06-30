package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 错误报告器的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorReporter extends DefaultOrdered {
    /**
     * 报告指定的错误。
     *
     * @param record 错误报告
     */
    void report(ErrorRecord record);
}
