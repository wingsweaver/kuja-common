package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.returnvalue.EnableKujaReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * 异常处理功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaReturnValue
public class ErrorHandlingConfiguration extends AbstractConfiguration {
    @Bean
    public PreDefinedBusinessExceptionHandler preDefinedBusinessExceptionHandler(
            ReturnValueFactory returnValueFactory,
            ObjectProvider<ErrorDefinition2ReturnValuePatcher> patchers) {
        PreDefinedBusinessExceptionHandler handler = new PreDefinedBusinessExceptionHandler();
        handler.setReturnValueFactory(returnValueFactory);
        handler.setPatchers(patchers.orderedStream().collect(Collectors.toList()));
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorHandlingDelegate errorHandlingDelegate(ObjectProvider<ErrorHandlerContextCustomizer> contextCustomizers,
                                                       ObjectProvider<ErrorHandler> errorHandlers) {
        DefaultErrorHandlingDelegate handlerManager = new DefaultErrorHandlingDelegate();
        handlerManager.setContextCustomizers(contextCustomizers.orderedStream().collect(Collectors.toList()));
        handlerManager.setErrorHandlers(errorHandlers.orderedStream().collect(Collectors.toList()));
        return handlerManager;
    }
}
