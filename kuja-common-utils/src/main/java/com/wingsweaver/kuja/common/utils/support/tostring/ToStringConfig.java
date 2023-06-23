package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.model.attributes.AbstractAttributesBuilder;
import com.wingsweaver.kuja.common.utils.model.attributes.Attributes;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Map;

/**
 * 字符串转换处理的设置工具类。
 *
 * @author wingsweaver
 */
public interface ToStringConfig extends Attributes<Object> {
    String KEY_PUBLIC_ONLY = "default-only";

    /**
     * 检查是否只输出公共属性。
     *
     * @return 是否只输出公共属性
     */
    default boolean isPublicOnly() {
        return getAttribute(KEY_PUBLIC_ONLY, false);
    }

    String KEY_INCLUDE_TYPE_NAME = "include-type-name";

    /**
     * 检查是否输出类型名称。
     *
     * @return 是否输出类型名称
     */
    default boolean isIncludeTypeName() {
        return getAttribute(KEY_INCLUDE_TYPE_NAME, false);
    }

    String KEY_INCLUDE_TRANSIENT = "include-transieent";

    /**
     * 检查是否输出 transient 属性。
     *
     * @return 是否输出 transient 属性
     */
    default boolean isIncludeTransient() {
        return getAttribute(KEY_INCLUDE_TRANSIENT, false);
    }

    String KEY_REFLECT_FIELDS = "reflect-fields";

    /**
     * 检查是否输出字段。
     *
     * @return 是否输出字段
     */
    default boolean isReflectFields() {
        return getAttribute(KEY_REFLECT_FIELDS, true);
    }

    String KEY_REFLECT_METHODS = "reflect-methods";

    /**
     * 检查是否输出方法。
     *
     * @return 是否输出方法
     */
    default boolean isReflectMethods() {
        return getAttribute(KEY_REFLECT_METHODS, true);
    }

    String KEY_DATETIME_FORMAT = "datetime-format";

    String DEFAULT_DATETIME_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern();

    /**
     * 获取日期时间格式。
     *
     * @return 日期时间格式
     */
    default String getDateTimeFormat() {
        return getAttribute(KEY_DATETIME_FORMAT, DEFAULT_DATETIME_FORMAT);
    }

    String KEY_DATE_FORMAT = "date-format";

    String DEFAULT_DATE_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern();

    /**
     * 获取日期格式。
     *
     * @return 日期格式
     */
    default String getDateFormat() {
        return getAttribute(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
    }

    String KEY_TIME_FORMAT = "time-format";

    String DEFAULT_TIME_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern();

    /**
     * 获取时间格式。
     *
     * @return 时间格式
     */
    default String getTimeFormat() {
        return getAttribute(KEY_TIME_FORMAT, DEFAULT_TIME_FORMAT);
    }

    /**
     * 生成一个可供修改数据的 {@link Builder} 对象。
     *
     * @return {@link Builder} 对象
     */
    default Builder mutable() {
        return new Builder(this);
    }

    /**
     * 基于字典生成 {@link ToStringConfig} 实例。
     *
     * @param map 字典
     * @return {@link ToStringConfig} 实例
     */
    static ToStringConfig of(Map<?, ?> map) {
        return new DefaultToStringConfig(map);
    }

    /**
     * {@link ToStringConfig} 的生成工具类。
     *
     * @author wingsweaver
     */
    class Builder extends AbstractAttributesBuilder<Object, Builder> {
        @SuppressWarnings("unchecked")
        public Builder(Map<?, ?> map) {
            super((Map<Object, ?>) map);
        }

        public Builder(ToStringConfig config) {
            this(config.asMap());
        }

        public Builder setPublicOnly(boolean defaultOnly) {
            return update(KEY_PUBLIC_ONLY, defaultOnly);
        }

        public Builder setIncludeTypeName(boolean includeTypeName) {
            return update(KEY_INCLUDE_TYPE_NAME, includeTypeName);
        }

        public Builder setIncludeTransient(boolean includeTransient) {
            return update(KEY_INCLUDE_TRANSIENT, includeTransient);
        }

        public Builder setReflectFields(boolean reflectFields) {
            return update(KEY_REFLECT_FIELDS, reflectFields);
        }

        public Builder setReflectMethods(boolean reflectMethods) {
            return update(KEY_REFLECT_METHODS, reflectMethods);
        }

        public Builder setDateTimeFormat(String dateTimeFormat) {
            return update(KEY_DATETIME_FORMAT, dateTimeFormat);
        }

        public Builder setDateFormat(String dateFormat) {
            return update(KEY_DATE_FORMAT, dateFormat);
        }

        public Builder setTimeFormat(String timeFormat) {
            return update(KEY_TIME_FORMAT, timeFormat);
        }

        public ToStringConfig build() {
            return of(map);
        }
    }
}
