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
    /**
     * 生成 ErrorDefinitionRepository 的 Bean。
     *
     * @param errorDefinitionRegisters 错误定义注册器的集合
     * @return ErrorDefinitionRepository 的 Bean
     */
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

    /**
     * 生成 BeanErrorDefinitionRegister 的 Bean。
     *
     * @return BeanErrorDefinitionRegister 的 Bean
     */
    @Bean
    public BeanErrorDefinitionRegister beanErrorDefinitionRegister() {
        return new BeanErrorDefinitionRegister();
    }

    /**
     * 生成 AnnotationBeanErrorDefinitionRegister 的 Bean。
     *
     * @return AnnotationBeanErrorDefinitionRegister 的 Bean
     */
    @Bean
    public AnnotationBeanErrorDefinitionRegister annotationBeanErrorDefinitionRegister() {
        return new AnnotationBeanErrorDefinitionRegister();
    }

    /**
     * 生成 PropertiesErrorDefinitionRegister 的 Bean。
     *
     * @param properties 错误定义的属性
     * @return PropertiesErrorDefinitionRegister 的 Bean
     */
    @Bean
    public PropertiesErrorDefinitionRegister propertiesErrorDefinitionRegister(ErrorDefinitionProperties properties) {
        PropertiesErrorDefinitionRegister register = new PropertiesErrorDefinitionRegister();
        register.setProperties(properties);
        return register;
    }

    /**
     * 生成 ErrorDefinitionAttributes 的 Bean。
     *
     * @param messageHelper 消息辅助器
     * @param properties    错误定义的属性
     * @return ErrorDefinitionAttributes 的 Bean
     */
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
