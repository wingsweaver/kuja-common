package com.wingsweaver.kuja.common.boot.warmup;

/**
 * 预热处理开始的事件。
 *
 * @author wingsweaver
 */
public class WarmUpStartEvent extends WarmUpEvent {
    public WarmUpStartEvent(Object source, WarmUpExecution record) {
        super(source, record);
    }
}
