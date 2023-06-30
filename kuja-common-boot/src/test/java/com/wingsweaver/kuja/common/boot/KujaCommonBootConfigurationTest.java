package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoCustomizationBean;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringBootVersion;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringBootVersion2x;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringBootVersion3x;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringVersion;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringVersion5x;
import com.wingsweaver.kuja.common.boot.condition.ConditionalOnSpringVersion6x;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionAttributes;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionProperties;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionRepository;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitions;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.warmup.AsyncWarmUpTaskExecutor;
import com.wingsweaver.kuja.common.boot.warmup.WarmUpTaskExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(KujaCommonBootConfigurationTest.TestConfiguration.class)
@EnableKujaCommonBoot
@PropertySource("classpath:application-test.properties")
class KujaCommonBootConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AppInfoCustomizationBean appInfoCustomizationBean;

    @Test
    void testCommandLineRunners() throws Exception {
        List<CommandLineRunner> commandLineRunners = this.applicationContext.getBeanProvider(CommandLineRunner.class)
                .orderedStream().collect(Collectors.toList());
        for (CommandLineRunner commandLineRunner : commandLineRunners) {
            commandLineRunner.run();
        }
    }

    @Test
    void testAppInfo() {
        assertNotNull(appInfoCustomizationBean);
        assertNotNull(appInfoCustomizationBean.getAppInfo());
        assertEquals(KujaCommonBootOrders.APP_INFO_CUSTOMIZATION_BEAN, appInfoCustomizationBean.getOrder());
    }

    @Test
    void testConditionalOnSpringBootVersion() {
        assertNotNull(applicationContext.getBean(BusinessContextFactory.class));

        assertNotNull(applicationContext.getBean("springBootVersionBean2x"));
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean("springBootVersionBean3x"));
        assertNotNull(applicationContext.getBean("springBootVersionBean2x2"));
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean("springBootVersionBean3x2"));

        assertNotNull(applicationContext.getBean("springVersionBean5x"));
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean("springVersionBean6x"));
        assertNotNull(applicationContext.getBean("springVersionBean5x2"));
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean("springVersionBean6x2"));
    }

    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private MessageSource messageSource;

    @Test
    void testI18n() {
        assertNotNull(messageHelper.getMessageSource());

        {
            assertEquals("hello, world", messageHelper.getMessage(Locale.ENGLISH, "hello", "world").get());
            assertEquals("你好, world", messageHelper.getMessage(Locale.CHINA, "hello", "world").get());
            assertFalse(messageHelper.getMessage(Locale.ENGLISH, "hello2", "world").isPresent());

            assertEquals("hello, world", MessageHelper.getMessage(messageSource, Locale.ENGLISH, "hello", "world").get());
            assertEquals("你好, world", MessageHelper.getMessage(messageSource, Locale.CHINA, "hello", "world").get());
            assertFalse(MessageHelper.getMessage(messageSource, Locale.ENGLISH, "hello2", "world").isPresent());
        }

        {
            assertEquals("hello, world", messageHelper.format(Locale.ENGLISH, "{hello}", "world").get());
            assertEquals("你好, world", messageHelper.format(Locale.CHINA, "{hello}", "world").get());
            assertFalse(messageHelper.format(Locale.ENGLISH, "{hello2}", "world").isPresent());
            assertEquals("bonjour, world", messageHelper.format(Locale.ENGLISH, "bonjour, {}", "world").get());

            assertEquals("hello, world", MessageHelper.format(messageSource, Locale.ENGLISH, "{hello}", "world").get());
            assertEquals("你好, world", MessageHelper.format(messageSource, Locale.CHINA, "{hello}", "world").get());
            assertFalse(MessageHelper.format(messageSource, Locale.ENGLISH, "{hello2}", "world").isPresent());
            assertEquals("aloha, world", MessageHelper.format(messageSource, Locale.CHINA, "aloha, {0}", "world").get());
        }

        {
            LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);
            LocaleContextHolder.setLocale(Locale.ENGLISH, true);

            assertEquals("hello, world", messageHelper.getMessage("hello", "world").get());
            assertEquals("hello, world", MessageHelper.getMessage(messageSource, "hello", "world").get());
            assertEquals("hello, world", messageHelper.getMessage((Locale) null, "hello", "world").get());
            assertEquals("hello, world", MessageHelper.getMessage(messageSource, (Locale) null, "hello", "world").get());

            assertEquals("hello, world", messageHelper.format("{hello}", "world").get());
            assertEquals("bonjour, world", messageHelper.format("bonjour, {}", "world").get());
            assertEquals("hello, world", messageHelper.format((Locale) null, "{hello}", "world").get());
            assertEquals("bonjour, world", messageHelper.format((Locale) null, "bonjour, {}", "world").get());
            assertEquals("hello, world", MessageHelper.format(messageSource, "{hello}", "world").get());
            assertEquals("aloha, world", MessageHelper.format(messageSource, "aloha, {}", "world").get());
            assertEquals("hello, world", MessageHelper.format(messageSource, (Locale) null, "{hello}", "world").get());
            assertEquals("aloha, world", MessageHelper.format(messageSource, (Locale) null, "aloha, {}", "world").get());

            LocaleContextHolder.setLocale(Locale.getDefault(), true);
            LocaleContextHolder.setDefaultLocale(Locale.getDefault());
        }
    }

    @Autowired
    private ErrorDefinitionRepository errorDefinitionRepository;

    @Test
    void testErrorDefinition() {
        {
            ErrorDefinition errorDefinition = errorDefinitionRepository.getErrorDefinition("error.code.1");
            assertNotNull(errorDefinition);
            assertEquals("error.code.1", errorDefinition.getCode());
            assertEquals("error.message.1", errorDefinition.getMessage());
        }

        assertNotNull(errorDefinitionRepository.getErrorDefinition("error.code.2"));
        assertNull(errorDefinitionRepository.getErrorDefinition("error.code.3"));
        assertNull(errorDefinitionRepository.getErrorDefinition("error.code.4"));

        assertNotNull(errorDefinitionRepository.getErrorDefinition("error.code.10"));
        assertNotNull(errorDefinitionRepository.getErrorDefinition("error.code.11"));
        assertNull(errorDefinitionRepository.getErrorDefinition("error.code.12"));
        assertNull(errorDefinitionRepository.getErrorDefinition("error.code.13"));

        assertNotNull(errorDefinitionRepository.getErrorDefinition("error.code.20"));

        {
            ErrorDefinition errorDefinition = errorDefinitionRepository.getErrorDefinition("error.code.100");
            assertNotNull(errorDefinition);
            assertEquals(1234, errorDefinition.getOrder());
            assertEquals("error.code.100", errorDefinition.getCode());
            assertEquals("error.message.100", errorDefinition.getMessage());
            assertEquals("error.userTip.100", errorDefinition.getUserTip());
            assertEquals("red", errorDefinition.getTemps().get("color"));
        }

        {
            ErrorDefinition errorDefinition = errorDefinitionRepository.getErrorDefinition("error-101");
            assertNotNull(errorDefinition);
            assertEquals("error-101", errorDefinition.getCode());
            assertEquals("error.message.101", errorDefinition.getMessage());
            assertEquals("error.tags.traceId.101", errorDefinition.getTags().get("traceId"));
        }

        {
            ErrorDefinition errorDefinition = errorDefinitionRepository.getErrorDefinition("error-102");
            assertNotNull(errorDefinition);
            assertEquals("error-102", errorDefinition.getCode());
            assertEquals("error.message.102", errorDefinition.getMessage());
            assertEquals("404", errorDefinition.getTemps().get("status"));
        }
    }

    @Autowired
    private ErrorDefinitionAttributes errorDefinitionAttributes;

    @Autowired
    private ErrorDefinitionProperties errorDefinitionProperties;

    @Test
    void testErrorDefinition2() {
        assertNotNull(this.errorDefinitionAttributes);
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        String message = "custom error: " + UUID.randomUUID();
        Exception error = new Exception(message);

        {
            ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                    .code("http-401").build();
            assertNull(this.errorDefinitionAttributes.resolveMessage(error, errorDefinition, null));

            this.errorDefinitionProperties.setFallbackToCode(true);
            assertEquals("Unauthorized", this.errorDefinitionAttributes.resolveUserTip(error, errorDefinition, null));

            this.errorDefinitionProperties.setFallbackToCode(false);
            assertNull(this.errorDefinitionAttributes.resolveUserTip(error, errorDefinition, null));
        }

        {
            ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                    .code("402")
                    .message("{402}")
                    .userTip("{402}")
                    .build();

            this.errorDefinitionProperties.setFailFast(true);
            assertThrows(IllegalArgumentException.class, () -> this.errorDefinitionAttributes.resolveMessage(error, errorDefinition, null));
            assertThrows(IllegalArgumentException.class, () -> this.errorDefinitionAttributes.resolveUserTip(error, errorDefinition, null));

            this.errorDefinitionProperties.setFailFast(false);
            assertNull(this.errorDefinitionAttributes.resolveMessage(error, errorDefinition, null));
            assertNull(this.errorDefinitionAttributes.resolveUserTip(error, errorDefinition, null));
        }

        {
            ErrorDefinition http401 = DefaultErrorDefinition.builder()
                    .code("http.401")
                    .message("{http-401}")
                    .userTip("{http-401}")
                    .build();

            assertEquals("Unauthorized", this.errorDefinitionAttributes.resolveMessage(error, http401, null));
            assertEquals("Unauthorized", this.errorDefinitionAttributes.resolveUserTip(error, http401, null));
        }
        {
            ErrorDefinition http402 = DefaultErrorDefinition.builder()
                    .code("http.402")
                    .message("http-402.message")
                    .userTip("http-402.userTip")
                    .build();

            assertEquals("http-402.message", this.errorDefinitionAttributes.resolveMessage(error, http402, null));
            assertEquals("http-402.userTip", this.errorDefinitionAttributes.resolveUserTip(error, http402, null));
        }
        {
            ErrorDefinition http403 = DefaultErrorDefinition.builder()
                    .code("http.403")
                    .message("{http-403}")
                    .userTip("{http-403}")
                    .build();

            assertEquals("Forbidden", this.errorDefinitionAttributes.resolveMessage(error, http403, null));
            assertEquals("Forbidden", this.errorDefinitionAttributes.resolveUserTip(error, http403, null));
        }

        LocaleContextHolder.setLocale(Locale.getDefault());
    }

    @Autowired
    private BusinessExceptionFactory businessExceptionFactory;

    @Test
    void testException() {
        Exception cause = new Exception("cause of the error: " + UUID.randomUUID());
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        {
            BusinessException businessException = this.businessExceptionFactory.create("error.code.401");
            assertEquals("error.code.401", businessException.getCode());
            assertEquals("Unauthorized", businessException.getMessage());
            assertEquals("http error 401", businessException.getUserTip());
        }
        {
            BusinessException businessException = this.businessExceptionFactory.create(cause, "error.code.403", cause);
            assertEquals("error.code.403", businessException.getCode());
            assertEquals("Forbidden", businessException.getMessage());
            assertEquals("http error 403", businessException.getUserTip());
        }
        {
            assertThrows(IllegalArgumentException.class, () -> this.businessExceptionFactory.create((ErrorDefinition) null));
            assertThrows(IllegalArgumentException.class, () -> this.businessExceptionFactory.create(cause, (ErrorDefinition) null));
            assertThrows(IllegalArgumentException.class, () -> this.businessExceptionFactory.create("invalid-error-code"));
            assertThrows(IllegalArgumentException.class, () -> this.businessExceptionFactory.create(cause, "invalid-error-code"));
        }
        LocaleContextHolder.setLocale(Locale.getDefault());
    }

    @Configuration(proxyBeanMethods = false)
    public static class ErrorDefinitionConfiguration extends AbstractConfiguration {
        @Bean
        public CustomErrorDefinitions customErrorDefinitions() {
            return new CustomErrorDefinitions();
        }

        @Bean
        public ErrorDefinition errorDefinition20() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.20")
                    .message("error.message.20")
                    .build();
        }

        @Bean
        public ErrorDefinition errorDefinition20new() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.20")
                    .message("error.message.20.new")
                    .userTip("error.userTip.20.new")
                    .build();
        }

        @Bean
        public ErrorDefinition errorDefinition21() {
            return new ErrorDefinition() {
                @Override
                public String getCode() {
                    return "error.code.21";
                }

                @Override
                public String getMessage() {
                    return "error.message.21";
                }

                @Override
                public String getUserTip() {
                    return null;
                }

                @Override
                public Map<String, Object> getTags() {
                    return null;
                }

                @Override
                public Map<String, Object> getTemps() {
                    return null;
                }
            };
        }

        @Bean
        public ErrorDefinition errorDefinition21new() {
            return new ErrorDefinition() {
                @Override
                public String getCode() {
                    return "error.code.21";
                }

                @Override
                public String getMessage() {
                    return "error.message.21.new";
                }

                @Override
                public String getUserTip() {
                    return "error.userTip.21.new";
                }

                @Override
                public Map<String, Object> getTags() {
                    return null;
                }

                @Override
                public Map<String, Object> getTemps() {
                    return null;
                }
            };
        }
    }

    @ErrorDefinitions
    public static class CustomErrorDefinitions {
        public static final ErrorDefinition CUSTOM_ERROR_1 = DefaultErrorDefinition.builder()
                .code("error.code.1")
                .message("error.message.1")
                .build();

        public final ErrorDefinition CUSTOM_ERROR_2 = DefaultErrorDefinition.builder()
                .code("error.code.2")
                .message("error.message.2")
                .build();

        protected final ErrorDefinition CUSTOM_ERROR_3 = DefaultErrorDefinition.builder()
                .code("error.code.3")
                .message("error.message.3")
                .build();

        private final ErrorDefinition CUSTOM_ERROR_4 = DefaultErrorDefinition.builder()
                .code("error.code.4")
                .message("error.message.4")
                .build();

        public static ErrorDefinition getCustomError10() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.10")
                    .message("error.message.10")
                    .build();
        }

        public ErrorDefinition getCustomError11() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.11")
                    .message("error.message.11")
                    .build();
        }

        public ErrorDefinition getCustomError11x() {
            throw new RuntimeException("error.message.11x");
        }

        protected ErrorDefinition getCustomError12() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.12")
                    .message("error.message.12")
                    .build();
        }

        private ErrorDefinition getCustomError13() {
            return DefaultErrorDefinition.builder()
                    .code("error.code.13")
                    .message("error.message.13")
                    .build();
        }
    }

    @Configuration(proxyBeanMethods = false)
    public static class TestConfiguration extends AbstractConfiguration {
        @Bean
        public WarmUpTaskExecutor kujaBootWarmUpTaskExecutor(ApplicationContext applicationContext) {
            return new AsyncWarmUpTaskExecutor(applicationContext);
        }

        @Bean
        @ConditionalOnSpringBootVersion2x
        public Object springBootVersionBean2x() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringBootVersion3x
        public Object springBootVersionBean3x() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringBootVersion("2.*")
        public Object springBootVersionBean2x2() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringBootVersion("3.*")
        public Object springBootVersionBean3x2() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringVersion5x
        public Object springVersionBean5x() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringVersion6x
        public Object springVersionBean6x() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringVersion("5.*")
        public Object springVersionBean5x2() {
            return new Object();
        }

        @Bean
        @ConditionalOnSpringVersion("6.*")
        public Object springVersionBean6x2() {
            return new Object();
        }
    }
}