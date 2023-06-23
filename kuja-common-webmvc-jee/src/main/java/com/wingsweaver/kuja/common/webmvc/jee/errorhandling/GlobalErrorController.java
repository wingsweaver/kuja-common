package com.wingsweaver.kuja.common.webmvc.jee.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.web.errorhandling.AbstractResponseEntityReturnValueErrorHandlingComponent;
import com.wingsweaver.kuja.common.web.exception.UnknownWebException;
import com.wingsweaver.kuja.common.webmvc.jee.util.ServletRequestUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 最终兜底的、全局错误处理控制器。<br>
 * 主要捕获 404 Not Found 这类 {@link com.wingsweaver.kuja.common.web.errorhandling.GlobalErrorAdvice} 无法捕获的错误。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractResponseEntityReturnValueErrorHandlingComponent implements ErrorController, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);

    private static final ErrorAttributeOptions DEFAULT_ERROR_ATTRIBUTE_OPTIONS =
            ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);

    /**
     * {@link ErrorAttributes} 实例。
     */
    private ErrorAttributes errorAttributes;

    /**
     * {@link ErrorProperties} 实例。
     */
    private ErrorProperties errorProperties;

    /**
     * {@link ErrorHandlingDelegate} 实例。
     */
    private ErrorHandlingDelegate errorHandlingDelegate;

    /**
     * {@link ReturnValueFactory} 实例。
     */
    private ReturnValueFactory returnValueFactory;

    /**
     * 渲染错误结果。
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @return 错误结果
     */
    @SuppressFBWarnings("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING")
    @RequestMapping
    public Object renderError(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.trace(LOGGER, () -> "GlobalErrorController.renderError, request = " + ServletRequestUtil.getErrorRequestUri(request));
        BusinessContext businessContext = this.resolveBusinessContext(request);
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            Throwable error = this.resolveError(request, response);
            try {
                return this.handleError(businessContext, error);
            } catch (Throwable ex) {
                LogUtil.trace(LOGGER, () -> "GlobalErrorController.fallbackRender, request = " + ServletRequestUtil.getErrorRequestUri(request));
                return this.fallbackRender(businessContext, error);
            }
        }
    }

    /**
     * 默认的错误处理。
     *
     * @param businessContext 业务上下文
     * @param error           错误
     * @return 处理结果
     */
    protected Object fallbackRender(BusinessContext businessContext, Throwable error) {
        ReturnValue returnValue = this.returnValueFactory.fail(error);
        return ResponseEntity.ok(returnValue);
    }

    /**
     * 解析请求中发生的错误。
     *
     * @param request HTTP 请求
     * @return 错误
     */
    protected Throwable resolveError(HttpServletRequest request, HttpServletResponse response) {
        WebRequest webRequest = new ServletWebRequest(request);
        Throwable error = this.errorAttributes.getError(webRequest);

        // 如果没有找到 HTTP 请求中关联的错误，那么尝试从错误状态码中创建一个 HttpStatusException
        if (error == null) {
            Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

            // 如果找到了错误状态码，那么创建一个 HttpStatusException
            if (statusCode != null) {
                error = new ResponseStatusException(HttpStatus.valueOf(statusCode), message);
            } else if (StringUtil.isNotEmpty(message)) {
                // 如果有了错误消息，那么创建一个错误
                error = new UnknownWebException(message);
            }
        }

        // 返回错误
        return (error != null) ? error : new UnknownWebException();
    }

    /**
     * 解析请求中的业务上下文。
     *
     * @param request HTTP 请求
     * @return 业务上下文
     */
    protected BusinessContext resolveBusinessContext(HttpServletRequest request) {
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(request);
        return (businessContext != null) ? businessContext : BusinessContextHolder.getCurrent();
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("errorProperties", this.getErrorProperties());
        AssertState.Named.notNull("errorAttributes", this.getErrorAttributes());
        AssertState.Named.notNull("errorHandlingDelegate", this.getErrorHandlingDelegate());
        AssertState.Named.notNull("returnValueFactory", this.getReturnValueFactory());
    }

    @Override
    @Deprecated
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }
}
