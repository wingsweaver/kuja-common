package com.wingsweaver.kuja.common.boot.i18n;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.StringFormatter;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

/**
 * 消息格式化工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageHelper extends AbstractComponent implements MessageSourceAware {
    private static final int MIN_CODE_LENGTH = 2;

    /**
     * 消息源。
     */
    private MessageSource messageSource;

    /**
     * 格式化消息。
     *
     * @param patternOrCode 消息格式或消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public Optional<String> format(String patternOrCode, Object... args) {
        return format(this.messageSource, LocaleContextHolder.getLocale(), patternOrCode, args);
    }

    /**
     * 格式化消息。
     *
     * @param locale        本地化信息
     * @param patternOrCode 消息格式或消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public Optional<String> format(Locale locale, String patternOrCode, Object... args) {
        return format(this.messageSource, locale, patternOrCode, args);
    }

    /**
     * 获取指定的国际化消息。
     *
     * @param code 消息编码
     * @param args 消息参数
     * @return 格式化后的消息
     */
    public Optional<String> getMessage(String code, Object... args) {
        return getMessage(this.messageSource, LocaleContextHolder.getLocale(), code, args);
    }

    /**
     * 获取指定的国际化消息。
     *
     * @param locale 本地化信息
     * @param code   消息编码
     * @param args   消息参数
     * @return 格式化后的消息
     */
    public Optional<String> getMessage(Locale locale, String code, Object... args) {
        return getMessage(this.messageSource, locale, code, args);
    }

    /**
     * 获取指定的国际化消息。
     *
     * @param messageSource MessageSource 实例
     * @param code          消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public static Optional<String> getMessage(MessageSource messageSource, String code, Object... args) {
        return getMessage(messageSource, LocaleContextHolder.getLocale(), code, args);
    }

    /**
     * 获取指定的国际化消息。
     *
     * @param messageSource MessageSource 实例
     * @param locale        本地化信息
     * @param code          消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public static Optional<String> getMessage(MessageSource messageSource, Locale locale, String code, Object... args) {
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }

        try {
            String message = messageSource.getMessage(code, args, locale);
            return Optional.of(message);
        } catch (NoSuchMessageException ex) {
            return Optional.empty();
        }
    }

    /**
     * 解析消息格式中的消息编码。
     *
     * @param patternOrCode 消息格式或消息编码。
     * @return 消息编码。如果有效的消息编码的话，返回 null。
     */
    public static String parseCode(String patternOrCode) {
        if (patternOrCode != null && patternOrCode.length() >= MIN_CODE_LENGTH
                && patternOrCode.charAt(0) == '{' && patternOrCode.charAt(patternOrCode.length() - 1) == '}') {
            return patternOrCode.substring(1, patternOrCode.length() - 1);
        }
        return null;
    }

    /**
     * 格式化消息。
     *
     * @param messageSource MessageSource 实例
     * @param patternOrCode 消息格式或消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public static Optional<String> format(MessageSource messageSource, String patternOrCode, Object... args) {
        return format(messageSource, LocaleContextHolder.getLocale(), patternOrCode, args);
    }

    /**
     * 格式化消息。
     *
     * @param messageSource MessageSource 实例
     * @param locale        本地化信息
     * @param patternOrCode 消息格式或消息编码
     * @param args          消息参数
     * @return 格式化后的消息
     */
    public static Optional<String> format(MessageSource messageSource, Locale locale, String patternOrCode, Object... args) {
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }

        String code = parseCode(patternOrCode);
        if (StringUtil.isEmpty(code)) {
            return Optional.of(StringFormatter.format(patternOrCode, args));
        } else {
            return getMessage(messageSource, locale, code, args);
        }
    }
}
