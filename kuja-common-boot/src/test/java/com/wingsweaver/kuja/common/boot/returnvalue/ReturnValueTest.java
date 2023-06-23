package com.wingsweaver.kuja.common.boot.returnvalue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReturnValueTest {
    @Test
    void test() {
        ReturnValue returnValue = new ReturnValue();

        // code
        {
            assertNull(returnValue.getCode());
            returnValue.setCode("error.code.1");
            assertEquals("error.code.1", returnValue.getCode());
        }

        // message
        {
            assertNull(returnValue.getMessage());
            returnValue.setMessage("error.message.1");
            assertEquals("error.message.1", returnValue.getMessage());
        }

        // userTip
        {
            assertNull(returnValue.getUserTip());
            returnValue.setUserTip("error.userTip.1");
            assertEquals("error.userTip.1", returnValue.getUserTip());
        }

        // tags
        {
            assertNull(returnValue.getTags());
            assertNull(returnValue.getTagValue("traceId"));
            assertEquals("trace-1234", returnValue.getTagValue("traceId", "trace-1234"));
            returnValue.removeTagValue("traceId");
            returnValue.setTagValue("traceId", "trace-9876");
            assertEquals("trace-9876", returnValue.getTagValue("traceId"));
            returnValue.setTagValue("spanId", "span-1234");
            assertEquals("span-1234", returnValue.getTagValue("spanId"));
            returnValue.setTagValue("traceId", null);
            assertNull(returnValue.getTagValue("traceId"));
            returnValue.removeTagValue("spanId");
            assertNull(returnValue.getTagValue("spanId"));
        }

        // temps
        {
            assertNull(returnValue.getTemps(false));
            assertNull(returnValue.getTempValue("traceId"));
            assertEquals("trace-1234", returnValue.getTempValue("traceId", "trace-1234"));
            returnValue.removeTempValue("traceId");
            returnValue.setTempValue("traceId", "trace-9876");
            assertEquals("trace-9876", returnValue.getTempValue("traceId"));
            returnValue.setTempValue("spanId", "span-1234");
            assertEquals("span-1234", returnValue.getTempValue("spanId"));
            returnValue.setTempValue("traceId", null);
            assertNull(returnValue.getTempValue("traceId"));
            returnValue.removeTempValue("spanId");
            assertNull(returnValue.getTempValue("spanId"));
        }
    }

    @Test
    void testLoad() {
        ReturnValue returnValue1 = new ReturnValue();
        returnValue1.setCode("error.code.1");
        returnValue1.setMessage("error.message.1");
        returnValue1.setUserTip("error.userTip.1");
        returnValue1.setTagValue("traceId", "trace-1234");
        returnValue1.setTempValue("os", "windows");

        {
            ReturnValue returnValue2 = new ReturnValue();
            returnValue2.load(returnValue1, true);
            assertEquals("error.code.1", returnValue2.getCode());
            assertEquals("error.message.1", returnValue2.getMessage());
            assertEquals("error.userTip.1", returnValue2.getUserTip());
            assertEquals("trace-1234", returnValue2.getTagValue("traceId"));
            assertEquals("windows", returnValue2.getTempValue("os"));
        }

        {
            ReturnValue returnValue3 = new ReturnValue();
            returnValue3.load(returnValue1, false);
            assertEquals("error.code.1", returnValue3.getCode());
            assertEquals("error.message.1", returnValue3.getMessage());
            assertEquals("error.userTip.1", returnValue3.getUserTip());
            assertEquals("trace-1234", returnValue3.getTagValue("traceId"));
            assertEquals("windows", returnValue3.getTempValue("os"));
        }

        {
            ReturnValue returnValue4 = new ReturnValue();
            returnValue4.setCode("error.code.4");
            returnValue4.setMessage("error.message.4");
            returnValue4.setTagValue("traceId", "trace-9876");
            returnValue4.setTagValue("spanId", "span-9876");
            returnValue4.load(returnValue1, true);
            assertEquals("error.code.1", returnValue4.getCode());
            assertEquals("error.message.1", returnValue4.getMessage());
            assertEquals("error.userTip.1", returnValue4.getUserTip());
            assertEquals("trace-1234", returnValue4.getTagValue("traceId"));
            assertEquals("span-9876", returnValue4.getTagValue("spanId"));
            assertEquals("windows", returnValue4.getTempValue("os"));
        }

        {
            ReturnValue returnValue5 = new ReturnValue();
            returnValue5.setCode("error.code.5");
            returnValue5.setMessage("error.message.5");
            returnValue5.setTagValue("traceId", "trace-9876");
            returnValue5.setTagValue("spanId", "span-9876");
            returnValue5.load(returnValue1, false);
            assertEquals("error.code.5", returnValue5.getCode());
            assertEquals("error.message.5", returnValue5.getMessage());
            assertEquals("error.userTip.1", returnValue5.getUserTip());
            assertEquals("trace-9876", returnValue5.getTagValue("traceId"));
            assertEquals("span-9876", returnValue5.getTagValue("spanId"));
            assertEquals("windows", returnValue5.getTempValue("os"));
        }
    }
}