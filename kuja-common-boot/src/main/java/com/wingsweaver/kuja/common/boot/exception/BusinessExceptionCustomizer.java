package com.wingsweaver.kuja.common.boot.exception;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 业务异常的初始化接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessExceptionCustomizer extends DefaultOrdered {
    /**
     * 定制业务异常。
     *
     * @param exception 业务异常
     */
    void customize(BusinessException exception);
}
