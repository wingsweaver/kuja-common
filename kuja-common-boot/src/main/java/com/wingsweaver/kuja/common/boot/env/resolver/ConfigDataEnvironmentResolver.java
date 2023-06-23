package com.wingsweaver.kuja.common.boot.env.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.boot.BootstrapContext;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.util.Instantiator;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.function.Supplier;

/**
 * 类似 SpringCloud 的 ConfigDataRefresher 的获取配置信息的 {@link AbstractEnvironmentResolver} 实现类。<br>
 * 其特点是，不运行 SpringApplication 实例，而只是使用 PostEnvironmentProcessor 进行环境的初始化。
 *
 * @author wingsweaver
 */
public class ConfigDataEnvironmentResolver extends AbstractEnvironmentResolver {
    /**
     * 空的 DeferredLogFactory 实现。
     */
    static class DummyDeferredLogFactory implements DeferredLogFactory {
        static final Log DUMMY_LOG = new NoOpLog();

        @Override
        public Log getLog(Supplier<Log> destination) {
            if (destination != null) {
                return destination.get();
            } else {
                return DUMMY_LOG;
            }
        }
    }

    @Override
    protected ConfigurableEnvironment createEnvironment() {
        ConfigurableEnvironment environment = super.createEnvironment();
        this.addCommandLineProperties(environment, this.getArguments());
        return environment;
    }

    private void addCommandLineProperties(ConfigurableEnvironment environment, String[] arguments) {
        // 检查参数
        if (arguments == null || arguments.length < 1) {
            return;
        }

        // 加入命令行对应的属性设置
        MutablePropertySources sources = environment.getPropertySources();
        String name = CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME;
        PropertySource<?> source = sources.get(name);
        if (source != null) {
            CompositePropertySource composite = new CompositePropertySource(name);
            composite.addPropertySource(
                    new SimpleCommandLinePropertySource("springApplicationCommandLineArgs", arguments));
            composite.addPropertySource(source);
            sources.replace(name, composite);
        } else {
            sources.addFirst(new SimpleCommandLinePropertySource(arguments));
        }
    }

    @Override
    protected ConfigurableEnvironment resolveInternal(SpringApplication springApplication, ConfigurableEnvironment environment) {
        // 创建 EnvironmentPostProcessor 实例
        ConfigurableBootstrapContext bootstrapContext = new DefaultBootstrapContext();
        DeferredLogFactory logFactory = new DummyDeferredLogFactory();
        List<String> classNames = SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class,
                getClass().getClassLoader());
        Instantiator<EnvironmentPostProcessor> instantiator = new Instantiator<>(EnvironmentPostProcessor.class,
                parameters -> {
                    parameters.add(DeferredLogFactory.class, logFactory);
                    parameters.add(Log.class, logFactory::getLog);
                    parameters.add(ConfigurableBootstrapContext.class, bootstrapContext);
                    parameters.add(BootstrapContext.class, bootstrapContext);
                    parameters.add(BootstrapRegistry.class, bootstrapContext);
                });
        List<EnvironmentPostProcessor> postProcessors = instantiator.instantiate(classNames);

        // 使用 EnvironmentPostProcessor 实例进行初始化
        for (EnvironmentPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessEnvironment(environment, springApplication);
        }

        // 返回
        return environment;
    }
}
