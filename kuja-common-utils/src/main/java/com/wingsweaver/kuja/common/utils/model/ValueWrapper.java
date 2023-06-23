package com.wingsweaver.kuja.common.utils.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据的包装工具类。<br>
 * 用于在 lambda 等传递数据。
 *
 * @param <T> 数据类型
 * @author wignsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValueWrapper<T> {
    /**
     * 实际数据。
     */
    private T value;
}
