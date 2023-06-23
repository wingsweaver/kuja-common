package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

import java.util.Map;

/**
 * 错误信息导出器的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorInfoExporter extends DefaultOrdered {
    /**
     * 导出错误信息。
     *
     * @param error     错误信息
     * @param map       存储错误信息的字典
     * @param predicate 检查是否导出的处理
     */
    void exportErrorInfo(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate);
}
