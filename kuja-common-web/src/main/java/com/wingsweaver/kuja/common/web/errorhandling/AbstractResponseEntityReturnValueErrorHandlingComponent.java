package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.errorhandling.AbstractReturnValueErrorHandlingComponent;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

/**
 * Web 场景中的 {@link AbstractReturnValueErrorHandlingComponent} 的实现类。
 *
 * @author wingsweaver
 */
public abstract class AbstractResponseEntityReturnValueErrorHandlingComponent extends AbstractReturnValueErrorHandlingComponent {
    @Override
    protected Object resolveReturnValue(ErrorHandlerContext context) throws Throwable {
        Object returnValue = super.resolveReturnValue(context);

        // 如果返回结果已经是 ResponseEntity，那么直接返回
        if (returnValue instanceof ResponseEntity) {
            return returnValue;
        }

        // 如果不是，根据 ErrorHandlerContext 中的 ResponseData 设置 ResponseEntity
        WebErrorHandlerContextAccessor accessor = new WebErrorHandlerContextAccessor(context);
        ResponseData responseData = accessor.getResponseData();
        if (responseData == null || responseData.isEmpty()) {
            // 如果没有设置 ResponseData，那么返回 ResponseEntity.ok
            return ResponseEntity.ok(returnValue);
        }

        // 否则根据 ResponseData 设置 ResponseEntity
        HttpStatus status = ObjectUtil.notNullOr(responseData.getStatus(), HttpStatus.OK);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(status);
        HttpHeaders headers = responseData.getHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            bodyBuilder = bodyBuilder.headers(headers);
        }
        return bodyBuilder.body(returnValue);
    }
}
