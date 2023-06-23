package com.wingsweaver.kuja.common.web.convert;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HttpStatusConverterTest {
    @Test
    void test() {
        assertNull(HttpStatusConverter.convert(null));
        assertNull(HttpStatusConverter.convert(UUID.randomUUID()));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusConverter.convert(HttpStatus.NOT_FOUND));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusConverter.convert(HttpStatus.NOT_FOUND.value()));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusConverter.convert(HttpStatus.NOT_FOUND.name()));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusConverter.convert("NOT_FOUND"));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatusConverter.convert(Integer.toString(HttpStatus.NOT_FOUND.value())));
    }
}