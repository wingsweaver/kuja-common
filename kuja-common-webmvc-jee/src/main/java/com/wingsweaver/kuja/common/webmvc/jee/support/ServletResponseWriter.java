package com.wingsweaver.kuja.common.webmvc.jee.support;

import com.wingsweaver.kuja.common.webmvc.common.support.WebResponseWriter;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将数据写入 HTTP 响应的工具类。
 *
 * @author wingsweaver
 */
public class ServletResponseWriter extends WebResponseWriter {
    /**
     * 将指定的数据写入到 HTTP 响应中。
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param value    要写入的数据
     * @throws Exception 发生错误
     */
    public void write(HttpServletRequest request, HttpServletResponse response, Object value) throws Exception {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        this.write(webRequest, value);
    }
}
