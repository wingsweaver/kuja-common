package com.wingsweaver.kuja.common.webflux;

import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.web.EnableKujaCommonWeb;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.web.constants.KujaCommonWebKeys;
import com.wingsweaver.kuja.common.webflux.config.KujaWebFluxConfigurer;
import com.wingsweaver.kuja.common.webflux.errorhandling.GlobalErrorWebExceptionHandler;
import com.wingsweaver.kuja.common.webflux.filter.BusinessWebFilter;
import com.wingsweaver.kuja.common.webflux.filter.DynamicLogContextWebFilter;
import com.wingsweaver.kuja.common.webflux.support.HandlerResultWriter;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.stream.Collectors;

/**
 * kuja-common-webmvc 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaCommonWeb
@Import({WebFluxAutoConfiguration.class, ErrorWebFluxAutoConfiguration.class})
public class KujaCommonWebMvcConfiguration extends AbstractConfiguration {
    @Bean
    public ServerWebExchangeBusinessContextFactory serverWebExchangeBusinessContextFactory(
            BusinessContextFactory businessContextFactory,
            ObjectProvider<BusinessContextCustomizer> businessContextCustomizers) {
        ServerWebExchangeBusinessContextFactory factory = new ServerWebExchangeBusinessContextFactory();
        factory.setBusinessContextFactory(businessContextFactory);
        factory.setBusinessContextCustomizers(businessContextCustomizers.orderedStream().collect(Collectors.toList()));
        return factory;
    }

    @Bean
    public KujaWebFluxConfigurer kujaWebFluxConfigurer(WebServerProperties properties,
                                                       ServerWebExchangeBusinessContextFactory businessContextFactory) {
        KujaWebFluxConfigurer configurer = new KujaWebFluxConfigurer();
        configurer.setProperties(properties);
        configurer.setBusinessContextFactory(businessContextFactory);
        return configurer;
    }

    @Bean
    public BusinessWebFilter businessWebFilter(ServerWebExchangeBusinessContextFactory businessContextFactory) {
        BusinessWebFilter webFilter = new BusinessWebFilter();
        webFilter.setBusinessContextFactory(businessContextFactory);
        return webFilter;
    }

    @Bean
    public DynamicLogContextWebFilter dynamicLogContextWebFilter() {
        return new DynamicLogContextWebFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonWebKeys.PREFIX_GLOBAL_ERROR_CONTROLLER_PROPERTIES, name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(ApplicationContext applicationContext,
                                                                         ErrorAttributes errorAttributes,
                                                                         WebProperties webProperties,
                                                                         ObjectProvider<ViewResolver> viewResolvers,
                                                                         ServerCodecConfigurer serverCodecConfigurer,
                                                                         ServerWebExchangeBusinessContextFactory businessContextFactory,
                                                                         ReturnValueFactory returnValueFactory,
                                                                         ErrorHandlingDelegate errorHandlingDelegate) {
        GlobalErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(errorAttributes,
                webProperties.getResources(), applicationContext);
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setBusinessContextFactory(businessContextFactory);
        exceptionHandler.setReturnValueFactory(returnValueFactory);
        exceptionHandler.setErrorHandlingDelegate(errorHandlingDelegate);
        return exceptionHandler;
    }

    @Bean
    public HandlerResultWriter handlerResultWriter() {
        return new HandlerResultWriter();
    }
}
