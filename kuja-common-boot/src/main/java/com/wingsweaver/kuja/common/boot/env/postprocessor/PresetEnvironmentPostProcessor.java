package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Comparator;

/**
 * 在 Spring 标准的环境设置之前，用于设置预设环境参数的 {@link EnvironmentPostProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class PresetEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        SpringFactoriesLoader.loadFactories(PresetEnvironmentProcessor.class, null)
                .stream().sorted(Comparator.comparingInt(PresetEnvironmentProcessor::getOrder).reversed())
                .forEach(processor -> processor.process(environment, application));
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.PRESET_ENVIRONMENT_POST_PROCESSOR;
    }
}
