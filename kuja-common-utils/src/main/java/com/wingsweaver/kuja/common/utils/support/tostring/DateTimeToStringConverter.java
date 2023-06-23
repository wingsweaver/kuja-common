package com.wingsweaver.kuja.common.utils.support.tostring;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

/**
 * 默认的 {@link ToStringConverter} 实现类。
 *
 * @author wingsweaver
 */
final class DateTimeToStringConverter implements ToStringConverter {
    static final DateTimeToStringConverter INSTANCE = new DateTimeToStringConverter();

    private DateTimeToStringConverter() {
        // 禁止外部生成实例
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config) {
        // 检查参数
        if (object == null) {
            return false;
        }

        boolean handled = true;
        Class<?> clazz = object.getClass();

        if (Date.class.isAssignableFrom(clazz)) {
            this.date2String(config, builder, (Date) object);
        } else if (Calendar.class.isAssignableFrom(clazz)) {
            this.calendar2String(config, builder, (Calendar) object);
        } else if (Temporal.class.isAssignableFrom(clazz)) {
            this.temporal2String(config, builder, (Temporal) object);
        } else {
            handled = false;
        }

        // 返回
        return handled;
    }

    private void date2String(ToStringConfig config, StringBuilder builder, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar2String(config, builder, calendar);
    }

    private void calendar2String(ToStringConfig config, StringBuilder builder, Calendar calendar) {
        String format = config.getDateTimeFormat();
        if (calendar.get(Calendar.HOUR) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
            format = config.getDateFormat();
        }
        builder.append(DateFormatUtils.format(calendar, format, LocaleContextHolder.getLocale()));
    }

    private void temporal2String(ToStringConfig config, StringBuilder builder, Temporal temporal) {
        // 计算格式设置
        String format = config.getDateTimeFormat();
        Class<?> clazz = temporal.getClass();
        if (Instant.class.isAssignableFrom(clazz) || ChronoLocalDateTime.class.isAssignableFrom(clazz)
                || ChronoZonedDateTime.class.isAssignableFrom(clazz)) {
            format = config.getDateTimeFormat();
        } else if (ChronoLocalDate.class.isAssignableFrom(clazz) || YearMonth.class.isAssignableFrom(clazz)) {
            format = config.getDateFormat();
        } else if (LocalTime.class.isAssignableFrom(clazz)) {
            format = config.getTimeFormat();
        }

        // 格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, LocaleContextHolder.getLocale())
                .withZone(ZoneOffset.systemDefault());
        formatter.formatTo(temporal, builder);
    }
}
