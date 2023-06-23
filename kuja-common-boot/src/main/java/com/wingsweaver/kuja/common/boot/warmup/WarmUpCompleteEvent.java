package com.wingsweaver.kuja.common.boot.warmup;

/**
 * 预热处理完成的事件。
 *
 * @author wingsweaver
 */
public class WarmUpCompleteEvent extends WarmUpEvent {
    public WarmUpCompleteEvent(Object source, WarmUpExecution record) {
        super(source, record);
    }
}
