package com.wingsweaver.kuja.common.utils.model;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractPojoTest {
    @Test
    void test() {
        TestPojo pojo = new TestPojo();
        pojo.setAge(10);
        pojo.setName("tom");
        String text = pojo.toString();
        System.out.println("text = " + text);
        assertTrue(text.startsWith(TestPojo.class.getTypeName()));
        assertTrue(text.contains("name: tom"));
        assertTrue(text.contains("age: 10"));
    }

    @Getter
    @Setter
    public static class TestPojo extends AbstractPojo {
        private String name;

        private int age;
    }
}