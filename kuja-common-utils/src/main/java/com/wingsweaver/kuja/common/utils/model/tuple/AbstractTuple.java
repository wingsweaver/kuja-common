package com.wingsweaver.kuja.common.utils.model.tuple;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;

import java.util.Arrays;

/**
 * 自定义的 Tuple 工具类的基类。
 * 用于返回多个数据。
 *
 * @author wingsweaver
 */
public abstract class AbstractTuple extends AbstractPojo {
    /**
     * 元素数量。
     */
    private final int size;

    /**
     * 构造方法。
     *
     * @param size 元素数量
     */
    protected AbstractTuple(int size) {
        this.size = size;
    }

    /**
     * 获取 Tuple 的长度。
     *
     * @return Tuple 的长度
     */
    public int size() {
        return this.size;
    }

    /**
     * 获取指定下标的数据。
     *
     * @param index 下标
     * @return 对应数据
     */
    public abstract Object get(int index);

    /**
     * 转换到数组。
     *
     * @return 转换结果
     */
    public abstract Object[] toArray();

    /**
     * 检查 index 参数是否有效。
     *
     * @param index 下标参数
     */
    protected void checkIndex(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + this.size());
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.toArray());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractTuple)) {
            return false;
        }
        Object[] otherArray = ((AbstractTuple) obj).toArray();
        return Arrays.equals(this.toArray(), otherArray);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }
}
