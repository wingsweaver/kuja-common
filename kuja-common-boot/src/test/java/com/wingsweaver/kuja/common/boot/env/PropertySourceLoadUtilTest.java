package com.wingsweaver.kuja.common.boot.env;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PropertySourceLoadUtilTest {
    @Test
    void testProperties() {
        {
            List<PropertySource<?>> propertySources = PropertySourceLoadUtil.load(null, new String[]{"classpath:test.properties"});
            assertNotNull(propertySources);
            assertEquals(1, propertySources.size());
            PropertySource<?> propertySource = propertySources.get(0);
            assertEquals("1234", propertySource.getProperty("pid"));
            assertEquals("windows", propertySource.getProperty("os.type"));
        }

        {
            assertSame(Collections.emptyList(), PropertySourceLoadUtil.load(null, new String[]{"classpath:test2.properties"}));
        }
    }

    @Test
    void testYaml() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        {
            List<PropertySource<?>> propertySources = PropertySourceLoadUtil.load(resourceLoader, new String[]{"classpath:test.yaml"});
            assertNotNull(propertySources);
            assertEquals(1, propertySources.size());
            PropertySource<?> propertySource = propertySources.get(0);
            assertEquals("localhost", propertySource.getProperty("name"));
            assertEquals("tomcat", propertySource.getProperty("app.name"));
        }

        {
            assertSame(Collections.emptyList(), PropertySourceLoadUtil.load(resourceLoader, new String[]{"classpath:test2.yaml"}));
        }
    }

    @Test
    void testOthers() {
        assertSame(Collections.emptyList(), PropertySourceLoadUtil.load(null, null));
        assertSame(Collections.emptyList(), PropertySourceLoadUtil.load(null, new String[0]));

        // 无效的资源路径
        {
            ExtendedRuntimeException error = null;
            try {
                PropertySourceLoadUtil.load(null, new String[]{"classpath://test?.properties"});
            } catch (ExtendedRuntimeException ex) {
                error = ex;
            }
            assertNotNull(error);
            assertEquals("classpath://test?.properties", error.getExtendedAttribute("location"));
        }

        // 不存在的资源路径
        {
            String location = "https://" + UUID.randomUUID() + ".com/" + UUID.randomUUID() + ".properties";
            assertSame(Collections.emptyList(), PropertySourceLoadUtil.load(null, new String[]{location}));
        }
    }
}