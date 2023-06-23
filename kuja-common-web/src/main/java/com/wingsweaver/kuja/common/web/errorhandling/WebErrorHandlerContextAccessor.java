package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.utils.model.attributes.AttributesAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;

/**
 * Web 环境中的错误处理上下文的访问器。
 *
 * @author wingsweaver
 */
@Getter
public class WebErrorHandlerContextAccessor extends AttributesAccessor<String> {
    private final ErrorHandlerContext errorHandlerContext;

    /**
     * 构造函数。
     *
     * @param errorHandlerContext ErrorHandlerContext 实例
     */
    public WebErrorHandlerContextAccessor(ErrorHandlerContext errorHandlerContext) {
        super(errorHandlerContext);
        this.errorHandlerContext = errorHandlerContext;
    }

    public static final String KEY_RESPONSE_DATA = ClassUtil.resolveKey(ResponseData.class);

    public ResponseData getResponseData() {
        return getAttribute(KEY_RESPONSE_DATA);
    }

    public void setResponseData(ResponseData responseData) {
        setAttribute(KEY_RESPONSE_DATA, responseData);
    }

    public ResponseData getResponseData(boolean createIfAbsent) {
        ResponseData responseData = this.getResponseData();
        if (responseData == null) {
            responseData = new ResponseData();
            this.setResponseData(responseData);
        }
        return responseData;
    }
}
