package com.wingsweaver.kuja.common.webmvc.jakarta.errorhandling;

import com.wingsweaver.kuja.common.boot.errorhandling.DefaultErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.returnvalue.DefaultReturnValueFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class GlobalErrorControllerTest {

    @Test
    void renderError() {
        GlobalErrorController controller = new GlobalErrorController();
        DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes();
        controller.setErrorAttributes(errorAttributes);

        ReturnValueFactory returnValueFactory = this.createReturnValueFactory();
        controller.setReturnValueFactory(returnValueFactory);

        DefaultErrorHandlingDelegate errorHandlingDelegate = new DefaultErrorHandlingDelegate();
        controller.setErrorHandlingDelegate(errorHandlingDelegate);

        testRenderError(controller, errorAttributes);
        testRenderError2(controller, errorAttributes);
        testRenderError3(controller, errorAttributes);
        testRenderError4(controller, errorAttributes);
    }

    private void testRenderError4(GlobalErrorController controller, DefaultErrorAttributes errorAttributes) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        servletRequest.setAttribute(RequestDispatcher.ERROR_MESSAGE, "some-error-message");

        Object returnValue = controller.renderError(servletRequest, servletResponse);
        System.out.println(returnValue);

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ReturnValue returnValue2 = (ReturnValue) responseEntity.getBody();
        assertEquals("error.code.fail", returnValue2.getCode());
        assertEquals("some-error-message", returnValue2.getMessage());
        assertEquals("error.userTip.fail", returnValue2.getUserTip());
    }

    private void testRenderError3(GlobalErrorController controller, DefaultErrorAttributes errorAttributes) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        servletRequest.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED.value());

        Object returnValue = controller.renderError(servletRequest, servletResponse);
        System.out.println(returnValue);

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ReturnValue returnValue2 = (ReturnValue) responseEntity.getBody();
        assertEquals("error.code.fail", returnValue2.getCode());
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), returnValue2.getMessage());
        assertEquals("error.userTip.fail", returnValue2.getUserTip());
    }

    private void testRenderError2(GlobalErrorController controller, DefaultErrorAttributes errorAttributes) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        Exception error = new Exception("some-error");
        errorAttributes.resolveException(servletRequest, servletResponse, null, error);

        Object returnValue = controller.renderError(servletRequest, servletResponse);
        System.out.println(returnValue);

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ReturnValue returnValue2 = (ReturnValue) responseEntity.getBody();
        assertEquals("error.code.fail", returnValue2.getCode());
        assertEquals("some-error", returnValue2.getMessage());
        assertEquals("error.userTip.fail", returnValue2.getUserTip());
    }

    private void testRenderError(GlobalErrorController controller, DefaultErrorAttributes errorAttributes) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        Object returnValue = controller.renderError(servletRequest, servletResponse);
        System.out.println(returnValue);

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ReturnValue returnValue2 = (ReturnValue) responseEntity.getBody();
        assertEquals("error.code.fail", returnValue2.getCode());
        assertNull(returnValue2.getMessage());
        assertEquals("error.userTip.fail", returnValue2.getUserTip());
    }

    private ReturnValueFactory createReturnValueFactory() {
        DefaultReturnValueFactory returnValueFactory = new DefaultReturnValueFactory();

        ReturnValue fail = new ReturnValue();
        fail.setCode("error.code.fail");
//        fail.setMessage("error.message.fail");
        fail.setUserTip("error.userTip.fail");
        returnValueFactory.setFail(fail);

        returnValueFactory.setCustomizers(Collections.emptyList());

        returnValueFactory.setMessageHelper(new MessageHelper());

        return returnValueFactory;
    }

    @Test
    void afterPropertiesSet() throws Exception {
        GlobalErrorController controller = new GlobalErrorController();

        assertNull(controller.getErrorAttributes());
        DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes();
        controller.setErrorAttributes(errorAttributes);
        assertSame(errorAttributes, controller.getErrorAttributes());

        assertNull(controller.getReturnValueFactory());
        DefaultReturnValueFactory returnValueFactory = new DefaultReturnValueFactory();
        controller.setReturnValueFactory(returnValueFactory);
        assertSame(returnValueFactory, controller.getReturnValueFactory());

        assertNull(controller.getErrorHandlingDelegate());
        DefaultErrorHandlingDelegate errorHandlingDelegate = new DefaultErrorHandlingDelegate();
        controller.setErrorHandlingDelegate(errorHandlingDelegate);
        assertSame(errorHandlingDelegate, controller.getErrorHandlingDelegate());

        controller.afterPropertiesSet();
    }
}