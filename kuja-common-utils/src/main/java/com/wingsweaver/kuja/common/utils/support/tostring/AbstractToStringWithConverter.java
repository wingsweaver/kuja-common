package com.wingsweaver.kuja.common.utils.support.tostring;

import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * {@link ToStringWithConverter} 的实现类的基类。
 *
 * @param <A> 附加注解的类型
 * @author wingsweaver
 */
@Getter
public abstract class AbstractToStringWithConverter<A extends Annotation> implements ToStringWithConverter {
    private final Class<A> annotationClass;

    /**
     * 构造方法。
     *
     * @param annotationClass 附加注解的类型
     */
    protected AbstractToStringWithConverter(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public boolean toString(Object object, AnnotatedElement annotatedElement, StringBuilder builder, ToStringConfig config) {
        A annotation = this.resolveAnnotation(annotatedElement);
        if (annotation == null) {
            return false;
        }
        this.toStringInternal(object, annotatedElement, annotation, builder, config);
        return true;
    }

    /**
     * 实现类需要实现的方法，用于将注解的值转换为字符串。
     *
     * @param annotation       附加注解
     * @param annotatedElement 指定注解
     * @param object           指定实例
     * @param config           转换设置
     * @param builder          用于存储转换结果的 StringBuilder 实例
     */
    protected abstract void toStringInternal(Object object, AnnotatedElement annotatedElement, A annotation,
                                             StringBuilder builder, ToStringConfig config);

    /**
     * 从指定的注解元素中解析出附加注解。
     *
     * @param annotatedElement 指定注解元素
     * @return 附加注解
     */
    protected A resolveAnnotation(AnnotatedElement annotatedElement) {
        return AnnotationUtils.findAnnotation(annotatedElement, this.getAnnotationClass());
    }
}
