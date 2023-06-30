package com.wingsweaver.kuja.common.boot.env.resolver;

import lombok.Getter;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.LinkedList;
import java.util.List;

/**
 * 由多个 {@link EnvironmentResolver} 组合而成的实现类。
 *
 * @author wingsweaver
 */
@Getter
public class CompositeEnvironmentResolver implements EnvironmentResolver {
    /**
     * {@link EnvironmentResolver} 实例的列表。
     */
    private final List<EnvironmentResolver> environmentResolvers = new LinkedList<>();

    @Override
    public ConfigurableEnvironment resolve() {
        ConfigurableEnvironment environment = null;
        for (EnvironmentResolver environmentResolver : this.getEnvironmentResolvers()) {
            environment = environmentResolver.resolve();
            if (environment != null) {
                break;
            }
        }
        return environment;
    }
}
