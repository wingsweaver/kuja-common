package com.wingsweaver.kuja.common.utils.support.spring;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(BeanUtilTest.TestConfiguration.class)
class BeanUtilTest {
    @Configuration
    static class TestConfiguration {
        @Bean
        public Tester tester() {
            return new Tester();
        }
    }

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Test
    void autowire() {
        Object object = new Object();
        assertSame(object, BeanUtil.autowire(null, object));

        Tester tester = new Tester();
        assertNull(tester.applicationContext);
        assertNull(tester.applicationContextName);
        assertNull(tester.id);
        assertNull(tester.environment);

        BeanUtil.autowire(beanFactory, tester);
        assertNotNull(tester.applicationContext);
        assertNotNull(tester.applicationContextName);
        assertNotNull(tester.id);
        assertNotNull(tester.environment);
    }

    @Test
    void create() {
        Tester tester = BeanUtil.create(beanFactory, Tester.class);
        assertNotNull(tester.applicationContext);
        assertNotNull(tester.applicationContextName);
        assertNotNull(tester.id);
        assertNotNull(tester.environment);

        Tester2 tester2 = BeanUtil.create(beanFactory, Tester2.class);
        assertNotNull(tester2.getApplicationContext());
        assertNotNull(tester2.getApplicationContextName());
        assertNotNull(tester2.getId());
        assertNotNull(tester2.getEnvironment());
        assertNotNull(tester2.messageSource);
    }

    @Test
    void beanOrClassInstance() throws ClassNotFoundException {
        assertNotNull(BeanUtil.beanOrClassInstance(beanFactory, "tester"));
        assertNotNull(BeanUtil.beanOrClassInstance(beanFactory, Tester.class.getName()));
        assertNotNull(BeanUtil.beanOrClassInstance(beanFactory, Tester2.class.getName()));
    }

    @Getter
    @Setter
    static class Tester implements InitializingBean {
        @Autowired
        private ApplicationContext applicationContext;

        private String applicationContextName;

        private String id;

        private Environment environment;

        @Override
        public void afterPropertiesSet() {
            this.applicationContextName = applicationContext.toString();
        }

        @PostConstruct
        public void init() {
            this.id = UUID.randomUUID().toString();
        }

        public Environment getEnvironment() {
            return environment;
        }

        public void setEnvironment(@Autowired Environment environment) {
            this.environment = environment;
        }
    }

    @Getter
    @Setter
    static class Tester2 extends Tester {
        private final MessageSource messageSource;

        Tester2(MessageSource messageSource) {
            this.messageSource = messageSource;
        }
    }
}