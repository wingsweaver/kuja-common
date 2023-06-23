package com.wingsweaver.kuja.common.web.wrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HostInfoTest {
    @Test
    void test() {
        HostInfo hostInfo = new HostInfo();

        assertNull(hostInfo.getName());
        hostInfo.setName("localhost");
        assertEquals("localhost", hostInfo.getName());

        assertNull(hostInfo.getAddress());
        hostInfo.setAddress("127.0.0.1");
        assertEquals("127.0.0.1", hostInfo.getAddress());

        assertEquals(0, hostInfo.getPort());
        hostInfo.setPort(8080);
        assertEquals(8080, hostInfo.getPort());
    }
}