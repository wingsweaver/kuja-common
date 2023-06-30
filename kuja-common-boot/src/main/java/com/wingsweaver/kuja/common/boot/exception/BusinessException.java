package com.wingsweaver.kuja.common.boot.exception;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import lombok.Getter;
import lombok.Setter;

/**
 * 预定义好的业务异常。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessException extends ExtendedRuntimeException {
    /**
     * 错误定义。
     */
    private final ErrorDefinition errorDefinition;

    /**
     * 构造函数。
     *
     * @param message         错误消息
     * @param errorDefinition 错误定义
     */
    public BusinessException(String message, ErrorDefinition errorDefinition) {
        super(message);
        this.errorDefinition = errorDefinition;
    }

    /**
     * 构造函数。
     *
     * @param message         错误消息
     * @param cause           错误原因
     * @param errorDefinition 错误定义
     */
    public BusinessException(String message, Throwable cause, ErrorDefinition errorDefinition) {
        super(message, cause);
        this.errorDefinition = errorDefinition;
    }

    /**
     * 获取错误编码。
     *
     * @return 错误编码
     */
    public String getCode() {
        return this.errorDefinition.getCode();
    }

    /**
     * Key: 用户提示。
     */
    public static final String KEY_USER_TIP = "userTip";

    /**
     * 获取用户提示。
     *
     * @return 用户提示
     */
    public String getUserTip() {
        return this.getExtendedAttribute(KEY_USER_TIP);
    }

    /**
     * 设置用户提示。
     *
     * @param userTip 用户提示
     */
    public void setUserTip(String userTip) {
        this.setExtendedAttribute(KEY_USER_TIP, userTip);
    }
}
