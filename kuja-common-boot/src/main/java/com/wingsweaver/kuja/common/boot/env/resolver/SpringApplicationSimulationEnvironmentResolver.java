package com.wingsweaver.kuja.common.boot.env.resolver;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 类似 SpringCloud 的 LegacyContextRefresher 的获取配置信息的 {@link AbstractEnvironmentResolver} 实现类。<br>
 * 其特点是，运行 SpringApplication 实例，然后获取返回的 ConfigurableApplicationContext 实例中对应的配置信息。
 *
 * @author wingsweaver
 */
@SuppressWarnings("unused")
public class SpringApplicationSimulationEnvironmentResolver extends AbstractEnvironmentResolver {
    @SuppressWarnings("unused")
    @Override
    protected ConfigurableEnvironment resolveInternal(SpringApplication springApplication, ConfigurableEnvironment environment) {
        try (ConfigurableApplicationContext applicationContext = springApplication.run(this.getArguments())) {
            return applicationContext.getEnvironment();
        }
    }
}
