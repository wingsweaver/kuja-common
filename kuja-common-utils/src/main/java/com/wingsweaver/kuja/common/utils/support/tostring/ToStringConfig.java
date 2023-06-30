package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.model.attributes.Attributes;
import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Map;

/**
 * 字符串转换处理的设置工具类。
 *
 * @author wingsweaver
 */
public class ToStringConfig extends MapMutableAttributes<String> {
    public ToStringConfig(Map<String, ?> map) {
        super(map);
    }

    public ToStringConfig(Attributes<String> attributes) {
        super(attributes);
    }

    /**
     * Key: 是否只导出 public 属性和方法。
     */
    public static final String KEY_PUBLIC_ONLY = "public-only";

    /**
     * {@link #isPublicOnly()} 的默认值。
     */
    public static final boolean DEFAULT_PUBLIC_ONLY = true;

    /**
     * 检查是否只输出 public 属性和方法。
     *
     * @return 是否只输出 public 属性和方法。
     */
    public boolean isPublicOnly() {
        return getAttribute(KEY_PUBLIC_ONLY, DEFAULT_PUBLIC_ONLY);
    }

    /**
     * 设置是否只输出 public 属性和方法。
     *
     * @param publicOnly 是否只输出 public 属性和方法
     */
    public void setPublicOnly(Boolean publicOnly) {
        this.setAttribute(KEY_PUBLIC_ONLY, publicOnly);
    }

    /**
     * Key: 是否包含类型名称。
     */
    public static final String KEY_INCLUDE_TYPE_NAME = "include-type-name";

    /**
     * {@link #isIncludeTypeName()} 的默认值。
     */
    public static final boolean DEFAULT_INCLUDE_TYPE_NAME = false;

    /**
     * 检查是否包含类型名称。
     *
     * @return 是否包含类型名称
     */
    public boolean isIncludeTypeName() {
        return getAttribute(KEY_INCLUDE_TYPE_NAME, DEFAULT_INCLUDE_TYPE_NAME);
    }

    /**
     * 设置是否包含类型名称。
     *
     * @param includeTypeName 是否包含类型名称
     */
    public void setIncludeTypeName(Boolean includeTypeName) {
        this.setAttribute(KEY_INCLUDE_TYPE_NAME, includeTypeName);
    }

    /**
     * Key: 是否包含 transient 属性。
     */
    public static final String KEY_INCLUDE_TRANSIENT = "include-transient";

    /**
     * {@link #isIncludeTransient()} 的默认值。
     */
    public static final boolean DEFAULT_INCLUDE_TRANSIENT = false;

    /**
     * 检查是否包含 transient 属性。
     *
     * @return 是否包含 transient 属性
     */
    public boolean isIncludeTransient() {
        return getAttribute(KEY_INCLUDE_TRANSIENT, DEFAULT_INCLUDE_TRANSIENT);
    }

    /**
     * 设置是否包含 transient 属性。
     *
     * @param includeTransient 是否包含 transient 属性
     */
    public void setIncludeTransient(Boolean includeTransient) {
        this.setAttribute(KEY_INCLUDE_TRANSIENT, includeTransient);
    }

    /**
     * Key: 是否使用反射的方式获取属性。
     */
    public static final String KEY_REFLECT_FIELDS = "reflect-fields";

    /**
     * {@link #isReflectFields()} 的默认值。
     */
    public static final boolean DEFAULT_REFLECT_FIELDS = true;

    /**
     * 检查是否使用反射的方式获取属性。
     *
     * @return 是否使用反射的方式获取属性
     */
    public boolean isReflectFields() {
        return getAttribute(KEY_REFLECT_FIELDS, DEFAULT_REFLECT_FIELDS);
    }

    /**
     * 设置是否使用反射的方式获取属性。
     *
     * @param reflectFields 是否使用反射的方式获取属性
     */
    public void setReflectFields(Boolean reflectFields) {
        this.setAttribute(KEY_REFLECT_FIELDS, reflectFields);
    }

    /**
     * Key: 是否使用反射的方式获取方法。
     */
    public static final String KEY_REFLECT_METHODS = "reflect-methods";

    /**
     * {@link #isReflectMethods()} 的默认值。
     */
    public static final boolean DEFAULT_REFLECT_METHODS = true;

    /**
     * 检查是否使用反射的方式获取方法。
     *
     * @return 是否使用反射的方式获取方法
     */
    public boolean isReflectMethods() {
        return getAttribute(KEY_REFLECT_METHODS, DEFAULT_REFLECT_METHODS);
    }

    /**
     * 设置是否使用反射的方式获取方法。
     *
     * @param reflectMethods 是否使用反射的方式获取方法
     */
    public void setReflectMethods(Boolean reflectMethods) {
        this.setAttribute(KEY_REFLECT_METHODS, reflectMethods);
    }

