package com.wingsweaver.kuja.common.utils.model.tuple;

/**
 * 含有 4 个元素的 Tuple。
 *
 * @param <T1> 第 1 个元素的类型
 * @param <T2> 第 2 个元素的类型
 * @param <T3> 第 3 个元素的类型
 * @param <T4> 第 4 个元素的类型
 * @author wingsweaver
 */
@SuppressWarnings("squid:S2160")
public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
    public static final int SIZE = 4;

    public static final int INDEX_T4 = 3;

    /**
     * 第 4 个元素。
     */
    protected final T4 t4;

    @SuppressWarnings("unused")
    public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        this(SIZE, t1, t2, t3, t4);
    }

    protected Tuple4(int size, T1 t1, T2 t2, T3 t3, T4 t4) {
        super(size, t1, t2, t3);
        this.t4 = t4;
    }

    public T4 getT4() {
        return this.t4;
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
            default:
                return this.t4;
        }
    }

    @Override
    public Object[] toArray() {
        return new Object[]{this.t1, this.t2, this.t3, this.t4};
    }

    /**
     * 生成 {@link Tuple4} 实例。
     *
     * @param t1   第 1 个元素
     * @param t2   第 2 个元素
     * @param t3   第 3 个元素
     * @param t4   第 4 个元素
     * @param <T1> 第 1 个元素的类型
     * @param <T2> 第 2 个元素的类型
     * @param <T3> 第 3 个元素的类型
     * @param <T4> 第 4 个元素的类型
     * @return Tuple4 实例
     */
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }
}