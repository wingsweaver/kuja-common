package com.wingsweaver.kuja.common.webflux.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.BusinessContextType;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.webflux.context.WebFluxContextAccessor;
import com.wingsweaver.kuja.common.webflux.util.ServerWebExchangeUtil;
import com.wingsweaver.kuja.common.webflux.wrapper.ServerHttpRequestWrapper;
import com.wingsweaver.kuja.common.webflux.wrapper.ServerHttpResponseWrapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * 基于 {@link ServerWebExchange} 处理 {@link BusinessContext} 的辅助工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ServerWebExchangeBusinessContextFactory implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerWebExchangeBusinessContextFactory.class);

    /**
     * 业务上下文工厂类的实例。
     */
    private BusinessContextFactory businessContextFactory;

    /**
     * 业务上下文的定制处理器。
     */
    private List<BusinessContextCustomizer> businessContextCustomizers;

    /**
     * 获取或者创建指定 Web 请求关联的业务上下文。
     *
     * @param exchange Web 请求
     * @return 业务上下文
     */
    public BusinessContext getOrCreateBusinessContext(ServerWebExchange exchange) {
        BusinessContext businessContext = ServerWebExchangeUtil.getBusinessContext(exchange);
        if (businessContext == null) {
            businessContext = this.createBusinessContext(exchange);
            ServerWebExchangeUtil.setBusinessContext(exchange, businessContext);
            LogUtil.trace(LOGGER, "Create new business context {} and bind to exchange {}", businessContext, exchange);
        }
        return businessContext;
    }

    /**
     * 基于指定的 Web 请求，创建业务上下文实例。
     *
     * @param exchange Web 请求
     * @return 业务上下文
     */
    public BusinessContext createBusinessContext(ServerWebExchange exchange) {
        return this.createBusinessContext(exchange, null, null);
    }

    /**
     * 基于指定的 Web 请求，创建业务上下文实例。
     *
     * @param exchange      Web 请求
     * @param preProcessor  自定义的预处理，可以为 null
     * @param postProcessor 自定义的后处理，可以为 null
     * @return 业务上下文
     */
    public BusinessContext createBusinessContext(ServerWebExchange exchange,
                                                 BusinessContextCustomizer preProcessor,
                                                 BusinessContextCustomizer postProcessor) {
        // 创建业务上下文的实例
        BusinessContext businessContext = this.businessContextFactory.create();

        // 初始化业务上下文
        this.initializeBusinessContext(exchange, businessContext);

        // 执行自定义的初始化
        if (preProcessor != null) {
            preProcessor.customize(businessContext);
        }

        // 执行内置的初始化处理
        for (BusinessContextCustomizer customizer : this.businessContextCustomizers) {
            customizer.customize(businessContext);
        }

        // 执行自定义的后处理
        if (postProcessor != null) {
            postProcessor.customize(businessContext);
        }

        // 返回结果
        return businessContext;
    }

    /**
     * 初始化业务上下文。
     *
     * @param exchange        Web 请求
     * @param businessContext 业务上下文
     */
    protected void initializeBusinessContext(ServerWebExchange exchange, BusinessContext businessContext) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        WebFluxContextAccessor accessor = new WebFluxContextAccessor(businessContext);
        accessor.setContextType(BusinessContextType.Web.Reactive.WEB_FLUX);
        accessor.setOriginalRequest(request);
        accessor.setOriginalResponse(response);
        accessor.setRequestWrapper(new ServerHttpRequestWrapper(exchange, request));
        accessor.setResponseWrapper(new ServerHttpResponseWrapper(response));
        accessor.setServerWebExchange(exchange);
        accessor.setServerHttpRequest(request);
        accessor.setServerHttpResponse(response);

    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("businessContextFactory", this.getBusinessContextFactory());
        AssertState.Named.notNull("businessContextCustomizers", this.getBusinessContextCustomizers());
    }

}
