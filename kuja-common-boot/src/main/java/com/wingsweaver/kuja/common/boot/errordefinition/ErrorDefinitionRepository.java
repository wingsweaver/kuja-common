package com.wingsweaver.kuja.common.boot.errordefinition;

/**
 * 错误定义的仓储接口。
 *
 * @author wingsweaver
 */
public interface ErrorDefinitionRepository {
    /**
     * 根据错误定义的编码获取错误定义。
     *
     * @param code 错误定义的编码
     * @return 错误定义
     */
    ErrorDefinition getErrorDefinition(String code);
}
