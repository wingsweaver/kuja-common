package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.utils.support.EmptyChecker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

/**
 * 响应数据的定义。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ResponseData implements EmptyChecker {
    /**
     * 状态码。
     */
    private HttpStatus status;

    /**
     * Header 定义。
     */
    private HttpHeaders headers;

    @Override
    public boolean isEmpty() {
        return this.status == null && CollectionUtils.isEmpty(this.headers);
    }

    public HttpHeaders getHeaders(boolean createIfAbsent) {
        if (this.headers == null && createIfAbsent) {
            this.headers = new HttpHeaders();
        }
        return this.headers;
    }
}
