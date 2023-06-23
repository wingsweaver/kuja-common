package com.wingsweaver.kuja.common.utils.support.spring;

import org.junit.jupiter.api.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MethodParameterWrapperTest {
    Method method;

    Method method2;

    Constructor<?> constructor = Tester.class.getConstructors()[0];

    public MethodParameterWrapperTest() throws NoSuchMethodException {
        for (Method declaredMethod : Tester.class.getDeclaredMethods()) {
            if ("test".equalsIgnoreCase(declaredMethod.getName())) {
                method = declaredMethod;
            } else if ("test2".equalsIgnoreCase(declaredMethod.getName())) {
                method2 = declaredMethod;
            }
        }
    }

    @Test
    void test() {
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodParameterWrapper wrapper = new MethodParameterWrapper(methodParameter);
        assertSame(methodParameter, wrapper.getMethodParameter());
        assertEquals(methodParameter.hashCode(), wrapper.hashCode());
        assertEquals(methodParameter.toString(), wrapper.toString());

        assertEquals(methodParameter, wrapper);
        assertEquals(methodParameter, wrapper.clone());
        assertEquals(wrapper.clone(), methodParameter.clone());

        wrapper.increaseNestingLevel();
        assertEquals(methodParameter.getNestingLevel(), wrapper.getNestingLevel());
        wrapper.decreaseNestingLevel();
        assertEquals(methodParameter.getNestingLevel(), wrapper.getNestingLevel());

        assertEquals(methodParameter.isOptional(), wrapper.isOptional());

        assertArrayEquals(methodParameter.getMethodAnnotations(), wrapper.getMethodAnnotations());
        assertEquals(methodParameter.getMethodAnnotation(Deprecated.class), wrapper.getMethodAnnotation(Deprecated.class));
        assertEquals(methodParameter.hasMethodAnnotation(Deprecated.class), wrapper.hasMethodAnnotation(Deprecated.class));

        assertArrayEquals(methodParameter.getParameterAnnotations(), wrapper.getParameterAnnotations());
        assertEquals(methodParameter.getParameterAnnotation(Nullable.class), wrapper.getParameterAnnotation(Nullable.class));
        assertEquals(methodParameter.hasParameterAnnotation(Nullable.class), wrapper.hasParameterAnnotation(Nullable.class));
        assertEquals(methodParameter.hasParameterAnnotations(), wrapper.hasParameterAnnotations());

        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        wrapper.initParameterNameDiscovery(discoverer);

        Integer typeIndex = wrapper.getTypeIndexForLevel(methodParameter.getNestingLevel());
        Integer currentTypeIndex = wrapper.getTypeIndexForCurrentLevel();
        if (currentTypeIndex != null) {
            wrapper.setTypeIndexForCurrentLevel(currentTypeIndex);
            wrapper.withTypeIndex(currentTypeIndex);
        } else {
            wrapper.setTypeIndexForCurrentLevel(0);
            wrapper.withTypeIndex(0);
        }

        assertEquals(methodParameter.nested(), wrapper.nested());
        assertEquals(methodParameter.nestedIfOptional(), wrapper.nestedIfOptional());
        assertEquals(methodParameter.nested(currentTypeIndex), wrapper.nested(currentTypeIndex));

        assertEquals(methodParameter.getGenericParameterType(), wrapper.getGenericParameterType());
        wrapper.setGenericParameterType(String.class);
        assertEquals(String.class, wrapper.getGenericParameterType());

        assertEquals(methodParameter.getNestedParameterType(), wrapper.getNestedParameterType());
        assertEquals(methodParameter.getNestedGenericParameterType(), wrapper.getNestedGenericParameterType());

        assertEquals(methodParameter.getMethod(), wrapper.getMethod());
        wrapper.setMethod(method2);
        assertSame(method2, wrapper.getMethod());

        assertEquals(methodParameter.getDeclaringClass(), wrapper.getDeclaringClass());
        wrapper.setDeclaringClass(MethodParameterWrapperTest.class);
        assertSame(MethodParameterWrapperTest.class, wrapper.getDeclaringClass());

        assertEquals(methodParameter.getMember(), wrapper.getMember());
        wrapper.setMember(method2);
        assertSame(method2, wrapper.getMember());

        assertEquals(methodParameter.getAnnotatedElement(), wrapper.getAnnotatedElement());
        wrapper.setAnnotatedElement(MethodParameterWrapperTest.class);
        assertSame(MethodParameterWrapperTest.class, wrapper.getAnnotatedElement());

        assertEquals(methodParameter.getExecutable(), wrapper.getExecutable());
        wrapper.setExecutable(method2);
        assertSame(method2, wrapper.getExecutable());

        assertEquals(methodParameter.getParameter(), wrapper.getParameter());
        Parameter parameter = method2.getParameters()[1];
        wrapper.setParameter(parameter);
        assertSame(parameter, wrapper.getParameter());

        assertEquals(methodParameter.getParameterIndex(), wrapper.getParameterIndex());
        wrapper.setParameterIndex(1);
        assertEquals(1, wrapper.getParameterIndex());

        assertEquals(methodParameter.getParameterName(), wrapper.getParameterName());
        wrapper.setParameterName("testWrapped");
        assertEquals("testWrapped", wrapper.getParameterName());

        assertEquals(methodParameter.getContainingClass(), wrapper.getContainingClass());
        wrapper.setContainingClass(MethodParameterWrapperTest.class);
        assertSame(MethodParameterWrapperTest.class, wrapper.getContainingClass());
        assertEquals(Object.class, wrapper.withContainingClass(Object.class).getContainingClass());

        assertEquals(methodParameter.getParameterType(), wrapper.getParameterType());
        wrapper.setParameterType(String.class);
        assertEquals(String.class, wrapper.getParameterType());


    }

    @Test
    void test2() {
        MethodParameter methodParameter = new MethodParameter(constructor, 0);
        MethodParameterWrapper wrapper = new MethodParameterWrapper(methodParameter);

        assertEquals(methodParameter.getConstructor(), wrapper.getConstructor());
        Constructor<?> constructor2 = MethodParameterWrapperTest.class.getConstructors()[0];
        wrapper.setConstructor(constructor2);
        assertSame(constructor2, wrapper.getConstructor());

    }

    static class Tester {
        private final String id;

        @Deprecated
        public Tester(@Nullable String id) {
            this.id = id;
        }

        @Deprecated
        public void test(@Nullable Supplier<String> msg) {
            System.out.println("Test.test " + id + ", msg: " + msg);
        }

        @Deprecated
        public void test2(@Nullable String msg, @Nullable Integer msg2) {
            System.out.println("Test.tset2 " + id + ", msg: " + msg + ", msg2: " + msg2);
        }
    }
}