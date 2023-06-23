package com.wingsweaver.kuja.common.utils.support.aop;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PointcutUtilsTest {

    @Test
    void test4empty() {
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut());
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut((String) null));
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut((Collection<String>) null));
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut(Collections.emptyList()));
    }

    @Test
    void testDefaultBuilder() {
        assertNotNull(PointcutUtils.getDefaultPointcutBuilder());
        PointcutUtils.setDefaultPointcutBuilder(PointcutUtils.JDK_REGEX_METHOD);
        assertEquals(PointcutUtils.JDK_REGEX_METHOD, PointcutUtils.getDefaultPointcutBuilder());
        PointcutUtils.setDefaultPointcutBuilder(PointcutUtils.ASPECTJ);
        assertTrue(PointcutUtils.buildPointcut("within(@org.springframework.stereotype.Repository *)") instanceof AspectJExpressionPointcut);
        assertTrue(PointcutUtils.buildPointcut("within(@org.springframework.stereotype.Repository *)", "execution(* transfer(..))")
                instanceof ComposablePointcut);
    }

    @Test
    void testAspectJBuilder() {
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut(PointcutUtils.ASPECTJ));
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ASPECTJ, "within(@org.springframework.stereotype.Repository *)")
                instanceof AspectJExpressionPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ASPECTJ, "within(@org.springframework.stereotype.Repository *)", "execution(* transfer(..))")
                instanceof ComposablePointcut);

    }

    @Test
    void testJdkRegexMethodBuilder() {
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut(PointcutUtils.JDK_REGEX_METHOD));
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.JDK_REGEX_METHOD, ".*foo.*") instanceof JdkRegexpMethodPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.JDK_REGEX_METHOD, ".*foo.*", ".*bar.*")
                instanceof ComposablePointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.JDK_REGEX_METHOD, ".*foo.*,.*bar.*") instanceof JdkRegexpMethodPointcut);
    }

    @Test
    void testAnnotationBuilder() {
        assertEquals(FalsePointcut.INSTANCE, PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION));
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName())
                instanceof AnnotationMatchingPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName(), Target.class.getName())
                instanceof ComposablePointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName() + "," + Target.class.getName())
                instanceof AnnotationMatchingPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName() + "," + true)
                instanceof AnnotationMatchingPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName() + ","
                + Target.class.getName() + "," + false) instanceof AnnotationMatchingPointcut);
        assertTrue(PointcutUtils.buildPointcut(PointcutUtils.ANNOTATION, Deprecated.class.getName() + ","
                + " " + Target.class.getName() + ",") instanceof AnnotationMatchingPointcut);
    }
}