package com.wingsweaver.kuja.common.utils.support.idgen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CachedLongStringGeneratorTest {
    @Test
    void test() throws Exception {
        CustomStringIdGenerator idGenerator = new CustomStringIdGenerator();
        BackOff backOff = new FixedBackOff(100L, 10L);
        CachedStringIdGenerator cached = new CachedStringIdGenerator(idGenerator, 10, backOff);
        cached.afterPropertiesSet();
        assertEquals("1234", cached.nextId());
    }

    static class CustomStringIdGenerator implements StringIdGenerator, InitializingBean {
        private int index;

        @Override
        public String nextId() {
            String id = String.valueOf(index);
            this.index++;
            return id;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            this.index = 1234;
        }
    }
}