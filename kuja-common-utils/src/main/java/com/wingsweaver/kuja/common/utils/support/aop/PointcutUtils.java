package com.wingsweaver.kuja.common.utils.support.aop;

import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Pointcut (切入点) 相关的工具类。
 *
 * @author wingsweaver
 */
public final class PointcutUtils {
    private PointcutUtils() {
        // 禁止实例化
    }

    /**
     * 使用指定表达式生成 Pointcut 处理的接口定义。
     */
    public interface ExpressionPointcutBuilder {
        /**
         * 使用指定的表达式生成 Pointcut。
         *
         * @param expression 表达式
         * @return Pointcut
         */
        Pointcut build(String expression);
    }

    /**
     * 基于 {@link AspectJExpressionPointcut} 的 {@link ExpressionPointcutBuilder} 实现。
     */
    public static final ExpressionPointcutBuilder ASPECTJ = expression -> {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    };

    /**
     * 基于 {@link JdkRegexpMethodPointcut} 的 {@link ExpressionPointcutBuilder} 实现。<br>
     * {@code expression} 中使用逗号分隔多个表达式。
     */
    public static final ExpressionPointcutBuilder JDK_REGEX_METHOD = expression -> {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns(StringUtils.split(expression, ','));
        return pointcut;
    };

    /**
     * 基于 {@link AnnotationMatchingPointcut}。<br>
     * 表达式中指定的是注解的全限定名。<br>
     * 支持的表达式格式为：<br>
     * <ul>
     *     <li>classAnnotationType</li>
     *     <li>classAnnotationType,methodAnnotationType</li>
     *     <li>classAnnotationType,methodAnnotationType,checkInherited</li>
     *     <li>classAnnotationType,checkInherited</li>
     * </ul>
     */
    public static final ExpressionPointcutBuilder ANNOTATION = expression -> {
        String[] segments = StringUtils.split(expression, ',');

        String first = segments[0];
        Class<? extends Annotation> classAnnotationType = ClassUtil.forName(first);
        if (classAnnotationType == null) {
            throw new IllegalArgumentException("Invalid expression (classAnnotationType not found): " + first);
        }
        if (segments.length == 1) {
            return new AnnotationMatchingPointcut(classAnnotationType);
        }

        // 计算 methodAnnotationType 和 checkInherited
        Class<? extends Annotation> methodAnnotationType = null;
        boolean checkInherited = false;
        for (int i = 1; i < segments.length; i++) {
            String segment = segments[i];
            if ("true".equalsIgnoreCase(segment)) {
                checkInherited = true;
            } else if ("false".compareToIgnoreCase(segment) == 0) {
                checkInherited = false;
            } else {
                methodAnnotationType = ClassUtil.forName(segment);
            }
        }

        // 生成 Pointcut
        if (methodAnnotationType == null) {
            return new AnnotationMatchingPointcut(classAnnotationType, checkInherited);
        } else {
            return new AnnotationMatchingPointcut(classAnnotationType, methodAnnotationType, checkInherited);
        }
    };

    private static final AtomicReference<ExpressionPointcutBuilder> DEFAULT_POINTCUT_BUILDER = new AtomicReference<>(ASPECTJ);

    public static ExpressionPointcutBuilder getDefaultPointcutBuilder() {
        return DEFAULT_POINTCUT_BUILDER.get();
    }

    public static void setDefaultPointcutBuilder(ExpressionPointcutBuilder pointcutBuilder) {
        Objects.requireNonNull(pointcutBuilder, "pointcutBuilder is null");
        DEFAULT_POINTCUT_BUILDER.set(pointcutBuilder);
    }

    /**
     * 基于表达式生成切入点。
     *
     * @param builder     切入点生成器
     * @param expressions 表达式列表
     * @return 切入点
     */
    @SuppressWarnings("unused")
    public static Pointcut buildPointcut(ExpressionPointcutBuilder builder, String... expressions) {
        Objects.requireNonNull(builder, "builder is null");
        Collection<String> collection = expressions != null && expressions.length > 0 ? Arrays.asList(expressions) : null;
        return buildPointcut(builder, collection);
    }

    /**
     * 基于表达式生成切入点。
     *
     * @param expressions 表达式列表
     * @return 切入点
     */
    @SuppressWarnings("unused")
    public static Pointcut buildPointcut(String... expressions) {
        return buildPointcut(getDefaultPointcutBuilder(), expressions);
    }

    /**
     * 基于表达式生成切入点。
     *
     * @param builder     切入点生成器
     * @param expressions 表达式列表
     * @return 切入点
     */
    public static Pointcut buildPointcut(ExpressionPointcutBuilder builder, Collection<String> expressions) {
        Objects.requireNonNull(builder, "builder is null");

        Pointcut ret;
        // 提取表达式中的有效内容
        List<String> list = new LinkedList<>();
        if (!CollectionUtils.isEmpty(expressions)) {
            expressions.forEach(expression -> {
                if (StringUtil.isNotEmpty(expression)) {
                    list.add(expression);
                }
            });
        }

        // 生成切入点
        if (CollectionUtils.isEmpty(list)) {
            // 如果没有指定表达式, 那么恒定返回 FALSE
            ret = FalsePointcut.INSTANCE;
        } else if (list.size() == 1) {
            ret = builder.build(list.get(0));
        } else {
            // 如果指定了表达式, 那么按照 OR 的关系生成联合切入点
            ComposablePointcut composablePointcut = null;
            for (String expression : list) {
                // 生成当前表达式对应的切入点
                Pointcut pointcut = builder.build(expression);

                // 合并到联合切入点
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pointcut);
                } else {
                    composablePointcut = composablePointcut.union(pointcut);
                }
            }
            ret = composablePointcut;
        }

        // 返回
        return ret;
    }

    /**
     * 基于表达式生成切入点。
     *
     * @param expressions 表达式列表
     * @return 切入点
     */
    public static Pointcut buildPointcut(Collection<String> expressions) {
        return buildPointcut(getDefaultPointcutBuilder(), expressions);
    }
}
