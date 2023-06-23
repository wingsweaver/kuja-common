package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
class EnvironmentProcessorTest {
    @Test
    void test() {
        try {
            List<String> environmentPostProcessors =
                    SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class, null);
            System.out.println("count of EnvironmentPostProcessor: " + environmentPostProcessors.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SpringApplication application = new SpringApplication(EnvironmentProcessorTest.class);
        application.setBannerMode(Banner.Mode.OFF);
        ApplicationContext applicationContext = application.run();
        assertNotNull(applicationContext);

        Environment environment = applicationContext.getEnvironment();
        assertEquals("1234", environment.getProperty("kuja-preset.id"));
        assertEquals("9876", environment.getProperty("kuja-fallback.id"));
    }

    @Test
    void test2() {
        SpringApplication application = new SpringApplication(EnvironmentProcessorTest.class);
        application.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext applicationContext = application.run("--kuja.boot.env.inherited.enabled=true",
                "--kuja.boot.env.inherited.includes=java.(.)*,kuja.(.)*");
        assertNotNull(applicationContext);

        {
            // 第一次运行，生成继承的 PropertySource
            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
            PropertySource<?> propertySource = propertySources.get(KujaCommonBootKeys.PropertySourceNames.INHERITED);
            assertNull(propertySource);
            assertNotNull(InheritEnvironmentPropertySourceHolder.getInheritEnvironmentPropertySource());
        }

        {
            // 第二次运行，继承的 PropertySource 会被临时加入后，再被删除
            ConfigurableApplicationContext applicationContext2 = application.run("--kuja.boot.env.inherited.enabled=true");
            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
            PropertySource<?> propertySource = propertySources.get(KujaCommonBootKeys.PropertySourceNames.INHERITED);
            assertNull(propertySource);
        }

        // 恢复默认值
        InheritEnvironmentPropertySourceHolder.setInheritEnvironmentPropertySource(null);
    }
}