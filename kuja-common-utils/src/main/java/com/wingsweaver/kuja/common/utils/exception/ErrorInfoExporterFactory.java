package com.wingsweaver.kuja.common.utils.exception;

/**
 * {@link ErrorInfoExporter} 工厂类的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorInfoExporterFactory {
    /**
     * 创建 {@link ErrorInfoExporter} 实例。
     *
     * @return {@link ErrorInfoExporter} 实例
     */
    ErrorInfoExporter create();
}
