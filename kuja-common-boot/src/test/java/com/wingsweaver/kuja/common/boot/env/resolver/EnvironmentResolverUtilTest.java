package com.wingsweaver.kuja.common.boot.env.resolver;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class EnvironmentResolverUtilTest {
    @Test
    void test() {
        EnvironmentResolver environmentResolver = EnvironmentResolverUtil.getEnvironmentResolver();
        assertNotNull(environmentResolver);
        EnvironmentResolverUtil.setEnvironmentResolver(environmentResolver);
        assertSame(environmentResolver, EnvironmentResolverUtil.getEnvironmentResolver());
    }

    @Test
    void test2() {
        ConfigurableEnvironment environment = EnvironmentResolverUtil.resolveEnvironment();
        assertNotNull(environment);
    }

    @Test
    void test3() {
        EnvironmentResolverUtil.setConfigurableEnvironment(null);
        assertNull(EnvironmentResolverUtil.getEnvironment());
        assertNull(EnvironmentResolverUtil.getEnvironment(false));

        ConfigurableEnvironment environment = EnvironmentResolverUtil.getEnvironment(true);
        assertNotNull(environment);

        assertSame(environment, EnvironmentResolverUtil.getEnvironment());
        assertSame(environment, EnvironmentResolverUtil.getEnvironment(true));

        EnvironmentResolverUtil.setConfigurableEnvironment(null);
        assertNotSame(environment, EnvironmentResolverUtil.getEnvironment());
        assertNotSame(environment, EnvironmentResolverUtil.getEnvironment(true));
    }
}