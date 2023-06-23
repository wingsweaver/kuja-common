package com.wingsweaver.kuja.common.boot.env.resolver;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.LinkedList;
import java.util.List;

/**
 * 由多个 {@link EnvironmentResolver} 组合而成的实现类。
 *
 * @author wingsweaver
 */
public class CompositeEnvironmentResolver implements EnvironmentResolver {
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

    public List<EnvironmentResolver> getEnvironmentResolvers() {
        return environmentResolvers;
    }
}
