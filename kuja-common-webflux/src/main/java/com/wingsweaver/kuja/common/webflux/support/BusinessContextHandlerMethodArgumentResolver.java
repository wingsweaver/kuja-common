package com.wingsweaver.kuja.common.webflux.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.webflux.util.ServerWebExchangeUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 业务上下文参数解析器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessContextHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver, InitializingBean {
    /**
     * 业务上下文工厂类的实例。
     */
    private ServerWebExchangeBusinessContextFactory businessContextFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return BusinessContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
                                        ServerWebExchange exchange) {
        BusinessContext businessContext = ServerWebExchangeUtil.getBusinessContext(exchange);
        if (businessContext != null) {
            // 如果找到的话，使用找到的业务上下文
            return Mono.just(businessContext);
        } else {
            // 否则使用 Context 中关联的业务上下文
            return Mono.deferContextual(contextView -> Mono.just(contextView.get(BusinessContext.class))
                    .switchIfEmpty(Mono.fromCallable(() -> this.businessContextFactory.getOrCreateBusinessContext(exchange))));
        }
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("businessContextFactory", this.getBusinessContextFactory());
    }
}
