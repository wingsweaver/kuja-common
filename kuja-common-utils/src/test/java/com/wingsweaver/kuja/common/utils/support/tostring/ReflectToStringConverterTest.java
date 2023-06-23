package com.wingsweaver.kuja.common.utils.support.tostring;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectToStringConverterTest {
    @Test
    void test() {
        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();
        assertFalse(converter.toString(null, sb, config));
        assertFalse(converter.toString(1234, sb, config));
        assertFalse(converter.toString(new Date(), sb, config));
    }

    @Test
    void test2() {
        Person person = new Person();

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();

        converter.toString(person, sb, config);

        String result = sb.toString();
        System.out.println("test2: " + result);

        // 字段 id：包含，内容 masked
        assertTrue(result.contains("id"));
        assertFalse(result.contains("13579"));

        // 字段 name: 包含，内容原文
        assertTrue(result.contains("name"));
        assertTrue(result.contains("tom"));

        // 字段 _phone：不包含（非 public）
        assertFalse(result.contains("_phone"));

        // 字段 password：不包含（transient）
        assertFalse(result.contains("password"));
        assertFalse(result.contains("topsecret"));

        // 字段 ignored：不包含（ToStringIgnored 注解）
        assertFalse(result.contains("ignored"));

        // 字段 globalIndex：不包含（static 字段）
        assertFalse(result.contains("globalIndex"));

        // 字段 token：包含，但是值被同名函数覆盖
        // 函数 getToken：包含
        assertTrue(result.contains("token"));
        assertFalse(result.contains("tk-1234"));
        assertTrue(result.contains("tk-5678"));

        // 函数 getPhone：包含，内容 masked
        assertTrue(result.contains("phone"));
        assertFalse(result.contains("13912345678"));
        assertTrue(result.contains("139****5678"));

        // 函数 isValid：不包含（非 public）
        assertFalse(result.contains("valid"));

        // 函数 getIdHash：不包含（ToStringIgnored 注解）
        assertFalse(result.contains("idHash"));

        // 函数 getVoidValue：不包含（返回 void）
        assertFalse(result.contains("voidValue"));

        // 函数 getIntValue：不包含（带有参数）
        assertFalse(result.contains("intValue"));
    }

    @Test
    void test3() {
        Person person = new Person();

        ToStringConfig config = ToStringBuilder.getDefaultConfig().mutable()
                .setIncludeTransient(true).setPublicOnly(false)
                .build();

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        StringBuilder sb = new StringBuilder();
        converter.toString(person, sb, config);

        String result = sb.toString();
        System.out.println("test3: " + result);

        // 字段 id：包含，内容 masked
        assertTrue(result.contains("id"));
        assertFalse(result.contains("13579"));

        // 字段 name: 包含，内容原文
        assertTrue(result.contains("name"));
        assertTrue(result.contains("tom"));

        // 字段 _phone：包含（允许非 public），内容原文
        assertTrue(result.contains("_phone"));
        assertTrue(result.contains("13912345678"));

        // 字段 password：包含（允许 transient），内容 hashed
        assertTrue(result.contains("password"));
        assertFalse(result.contains("topsecret"));

        // 字段 ignored：不包含（ToStringIgnored 注解）
        assertFalse(result.contains("ignored"));

        // 字段 globalIndex：不包含（static 字段）
        assertFalse(result.contains("globalIndex"));

        // 字段 token：包含，但是值被同名函数覆盖
        // 函数 getToken：包含
        assertTrue(result.contains("token"));
        assertFalse(result.contains("tk-1234"));
        assertTrue(result.contains("tk-5678"));

        // 函数 getPhone：包含，内容 masked
        assertTrue(result.contains("phone"));
        assertTrue(result.contains("139****5678"));

        // 函数 isValid：包含（允许非 public）
        assertTrue(result.contains("valid"));

        // 函数 getIdHash：不包含（ToStringIgnored 注解）
        assertFalse(result.contains("idHash"));
    }

    @Test
    void test4() {
        YearMonth yearMonth = new YearMonth();
        yearMonth.setYear(2023);
        yearMonth.setMonth(10);

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();
        converter.toString(yearMonth, sb, config);

        String result = sb.toString();
        System.out.println("test4: " + result);
        assertEquals("2023#10", result);
    }

    @Test
    void test5() {
        Person2 person2 = new Person2();

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();
        converter.toString(person2, sb, config);

        String result = sb.toString();
        System.out.println("test5: " + result);

        assertTrue(result.contains("name: 张*丰"));
        assertTrue(result.contains("id: zhangsanfeng"));
        assertTrue(result.contains("token: tk-1234"));
    }

    @Test
    void test6() {
        Person3 person2 = new Person3();

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;

        ToStringConfig config = ToStringBuilder.getDefaultConfig();

        StringBuilder sb = new StringBuilder();
        converter.toString(person2, sb, config);

        String result = sb.toString();
        System.out.println("test6: " + result);

        // 字段 name: Person3 覆盖 Person2
        assertFalse(result.contains("name: 张*丰"));
        assertTrue(result.contains("name: 王重阳"));

        // 字段 id: Person3 不覆盖 Person2 （非 public）
        assertTrue(result.contains("id: zhangsanfeng"));

        // 函数 getToken：Person3 覆盖 Person2
        assertFalse(result.contains("token: tk-1234"));
        assertTrue(result.contains("token: tk-5678"));

        // 函数 getPhone：不包含（内部出错）
        assertFalse(result.contains("phone"));
    }

    @Test
    void test7() {
        Person3 person2 = new Person3();

        ToStringConfig config = ToStringBuilder.getDefaultConfig().mutable()
                .setIncludeTransient(true).setPublicOnly(false)
                .setIncludeTypeName(true)
                .build();

        ReflectToStringConverter converter = ReflectToStringConverter.INSTANCE;
        StringBuilder sb = new StringBuilder();
        converter.toString(person2, sb, config);

        String result = sb.toString();
        System.out.println("test7: " + result);

        // 字段 name: Person3 覆盖 Person2
        assertFalse(result.contains("name: 张*丰"));
        assertTrue(result.contains("name: 王重阳"));

        // 字段 id: Person3 覆盖 Person2 （启用非 public）
        assertTrue(result.contains("id: wangchongyang"));

        // 函数 getToken：Person3 覆盖 Person2
        assertFalse(result.contains("token: tk-1234"));
        assertTrue(result.contains("token: tk-5678"));
    }

    static class Person {
        @Masked
        public int id = 13579;

        public String name = "tom";

        protected String _phone = "13912345678";

        @Hashed
        public transient String password = "topsecret";

        @ToStringIgnored
        public Date ignored = new Date();

        public static int globalIndex;

        public String token = "tk-1234";

        @Masked(ranges = @Masked.Range(start = 3, end = -4))
        public String getPhone() {
            return _phone;
        }

        protected boolean isValid() {
            return true;
        }

        @ToStringIgnored
        public int getIdHash() {
            return Objects.hash(this.id);
        }

        public String getToken() {
            return "tk-5678";
        }

        public void getVoidValue() {
        }

        public int getIntValue(int i) {
            return i;
        }
    }

    @Getter
    @Setter
    @ToStringWith(ToStringWithYearMonth.class)
    static class YearMonth {
        private int year;

        private int month;
    }

    public static class ToStringWithYearMonth implements ToStringWithConverter {
        @Override
        public boolean toString(Object value, AnnotatedElement annotatedElement, StringBuilder builder, ToStringConfig context) {
            if (!(value instanceof YearMonth)) {
                return false;
            }

            YearMonth yearMonth = (YearMonth) value;
            builder.append(yearMonth.getYear());
            builder.append("#");
            builder.append(yearMonth.getMonth());
            return true;
        }
    }

    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @Masked(ranges = @Masked.Range(start = 1, end = -1))
    public @interface InheritedMask {
    }

    static class Person2 {
        @InheritedMask
        public String name = "张三丰";

        public String id = "zhangsanfeng";

        public String getToken() {
            return "tk-1234";
        }

        public String getPhone() {
            throw new RuntimeException("not implemented");
        }
    }

    static class Person3 extends Person2 {
        public String name = "王重阳";

        protected String id = "wangchongyang";

        @Override
        public String getToken() {
            return "tk-5678";
        }
    }
}