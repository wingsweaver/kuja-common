package com.wingsweaver.kuja.common.boot.returnvalue;

/**
 * 对返回值进行定制化的接口定义。
 *
 * @author wingsweaver
 */
public interface ReturnValueCustomizer {
    /**
     * 对返回值进行定制化。
     *
     * @param returnValue 返回值
     */
    void customize(ReturnValue returnValue);
}
