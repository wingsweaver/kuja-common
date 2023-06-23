package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * Tag 值存储为字符串的接口定义。
 *
 * @author wingsweaver
 */
public interface TagValueWriter extends DefaultOrdered {
    /**
     * 将指定数据转换成字符串。
     *
     * @param value 数据
     * @return 字符串
     */
    Tuple2<Boolean, String> saveAsText(Object value);
}
