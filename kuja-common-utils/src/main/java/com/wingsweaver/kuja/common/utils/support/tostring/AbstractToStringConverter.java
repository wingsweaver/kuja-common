package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ReflectUtil;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的 {@link ToStringConverter} 实现类。
 *
 * @author wingsweaver
 */
public abstract class AbstractToStringConverter implements ToStringConverter, ToStringConstants {
    private static final Map<Class<?>, ToStringWithConverter> TO_STRING_WITH_CONVERTER_MAP = new ConcurrentHashMap<>();

    /**
     * 解析指定的注解，生成对应的 ToStringConverter 实例。
     *
     * @param element 注解所在的元素
     * @return 对应的 ToStringConverter 实例。如果没有 ToStringWith 注解的话，返回 null。
     */
    public static ToStringWithConverter resolveToStringWith(AnnotatedElement element) {
        // 先查找直接定义的注解
        ToStringWith toStringWith = AnnotationUtils.getAnnotation(element, ToStringWith.class);
        if (toStringWith == null) {
            // 如果不存在的话，查找继承的注解
            toStringWith = AnnotationUtils.findAnnotation(element, ToStringWith.class);
        }
        if (toStringWith != null) {
            Class<? extends ToStringWithConverter>[] classes = toStringWith.value();
            if (classes != null && classes.length > 0) {
                Class<? extends ToStringWithConverter> clazz = classes[0];
                return TO_STRING_WITH_CONVERTER_MAP.computeIfAbsent(clazz, key -> {
                    try {
                        return ConstructorUtils.invokeConstructor(clazz);
                    } catch (Exception ex) {
                        throw new ExtendedRuntimeException("Failed to create ToStringWithConverter instance, clazz : " + clazz + " .", ex)
                                .withExtendedAttribute("clazz", clazz);
                    }
                });
            }
        }
        return null;
    }

    /**
     * 检查指定元素是否被忽略。<br>
     * 即是否含有 {@link ToStringIgnored} 注解。
     *
     * @param element 注解所在的元素
     * @return 是否被忽略
     */
    public static boolean isIgnored(AnnotatedElement element) {
        return AnnotationUtils.getAnnotation(element, ToStringIgnored.class) != null;
    }

    /**
     * 内部辅助工具类。
     */
    protected static class InnerHelper {
        final boolean publicOnly;

        final boolean includeTransient;

        /**
         * 构造函数。
         *
         * @param config 配置
         */
        public InnerHelper(ToStringConfig config) {
            this.publicOnly = ReflectUtil.CAN_REFLECT_NON_PUBLIC || config.isPublicOnly();
            this.includeTransient = config.isIncludeTransient();
        }

        /**
         * 检查是否排除指定 Field 或者 Method。
         *
         * @param member Field 或者 Method
         * @return 是否排除
         */
        public boolean excludes(Member member) {
            return !this.includes(member);
        }

        /**
         * 检查是否包含指定 Field 或者 Method。
         *
         * @param member Field 或者 Method
         * @return 是否包含
         */
        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean includes(Member member) {
            int modifiers = member.getModifiers();

            // 忽略 static 类型
            if (Modifier.isStatic(modifiers)) {
                return false;
            }

            // 检查是否只处理 public 类型
            if (this.publicOnly && !Modifier.isPublic(modifiers)) {
                return false;
            }

            // 检查是否包含 transient 类型
            if (Modifier.isTransient(modifiers) && !this.includeTransient) {
                return false;
            }

            // 检查是否有 ToStringIgnored 的注解
            if (member instanceof AnnotatedElement) {
                return !isIgnored((AnnotatedElement) member);
            } else {
                return true;
            }
        }
    }
}
