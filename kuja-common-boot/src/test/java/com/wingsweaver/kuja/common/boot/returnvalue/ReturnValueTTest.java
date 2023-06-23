package com.wingsweaver.kuja.common.boot.returnvalue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReturnValueTTest {
    @Test
    void test() {
        ReturnValueT<Integer> returnValue = new ReturnValueT<>();
        assertNull(returnValue.getData());
        returnValue.setData(1234);
        assertEquals(1234, returnValue.getData());
    }

    @Test
    void testLoad() {
        ReturnValueT<Integer> returnValue = new ReturnValueT<>();
        returnValue.setData(1234);

        {
            ReturnValueT<Integer> returnValue2 = new ReturnValueT<>();
            returnValue2.load(returnValue, false);
            assertEquals(1234, returnValue2.getData());
        }

        {
            ReturnValueT<Integer> returnValue3 = new ReturnValueT<>();
            returnValue3.setData(5678);
            returnValue3.load(returnValue, false);
            assertEquals(5678, returnValue3.getData());
        }

        {
            ReturnValueT<Integer> returnValue4 = new ReturnValueT<>();
            returnValue4.setData(5678);
            returnValue4.load(returnValue, true);
            assertEquals(1234, returnValue4.getData());
        }
    }
}