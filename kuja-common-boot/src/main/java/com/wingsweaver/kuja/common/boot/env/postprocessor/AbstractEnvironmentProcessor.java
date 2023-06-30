package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.env.PropertySourceLoadUtil;
import com.wingsweaver.kuja.common.boot.env.PropertySourceUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 环境对象处理器的抽象类。
 *
 * @author wingsweaver
 */
public abstract class AbstractEnvironmentProcessor {
    /**
     * 处理环境对象。
     *
     * @param environment   环境对象
     * @param application   应用对象
     * @param locationArray 资源所在位置的数组
     */
    protected void processInternal(ConfigurableEnvironment environment, SpringApplication application, String locationArray) {
        if (StringUtil.isNotEmpty(locationArray)
                && !PropertySourceUtil.containsProperty(environment.getPropertySources(), this.getPropertySourceName())) {
            this.processInternal(environment, application, StringUtils.split(locationArray, ','));
        }
    }

    /**
     * 处理环境对象。
     *
     * @param environment 环境对象
     * @param application 应用对象
     * @param locations   资源所在位置的数组
     */
    protected void processInternal(ConfigurableEnvironment environment, SpringApplication application, String[] locations) {
        List<PropertySource<?>> propertySources = PropertySourceLoadUtil.load(application.getResourceLoader(), locations);
        if (CollectionUtils.isEmpty(propertySources)) {
            return;
        }

        CompositePropertySource compositePropertySource = new CompositePropertySource(this.getPropertySourceName());
        compositePropertySource.getPropertySources().addAll(propertySources);
        environment.getPropertySources().addLast(compositePropertySource);
    }

    /**
     * 获取属性源名称。
     *
     * @return 属性源名称
     */
    protected abstract String getPropertySourceName();
}
