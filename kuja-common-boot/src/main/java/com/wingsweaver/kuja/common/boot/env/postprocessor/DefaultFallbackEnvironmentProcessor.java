package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 默认的 {@link PresetEnvironmentProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultFallbackEnvironmentProcessor extends AbstractEnvironmentProcessor implements FallbackEnvironmentProcessor {
    /**
     * kuja.boot.env.fallback 属性的键。
     */
    public static final String KEY_KUJA_BOOT_ENV_FALLBACK = "kuja.boot.env.fallback";

    /**
     * kuja.boot.env.fallback 属性的值。
     */
    public static final String VALUE_KUJA_BOOT_ENV_FALLBACK = "classpath:/kuja-env-fallback.properties";

    @Override
    public void process(ConfigurableEnvironment environment, SpringApplication application) {
        String propertyValue = environment.getProperty(KEY_KUJA_BOOT_ENV_FALLBACK, VALUE_KUJA_BOOT_ENV_FALLBACK).trim();
        this.processInternal(environment, application, propertyValue);
    }

    @Override
    protected String getPropertySourceName() {
        return KujaCommonBootKeys.PropertySourceNames.FALLBACK;
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.DEFAULT_FALLBACK_ENVIRONMENT_PROCESSOR;
    }
}
