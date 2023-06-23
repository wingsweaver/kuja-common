package com.wingsweaver.kuja.common.utils.model.tuple;

/**
 * 含有 2 个元素的 Tuple。
 *
 * @param <T1> 第 1 个元素的类型
 * @param <T2> 第 2 个元素的类型
 * @author wingsweaver
 */
@SuppressWarnings("squid:S2160")
public class Tuple2<T1, T2> extends AbstractTuple {
    public static final int SIZE = 2;

    public static final int INDEX_T1 = 0;

    public static final int INDEX_T2 = 1;

    /**
     * 第 1 个元素。
     */
    protected final T1 t1;

    /**
     * 第 2 个元素。
     */
    protected final T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this(SIZE, t1, t2);
    }

    protected Tuple2(int size, T1 t1, T2 t2) {
        super(size);
        this.t1 = t1;
        this.t2 = t2;
    }

    public final T1 getT1() {
        return this.t1;
    }

    public final T2 getT2() {
        return this.t2;
    }

    @Override
    public Object get(int index) {
        this.checkIndex(index);
        if (index == INDEX_T1) {
            return this.t1;
        }
        return this.t2;
    }

    @Override
    public Object[] toArray() {
        return new Object[]{this.t1, this.t2};
    }

    /**
     * 生成 {@link Tuple2} 实例。
     *
     * @param t1   第 1 个元素
     * @param t2   第 2 个元素
     * @param <T1> 第 1 个元素的类型
     * @param <T2> 第 2 个元素的类型
     * @return Tuple2 实例
     */
    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }
}