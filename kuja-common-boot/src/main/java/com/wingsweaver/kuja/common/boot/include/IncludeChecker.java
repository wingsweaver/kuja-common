package com.wingsweaver.kuja.common.boot.include;

/**
 * 是否包含某数据的检查器接口定义。
 *
 * @author wingsweaver
 */
public interface IncludeChecker {
    /**
     * 检查是否包含某数据。
     *
     * @param key 数据名称
     * @return 是否包含
     */
    boolean includes(String key);
}
