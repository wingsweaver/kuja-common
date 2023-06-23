package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.wingsweaver.kuja.common.utils.support.lang.ThrowableRunnable;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * 用于包装函数以使用指定日志上下文的工具类。
 *
 * @author wingsweaver
 */
public final class LogContextMethodWrapper {
    private LogContextMethodWrapper() {
        // 禁止实例化
    }

    /**
     * 包装一个 {@link Runnable} 对象，使其在执行时使用指定的日志上下文。
     *
     * @param runnable 原始的 {@link Runnable} 对象。
     * @param config   使用的日志上下文。
     * @return 包装后的 {@link Runnable} 对象。
     */
    public static Runnable runnable(Runnable runnable, LogContext.Config config) {
        return new RunnableWrapped(config, runnable);
    }

    /**
     * 包装一个 {@link Runnable} 对象，使其在执行时使用当前的日志上下文。
     *
     * @param runnable 原始的 {@link Runnable} 对象。
     * @return 包装后的 {@link Runnable} 对象。
     */
    public static Runnable runnable(Runnable runnable) {
        return runnable(runnable, LogContext.getConfig(false));
    }

    /**
     * 包装一个 {@link ThrowableRunnable} 对象，使其在执行时使用指定的日志上下文。
     *
     * @param runnable 原始的 {@link ThrowableRunnable} 对象。
     * @param config   使用的日志上下文。
     * @param <E>      异常类型
     * @return 包装后的 {@link ThrowableRunnable} 对象。
     */
    public static <E extends Throwable> ThrowableRunnable<E> throwableRunnable(ThrowableRunnable<E> runnable, LogContext.Config config) {
        return new ThrowableRunnableWrapped<>(config, runnable);
    }

    /**
     * 包装一个 {@link ThrowableRunnable} 对象，使其在执行时使用当前的日志上下文。
     *
     * @param runnable 原始的 {@link ThrowableRunnable} 对象。
     * @param <E>      异常类型
     * @return 包装后的 {@link ThrowableRunnable} 对象。
     */
    public static <E extends Throwable> ThrowableRunnable<E> throwableRunnable(ThrowableRunnable<E> runnable) {
        return throwableRunnable(runnable, LogContext.getConfig());
    }

    /**
     * 包装一个 {@link Callable} 对象，使其在执行时使用指定的日志上下文。
     *
     * @param callable 原始的 {@link Callable} 对象。
     * @param config   使用的日志上下文。
     * @param <V>      返回值类型
     * @return 包装后的 {@link Callable} 对象。
     */
    public static <V> Callable<V> callable(Callable<V> callable, LogContext.Config config) {
        return new CallableWrapped<>(config, callable);
    }

    /**
     * 包装一个 {@link Callable} 对象，使其在执行时使用当前的日志上下文。
     *
     * @param callable 原始的 {@link Callable} 对象。
     * @param <V>      返回值类型
     * @return 包装后的 {@link Callable} 对象。
     */
    public static <V> Callable<V> callable(Callable<V> callable) {
        return callable(callable, LogContext.getConfig());
    }

    /**
     * 包装一个 {@link Supplier} 对象，使其在执行时使用指定的日志上下文。
     *
     * @param supplier 原始的 {@link Supplier} 对象。
     * @param config   使用的日志上下文。
     * @param <T>      返回值类型
     * @return 包装后的 {@link Supplier} 对象。
     */
    public static <T> Supplier<T> supplier(Supplier<T> supplier, LogContext.Config config) {
        return new SupplierWrapped<>(config, supplier);
    }

    /**
     * 包装一个 {@link Supplier} 对象，使其在执行时使用当前的日志上下文。
     *
     * @param supplier 原始的 {@link Supplier} 对象。
     * @param <T>      返回值类型
     * @return 包装后的 {@link Supplier} 对象。
     */
    public static <T> Supplier<T> supplier(Supplier<T> supplier) {
        return supplier(supplier, LogContext.getConfig());
    }

    /**
     * 包装实现的基类。
     *
     * @param <T> 原始对象的类型
     */
    @Getter
    public abstract static class AbstractWrapped<T> {
        /**
         * 使用的日志上下文。
         */
        private final LogContext.Config config;

        /**
         * 原始的对象。
         */
        private final T source;

        AbstractWrapped(LogContext.Config config, T source) {
            this.config = config;
            this.source = source;
        }
    }

    /**
     * 包装 {@link Runnable} 对象的实现。
     */
    static class RunnableWrapped extends AbstractWrapped<Runnable> implements Runnable {
        RunnableWrapped(LogContext.Config config, Runnable source) {
            super(config, source);
        }

        @Override
        public void run() {
            try (LogContext.TempHolder ignored = LogContext.with(this.getConfig())) {
                this.getSource().run();
            }
        }
    }

    /**
     * 包装 {@link Callable} 对象的实现。
     *
     * @param <V> 返回值类型
     */
    static class CallableWrapped<V> extends AbstractWrapped<Callable<V>> implements Callable<V> {
        CallableWrapped(LogContext.Config config, Callable<V> source) {
            super(config, source);
        }

        @Override
        public V call() throws Exception {
            try (LogContext.TempHolder ignored = LogContext.with(this.getConfig())) {
                return this.getSource().call();
            }
        }
    }

    /**
     * 包装 {@link ThrowableRunnable} 对象的实现。
     *
     * @param <E> 异常类型
     */
    static class ThrowableRunnableWrapped<E extends Throwable> extends AbstractWrapped<ThrowableRunnable<E>> implements ThrowableRunnable<E> {
        ThrowableRunnableWrapped(LogContext.Config config, ThrowableRunnable<E> source) {
            super(config, source);
        }

        @Override
        public void run() throws E {
            try (LogContext.TempHolder ignored = LogContext.with(this.getConfig())) {
                this.getSource().run();
            }
        }
    }

    /**
     * 包装 {@link Supplier} 对象的实现。
     *
     * @param <T> 返回值类型
     */
    static class SupplierWrapped<T> extends AbstractWrapped<Supplier<T>> implements Supplier<T> {
        SupplierWrapped(LogContext.Config config, Supplier<T> source) {
            super(config, source);
        }

        @Override
        public T get() {
            try (LogContext.TempHolder ignored = LogContext.with(this.getConfig())) {
                return this.getSource().get();
            }
        }
    }
}
