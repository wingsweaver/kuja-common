package com.wingsweaver.kuja.common.webflux.support;

import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用于写入处理结果的工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class HandlerResultWriter extends AbstractComponent {
    /**
     * HandlerResultHandler 实例的列表。
     */
    private final List<HandlerResultHandler> resultHandlers = new LinkedList<>();

    @Getter(AccessLevel.NONE)
    private final MethodParameter returnValueMethodParameter;

    public HandlerResultWriter() {
        this.returnValueMethodParameter = this.createReturnValueMethodParameter();
    }

    private MethodParameter createReturnValueMethodParameter() {
        try {
            Method method = this.getClass().getDeclaredMethod("returnValueMethod");
            return new MethodParameter(method, -1);
        } catch (Exception ex) {
            // 忽略此错误
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 resultHandlers
        this.initResultHandlers();
    }

    /**
     * 初始化 {@link #resultHandlers}。
     */
    protected void initResultHandlers() {
        if (!CollectionUtils.isEmpty(this.resultHandlers)) {
            return;
        }

        Map<String, HandlerResultHandler> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                this.getApplicationContext(), HandlerResultHandler.class, true, false);

        this.getResultHandlers().addAll(beans.values());
        AnnotationAwareOrderComparator.sort(this.getResultHandlers());
    }

    /**
     * 写入指定的返回数据 {@link HandlerResult}。
     *
     * @param exchange ServerWebExchange
     * @param result   HandlerResult
     * @return 处理结果
     */
    public Mono<Void> writeResult(ServerWebExchange exchange, HandlerResult result) {
        return this.getResultHandler(result)
                .handleResult(exchange, result)
                .checkpoint("Handler " + result.getHandler() + " [HandlerResultWriter]")
                .onErrorResume(ex ->
                        result.applyExceptionHandler(ex).flatMap(exResult -> {
                            String text = "Exception handler " + exResult.getHandler()
                                    + ", error=\"" + ex.getMessage() + "\" [HandlerResultWriter]";
                            return getResultHandler(exResult).handleResult(exchange, exResult).checkpoint(text);
                        }));
    }

    /**
     * 写入指定的返回结果 {@link ReturnValue}。
     *
     * @param exchange    ServerWebExchange
     * @param returnValue 返回结果
     * @return 处理结果
     */
    public Mono<Void> writeReturnValue(ServerWebExchange exchange, ReturnValue returnValue) {
        AssertState.notNull(this.returnValueMethodParameter, "Cannot resolve MethodParameter instance for ReturnValue");
        return this.writeResult(exchange, new HandlerResult(this, returnValue, this.returnValueMethodParameter));
    }

    @ResponseBody
    private ReturnValue returnValueMethod() {
        return null;
    }

    /**
     * 获取错误处理接口。
     *
     * @param handlerResult HandlerResult
     * @return 错误处理接口的实例
     */
    protected HandlerResultHandler getResultHandler(HandlerResult handlerResult) {
        if (this.getResultHandlers() != null) {
            for (HandlerResultHandler resultHandler : this.getResultHandlers()) {
                if (resultHandler.supports(handlerResult)) {
                    return resultHandler;
                }
            }
        }
        throw new IllegalStateException("No HandlerResultHandler for " + handlerResult.getReturnValue());
    }
}
