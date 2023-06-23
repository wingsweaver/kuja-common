package com.wingsweaver.kuja.common.utils.support.spring;

import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(BeanAutowireHelperTest.TestConfiguration.class)
class BeanAutowireHelperTest {
    @Autowired
    private BeanFactory beanFactory;

    @Configuration
    static class TestConfiguration {
        @Bean
        public Tester tester() {
            return new Tester();
        }
    }

    @Test
    void autowire() {
        {
            ValueWrapper<String> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, () -> "Hello, World!");
            assertEquals("Hello, World!", valueWrapper.getValue());
        }

        {
            ValueWrapper<String> valueWrapper = new ValueWrapper<>("tom");
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, () -> "Hello, World!");
            assertEquals("tom", valueWrapper.getValue());
        }
    }

    @Test
    void autowire2() {
        {
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, beanFactory, Tester.class);
            assertNotNull(valueWrapper.getValue());
        }
        {
            Tester tester = new Tester();
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>(tester);
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, beanFactory, Tester.class);
            assertSame(tester, valueWrapper.getValue());
        }
    }

    @Test
    void autowire3() {
        {
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, beanFactory, "tester");
            assertNotNull(valueWrapper.getValue());
        }
        {
            Tester tester = new Tester();
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>(tester);
            BeanAutowireHelper.autowire(valueWrapper::getValue, valueWrapper::setValue, beanFactory, "tester");
            assertSame(tester, valueWrapper.getValue());
        }
    }

    @Test
    void autowireNoThrow() {
        {
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireNoThrow(valueWrapper::getValue, valueWrapper::setValue, beanFactory, "tester");
            assertNotNull(valueWrapper.getValue());
        }
        {
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireNoThrow(valueWrapper::getValue, valueWrapper::setValue, beanFactory, Tester.class);
            assertNotNull(valueWrapper.getValue());
        }
    }

    @Test
    void autowireNoThrow2() {
        {
            ValueWrapper<Tester> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireNoThrow(valueWrapper::getValue, valueWrapper::setValue, beanFactory, "tester2");
            assertNull(valueWrapper.getValue());
        }
        {
            ValueWrapper<Tester2> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireNoThrow(valueWrapper::getValue, valueWrapper::setValue, beanFactory, Tester2.class);
            assertNull(valueWrapper.getValue());
        }
    }

    @Test
    void autowireCollection() {
        {
            ValueWrapper<Collection<ApplicationListener>> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireCollection(valueWrapper::getValue, valueWrapper::setValue, beanFactory,
                    ApplicationListener.class, bean -> true);
            assertTrue(valueWrapper.getValue().size() > 0);
        }
        {
            ValueWrapper<Collection<Tester>> valueWrapper = new ValueWrapper<>();
            BeanAutowireHelper.autowireCollection(valueWrapper::getValue, valueWrapper::setValue, beanFactory,
                    Tester.class);
            assertEquals(1, valueWrapper.getValue().size());
        }
    }

    @Test
    void autowireCollection2() {
        {
            Collection<ApplicationListener> collection = new LinkedList<>();
            BeanAutowireHelper.autowireCollection(() -> collection, beanFactory,
                    ApplicationListener.class, bean -> true);
            assertTrue(collection.size() > 0);
        }
        {
            Collection<Tester> collection = new LinkedList<>();
            BeanAutowireHelper.autowireCollection(() -> collection, beanFactory,
                    Tester.class);
            assertEquals(1, collection.size());
        }
    }

    static class Tester {
    }

    static class Tester2 extends Tester {
    }
}