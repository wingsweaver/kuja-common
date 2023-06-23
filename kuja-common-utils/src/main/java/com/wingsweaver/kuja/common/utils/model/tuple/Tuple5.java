package com.wingsweaver.kuja.common.utils.model.tuple;

/**
 * 含有 5 个元素的 Tuple。
 *
 * @param <T1> 第 1 个元素的类型
 * @param <T2> 第 2 个元素的类型
 * @param <T3> 第 3 个元素的类型
 * @param <T4> 第 4 个元素的类型
 * @param <T5> 第 5 个元素的类型
 * @author wingsweaver
 */
@SuppressWarnings({"squid:S110", "squid:S2160"})
public class Tuple5<T1, T2, T3, T4, T5> extends Tuple4<T1, T2, T3, T4> {
    public static final int SIZE = 5;

    public static final int INDEX_T5 = 4;

    /**
     * 第 4 个元素。
     */
    protected final T5 t5;

    public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        this(SIZE, t1, t2, t3, t4, t5);
    }

    protected Tuple5(int size, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        super(size, t1, t2, t3, t4);
        this.t5 = t5;
    }

    public T5 getT5() {
        return this.t5;
    }

    @Override
    public Object get(int index) {
        this.checkIndex(index);
        switch (index) {
            case INDEX_T1:
                return this.t1;
            case INDEX_T2:
                return this.t2;
            case INDEX_T3:
                return this.t3;
            case INDEX_T4:
                return this.t4;
            default:
                return this.t5;
        }
    }

    @Override
    public Object[] toArray() {
        return new Object[]{this.t1, this.t2, this.t3, this.t4, this.t5};
    }

    /**
     * 生成 {@link Tuple5} 实例。
     *
     * @param t1   第 1 个元素
     * @param t2   第 2 个元素
     * @param t3   第 3 个元素
     * @param t4   第 4 个元素
     * @param t5   第 5 个元素
     * @param <T1> 第 1 个元素的类型
     * @param <T2> 第 2 个元素的类型
     * @param <T3> 第 3 个元素的类型
     * @param <T4> 第 4 个元素的类型
     * @param <T5> 第 5 个元素的类型
     * @return Tuple5 实例
     */
    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return new Tuple5<>(t1, t2, t3, t4, t5);
    }
}