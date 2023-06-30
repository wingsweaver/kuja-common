package com.wingsweaver.kuja.common.web.controller;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueProperties;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.web.EnableKujaCommonWeb;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(AbstractControllerTest.TestConfiguration.class)
@EnableKujaCommonWeb
@PropertySource("classpath:/application.properties")
class AbstractControllerTest {
    @Autowired
    private WebServerProperties webServerProperties;

    @Test
    void testWebServerProperties() {
        assertNull(this.webServerProperties.getCaseSensitive());
        assertNull(this.webServerProperties.getTrailingSlash());
    }

    @Autowired
    private ReturnValueProperties returnValueProperties;

    @Autowired
    private CustomController customController;

    @Test
    void test() throws Throwable {
        BusinessContext businessContext = new MapBusinessContext();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.customController.onError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ReturnValue returnValue = (ReturnValue) ((ResponseEntity<?>) result).getBody();
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
    }

    @Autowired
    private BusinessExceptionFactory businessExceptionFactory;

    @Test
    void test2() throws Throwable {
        ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                .code("error.code.1")
                .message("error.message.1")
                .userTip("error.userTip.1")
                .build();
        BusinessException error = this.businessExceptionFactory.create(errorDefinition);
        BusinessContext businessContext = new MapBusinessContext();

        LogContext.setLevel(Level.INFO);
        Object result = this.customController.onError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ReturnValue returnValue = (ReturnValue) ((ResponseEntity<?>) result).getBody();
        assertEquals("error.code.1", returnValue.getCode());
        assertEquals("error.message.1", returnValue.getMessage());
        assertEquals("error.userTip.1", returnValue.getUserTip());
    }

    static class CustomController extends AbstractController {
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public CustomController customController() {
            return new CustomController();
        }
    }
}