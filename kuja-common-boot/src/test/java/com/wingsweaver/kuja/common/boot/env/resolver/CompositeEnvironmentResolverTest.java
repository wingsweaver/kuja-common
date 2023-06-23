package com.wingsweaver.kuja.common.boot.env.resolver;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeEnvironmentResolverTest {
    @Test
    void test() {
        CompositeEnvironmentResolver environmentResolvers = new CompositeEnvironmentResolver();
        assertTrue(environmentResolvers.getEnvironmentResolvers().isEmpty());
        assertNull(environmentResolvers.resolve());

        StandardEnvironment environment = new StandardEnvironment();
        EnvironmentResolver resolver = () -> environment;
        environmentResolvers.getEnvironmentResolvers().add(resolver);
        assertSame(environment, environmentResolvers.resolve());
    }
}