    /**
     * 使用反射递归分析对象的最大深度。
     */
    public static final String KEY_MAX_REFLECT_DEPTH = "max-reflect-depth";

    /**
     * {@link #getMaxReflectDepth()} 的默认值。<br>
     * 默认只处理 1 级，也即是只对传入的参数进行反射处理。
     */
    public static final int DEFAULT_MAX_REFLECT_DEPTH = 1;

    /**
     * 获取使用反射递归分析对象的最大深度。
     *
     * @return 使用反射递归分析对象的最大深度
     */
    public int getMaxReflectDepth() {
        return getAttribute(KEY_MAX_REFLECT_DEPTH, DEFAULT_MAX_REFLECT_DEPTH);
    }

    /**
     * 设置使用反射递归分析对象的最大深度。
     *
     * @param maxReflectDepth 使用反射递归分析对象的最大深度
     */
    public void setMaxReflectDepth(int maxReflectDepth) {
        this.setAttribute(KEY_MAX_REFLECT_DEPTH, maxReflectDepth);
    }

    /**
     * 检查是否可以使用反射分析对象。
     *
     * @return 是否可以
     */
    public boolean canReflect() {
        return getMaxReflectDepth() > 0;
    }

    /**
     * Key: 日期时间类型的格式。
     */
    public static final String KEY_DATETIME_FORMAT = "datetime-format";

    /**
     * {@link #getDateTimeFormat()} 的默认值。
     */
    public static final String DEFAULT_DATETIME_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern();

    /**
     * 获取日期时间类型的格式。
     *
     * @return 日期时间类型的格式
     */
    public String getDateTimeFormat() {
        return getAttribute(KEY_DATETIME_FORMAT, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 设置日期时间类型的格式。
     *
     * @param datetimeFormat 日期时间类型的格式
     */
    public void setDateTimeFormat(String datetimeFormat) {
        this.setAttribute(KEY_DATETIME_FORMAT, datetimeFormat);
    }

    /**
     * Key: 日期类型的格式。
     */
    public static final String KEY_DATE_FORMAT = "date-format";

    /**
     * {@link #getDateFormat()} 的默认值。
     */
    public static final String DEFAULT_DATE_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern();

    /**
     * 获取日期类型的格式。
     *
     * @return 日期类型的格式
     */
    public String getDateFormat() {
        return getAttribute(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
    }

    /**
     * 设置日期类型的格式。
     *
     * @param dateFormat 日期类型的格式
     */
    public void setDateFormat(String dateFormat) {
        this.setAttribute(KEY_DATE_FORMAT, dateFormat);
    }

    /**
     * Key: 时间类型的格式。
     */
    public static final String KEY_TIME_FORMAT = "time-format";

    /**
     * {@link #getTimeFormat()} 的默认值。
     */
    public static final String DEFAULT_TIME_FORMAT = DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern();

    /**
     * 获取时间类型的格式。
     *
     * @return 时间类型的格式
     */
    public String getTimeFormat() {
        return getAttribute(KEY_TIME_FORMAT, DEFAULT_TIME_FORMAT);
    }

    /**
     * 设置时间类型的格式。
     *
     * @param timeFormat 时间类型的格式
     */
    public void setTimeFormat(String timeFormat) {
        this.setAttribute(KEY_TIME_FORMAT, timeFormat);
    }

    /**
     * Key: 数组、集合和字典中输出元素的最大个数。
     */
    public static final String KEY_MAX_ITEM_COUNT = "max-item-count";

    /**
     * {@link #getMaxItemCount()} 的默认值。
     */
    public static final int DEFAULT_MAX_ITEM_COUNT = 10;

    /**
     * 获取数组、集合和字典中输出元素的最大个数。
     *
     * @return 数组、集合和字典中输出元素的最大个数
     */
    public int getMaxItemCount() {
        return getAttribute(KEY_MAX_ITEM_COUNT, DEFAULT_MAX_ITEM_COUNT);
    }

    /**
     * 设置数组、集合和字典中输出元素的最大个数。
     *
     * @param maxItemCount 数组、集合和字典中输出元素的最大个数
     */
    public void setMaxItemCount(int maxItemCount) {
        this.setAttribute(KEY_MAX_ITEM_COUNT, maxItemCount);
    }

    /**
     * Key: 调用 {@link Object#toString()} 进而触发 {@link ToStringBuilder} 处理的实例。
     */
    public static final String KEY_OBJECT_TO_STRING = "object-to-string";

    public Object getObjectToString() {
        return getAttribute(KEY_OBJECT_TO_STRING);
    }

    public void setObjectToString(Object object) {
        this.setAttribute(KEY_OBJECT_TO_STRING, object);
    }
}
