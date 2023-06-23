package com.wingsweaver.kuja.common.boot.warmup;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 预热处理相关事件。
 *
 * @author wingsweaver
 */
public class WarmUpEvent extends ApplicationEvent {
    @Getter
    private final WarmUpExecution execution;

    public WarmUpEvent(Object source, WarmUpExecution execution) {
        super(source);
        this.execution = execution;
    }
}
