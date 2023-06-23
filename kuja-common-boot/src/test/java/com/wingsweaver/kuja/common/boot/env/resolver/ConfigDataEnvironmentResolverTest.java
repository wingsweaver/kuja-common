package com.wingsweaver.kuja.common.boot.env.resolver;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigDataEnvironmentResolverTest {
    @Test
    void test() {
        ConfigDataEnvironmentResolver environmentResolver = new ConfigDataEnvironmentResolver();
        environmentResolver.setArguments(new String[]{"--some-commandline-argument=some-commandline-value"});

        ConfigurableEnvironment environment = environmentResolver.resolve();
        assertEquals("some-commandline-value", environment.getProperty("some-commandline-argument"));
    }

    @Test
    void test2() {
        ConfigDataEnvironmentResolver environmentResolver = new ConfigDataEnvironmentResolver();
        environmentResolver.setArguments(new String[]{"--cmd-arg-1=cmd-value-1"});
        environmentResolver.setEnvironmentInitializer(environment -> {
            PropertySource<?> propertySource = new SimpleCommandLinePropertySource(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME,
                    "--cmd-arg-1=cmd-value-1.2", "--cmd-arg-2=cmd-value-2");
            environment.getPropertySources().addFirst(propertySource);
            return environment;
        });

        ConfigurableEnvironment environment = environmentResolver.resolve();
        assertEquals("cmd-value-1", environment.getProperty("cmd-arg-1"));
        assertEquals("cmd-value-2", environment.getProperty("cmd-arg-2"));
    }
}