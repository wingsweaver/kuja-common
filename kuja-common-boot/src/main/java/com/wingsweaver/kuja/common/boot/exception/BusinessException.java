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

    public BusinessException(String message, ErrorDefinition errorDefinition) {
        super(message);
        this.errorDefinition = errorDefinition;
    }

    public BusinessException(String message, Throwable cause, ErrorDefinition errorDefinition) {
        super(message, cause);
        this.errorDefinition = errorDefinition;
    }

    public String getCode() {
        return this.errorDefinition.getCode();
    }

    public static final String KEY_USER_TIP = "userTip";

    public String getUserTip() {
        return this.getExtendedAttribute(KEY_USER_TIP);
    }

    public void setUserTip(String userTip) {
        this.setExtendedAttribute(KEY_USER_TIP, userTip);
    }
}
