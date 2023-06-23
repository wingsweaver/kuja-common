package com.wingsweaver.kuja.common.utils.model.tags.impl;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 返回 {@link List} 的 {@link AbstractTagKey} 实现类。
 *
 * @param <T> 元素类型
 * @author wingsweaver
 */
public abstract class AbstractArrayTagKey<T> extends AbstractTagKey<List<T>> {
    protected AbstractArrayTagKey(String key) {
        super(key);
    }

    /**
     * 获取元素类型。
     *
     * @return 元素类型
     */
    public abstract Type getItemType();

    /**
     * 计算列表的类型标识。
     *
     * @param itemTypeName 元素类型名称
     * @return 列表的类型标识
     */
    protected static String getListTypeCode(String itemTypeName) {
        return "[" + itemTypeName + "]";
    }

    /**
     * 计算列表的类型标识。
     *
     * @param itemType 元素类型
     * @return 列表的类型标识
     */
    protected static String getListTypeCode(Class<?> itemType) {
        return getListTypeCode(getTypeCode(itemType));
    }
}
