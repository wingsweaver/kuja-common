package com.wingsweaver.kuja.common.messaging.errorreporting.common;

import com.wingsweaver.kuja.common.boot.errorreporting.ErrorRecord;
import com.wingsweaver.kuja.common.boot.errorreporting.ErrorReporter;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSender;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * 使用消息队列报告错误的 {@link ErrorReporter} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageErrorReporter extends AbstractComponent implements ErrorReporter {
    /**
     * MessageSender 实例。
     */
    private MessageSender messageSender;

    @Override
    public void report(ErrorRecord record) {
        this.messageSender.send(record);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 messageSender
        this.initMessageSender();
    }

    /**
     * 初始化 messageSender。
     */
    protected void initMessageSender() {
        if (this.messageSender == null) {
            this.messageSender = this.getBean(MessageSender.class);
        }
    }
}
