package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.lang.ThrowableRunnable;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * 用于包装函数以使用指定业务上下文的工具类。
 *
 * @author wingsweaver
 */
public final class BusinessContextMethodWrapper {
    private BusinessContextMethodWrapper() {
        // 禁止实例化
    }

    /**
     * 包装一个 {@link Runnable} 对象，使其在执行时使用指定的业务上下文。
     *
     * @param runnable        原始的 {@link Runnable} 对象。
     * @param businessContext 使用的业务上下文。
     * @return 包装后的 {@link Runnable} 对象。
     */
    public static Runnable runnable(Runnable runnable, BusinessContext businessContext) {
        return new RunnableWrapped(businessContext, runnable);
    }

    /**
     * 包装一个 {@link Runnable} 对象，使其在执行时使用当前的业务上下文。
     *
     * @param runnable 原始的 {@link Runnable} 对象。
     * @return 包装后的 {@link Runnable} 对象。
     */
    public static Runnable runnable(Runnable runnable) {
        return runnable(runnable, BusinessContextHolder.getCurrent());
    }

    /**
     * 包装一个 {@link ThrowableRunnable} 对象，使其在执行时使用指定的业务上下文。
     *
     * @param runnable        原始的 {@link ThrowableRunnable} 对象。
     * @param businessContext 使用的业务上下文。
     * @param <E>             异常类型
     * @return 包装后的 {@link ThrowableRunnable} 对象。
     */
    public static <E extends Throwable> ThrowableRunnable<E> throwableRunnable(ThrowableRunnable<E> runnable, BusinessContext businessContext) {
        return new ThrowableRunnableWrapped<>(businessContext, runnable);
    }

    /**
     * 包装一个 {@link ThrowableRunnable} 对象，使其在执行时使用当前的业务上下文。
     *
     * @param runnable 原始的 {@link ThrowableRunnable} 对象。
     * @param <E>      异常类型
     * @return 包装后的 {@link ThrowableRunnable} 对象。
     */
    public static <E extends Throwable> ThrowableRunnable<E> throwableRunnable(ThrowableRunnable<E> runnable) {
        return throwableRunnable(runnable, BusinessContextHolder.getCurrent());
    }

    /**
     * 包装一个 {@link Callable} 对象，使其在执行时使用指定的业务上下文。
     *
     * @param callable        原始的 {@link Callable} 对象。
     * @param businessContext 使用的业务上下文。
     * @param <V>             返回值类型
     * @return 包装后的 {@link Callable} 对象。
     */
    public static <V> Callable<V> callable(Callable<V> callable, BusinessContext businessContext) {
        return new CallableWrapped<>(businessContext, callable);
    }

    /**
     * 包装一个 {@link Callable} 对象，使其在执行时使用当前的业务上下文。
     *
     * @param callable 原始的 {@link Callable} 对象。
     * @param <V>      返回值类型
     * @return 包装后的 {@link Callable} 对象。
     */
    public static <V> Callable<V> callable(Callable<V> callable) {
        return callable(callable, BusinessContextHolder.getCurrent());
    }

    /**
     * 包装一个 {@link Supplier} 对象，使其在执行时使用指定的业务上下文。
     *
     * @param supplier        原始的 {@link Supplier} 对象。
     * @param businessContext 使用的业务上下文。
     * @param <T>             返回值类型
     * @return 包装后的 {@link Supplier} 对象。
     */
    public static <T> Supplier<T> supplier(Supplier<T> supplier, BusinessContext businessContext) {
        return new SupplierWrapped<>(businessContext, supplier);
    }

    /**
     * 包装一个 {@link Supplier} 对象，使其在执行时使用当前的业务上下文。
     *
     * @param supplier 原始的 {@link Supplier} 对象。
     * @param <T>      返回值类型
     * @return 包装后的 {@link Supplier} 对象。
     */
    public static <T> Supplier<T> supplier(Supplier<T> supplier) {
        return supplier(supplier, BusinessContextHolder.getCurrent());
    }

    /**
     * 包装实现的基类。
     *
     * @param <T> 原始对象的类型
     */
    @Getter
    public abstract static class AbstractWrapped<T> {
        /**
         * 使用的业务上下文。
         */
        private final BusinessContext businessContext;

        /**
         * 原始的对象。
         */
        private final T source;

        AbstractWrapped(BusinessContext businessContext, T source) {
            this.businessContext = businessContext;
            this.source = source;
        }
    }

    /**
     * 包装 {@link Runnable} 对象的实现。
     */
    static class RunnableWrapped extends AbstractWrapped<Runnable> implements Runnable {
        RunnableWrapped(BusinessContext businessContext, Runnable source) {
            super(businessContext, source);
        }

        @Override
        public void run() {
            try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(this.getBusinessContext())) {
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
        CallableWrapped(BusinessContext businessContext, Callable<V> source) {
            super(businessContext, source);
        }

        @Override
        public V call() throws Exception {
            try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(this.getBusinessContext())) {
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
        ThrowableRunnableWrapped(BusinessContext businessContext, ThrowableRunnable<E> source) {
            super(businessContext, source);
        }

        @Override
        public void run() throws E {
            try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(this.getBusinessContext())) {
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
        SupplierWrapped(BusinessContext businessContext, Supplier<T> source) {
            super(businessContext, source);
        }

        @Override
        public T get() {
            try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(this.getBusinessContext())) {
                return this.getSource().get();
            }
        }
    }
}
