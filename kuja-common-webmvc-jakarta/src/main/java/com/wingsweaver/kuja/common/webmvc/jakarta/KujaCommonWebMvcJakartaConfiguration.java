package com.wingsweaver.kuja.common.webmvc.jakarta;

import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringBootVersion3x;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.web.constants.KujaCommonWebKeys;
import com.wingsweaver.kuja.common.webmvc.common.EnableKujaCommonWebMvcCommon;
import com.wingsweaver.kuja.common.webmvc.jakarta.config.KujaWebMvcConfigurer;
import com.wingsweaver.kuja.common.webmvc.jakarta.errorhandling.GlobalErrorController;
import com.wingsweaver.kuja.common.webmvc.jakarta.filter.BusinessContextFilter;
import com.wingsweaver.kuja.common.webmvc.jakarta.filter.DynamicLogContextFilter;
import com.wingsweaver.kuja.common.webmvc.jakarta.listener.BusinessContextServletRequestListener;
import com.wingsweaver.kuja.common.webmvc.jakarta.support.ServletResponseWriter;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * kuja-common-webmvc-jakarta 的自动配置。
 *
 * @author wingsweaver
 */
@AutoConfiguration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableKujaCommonWebMvcCommon
@ConditionalOnSpringBootVersion3x
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@Import(WebMvcAutoConfiguration.class)
public class KujaCommonWebMvcJakartaConfiguration extends AbstractConfiguration {
    @Bean
    public BusinessContextServletRequestListener kujaJakartaServletRequestListener(BusinessContextFactory businessContextFactory) {
        BusinessContextServletRequestListener listener = new BusinessContextServletRequestListener();
        listener.setBusinessContextFactory(businessContextFactory);
        return listener;
    }

    @Bean
    public BusinessContextFilter kujaJakartaBusinessContextFilter(BusinessContextFactory businessContextFactory,
                                                                  ObjectProvider<BusinessContextCustomizer> businessContextCustomizers) {
        BusinessContextFilter filter = new BusinessContextFilter();
        filter.setBusinessContextFactory(businessContextFactory);
        filter.setBusinessContextCustomizers(businessContextCustomizers.orderedStream().collect(Collectors.toList()));
        return filter;
    }

    @Bean
    public DynamicLogContextFilter kujaJakartaDynamicLogContextFilter() {
        return new DynamicLogContextFilter();
    }

    @Bean
    public KujaWebMvcConfigurer kujaJakartaWebMvcConfigurer(WebServerProperties properties) {
        KujaWebMvcConfigurer configurer = new KujaWebMvcConfigurer();
        configurer.setProperties(properties);
        return configurer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonWebKeys.PREFIX_GLOBAL_ERROR_CONTROLLER_PROPERTIES, name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public GlobalErrorController kujaJakartaGlobalErrorController(ErrorAttributes errorAttributes,
                                                                  ReturnValueFactory returnValueFactory,
                                                                  ErrorHandlingDelegate errorHandlingDelegate) {
        GlobalErrorController controller = new GlobalErrorController();
        controller.setErrorAttributes(errorAttributes);
        controller.setReturnValueFactory(returnValueFactory);
        controller.setErrorHandlingDelegate(errorHandlingDelegate);
        return controller;
    }

    @Bean
    @ConditionalOnBean(RequestMappingHandlerAdapter.class)
    public ServletResponseWriter kujaJakartaServletResponseWriter(RequestMappingHandlerAdapter adapter) {
        ServletResponseWriter writer = new ServletResponseWriter();
        List<HandlerMethodReturnValueHandler> handlers = new LinkedList<>();
        CollectionUtil.addAll(handlers, adapter.getReturnValueHandlers());
        CollectionUtil.addAll(handlers, adapter.getCustomReturnValueHandlers());
        writer.setReturnValueHandlers(new ArrayList<>(handlers));
        return writer;
    }
}
