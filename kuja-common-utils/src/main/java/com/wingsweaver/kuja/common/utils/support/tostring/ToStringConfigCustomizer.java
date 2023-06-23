package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * ToStringConfig 的定制处理的接口定义。
 *
 * @author wingsweaver
 */
public interface ToStringConfigCustomizer extends DefaultOrdered {
    /**
     * 定制 ToString 转换设置。
     *
     * @param builder ToString 转换设置的生成器
     */
    void customize(ToStringConfig.Builder builder);
}
