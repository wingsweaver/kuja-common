package com.wingsweaver.kuja.common.utils.model.tuple;

/**
 * 含有 3 个元素的 Tuple。
 *
 * @param <T1> 第 1 个元素的类型
 * @param <T2> 第 2 个元素的类型
 * @param <T3> 第 3 个元素的类型
 * @author wingsweaver
 */
@SuppressWarnings("squid:S2160")
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
    public static final int SIZE = 3;

    public static final int INDEX_T3 = 2;

    /**
     * 第 3 个元素。
     */
    protected final T3 t3;

    @SuppressWarnings("unused")
    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this(SIZE, t1, t2, t3);
    }

    protected Tuple3(int size, T1 t1, T2 t2, T3 t3) {
        super(size, t1, t2);
        this.t3 = t3;
    }

    public T3 getT3() {
        return this.t3;
    }

    @Override
    public Object get(int index) {
        this.checkIndex(index);
        switch (index) {
            case INDEX_T1:
                return this.t1;
            case INDEX_T2:
                return this.t2;
            default:
                return this.t3;
        }
    }

    @Override
    public Object[] toArray() {
        return new Object[]{this.t1, this.t2, this.t3};
    }

    /**
     * 生成 {@link Tuple3} 实例。
     *
     * @param t1   第 1 个元素
     * @param t2   第 2 个元素
     * @param t3   第 3 个元素
     * @param <T1> 第 1 个元素的类型
     * @param <T2> 第 2 个元素的类型
     * @param <T3> 第 3 个元素的类型
     * @return Tuple3 实例
     */
    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }
}