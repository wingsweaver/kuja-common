package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;

import java.lang.reflect.Type;

/**
 * {@link TagValueConverter} 实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractTagValueConverter implements TagValueConverter {
    @Override
    public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
        if (!support(source, type)) {
            return UNHANDLED;
        }
        return convertToInternal(source, type);
    }

    /**
     * 将指定数据转换成可支持的类型。
     *
     * @param source 数据
     * @param type   类型
     * @return 转换后的数据
     */
    protected abstract Tuple2<Boolean, Object> convertToInternal(Object source, Type type);

    /**
     * 检查是否可以将指定的数据转换成指定的类型。
     *
     * @param source 数据
     * @param type   类型
     * @return 是否可以转换
     */
    protected abstract boolean support(Object source, Type type);
}
