package com.wingsweaver.kuja.common.boot.returnvalue;

import lombok.Getter;
import lombok.Setter;

/**
 * 带数据的返回结果的定义。
 *
 * @param <T> 数据类型。
 * @author wingsweaver
 */
@Getter
@Setter
public class ReturnValueT<T> extends ReturnValue {
    /**
     * 实际返回的数据。
     */
    private T data;

    @SuppressWarnings("unchecked")
    @Override
    public void load(ReturnValue other, boolean overwrite) {
        super.load(other, overwrite);

        // 导入 data
        if (other instanceof ReturnValueT) {
            if (overwrite || this.data == null) {
                this.data = ((ReturnValueT<T>) other).getData();
            }
        }
    }
}
