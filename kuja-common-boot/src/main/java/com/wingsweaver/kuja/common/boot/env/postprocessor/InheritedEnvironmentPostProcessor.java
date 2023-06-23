package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.boot.env.PropertySourceUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * 用于处理继承数据的 {@link EnvironmentPostProcessor} 实现类。<br>
 * 主要用于 Cloud Config 环境中、不使用 bootstrap 的情况下，有时会出现 spring.application.name 等丢失的情况。<br>
 * 如: Nacos + Spring Cloud Config 的情况下。
 *
 * @author wingsweaver
 */
public class InheritedEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 检查是否已经加入了继承的 PropertySource，如果已经加入，则不再重复加入。
        if (PropertySourceUtil.containsProperty(environment.getPropertySources(), KujaCommonBootKeys.PropertySourceNames.INHERITED)) {
            return;
        }

        // 获取继承的 PropertySource，并且加入到 environment 中
        PropertySource<?> inheritEnvironmentPropertySource = InheritEnvironmentPropertySourceHolder.getInheritEnvironmentPropertySource();
        if (inheritEnvironmentPropertySource != null) {
            environment.getPropertySources().addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, inheritEnvironmentPropertySource);
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.INHERITED_ENVIRONMENT_POST_PROCESSOR;
    }
}
