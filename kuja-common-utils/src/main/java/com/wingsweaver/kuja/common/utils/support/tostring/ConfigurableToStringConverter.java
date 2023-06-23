package com.wingsweaver.kuja.common.utils.support.tostring;

/**
 * 将指定实例转换成字符串的转换器的接口定义。
 *
 * @param <C> 自定义的转换设置类型
 * @author wingsweaver
 */
public interface ConfigurableToStringConverter<C> {
    /**
     * 将指定实例转换成字符串。
     *
     * @param object       指定实例
     * @param builder      用于存储转换结果的 StringBuilder 实例
     * @param config       转换处理的上下文
     * @param customConfig 自定义的转换设置
     * @return 是否完成转换
     */
    boolean toString(Object object, StringBuilder builder, ToStringConfig config, C customConfig);
}
