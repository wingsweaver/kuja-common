package com.wingsweaver.kuja.common.webflux.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.errorhandling.AbstractErrorHandlingComponent;
import com.wingsweaver.kuja.common.boot.errorhandling.AbstractReturnValueErrorHandlingComponent;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import com.wingsweaver.kuja.common.utils.support.spring.BeanUtil;
import com.wingsweaver.kuja.common.web.errorhandling.ResponseData;
import com.wingsweaver.kuja.common.web.errorhandling.WebErrorHandlerContextAccessor;
import com.wingsweaver.kuja.common.web.exception.UnknownWebException;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 自定义的全局错误处理器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler
        implements ErrorWebExceptionHandler, ApplicationContextAware, Ordered {
    /**
     * ApplicationContext 实例。
     */
    private ApplicationContext applicationContext;

    /**
     * ErrorAttributes 实例。
     */
    private final ErrorAttributes errorAttributes;

    /**
     * ServerWebExchangeBusinessContextFactory 实例。
     */
    private ServerWebExchangeBusinessContextFactory businessContextFactory;

    /**
     * ReturnValueFactory 实例。
     */
    private ReturnValueFactory returnValueFactory;

    /**
     * ErrorHandlingDelegate 实例。
     */
    private ErrorHandlingDelegate errorHandlingDelegate;

    /**
     * 异常处理组件。
     */
    private AbstractErrorHandlingComponent errorHandlingComponent;

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext) {
        super(errorAttributes, resources, applicationContext);
        this.errorAttributes = errorAttributes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 businessContextFactory
        this.initBusinessContextFactory();

        // 初始化 returnValueFactory
        this.initReturnValueFactory();

        // 初始化 errorHandlingDelegate
        this.initErrorHandlingDelegate();

        // 初始化 errorHandlingComponent
        this.initErrorHandlingComponent();
    }

    /**
     * 初始化 errorHandlingComponent。
     */
    protected void initErrorHandlingComponent() {
        if (this.errorHandlingComponent == null) {
            InnerErrorHandlingComponent errorHandlingComponent = new InnerErrorHandlingComponent();
            errorHandlingComponent.setReturnValueFactory(this.returnValueFactory);
            errorHandlingComponent.setErrorHandlingDelegate(this.errorHandlingDelegate);
            this.errorHandlingComponent = errorHandlingComponent;
        }
    }

    /**
     * 初始化 errorHandlingDelegate。
     */
    protected void initErrorHandlingDelegate() {
        if (this.errorHandlingDelegate == null) {
            this.errorHandlingDelegate = BeanUtil.getBean(this.applicationContext, ErrorHandlingDelegate.class);
        }
    }

    /**
     * 初始化 returnValueFactory。
     */
    protected void initReturnValueFactory() {
        if (this.returnValueFactory == null) {
            this.returnValueFactory = BeanUtil.getBean(this.applicationContext, ReturnValueFactory.class);
        }
    }

    /**
     * 初始化 businessContextFactory。
     */
    protected void initBusinessContextFactory() {
        if (this.businessContextFactory == null) {
            this.businessContextFactory = BeanUtil.getBean(this.applicationContext, ServerWebExchangeBusinessContextFactory.class);
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonWebFluxOrders.GLOBAL_ERROR_WEB_EXCEPTION_HANDLER;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return route(all(), this::renderErrorResponse);
    }

    /**
     * 渲染错误响应。
     *
     * @param serverRequest 请求
     * @return 响应
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        ServerWebExchange exchange = serverRequest.exchange();
        BusinessContext businessContext = this.businessContextFactory.getOrCreateBusinessContext(exchange);

        // 获取或者构造异常
        HttpStatus status = this.resolveHttpStatus(serverRequest);
        Throwable error = this.errorAttributes.getError(serverRequest);
        if (error == null) {
            // 正常情况下，不会发生 error 为 null 的情况
            // 以下逻辑纯属防御性编程
            if (status != null) {
                error = new ResponseStatusException(status);
            } else {
                error = new UnknownWebException();
            }
        }

        // 处理异常
        Object returnValue;
        ValueWrapper<ErrorHandlerContext> errorHandlerContextWrapper = new ValueWrapper<>();
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            try {
                returnValue = this.errorHandlingComponent.handleError(businessContext, error, errorHandlerContextWrapper);
            } catch (Throwable ex) {
                returnValue = this.returnValueFactory.fail(error);
            }
        }
        WebErrorHandlerContextAccessor errorContextAccessor = new WebErrorHandlerContextAccessor(errorHandlerContextWrapper.getValue());
        ResponseData responseData = errorContextAccessor.getResponseData();

        // 检查 ResponseData，如果为空，则直接返回
        if (responseData == null || responseData.isEmpty()) {
            return ServerResponse.ok().bodyValue(returnValue);
        }

        // 否则应用 ResponseData 的内容
        HttpStatus httpStatus = responseData.getStatus();
        ServerResponse.BodyBuilder builder = (httpStatus != null) ? ServerResponse.status(httpStatus) : ServerResponse.ok();
        if (!CollectionUtils.isEmpty(responseData.getHeaders())) {
            builder = builder.headers(httpHeaders -> httpHeaders.putAll(responseData.getHeaders()));
        }
        return builder.bodyValue(returnValue);
    }

    private HttpStatus resolveHttpStatus(ServerRequest serverRequest) {
        Map<String, Object> map = super.getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        Integer status = (Integer) map.get("status");
        return (status != null) ? HttpStatus.valueOf(status) : null;
    }

    /**
     * 内置的异常处理组件。
     */
    private static class InnerErrorHandlingComponent extends AbstractReturnValueErrorHandlingComponent {
        // 暂无自定义处理
    }
}
