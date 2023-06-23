package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.wingsweaver.kuja.common.utils.support.lang.StringFormatter;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 日志记录的区域。
 *
 * @author wingsweaver
 */
@AllArgsConstructor
@Getter
public class LogSection {
    /**
     * 无效的返回结果。
     */
    @Getter(value = AccessLevel.NONE)
    private static final Object DUMMY_RETURN_VALUE = new Object() {
        @Override
        public String toString() {
            return "[no-return-value]";
        }
    };

    /**
     * 日志记录器。
     */
    private final Logger logger;

    /**
     * 日志标记。
     */
    private final Marker marker;

    /**
     * 日志级别。
     */
    private final Level level;

    /**
     * 日志区域名称的提供者。
     */
    private final Supplier<String> sectionNameSupplier;

    /**
     * 日志进入消息的提供者。
     */
    private final Supplier<String> enterMessageSupplier;

    /**
     * 日志离开消息的提供者。
     */
    private final Function<Object, String> leaveMessageSupplier;

    /**
     * 日志离开消息的提供者。
     */
    private final Function<Throwable, String> failMessageSupplier;

    /**
     * 是否进入处理。
     */
    private final AtomicBoolean entered = new AtomicBoolean(false);

    /**
     * 是否离开处理。
     */
    private final AtomicBoolean leaved = new AtomicBoolean(false);

    /**
     * 进入处理。
     */
    public void enter() {
        if (this.entered.compareAndSet(false, true)) {
            LogUtil.log(this.getLogger(), this.getMarker(), this.getLevel(), this.getEnterMessageSupplier());
        }
    }

    /**
     * 离开处理。
     */
    public void leave() {
        this.leave(DUMMY_RETURN_VALUE);
    }

    /**
     * 离开处理。
     *
     * @param returnValue 返回值
     */
    public void leave(Object returnValue) {
        if (this.leaved.compareAndSet(false, true)) {
            LogUtil.log(this.getLogger(), this.getMarker(), this.getLevel(),
                    () -> this.getLeaveMessageSupplier().apply(returnValue));
        }
    }

    /**
     * 失败处理。
     *
     * @param throwable 异常
     */
    public void fail(Throwable throwable) {
        if (this.leaved.compareAndSet(false, true)) {
            LogUtil.log(this.getLogger(), this.getMarker(), this.getLevel(), throwable,
                    () -> {
                        String message = this.getFailMessageSupplier() != null ? this.getFailMessageSupplier().apply(throwable) : null;
                        return StringUtil.notEmptyOrElse(message, throwable.getMessage());
                    });
        }
    }

    /**
     * 记录失败，并且继续抛出异常。
     *
     * @param throwable 异常
     * @param <T>       异常类型
     * @throws T 继续抛出异常
     */
    public <T extends Throwable> void rethrow(T throwable) throws T {
        fail(throwable);
        throw throwable;
    }

    /**
     * 记录失败，并且继续抛出异常。
     *
     * @param throwable          原始异常
     * @param throwableConverter 异常转换器
     * @param <T>                原始异常的类型
     * @param <R>                转换后的异常的类型
     * @throws R 继续抛出异常
     */
    public <T extends Throwable, R extends Throwable> void rethrow(T throwable, Function<T, R> throwableConverter) throws R {
        fail(throwable);
        throw throwableConverter.apply(throwable);
    }

    /**
     * 记录失败，并且继续抛出 {@link RuntimeException} 类型的异常。
     *
     * @param throwable 原始异常
     * @param <T>       原始异常的类型
     */
    public <T extends Throwable> void rethrowSneaky(T throwable) {
        fail(throwable);

        // 继续抛出异常
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else {
            throw new RuntimeException(throwable);
        }
    }

    public void log(Marker marker, Level level, String format, Object... args) {
        this.log(marker, level, () -> StringFormatter.format(format, args));
    }

    public void log(Marker marker, Level level, Supplier<String> messageSupplier) {
        LogUtil.log(this.getLogger(), marker, level, () -> StringFormatter.format("{} : {}",
                this.sectionNameSupplier.get(), messageSupplier.get()));
    }

    public void log(Level level, String format, Object... args) {
        this.log(null, level, () -> StringFormatter.format(format, args));
    }

    public void log(Level level, Supplier<String> messageSupplier) {
        this.log(null, level, messageSupplier);
    }

    public void log(Marker marker, String format, Object... args) {
        this.log(marker, this.getLevel(), () -> StringFormatter.format(format, args));
    }

    public void log(Marker marker, Supplier<String> messageSupplier) {
        this.log(marker, this.getLevel(), messageSupplier);
    }

    public void log(String format, Object... args) {
        this.log(null, this.getLevel(), () -> StringFormatter.format(format, args));
    }

    public void log(Supplier<String> messageSupplier) {
        this.log(null, this.getLevel(), messageSupplier);
    }

