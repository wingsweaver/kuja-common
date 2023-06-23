package com.wingsweaver.kuja.common.utils.model.tags.convert;

import java.util.Collection;

/**
 * {@link TagConversionService} 的配置器。
 *
 * @author wingsweaver
 */
public interface TagConversionServiceConfigurer {
    /**
     * 配置 {@link TagConversionService}。
     *
     * @param converters TagConversionService 列表
     */
    void registerConverters(Collection<TagValueConverter> converters);

    /**
     * 配置 {@link TagValueWriter}。
     *
     * @param writers TagValueWriter 列表
     */
    void registerWriters(Collection<TagValueWriter> writers);
}
