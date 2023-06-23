package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultErrorDefinitionTest {
    @Test
    void test() {
        DefaultErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                .order(135)
                .code("error.code.1")
                .message("error.message.1")
                .userTip("error.userTip.1")
                .build();
        assertEquals(135, errorDefinition.getOrder());
        assertEquals("error.code.1", errorDefinition.getCode());
        assertEquals("error.message.1", errorDefinition.getMessage());
        assertEquals("error.userTip.1", errorDefinition.getUserTip());
        assertNull(errorDefinition.getTags());
        assertNull(errorDefinition.getTemps());

        errorDefinition.getTags(true).put("traceId", 12345678);
        assertEquals(1, errorDefinition.getTags().size());
        assertEquals(12345678, errorDefinition.getTags().get("traceId"));

        errorDefinition.getTemps(true).put("status", 200);
        assertEquals(1, errorDefinition.getTemps().size());
        assertEquals(200, errorDefinition.getTemps().get("status"));

        ErrorDefinition errorDefinition2 = DefaultErrorDefinition.builder()
                .order(246)
                .code("error.code.2")
                .message("error.message.2")
                .userTip("error.userTip.2")
                .tags(MapUtil.from("traceId", 98765, "spanId", 1024))
                .temps(MapUtil.from("status", 404, "reason", "Not Found"))
                .build();

        errorDefinition.load(errorDefinition2, false);
        assertEquals(135, errorDefinition.getOrder());
        assertEquals("error.code.1", errorDefinition.getCode());
        assertEquals("error.message.1", errorDefinition.getMessage());
        assertEquals("error.userTip.1", errorDefinition.getUserTip());
        assertEquals(12345678, errorDefinition.getTags().get("traceId"));
        assertEquals(1024, errorDefinition.getTags().get("spanId"));
        assertEquals(200, errorDefinition.getTemps().get("status"));
        assertEquals("Not Found", errorDefinition.getTemps().get("reason"));

        errorDefinition.load(errorDefinition2, true);
        assertEquals(246, errorDefinition.getOrder());
        assertEquals("error.code.2", errorDefinition.getCode());
        assertEquals("error.message.2", errorDefinition.getMessage());
        assertEquals("error.userTip.2", errorDefinition.getUserTip());
        assertEquals(98765, errorDefinition.getTags().get("traceId"));
        assertEquals(1024, errorDefinition.getTags().get("spanId"));
        assertEquals(404, errorDefinition.getTemps().get("status"));
        assertEquals("Not Found", errorDefinition.getTemps().get("reason"));

        errorDefinition.setOrder(0);
        errorDefinition.setCode(null);
        errorDefinition.setMessage(null);
        errorDefinition.setUserTip(null);
        errorDefinition.load(errorDefinition2, false);
        assertEquals(246, errorDefinition.getOrder());
        assertEquals("error.code.2", errorDefinition.getCode());
        assertEquals("error.message.2", errorDefinition.getMessage());
        assertEquals("error.userTip.2", errorDefinition.getUserTip());
    }

    @Test
    void test2() {
        DefaultErrorDefinition errorDefinition = new DefaultErrorDefinition();

        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode("error.code.2");
        returnValue.setMessage("error.message.2");
        returnValue.setUserTip("error.userTip.2");
        returnValue.getTags(true).put("traceId", 12345678);
        returnValue.getTemps(true).put("status", 200);

        errorDefinition.load(returnValue, false);
        assertEquals(0, errorDefinition.getOrder());
        assertEquals("error.code.2", errorDefinition.getCode());
        assertEquals("error.message.2", errorDefinition.getMessage());
        assertEquals("error.userTip.2", errorDefinition.getUserTip());
        assertEquals(12345678, errorDefinition.getTags().get("traceId"));
        assertEquals(200, errorDefinition.getTemps().get("status"));

        errorDefinition.setCode("error.code.1");
        errorDefinition.setMessage("error.message.1");
        errorDefinition.setUserTip("error.userTip.1");
        errorDefinition.load(returnValue, false);
        assertEquals("error.code.1", errorDefinition.getCode());
        assertEquals("error.message.1", errorDefinition.getMessage());
        assertEquals("error.userTip.1", errorDefinition.getUserTip());

        errorDefinition.load(returnValue, true);
        assertEquals("error.code.2", errorDefinition.getCode());
        assertEquals("error.message.2", errorDefinition.getMessage());
        assertEquals("error.message.2", errorDefinition.getMessage());
        assertEquals("error.userTip.2", errorDefinition.getUserTip());
        assertEquals(12345678, errorDefinition.getTags().get("traceId"));
        assertEquals(200, errorDefinition.getTemps().get("status"));
    }
}