    /**
     * 生成 {@link Builder} 实例。
     *
     * @param logger 日志记录器
     * @return Builder 实例
     */
    public static Builder builder(Logger logger) {
        return new Builder(logger);
    }

    /**
     * {@link LogSection} 的构建器。
     */
    public static final class Builder {
        /**
         * 日志记录器。
         */
        private final Logger logger;

        /**
         * 日志标记。
         */
        private Marker marker;

        /**
         * 日志级别。
         */
        private Level level = Level.TRACE;

        /**
         * 日志区域名称的提供者。
         */
        private Supplier<String> sectionNameSupplier;

        /**
         * 日志进入消息的提供者。
         */
        private Supplier<String> enterMessageSupplier;

        /**
         * 日志离开消息的提供者。
         */
        private Function<Object, String> leaveMessageSupplier;

        /**
         * 日志离开消息的提供者。
         */
        private Function<Throwable, String> failMessageSupplier;

        Builder(Logger logger) {
            this.logger = logger;
        }

        /**
         * 生成 {@link LogSection} 实例。
         *
         * @return LogSection 实例
         */
        public LogSection build() {
            // 补全必要的数据
            if (this.sectionNameSupplier == null) {
                StackTraceElement caller = deduceCallerStackTraceElement();
                if (caller != null) {
                    sectionName(caller.getClassName() + "#" + caller.getMethodName());
                }
            }

            // 生成 LogSection 实例
            return new LogSection(this.logger, this.marker, this.level,
                    this.sectionNameSupplier, this.enterMessageSupplier,
                    this.leaveMessageSupplier, this.failMessageSupplier);
        }

        /**
         * 推算调用者的堆栈。
         *
         * @return 调用者的堆栈
         */
        private StackTraceElement deduceCallerStackTraceElement() {
            String logSectionClassName = LogSection.class.getName();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            boolean hasLogSection = false;
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement.getClassName().startsWith(logSectionClassName)) {
                    hasLogSection = true;
                } else if (hasLogSection) {
                    return stackTraceElement;
                }
            }

            // 返回调用堆栈顶部元素（正常情况下不应该出现这种情况）
            return stackTraceElements[0];
        }

        /**
         * 设置日志标记。
         *
         * @param marker 日志标记
         * @return Builder 实例
         */
        public Builder marker(Marker marker) {
            this.marker = marker;
            return this;
        }

        /**
         * 设置日志级别。
         *
         * @param level 日志级别
         * @return Builder 实例
         */
        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        /**
         * 设置日志进入消息的提供者。
         *
         * @param enterMessageSupplier 日志进入消息的提供者
         * @return Builder 实例
         */
        @SuppressWarnings("UnusedReturnValue")
        public Builder enterMessageSupplier(Supplier<String> enterMessageSupplier) {
            this.enterMessageSupplier = enterMessageSupplier;
            return this;
        }

        /**
         * 设置日志离开消息的提供者。
         *
         * @param leaveMessageSupplier 日志离开消息的提供者
         * @return Builder 实例
         */
        @SuppressWarnings("UnusedReturnValue")
        public Builder leaveMessageSupplier(Function<Object, String> leaveMessageSupplier) {
            this.leaveMessageSupplier = leaveMessageSupplier;
            return this;
        }

        /**
         * 设置日志失败消息的提供者。
         *
         * @param failMessageSupplier 日志失败消息的提供者
         * @return Builder 实例
         */
        @SuppressWarnings("UnusedReturnValue")
        public Builder failMessageSupplier(Function<Throwable, String> failMessageSupplier) {
            this.failMessageSupplier = failMessageSupplier;
            return this;
        }

        /**
         * 通过日志区域的名称，批量设置 {@link #enterMessageSupplier}, {@link #leaveMessageSupplier} 和
         * {@link #failMessageSupplier} 。
         *
         * @param format 日志区域的名称格式
         * @param args   日志区域的名称格式参数
         * @return Builder 实例
         */
        public Builder sectionName(String format, Object... args) {
            return this.sectionName(() -> StringFormatter.format(format, args));
        }

        /**
         * 通过日志区域的名称，批量设置 {@link #enterMessageSupplier}, {@link #leaveMessageSupplier} 和
         * {@link #failMessageSupplier} 。
         *
         * @param supplier 日志区域的名称的计算函数
         * @return Builder 实例
         */
        public Builder sectionName(Supplier<String> supplier) {
            this.sectionNameSupplier = supplier;
            this.enterMessageSupplier(() -> StringFormatter.format("{} : started ...", supplier.get()));
            this.leaveMessageSupplier((returnValue) -> {
                if (returnValue == DUMMY_RETURN_VALUE) {
                    return StringFormatter.format("{} : completed", supplier.get());
                } else {
                    return StringFormatter.format("{} : completed, return value: {}",
                            supplier.get(), returnValue);
                }
            });
            this.failMessageSupplier((throwable) -> StringFormatter.format("{} : failed", supplier.get()));
            return this;
        }
    }
}
