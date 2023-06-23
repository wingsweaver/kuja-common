package com.wingsweaver.kuja.common.boot.exception;

import com.wingsweaver.kuja.common.boot.errordefinition.EnableKujaErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionAttributes;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionRepository;
import com.wingsweaver.kuja.common.boot.i18n.EnableKujaI18n;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * 返回值功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaI18n
@EnableKujaErrorDefinition
public class ExceptionConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BusinessExceptionFactory businessExceptionFactory(
            ErrorDefinitionAttributes errorDefinitionAttributes,
            ErrorDefinitionRepository errorDefinitionRepository,
            ObjectProvider<BusinessExceptionCustomizer> exceptionCustomizers) {
        DefaultBusinessExceptionFactory exceptionFactory = new DefaultBusinessExceptionFactory();
        exceptionFactory.setErrorDefinitionAttributes(errorDefinitionAttributes);
        exceptionFactory.setErrorDefinitionRepository(errorDefinitionRepository);
        exceptionFactory.setExceptionCustomizers(exceptionCustomizers.orderedStream().collect(Collectors.toList()));
        return exceptionFactory;
    }
}
