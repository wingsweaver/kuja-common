package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Comparator;

/**
 * 在 Spring 标准的环境设置之后，用于设置兜底环境参数的 {@link EnvironmentPostProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class FallbackEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        SpringFactoriesLoader.loadFactories(FallbackEnvironmentProcessor.class, null)
                .stream().sorted(Comparator.comparingInt(FallbackEnvironmentProcessor::getOrder).reversed())
                .forEach(processor -> processor.process(environment, application));
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.FALLBACK_ENVIRONMENT_POST_PROCESSOR;
    }
}
