package com.wingsweaver.kuja.common.utils.support.json;

import com.alibaba.fastjson.annotation.JSONField;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
class FastJson2CodecTest {
    @Test
    void testObject() {
        FastJson2Codec codec = new FastJson2Codec();
        assertNotNull(codec.getFeatures());

        {
            String text = "123456789";
            String json = codec.toJsonString(text);
            assertEquals(text, codec.parseObject(json, String.class));
        }

        {
            Long id = 97531L;
            String json = codec.toJsonString(id);
            assertEquals(id, codec.parseObject(json, Long.class));
        }

        {
            Student student = new Student();
            student.setId(24680);
            student.setName("张三丰");
            String json = codec.toJsonString(student);
            assertEquals(student, codec.parseObject(json, Student.class));
        }

        {
            assertEquals("{}", codec.toJsonString(System.in));
            assertThrows(JsonCodecException.class, () -> codec.parseObject("123456789", InputStream.class));
        }
    }

    @Test
    void testList() {
        FastJson2Codec codec = new FastJson2Codec();

        {
            int[] array = {1, 2, 3, 4, 5};
            // 将 array 转换成 List
            List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
            String json = codec.toJsonString(array);
            List<Object> output = codec.parseList(json);
            assertIterableEquals(list, output);

            List<Integer> output2 = codec.parseList(json, Integer.class);
            assertIterableEquals(list, output2);
        }

        {
            int count = 10;
            List<Student> students = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                Student student = new Student();
                student.setId(i);
                student.setName("学生 #" + i);
                students.add(student);
            }

            String json = codec.toJsonString(students);

            List<Student> output = codec.parseList(json, Student.class);
            assertIterableEquals(students, output);

            List<Object> output2 = codec.parseList(json);
            assertEquals(students.size(), output2.size());
            for (int i = 0; i < count; i++) {
                Map<String, Object> map = (Map<String, Object>) output2.get(i);
                assertEquals(students.get(i).getId(), map.get("id"));
                assertEquals(students.get(i).getName(), map.get("name"));
            }
        }

        {
            assertEquals("[{}]", codec.toJsonString(Collections.singletonList(System.in)));
            assertThrows(JsonCodecException.class, () -> codec.parseList("[123456789]", InputStream.class));
        }
    }

    @Test
    void testMap() {
        FastJson2Codec codec = new FastJson2Codec();

        {
            Student student = new Student();
            student.setId(24680);
            student.setName("张三丰");
            String json = codec.toJsonString(student);

            Map<String, Object> map = codec.parseMap(json);
            assertEquals(student.getId(), map.get("id"));
            assertEquals(student.getName(), map.get("name"));
        }

        {
            Map<String, Object> input = MapUtil.from("id", 24680, "name", "张三丰");
            String json = codec.toJsonString(input);

            Map<String, Object> output = codec.parseMap(json);
            assertEquals(input.size(), output.size());
            assertEquals(input.get("id"), output.get("id"));
            assertEquals(input.get("name"), output.get("name"));
        }

        {
            int count = 10;
            Map<Integer, Student> input = new HashMap<>(count + 1, MapUtil.FULL_LOAD_FACTOR);
            for (int i = 0; i < count; i++) {
                Student student = new Student();
                student.setId(i);
                student.setName("学生 #" + i);
                input.put(i, student);
            }

            String json = codec.toJsonString(input);

            Map<Integer, Student> output = codec.parseMap(json, Integer.class, Student.class);
            assertEquals(input.size(), output.size());
            for (int i = 0; i < count; i++) {
                assertEquals(input.get(i), output.get(i));
            }

            Map<String, Object> output2 = codec.parseMap(json);
            assertEquals(input.size(), output2.size());
            for (int i = 0; i < count; i++) {
                Student student = input.get(i);
                Map<String, Object> map = (Map<String, Object>) output2.get(String.valueOf(i));
                assertEquals(student.getId(), map.get("id"));
                assertEquals(student.getName(), map.get("name"));
            }
        }

        {
            assertEquals("{\"input\":{}}", codec.toJsonString(MapUtil.from("input", System.in)));
            assertThrows(JsonCodecException.class, () -> codec.parseMap("{\"id\":1234}", String.class, InputStream.class));
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    public static class Student extends AbstractPojo {
        private int id;

        private String name;

        private transient InputStream inputStream = System.in;

        @JSONField(serialize = false, deserialize = false)
        private OutputStream outputStream = System.out;
    }
}