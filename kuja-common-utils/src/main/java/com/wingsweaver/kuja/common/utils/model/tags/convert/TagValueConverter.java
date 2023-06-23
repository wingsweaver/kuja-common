package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

import java.lang.reflect.Type;

/**
 * Tag 值转换器的接口定义。
 *
 * @author wingsweaver
 */
public interface TagValueConverter extends DefaultOrdered {
    /**
     * 未（无法）处理。
     */
    Tuple2<Boolean, Object> UNHANDLED = Tuple2.of(false, null);

    /**
     * 空值。
     */
    Tuple2<Boolean, Object> NULL_RESULT = Tuple2.of(true, null);

    /**
     * 将指定数据转换成可支持的类型。
     *
     * @param source 数据
     * @param type   类型
     * @return 转换后的数据
     */
    Tuple2<Boolean, Object> convertTo(Object source, Type type);
}
