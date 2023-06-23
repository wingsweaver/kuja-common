package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 错误定义功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ErrorDefinitionProperties.class)
public class ErrorDefinitionConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ErrorDefinitionRepository errorDefinitionRepository(ObjectProvider<ErrorDefinitionRegister> errorDefinitionRegisters) {
        DefaultErrorDefinitionRepository repository = new DefaultErrorDefinitionRepository();
        // 按照优先度倒序排列，这样优先级高的 register 后执行，可以覆盖优先级低的 register 的结果
        List<ErrorDefinitionRegister> registers = errorDefinitionRegisters.orderedStream().collect(Collectors.toList());
        Collections.reverse(registers);
        repository.setErrorDefinitionRegisters(registers);
        return repository;
    }

    @Bean
    public BeanErrorDefinitionRegister beanErrorDefinitionRegister() {
        return new BeanErrorDefinitionRegister();
    }

    @Bean
    public AnnotationBeanErrorDefinitionRegister annotationBeanErrorDefinitionRegister() {
        return new AnnotationBeanErrorDefinitionRegister();
    }

    @Bean
    public PropertiesErrorDefinitionRegister propertiesErrorDefinitionRegister(ErrorDefinitionProperties properties) {
        PropertiesErrorDefinitionRegister register = new PropertiesErrorDefinitionRegister();
        register.setProperties(properties);
        return register;
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDefinitionAttributes errorDefinitionAttributes(MessageHelper messageHelper,
                                                               ErrorDefinitionProperties properties) {
        DefaultErrorDefinitionAttributes attributes = new DefaultErrorDefinitionAttributes();
        attributes.setMessageHelper(messageHelper);
        attributes.setProperties(properties);
        return attributes;
    }
}
