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
     * @param config ToStringConfig 实例
     */
    void customize(ToStringConfig config);
}
