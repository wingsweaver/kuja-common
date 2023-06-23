package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultErrorHandlerContextTest {
    @Test
    void test() {
        DefaultErrorHandlerContext context = new DefaultErrorHandlerContext();

        assertNull(context.getBusinessContext());
        BusinessContext businessContext = BusinessContext.create();
        context.setBusinessContext(businessContext);
        assertSame(businessContext, context.getBusinessContext());

        assertNull(context.getInputError());
        Exception inputError = new Exception("input-error");
        context.setInputError(inputError);
        assertSame(inputError, context.getInputError());

        assertNull(context.getOutputError());
        Exception outputError = new Exception("output-error");
        context.setOutputError(outputError);
        assertSame(outputError, context.getOutputError());

        assertFalse(context.isHandled());
        context.setHandled(true);
        assertTrue(context.isHandled());

        assertNull(context.getReturnValue());
        Object returnValue = new Object();
        context.setReturnValue(returnValue);
        assertSame(returnValue, context.getReturnValue());
        Object returnValue2 = new Object();
        context.setReturnValueIfAbsent(() -> returnValue2);
        assertSame(returnValue, context.getReturnValue());
    }
}