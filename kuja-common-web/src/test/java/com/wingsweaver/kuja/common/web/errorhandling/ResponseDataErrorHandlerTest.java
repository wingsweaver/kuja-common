package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.errorhandling.DefaultErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import com.wingsweaver.kuja.common.web.constants.KujaCommonWebOrders;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseDataErrorHandlerTest {
    @Test
    void test() {
        ResponseDataErrorHandler errorHandler = new ResponseDataErrorHandler();
        assertEquals(KujaCommonWebOrders.RESPONSE_DATA_ERROR_HANDLER, errorHandler.getOrder());

        DefaultErrorHandlerContext errorHandlerContext = new DefaultErrorHandlerContext();
        WebErrorHandlerContextAccessor accessor = new WebErrorHandlerContextAccessor(errorHandlerContext);
        assertFalse(errorHandler.handle(null));
        assertFalse(errorHandler.handle(errorHandlerContext));

        {
            ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                    .code("error.code.1")
                    .message("error.message.1")
                    .userTip("error.userTip.1")
                    .build();
            BusinessException error = new BusinessException("some-business-exception", errorDefinition);
            errorHandlerContext.setInputError(error);
            assertFalse(errorHandler.handle(errorHandlerContext));
        }

        {
            Map<String, Object> temps = MapUtil.from(
                    ResponseDataErrorHandler.KEY_RESPONSE_STATUS, "NOT_FOUND",
                    ResponseDataErrorHandler.KEY_RESPONSE_HEADERS_PREFIX, "some-data",
                    ResponseDataErrorHandler.KEY_RESPONSE_HEADERS_PREFIX + "os", "windows"
            );

            ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                    .code("error.code.1")
                    .message("error.message.1")
                    .userTip("error.userTip.1")
                    .temps(temps)
                    .build();
            BusinessException error = new BusinessException("some-business-exception", errorDefinition);
            errorHandlerContext.setInputError(error);
            assertFalse(errorHandler.handle(errorHandlerContext));

            ResponseData responseData = accessor.getResponseData(false);
            assertNotNull(responseData);
            assertEquals(HttpStatus.NOT_FOUND, responseData.getStatus());
            assertEquals(1, responseData.getHeaders().size());
            assertEquals("windows", responseData.getHeaders().getFirst("os"));
        }
    }
}