package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认的 {@link ToStringConverter} 实现类。
 *
 * @author wingsweaver
 */
final class ReflectToStringConverter extends AbstractToStringConverter {
    static final ReflectToStringConverter INSTANCE = new ReflectToStringConverter();

    private ReflectToStringConverter() {
        // 禁止外部生成实例
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config) {
        // 检查参数
        if (object == null) {
            return false;
        }

        Class<?> clazz = object.getClass();
        if (shouldDiveInto(clazz)) {
            this.object2String(object, builder, config, clazz);
            return true;
        }

        return false;
    }

    private void object2String(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz) {
        // 检查是否有 ToStringIgnored 的注解
        if (isIgnored(clazz)) {
            return;
        }

        // 检查是否有 ToStringWith 的注解
        ToStringWithConverter toStringWithConverter = resolveToStringWith(clazz);
        if (toStringWithConverter != null) {
            toStringWithConverter.toString(object, clazz, builder, config);
        } else {
            this.reflect2String(object, builder, config, clazz);
        }
    }

    private void reflect2String(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz) {
        // 加入类名
        if (config.isIncludeTypeName()) {
            builder.append(clazz.getName());
        }
        builder.append(TOKEN_OBJECT_START);

        // 使用反射处理 Field 和 Method
        ReflectContext context = new ReflectContext();
        InnerHelper innerHelper = new InnerHelper(config);
        ToStringConfig subConfig = subConfig(config);
        if (innerHelper.publicOnly) {
            // 只处理 public 类型的 Field 和 Method
            this.reflect2StringPublicOnly(object, builder, config, clazz, innerHelper, context, subConfig);
        } else {
            // 还要处理非 public 类型的 Field 和 Method
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                // 处理本类型
                this.reflect2StringAll(object, builder, config, currentClass, innerHelper, context, subConfig);

                // 处理父级类型
                Class<?> superClass = currentClass.getSuperclass();
                currentClass = shouldDiveInto(superClass) ? superClass : null;
            }
        }
        builder.append('}');
    }

    private void reflect2StringPublicOnly(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz,
                                          InnerHelper innerHelper, ReflectContext context, ToStringConfig subConfig) {
        // 优先处理 Methods
        if (config.isReflectMethods()) {
            Method[] methods = clazz.getMethods();
            this.reflectMethods(object, builder, config, clazz, innerHelper, context, methods, subConfig);
        }

        // 然后处理 Fields
        if (config.isReflectFields()) {
            Field[] fields = clazz.getFields();
            this.reflectFields(object, builder, config, clazz, innerHelper, context, fields, subConfig);
        }
    }

    private void reflect2StringAll(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz,
                                   InnerHelper innerHelper, ReflectContext context, ToStringConfig subConfig) {
        // 优先处理 Methods
        if (config.isReflectMethods()) {
            Method[] methods = clazz.getDeclaredMethods();
            this.reflectMethods(object, builder, config, clazz, innerHelper, context, methods, subConfig);
        }

        // 然后处理 Fields
        if (config.isReflectFields()) {
            Field[] fields = clazz.getDeclaredFields();
            this.reflectFields(object, builder, config, clazz, innerHelper, context, fields, subConfig);
        }
    }

    private void reflectMethods(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz,
                                InnerHelper innerHelper, ReflectContext context, Method[] methods, ToStringConfig subConfig) {
        // 处理本类型中定义的字段
        for (Method method : methods) {
            // 检查字段是否需要输出
            if (method.getParameterCount() != 0 || innerHelper.excludes(method)
                    || ToStringTypeUtil.isInternalType(method.getDeclaringClass())) {
                continue;
            }

            // 检查字段是否被处理过
            String fieldName = ReflectUtil.resolveFieldNameStrict(method, innerHelper.publicOnly);
            if (StringUtils.isEmpty(fieldName)) {
                continue;
            }
            if (context.getFieldsHandled(true).contains(fieldName)) {
                continue;
            }

            // 处理本字段
            try {
                ReflectionUtils.makeAccessible(method);
                Object methodValue = method.invoke(object);
                this.appendFieldMethodValue(builder, config, context, fieldName, methodValue, method, subConfig);
            } catch (Exception ex) {
                // 忽略此错误
            }
        }
    }

    private void reflectFields(Object object, StringBuilder builder, ToStringConfig config, Class<?> clazz,
                               InnerHelper innerHelper, ReflectContext context, Field[] fields, ToStringConfig subConfig) {
        // 处理本类型中定义的字段
        for (Field field : fields) {
            // 检查字段是否需要输出
            if (innerHelper.excludes(field)) {
                continue;
            }

            // 检查字段是否被处理过
            String fieldName = field.getName();
            if (context.getFieldsHandled(true).contains(fieldName)) {
                continue;
            }

            // 处理本字段
            try {
                Object fieldValue = field.get(object);
                this.appendFieldMethodValue(builder, config, context, fieldName, fieldValue, field, subConfig);
            } catch (Exception ex) {
                // 忽略此错误
            }
        }
    }

    private void appendFieldMethodValue(StringBuilder builder, ToStringConfig config, ReflectContext context,
                                        String fieldName, Object fieldMethodValue, AnnotatedElement fieldMethod,
                                        ToStringConfig subConfig) {

        // 加入分隔符
        if (!context.getFieldsHandled(true).isEmpty()) {
            builder.append(TOKEN_DELIMITER);
        }

        // 写入数据
        context.getFieldsHandled(true).add(fieldName);
        builder.append(fieldName);
        builder.append(TOKEN_VALUE);
        ToStringWithConverter toStringWithConverter = resolveToStringWith(fieldMethod);
        if (toStringWithConverter != null) {
            toStringWithConverter.toString(fieldMethodValue, fieldMethod, builder, config);
        } else {
            appendObject(builder, fieldMethodValue, config, subConfig);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean shouldDiveInto(Class<?> clazz) {
        if (clazz == null || ToStringTypeUtil.isInternalType(clazz)) {
            return false;
        }

        return true;
    }

    /**
     * 转换处理的上下文。
     */
    protected static class ReflectContext {
        /**
         * 已经处理过的字段。
         */
        private Set<String> fieldsHandled;

        public Set<String> getFieldsHandled(boolean createIfAbsent) {
            if (this.fieldsHandled == null && createIfAbsent) {
                this.fieldsHandled = new HashSet<>(BufferSizes.SMALL);
            }
            return fieldsHandled;
        }
    }
}
