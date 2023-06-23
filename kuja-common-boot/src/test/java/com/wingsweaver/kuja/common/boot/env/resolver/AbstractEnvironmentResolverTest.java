package com.wingsweaver.kuja.common.boot.env.resolver;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AbstractEnvironmentResolverTest {

    @Test
    void test() {
        CustomEnvironmentResolver environmentResolver = new CustomEnvironmentResolver();
        assertEquals(0, environmentResolver.getArguments().length);
        assertNull(environmentResolver.getEnvironmentInitializer());
        assertNull(environmentResolver.getSpringApplicationBuilderInitializer());
        assertNotNull(environmentResolver.getSpringApplicationInitializer());
        assertNotNull(environmentResolver.getPreProcessors());
        assertNotNull(environmentResolver.getPostProcessors());

        ConfigurableEnvironment environment = environmentResolver.resolve();
        assertNotNull(environment);
        assertEquals(System.getProperty("java.io.tmp"), environment.getProperty("java.io.tmp"));
    }


    @Test
    void test2() {
        CustomEnvironmentResolver environmentResolver = new CustomEnvironmentResolver();
        environmentResolver.setArguments(new String[]{"--spring.profiles.active=custom-test2"});
        environmentResolver.setEnvironmentInitializer(environment -> {
            environment.getPropertySources().addLast(new MapPropertySource("env-init",
                    MapUtil.from("custom-env-int", 123456789)));
            return environment;
        });
        environmentResolver.setSpringApplicationBuilderInitializer(builder -> {
            builder.bannerMode(Banner.Mode.OFF);
            return builder;
        });
        environmentResolver.getPreProcessors(true).add((env, app) -> {
            env.getPropertySources().addLast(new MapPropertySource("env-pre-process",
                    MapUtil.from("custom-pre-process", "-tom-jerry-")));
        });
        environmentResolver.getPostProcessors(true).add((env, app) -> {
            env.getPropertySources().addLast(new MapPropertySource("env-post-process",
                    MapUtil.from("custom-post-process", "#(:)#")));
        });

        ConfigurableEnvironment environment = environmentResolver.resolve();
//        assertEquals("custom-test2", environment.getProperty("spring.profiles.active"));
        // 无法解析命令行参数，因为没有 Spring Boot 的支持
        assertNull(environment.getProperty("spring.profiles.active"));
        assertEquals("123456789", environment.getProperty("custom-env-int"));
        assertEquals("-tom-jerry-", environment.getProperty("custom-pre-process"));
        assertEquals("#(:)#", environment.getProperty("custom-post-process"));
    }


    static class CustomEnvironmentResolver extends AbstractEnvironmentResolver {
        @Override
        protected ConfigurableEnvironment resolveInternal(SpringApplication springApplication, ConfigurableEnvironment environment) {
            return environment;
        }
    }
}