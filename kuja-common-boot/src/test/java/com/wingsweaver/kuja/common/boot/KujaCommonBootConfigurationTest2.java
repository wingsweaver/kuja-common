package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionProperties;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionRepository;
import com.wingsweaver.kuja.common.boot.errorhandling.AbstractErrorHandlingComponent;
import com.wingsweaver.kuja.common.boot.errorhandling.AbstractReturnValueErrorHandlingComponent;
import com.wingsweaver.kuja.common.boot.errorhandling.DefaultErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandler;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContextCustomizer;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.errorhandling.PreDefinedBusinessExceptionHandler;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.model.AbstractBusinessComponent;
import com.wingsweaver.kuja.common.boot.model.AbstractService;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(KujaCommonBootConfigurationTest2.TestConfiguration.class)
@PropertySource("classpath:application-test2.properties")
@EnableKujaCommonBoot
class KujaCommonBootConfigurationTest2 {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ErrorDefinitionRepository errorDefinitionRepository;

    @Autowired
    private ErrorDefinitionProperties errorDefinitionProperties;

    @Test
    void test() {
        assertNotNull(this.errorDefinitionRepository);
        assertNotNull(this.errorDefinitionProperties);
    }

    @Autowired
    private PreDefinedBusinessExceptionHandler preDefinedBusinessExceptionHandler;

    @Autowired
    private BusinessExceptionFactory businessExceptionFactory;

    @Test
    void testPreDefinedBusinessExceptionHandler() {
        DefaultErrorHandlerContext context = new DefaultErrorHandlerContext();

        // 没有异常
        assertFalse(this.preDefinedBusinessExceptionHandler.handle(context));

        // 非 BusinessException 异常
        {
            Exception error = new Exception("some-error");
            context.setInputError(error);
            assertFalse(this.preDefinedBusinessExceptionHandler.handle(context));
        }

        // BusinessException 异常
        {
            BusinessException businessException = this.businessExceptionFactory.create("error.code.401");
            context.setInputError(businessException);
            assertTrue(this.preDefinedBusinessExceptionHandler.handle(context));
            assertTrue(context.isHandled());
            ReturnValue returnValue = context.getReturnValue();
            assertEquals("error.code.401", returnValue.getCode());
        }
    }

    @Autowired
    private ErrorHandlingDelegate errorHandlingDelegate;

    @Test
    void testErrorHandlingDelegate() {
        {
            BusinessContext businessContext = BusinessContext.create();
            Exception error = new Exception("some-error");
            ErrorHandlerContext errorHandlerContext = this.errorHandlingDelegate.handleError(businessContext, error);
            assertNotNull(errorHandlerContext);
            assertSame(error, errorHandlerContext.getInputError());
        }

        {
            BusinessContext businessContext = BusinessContext.create();
            String message = "custom-error: " + UUID.randomUUID();
            CustomError error = new CustomError(message);
            ErrorHandlerContextCustomizer preProcessor = context -> {
                context.setAttribute("pre-processor", 1234);
            };
            ErrorHandlerContextCustomizer postProcessor = context -> {
                context.setAttribute("post-processor", 9876);
            };
            ErrorHandlerContext errorHandlerContext = this.errorHandlingDelegate.handleError(
                    businessContext, error, preProcessor, postProcessor);
            assertNotNull(errorHandlerContext);
            assertSame(error, errorHandlerContext.getInputError());
            assertEquals(message, errorHandlerContext.getReturnValue());
            assertTrue(errorHandlerContext.isHandled());
            assertEquals(1234, (int) errorHandlerContext.getAttribute("pre-processor"));
            assertEquals(9876, (int) errorHandlerContext.getAttribute("post-processor"));
        }
    }

    @Autowired
    private CustomErrorHandlingComponent customErrorHandlingComponent;

    @Test
    void testCustomErrorHandlingComponent() throws Throwable {
        {
            BusinessContext businessContext = BusinessContext.create();
            Exception error = new Exception("some-error");
            assertThrows(Exception.class, () -> this.customErrorHandlingComponent.handleError(businessContext, error));
        }
        {
            BusinessContext businessContext = BusinessContext.create();
            String message = "custom-error: " + UUID.randomUUID();
            CustomError error = new CustomError(message);
            Object result = this.customErrorHandlingComponent.handleError(businessContext, error);
            assertEquals(message, result);
        }
    }

    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private CustomReturnValueErrorHandlingComponent customReturnValueErrorHandlingComponent;

    @Test
    void testCustomReturnValueErrorHandlingComponent() throws Throwable {
        {
            // 无法处理的异常类型
            // 使用默认的失败返回结果兜底
            BusinessContext businessContext = BusinessContext.create();
            Exception error = new Exception("some-error");
            ReturnValue returnValue = (ReturnValue) this.customReturnValueErrorHandlingComponent.handleError(businessContext, error);
            assertEquals("24680", returnValue.getCode());
            assertEquals("failed!!!", returnValue.getMessage());
            assertEquals(messageHelper.getMessage("http-403").get(), returnValue.getUserTip());
            assertEquals("trace-abcdefgh", returnValue.getTags().get("traceId"));
            assertEquals("span-abcdefgh", returnValue.getTags().get("spanId"));
            assertEquals("1234", returnValue.getTemps(false).get("status"));
        }

        {
            // 可以处理的内置异常类型
            // 根据异常内容提取返回值
            BusinessContext businessContext = BusinessContext.create();
            BusinessException error = this.businessExceptionFactory.create("error.code.401");
            ReturnValue returnValue = (ReturnValue) this.customReturnValueErrorHandlingComponent.handleError(businessContext, error);
            assertEquals("error.code.401", returnValue.getCode());
            assertEquals(this.messageHelper.getMessage("http-401").get(), returnValue.getMessage());
            assertEquals("http error 401", returnValue.getUserTip());
        }

        {
            // 可以处理的自定义异常类型
            // 使用自定义的处理结果
            BusinessContext businessContext = BusinessContext.create();
            String message = "custom-error: " + UUID.randomUUID();
            CustomError error = new CustomError(message);
            assertEquals(message, this.customReturnValueErrorHandlingComponent.handleError(businessContext, error));
        }
    }

