package com.wingsweaver.kuja.common.utils.support;

import org.springframework.core.Ordered;

/**
 * 带默认值的 {@link Ordered} 接口。
 *
 * @author wingsweaver
 */
public interface DefaultOrdered extends Ordered {
    /**
     * 获取任务的优先级。
     *
     * @return 任务的优先级
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
