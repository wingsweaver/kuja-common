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
     * 加入指定实例的字符串转换结果。
     *
     * @param builder   字符串构建器
     * @param object    目标实例
     * @param config    当前的配置
     * @param subConfig 下一级配置
     */
    protected static void appendObject(StringBuilder builder, Object object,
                                       ToStringConfig config, ToStringConfig subConfig) {
        if (config.canReflect()) {
            // 如果还可以继续反射的话，那么继续反射
            builder.append(ToStringBuilder.toString(object, subConfig));
        } else {
            // 否则不深入分析，直接使用 toString() 方法
            builder.append(object);
        }
    }

    /**
     * 生成下一级处理用的 {@link ToStringConfig} 实例。<br>
     * 将 {@link ToStringConfig#getMaxReflectDepth()} 减 1。
     *
     * @param config 当前的配置
     * @return 下一级处理用的 {@link ToStringConfig} 实例
     */
    protected static ToStringConfig subConfig(ToStringConfig config) {
        ToStringConfig subConfig = new ToStringConfig(config);
        subConfig.setMaxReflectDepth(config.getMaxReflectDepth() - 1);
        return subConfig;
    }

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
