package com.wingsweaver.kuja.common.messaging.autoconfigurer.errorreporting;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;

/**
 * kuja-common-messaging 中错误上报功能的 Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonMessagingErrorReportingKeys {
    /**
     * kuja-common-messaging 中错误上报的 Key 前缀。
     */
    String PREFIX = KujaCommonMessagingKeys.PREFIX_KUJA_COMMON_MESSAGING + ".error-reporting";

    /**
     * {@link MessageErrorReporterProperties}。
     */
    String PREFIX_MESSAGE_ERROR_REPORTER = PREFIX + ".reporter";

    /**
     * {@link ErrorRecordPayloadResolverProperties}。
     */
    String PREFIX_ERROR_RECORD_PAYLOAD_RESOLVER = PREFIX + ".payload-resolver";
}
