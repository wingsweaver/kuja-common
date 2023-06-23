package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionProperties;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueProperties;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.web.EnableKujaCommonWeb;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@EnableKujaCommonWeb
@Import(CompositeErrorHandlingTest.TestConfiguration.class)
@PropertySource("classpath:application.properties")
class CompositeErrorHandlingTest {
    @Autowired
    private ErrorDefinitionProperties errorDefinitionProperties;

    @Autowired
    private ReturnValueProperties returnValueProperties;

    @Autowired
    private CustomResponseEntityReturnValueErrorHandlingComponent customResponseEntityReturnValueErrorHandlingComponent;

    @Test
    void test() throws Throwable {
        BusinessContext businessContext = BusinessContext.create();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.customResponseEntityReturnValueErrorHandlingComponent.handleError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ReturnValue returnValue = (ReturnValue) (((ResponseEntity<?>) result).getBody());
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
    }

    @Test
    void test2() throws Throwable {
        ResponseData responseData = new ResponseData();
        responseData.setStatus(HttpStatus.NOT_FOUND);
        this.customResponseEntityReturnValueErrorHandlingComponent.responseData = responseData;

        BusinessContext businessContext = BusinessContext.create();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.customResponseEntityReturnValueErrorHandlingComponent.handleError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        ReturnValue returnValue = (ReturnValue) responseEntity.getBody();
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        this.customResponseEntityReturnValueErrorHandlingComponent.responseData = null;
    }

    @Test
    void test3() throws Throwable {
        this.customResponseEntityReturnValueErrorHandlingComponent.useResponseEntity = true;

        BusinessContext businessContext = BusinessContext.create();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.customResponseEntityReturnValueErrorHandlingComponent.handleError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ReturnValue returnValue = (ReturnValue) (((ResponseEntity<?>) result).getBody());
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());

        this.customResponseEntityReturnValueErrorHandlingComponent.useResponseEntity = false;
    }

    @Test
    void test4() throws Throwable {
        ResponseData responseData = new ResponseData();
        responseData.setStatus(HttpStatus.NOT_FOUND);
        this.customResponseEntityReturnValueErrorHandlingComponent.useResponseEntity = true;
        this.customResponseEntityReturnValueErrorHandlingComponent.responseData = responseData;

        BusinessContext businessContext = BusinessContext.create();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.customResponseEntityReturnValueErrorHandlingComponent.handleError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        ReturnValue returnValue = (ReturnValue) responseEntity.getBody();
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        this.customResponseEntityReturnValueErrorHandlingComponent.responseData = null;
        this.customResponseEntityReturnValueErrorHandlingComponent.useResponseEntity = false;
    }

    @Autowired
    private GlobalErrorAdvice globalErrorAdvice;

    @Test
    void testGlobalErrorAdvice() throws Throwable {
        BusinessContext businessContext = BusinessContext.create();
        Exception error = new Exception("some-error");

        LogContext.setLevel(Level.INFO);
        Object result = this.globalErrorAdvice.onError(businessContext, error);
        LogContext.setLevel(null);

        assertTrue(result instanceof ResponseEntity);
        ReturnValue returnValue = (ReturnValue) (((ResponseEntity<?>) result).getBody());
        assertEquals("-1", returnValue.getCode());
        assertEquals("something wrong happened", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
    }

    @Autowired
    private ErrorHandlingProperties errorHandlingProperties;

    @Test
    void testErrorHandlingProperties() {
        assertNotNull(this.errorHandlingProperties);
        assertNotNull(this.errorHandlingProperties.getGlobalErrorAdvice());
        assertNotNull(this.errorHandlingProperties.getGlobalErrorController());
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public CustomResponseEntityReturnValueErrorHandlingComponent customResponseEntityReturnValueErrorHandlingComponent() {
            return new CustomResponseEntityReturnValueErrorHandlingComponent();
        }
    }

    @Getter
    @Setter
    static class CustomResponseEntityReturnValueErrorHandlingComponent extends AbstractResponseEntityReturnValueErrorHandlingComponent {
        private boolean useResponseEntity = false;

        private ResponseData responseData;

        @Override
        protected Object normalizeReturnValue(ErrorHandlerContext context, Object returnValue) {
            if (this.responseData != null) {
                WebErrorHandlerContextAccessor accessor = new WebErrorHandlerContextAccessor(context);
                accessor.setResponseData(this.responseData);
            }
            return super.normalizeReturnValue(context, returnValue);
        }
    }
}