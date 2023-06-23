package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandler;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import com.wingsweaver.kuja.common.web.constants.KujaCommonWebOrders;
import com.wingsweaver.kuja.common.web.convert.HttpStatusConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 用于设置 ResponseData 的错误处理器。<br>
 * 设置好的 ResponseData，将在 {@link AbstractResponseEntityReturnValueErrorHandlingComponent} 中被使用。
 *
 * @author wingsweaver
 */
public class ResponseDataErrorHandler implements ErrorHandler {
    public static final String KEY_RESPONSE_DATA = "response-data";

    public static final String KEY_RESPONSE_STATUS = KEY_RESPONSE_DATA + ".status";

    public static final String KEY_RESPONSE_HEADERS_PREFIX = KEY_RESPONSE_DATA + ".headers.";

    @Override
    public boolean handle(ErrorHandlerContext context) {
        if (context == null) {
            return false;
        }

        // 检查是否是 BusinessException，不是的话直接返回
        Throwable error = context.getInputError();
        if (!(error instanceof BusinessException)) {
            return false;
        }

        // 检查错误定义中是否有 ResponseData 的相关设置
        Map<String, Object> temps = ((BusinessException) error).getErrorDefinition().getTemps();
        if (CollectionUtils.isEmpty(temps)) {
            return false;
        }

        // 检查是否已经设置了 ResponseData，如果已经设置了，那么直接返回
        WebErrorHandlerContextAccessor accessor = new WebErrorHandlerContextAccessor(context);
        ResponseData responseData = accessor.getResponseData(true);
        this.updateResponseData(responseData, temps);

        // 恒定返回 false，以便继续处理
        return false;
    }

    private void updateResponseData(ResponseData responseData, Map<String, Object> temps) {
        // 更新 HttpStatus
        if (responseData.getStatus() == null) {
            HttpStatus status = this.resolveStatus(temps);
            responseData.setStatus(status);
        }

        // 更新 Headers
        HttpHeaders headers = this.resolveHeaders(temps);
        if (!CollectionUtils.isEmpty(headers)) {
            if (responseData.getHeaders() == null) {
                responseData.setHeaders(headers);
            } else {
                MapUtil.copy(headers, responseData.getHeaders(), false);
            }
        }
    }

    private HttpHeaders resolveHeaders(Map<String, Object> temps) {
        HttpHeaders headers = new HttpHeaders();
        temps.forEach((key, value) -> {
            if (key.startsWith(KEY_RESPONSE_HEADERS_PREFIX)) {
                String headerName = key.substring(KEY_RESPONSE_HEADERS_PREFIX.length()).trim();
                if (headerName.isEmpty()) {
                    return;
                }
                String headerValue = value.toString().trim();
                String[] headerValues = StringUtils.splitPreserveAllTokens(headerValue, ',');
                headers.put(headerName, CollectionUtil.listOf(headerValues));
            }
        });

        return headers;
    }

    private HttpStatus resolveStatus(Map<String, Object> temps) {
        Object value = temps.get(KEY_RESPONSE_STATUS);
        return HttpStatusConverter.convert(value);
    }

    @Override
    public int getOrder() {
        return KujaCommonWebOrders.RESPONSE_DATA_ERROR_HANDLER;
    }
}
