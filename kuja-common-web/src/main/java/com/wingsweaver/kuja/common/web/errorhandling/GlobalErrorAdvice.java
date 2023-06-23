package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局的异常处理器。<br>
 * 基于 Controller 的异常处理的第 2 条防线，用于捕获 Controller 未能处理的异常。<br>
 * 如: 405 Method Not Allowed、请求参数转换失败等问题。<br>
 * 注意，默认情况下 404 Not Found 异常不会被此处捕获，只能通过全局的 Error 处理。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ControllerAdvice
public class GlobalErrorAdvice extends AbstractResponseEntityReturnValueErrorHandlingComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorAdvice.class);

    /**
     * 处理全局异常。
     *
     * @param businessContext 业务上下文
     * @param error           异常
     * @return 异常处理结果
     * @throws Throwable 处理异常时发生的异常
     */
    @ExceptionHandler(Throwable.class)
    public Object onError(BusinessContext businessContext, Throwable error) throws Throwable {
        LogUtil.trace(LOGGER, () -> "GlobalErrorAdvice.onError, businessContext = " + businessContext
                + ", request = " + new WebContextAccessor(businessContext).getRequestFullPath());
        return this.handleError(businessContext, error);
    }
}
