package com.wingsweaver.kuja.common.boot.env.resolver;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于在 {@link AbstractEnvironmentResolver#getPreProcessors()} 中做控制设置的 {@link EnvironmentPostProcessor} 实现类。<br>
 * 主要是设置一些参数，以抑制不必要的 Web 初始化和 Cloud Config 设置。也可以按需定义其他需要控制的参数。<br>
 * 不过注意需要在 {@link AbstractEnvironmentResolver#getPostProcessors()} 中配合 {@link PostEnvironmentResolverProcessor} 来使用，
 * 避免在最终获取到的 Environment 中残留控制设置。
 *
 * @author wingsweaver
 */
public class PreEnvironmentResolverProcessor implements EnvironmentPostProcessor, Ordered {
    private int order;

    private final Map<String, Object> map = new HashMap<>(BufferSizes.SMALL);

    private static final String PROPERTY_SOURCE_NAME = KujaCommonBootKeys.PropertySourceNames.ENVIRONMENT_RESOLVER;

    public PreEnvironmentResolverProcessor() {
        this.map.put("spring.main.web-application-type", "NONE");
        this.map.put("spring.jmx.enabled", "false");
        this.map.put("spring.main.sources", EnvironmentResolver.class.getName());
        this.map.put("spring.config.import", "");
        this.map.put("spring.cloud.config.enabled", "false");
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        // 检查是否存在同名的 PropertySource
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            // 如果存在同名的 PropertySource 的话，直接返回
            return;
        }

        // 如果不存在的话，创建一个新的 PropertySource
        MapPropertySource propertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, this.getMap());
        propertySources.addLast(propertySource);
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
