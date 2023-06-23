package com.wingsweaver.kuja.common.boot.returnvalue;

/**
 * 返回值的工厂类接口定义。
 *
 * @author wingsweaver
 */
public interface ReturnValueFactory {
    /**
     * 生成一个成功的返回值。
     *
     * @return 返回值
     */
    default ReturnValue success() {
        ReturnValue returnValue = new ReturnValue();
        this.patchSuccess(returnValue);
        return returnValue;
    }

    /**
     * 生成一个成功的返回值。
     *
     * @param data 返回值中的数据
     * @param <T>  返回值中的数据类型
     * @return 返回值
     */
    default <T> ReturnValueT<T> success(T data) {
        ReturnValueT<T> returnValue = new ReturnValueT<>();
        returnValue.setData(data);
        this.patchSuccess(returnValue);
        return returnValue;
    }

    /**
     * 生成一个失败的返回值。
     *
     * @return 返回值
     */
    default ReturnValue fail() {
        return this.fail(null);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param error 发生的错误
     * @return 返回值
     */
    default ReturnValue fail(Throwable error) {
        ReturnValue returnValue = new ReturnValue();
        this.patchFail(returnValue, error);
        return returnValue;
    }

    /**
     * 补全返回值中的成功信息。
     *
     * @param returnValue 返回值
     */
    void patchSuccess(ReturnValue returnValue);

    /**
     * 补全返回值中的失败信息。
     *
     * @param returnValue 返回值
     * @param error       发生的错误
     */
    void patchFail(ReturnValue returnValue, Throwable error);
}
