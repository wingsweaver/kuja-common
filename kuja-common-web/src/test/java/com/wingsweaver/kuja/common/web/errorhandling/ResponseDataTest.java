package com.wingsweaver.kuja.common.web.errorhandling;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ResponseDataTest {
    @Test
    void test() {
        ResponseData responseData = new ResponseData();

        // status
        {
            assertNull(responseData.getStatus());
            responseData.setStatus(HttpStatus.BAD_GATEWAY);
            assertEquals(HttpStatus.BAD_GATEWAY, responseData.getStatus());
        }

        // headers
        {
            assertNull(responseData.getHeaders());
            HttpHeaders headers = new HttpHeaders();
            responseData.setHeaders(headers);
            assertSame(headers, responseData.getHeaders());

            responseData.setHeaders(null);
            HttpHeaders headers2 = responseData.getHeaders(true);
            assertNotSame(headers, headers2);
        }
    }
}