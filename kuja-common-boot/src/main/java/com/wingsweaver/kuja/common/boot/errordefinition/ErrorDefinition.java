package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

import java.util.Map;

/**
 * 错误定义。
 *
 * @author wingsweaver
 */
public interface ErrorDefinition extends DefaultOrdered {
    /**
     * 获取错误编码。
     *
     * @return 错误编码
     */
    String getCode();

    /**
     * 获取错误消息（面向开发者）。
     *
     * @return 错误消息
     */
    String getMessage();

    /**
     * 获取用户提示（面向终端用户）。
     *
     * @return 用户提示
     */
    String getUserTip();

    /**
     * 获取附加数据。
     *
     * @return 附加数据
     */
    Map<String, Object> getTags();

    /**
     * 获取临时数据。
     *
     * @return 临时数据
     */
    Map<String, Object> getTemps();
}
