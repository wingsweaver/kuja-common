package com.wingsweaver.kuja.common.utils.support.json;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 基于 JSON 的数据包装器。<br>
 * 提供将 JSON 解析为原始类型、指定类型、列表、 Map 的方法。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class JsonObjectWrapper extends AbstractPojo {
    /**
     * JSON 字符串。
     */
    private final String json;

    /**
     * 原始类型。
     */
    private final Type type;

    /**
     * 类型名称。
     */
    private final String typeName;

    public JsonObjectWrapper(String json, Type type) {
        this.json = json;
        this.type = type;
        this.typeName = (type != null) ? type.getTypeName() : null;
    }

    public JsonObjectWrapper(String json, String typeName) {
        this.json = json;
        this.type = ClassUtil.forName(typeName);
        this.typeName = typeName;
    }

    public JsonObjectWrapper(String json) {
        this.json = json;
        this.type = null;
        this.typeName = null;
    }

    /**
     * 获取 {@link #type} 指定的原始类型的附加数据。<br>
     * 适用于接收方含有该类型的类的场景。
     *
     * @param <T> 附加数据的类型
     * @return 附加数据。指定的类型不存在时，返回 null。
     */
    @SuppressWarnings("unchecked")
    public <T> T original() {
        if (this.type == null) {
            return null;
        }
        return (T) JsonCodecUtil.ensureJsonCodec().parseObject(this.json, this.type);
    }

    /**
     * 以指定类型获取附加数据。
     *
     * @param type 类型
     * @param <T>  类型
     * @return 附加数据
     */
    @SuppressWarnings("unchecked")
    public <T> T asType(Type type) {
        AssertArgs.Named.notNull("type", type);
        return (T) JsonCodecUtil.ensureJsonCodec().parseObject(this.json, type);
    }

    /**
     * 以指定元素类型的列表的形式获取附加数据。
     *
     * @param itemType 元素的类型
     * @param <T>      元素的类型
     * @return 附加数据对应的列表
     */
    public <T> List<T> asList(Class<T> itemType) {
        AssertArgs.Named.notNull("itemType", itemType);
        return JsonCodecUtil.ensureJsonCodec().parseList(this.json, itemType);
    }

    /**
     * 以指定键值类型的 Map 的形式获取附加数据。
     *
     * @param keyType   键的类型
     * @param valueType 值的类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 附加数据对应的 Map
     */
    public <K, V> Map<K, V> asMap(Class<K> keyType, Class<V> valueType) {
        AssertArgs.Named.notNull("keyType", keyType);
        AssertArgs.Named.notNull("valueType", valueType);
        return JsonCodecUtil.ensureJsonCodec().parseMap(this.json, keyType, valueType);
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName() + "(type = " + this.typeName + ")";
    }
}
