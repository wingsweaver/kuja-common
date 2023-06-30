package com.wingsweaver.kuja.common.boot.env.resolver;

import com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe.ReadWriteLockedValue;
import com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe.ThreadSafeValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link EnvironmentResolver} 的工具类。
 *
 * @author wingsweaver
 */
public final class EnvironmentResolverUtil {
    private EnvironmentResolverUtil() {
        // 禁止实例化
    }

    private static final CompositeEnvironmentResolver COMPOSITE_ENVIRONMENT_RESOLVER = new CompositeEnvironmentResolver();

    static {
        List<EnvironmentResolver> environmentResolvers = new LinkedList<>(
                SpringFactoriesLoader.loadFactories(EnvironmentResolver.class, null)
        );
        if (environmentResolvers.isEmpty()) {
            environmentResolvers.add(new ConfigDataEnvironmentResolver());
        }
        COMPOSITE_ENVIRONMENT_RESOLVER.getEnvironmentResolvers().addAll(environmentResolvers);
    }

    private static final AtomicReference<EnvironmentResolver> ENVIRONMENT_RESOLVER = new AtomicReference<>(null);

    /**
     * 获取当前的环境解析器。
     *
     * @return 当前的环境解析器
     */
    public static EnvironmentResolver getEnvironmentResolver() {
        EnvironmentResolver environmentResolver = ENVIRONMENT_RESOLVER.get();
        return (environmentResolver != null) ? environmentResolver : COMPOSITE_ENVIRONMENT_RESOLVER;
    }

    /**
     * 设置当前的环境解析器。
     *
     * @param environmentResolver 当前的环境解析器
     */
    public static void setEnvironmentResolver(EnvironmentResolver environmentResolver) {
        ENVIRONMENT_RESOLVER.set(environmentResolver);
    }

    /**
     * 解析当前的环境。
     *
     * @return 当前的环境
     */
    public static ConfigurableEnvironment resolveEnvironment() {
        return getEnvironmentResolver().resolve();
    }

    private static final ThreadSafeValue<ConfigurableEnvironment> CONFIGURABLE_ENVIRONMENT = new ReadWriteLockedValue<>(null);

    /**
     * 获取当前的环境。
     *
     * @return 当前的环境
     */
    public static ConfigurableEnvironment getEnvironment() {
        return CONFIGURABLE_ENVIRONMENT.get();
    }

    /**
     * 获取当前的环境。
     *
     * @param autoResolve 如果当前的环境为空，是否自动解析环境
     * @return 当前的环境
     */
    public static ConfigurableEnvironment getEnvironment(boolean autoResolve) {
        if (autoResolve) {
            return CONFIGURABLE_ENVIRONMENT.compute(environment -> environment != null ? environment : resolveEnvironment());
        } else {
            return getEnvironment();
        }
    }

    /**
     * 设置当前的环境。
     *
     * @param environment 当前的环境
     */
    public static void setConfigurableEnvironment(ConfigurableEnvironment environment) {
        CONFIGURABLE_ENVIRONMENT.set(environment);
    }
}
