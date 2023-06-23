package com.wingsweaver.kuja.common.utils.exception;

/**
 * 是否导出的检查器接口。
 *
 * @author wingsweaver
 */
public interface ErrorExportPredicate {
    /**
     * 检查是否导出指定数据。
     *
     * @param key 数据名称
     * @return 是否导出
     */
    boolean includes(String key);
}