    @Autowired
    private CustomBusinessComponent customBusinessComponent;

    @Test
    void testCustomBusinessComponent() {
        // return value
        assertNotNull(this.customBusinessComponent.success());
        assertNotNull(this.customBusinessComponent.success("some-data"));
        ReturnValue returnValue = new ReturnValue();
        this.customBusinessComponent.patchSuccess(returnValue);

        // create business exception
        assertNotNull(this.customBusinessComponent.createBusinessException("error.code.401"));
        assertNotNull(this.customBusinessComponent.createBusinessException(new Exception("some-error"), "error.code.401"));
        assertNotNull(this.customBusinessComponent.createBusinessException(DefaultErrorDefinition.builder().code("error.code.401").build()));
        assertNotNull(this.customBusinessComponent.createBusinessException(new Exception("some-error"), DefaultErrorDefinition.builder().code("error.code.401").build()));

        // throw business exception
        assertThrows(BusinessException.class, () -> this.customBusinessComponent.throwBusinessException("error.code.401"));
        assertThrows(BusinessException.class, () -> this.customBusinessComponent.throwBusinessException(new Exception("some-error"), "error.code.401"));
        assertThrows(BusinessException.class, () -> this.customBusinessComponent.throwBusinessException(DefaultErrorDefinition.builder().code("error.code.401").build()));
        assertThrows(BusinessException.class, () -> this.customBusinessComponent.throwBusinessException(new Exception("some-error"), DefaultErrorDefinition.builder().code("error.code.401").build()));
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public CustomErrorHandler customErrorHandler() {
            return new CustomErrorHandler();
        }

        @Bean
        public CustomErrorHandlingComponent customErrorHandlingComponent() {
            return new CustomErrorHandlingComponent();
        }

        @Bean
        public CustomReturnValueErrorHandlingComponent customReturnValueErrorHandlingComponent() {
            return new CustomReturnValueErrorHandlingComponent();
        }

        @Bean
        public CustomBusinessComponent customBusinessComponent() {
            return new CustomBusinessComponent();
        }

        @Bean
        public CustomService customService() {
            return new CustomService();
        }
    }

    static class CustomError extends Exception {
        public CustomError(String message) {
            super(message);
        }
    }

    public static class CustomErrorHandler implements ErrorHandler {
        @Override
        public boolean handle(ErrorHandlerContext context) {
            if (context.getInputError() instanceof CustomError) {
                CustomError customError = context.getInputError();
                context.setReturnValue(customError.getMessage());
                context.setHandled(true);
                return true;
            }
            return false;
        }
    }

    static class CustomErrorHandlingComponent extends AbstractErrorHandlingComponent {
    }

    static class CustomReturnValueErrorHandlingComponent extends AbstractReturnValueErrorHandlingComponent {
    }

    static class CustomService extends AbstractService {
    }

    static class CustomBusinessComponent extends AbstractBusinessComponent {
        @Override
        public ReturnValue success() {
            return super.success();
        }

        @Override
        public <T> ReturnValueT<T> success(T data) {
            return super.success(data);
        }

        @Override
        public void patchSuccess(ReturnValue returnValue) {
            super.patchSuccess(returnValue);
        }

        @Override
        public BusinessException createBusinessException(ErrorDefinition errorDefinition, Object... args) {
            return super.createBusinessException(errorDefinition, args);
        }

        @Override
        public BusinessException createBusinessException(Throwable cause, ErrorDefinition errorDefinition, Object... args) {
            return super.createBusinessException(cause, errorDefinition, args);
        }

        @Override
        public BusinessException createBusinessException(String errorDefinitionCode, Object... args) {
            return super.createBusinessException(errorDefinitionCode, args);
        }

        @Override
        public BusinessException createBusinessException(Throwable cause, String errorDefinitionCode, Object... args) {
            return super.createBusinessException(cause, errorDefinitionCode, args);
        }

        @Override
        public void throwBusinessException(ErrorDefinition errorDefinition, Object... args) throws BusinessException {
            super.throwBusinessException(errorDefinition, args);
        }

        @Override
        public void throwBusinessException(Throwable cause, ErrorDefinition errorDefinition, Object... args) throws BusinessException {
            super.throwBusinessException(cause, errorDefinition, args);
        }

        @Override
        public void throwBusinessException(String errorDefinitionCode, Object... args) throws BusinessException {
            super.throwBusinessException(errorDefinitionCode, args);
        }

        @Override
        public void throwBusinessException(Throwable cause, String errorDefinitionCode, Object... args) throws BusinessException {
            super.throwBusinessException(cause, errorDefinitionCode, args);
        }
    }
}