package com.wingsweaver.kuja.common.boot.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

/**
 * {@link MessageSource} 的持有者工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageSourceHolder implements MessageSourceAware {
    /**
     * MessageSource 的实例。
     */
    private static MessageSource messageSource;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setMessageSource(MessageSource messageSource) {
        MessageSourceHolder.messageSource = messageSource;
    }
}
