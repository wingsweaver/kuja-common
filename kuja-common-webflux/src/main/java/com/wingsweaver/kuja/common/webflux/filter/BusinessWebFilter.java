package com.wingsweaver.kuja.common.webflux.filter;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * 设置业务上下文的 {@link WebFilter} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessWebFilter implements WebFilter, InitializingBean, DefaultOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessWebFilter.class);

    /**
     * 业务上下文工厂类的实例。
     */
    private ServerWebExchangeBusinessContextFactory businessContextFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 获取或者创建业务上下文
        BusinessContext businessContext = this.businessContextFactory.getOrCreateBusinessContext(exchange);
        ValueWrapper<Thread> threadTuple = new ValueWrapper<>();

        // 返回带有业务上下文的 Mono 处理
        return chain.filter(exchange)
                .contextWrite(context -> this.onContextWrite(context, businessContext))
                .doFirst(() -> this.onFirst(businessContext, threadTuple))
                .doFinally(signal -> this.onFinally(businessContext, threadTuple));
    }

    private void onFinally(BusinessContext businessContext, ValueWrapper<Thread> threadTuple) {
        if (threadTuple.getValue() == Thread.currentThread()) {
            LogUtil.trace(LOGGER, "BusinessWebFilter.onFinally: remove business context {}", businessContext);
            BusinessContextHolder.removeCurrent();
        } else {
            LogUtil.trace(LOGGER, "BusinessWebFilter.onFinally: remain business context, as thread is not the same");
        }
    }

    private void onFirst(BusinessContext businessContext, ValueWrapper<Thread> threadTuple) {
        LogUtil.trace(LOGGER, "BusinessWebFilter.onFirst: set business context {}", businessContext);
        BusinessContextHolder.setCurrent(businessContext);
        threadTuple.setValue(Thread.currentThread());
    }

    private Context onContextWrite(Context context, BusinessContext businessContext) {
        LogUtil.trace(LOGGER, "BusinessWebFilter.onContextWrite: set business context {}", businessContext);
        return context.put(BusinessContext.class, businessContext);
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("businessContextFactory", this.getBusinessContextFactory());
    }

    @Override
    public int getOrder() {
        return KujaCommonWebFluxOrders.BUSINESS_CONTEXT_WEB_FILTER;
    }
}
