package com.wingsweaver.kuja.common.webflux.filter;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.web.utils.LogContextConfigBuilder;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 设置日志上下文的 {@link WebFilter} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DynamicLogContextWebFilter extends AbstractComponent implements WebFilter, DefaultOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicLogContextWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<Void> mono = chain.filter(exchange);
        ValueWrapper<Thread> threadTuple = new ValueWrapper<>();

        // 计算日志上下文配置
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        LogContext.Config config = LogContextConfigBuilder.build(httpHeaders::getFirst);
        if (config != null) {
            mono = mono.contextWrite(context -> context.put(LogContext.Config.class, config));
        }

        // 返回
        return mono.doFirst(() -> this.onFirst(config, threadTuple))
                .doFinally(signal -> this.onFinally(config, threadTuple));
    }

    private void onFirst(LogContext.Config config, ValueWrapper<Thread> threadTuple) {
        LogContext.setConfig(config);
        threadTuple.setValue(Thread.currentThread());
    }

    private void onFinally(LogContext.Config config, ValueWrapper<Thread> threadTuple) {
        if (threadTuple.getValue() == Thread.currentThread()) {
            LogContext.removeConfig();
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonWebFluxOrders.LOG_CONTEXT_WEB_FILTER;
    }
}
