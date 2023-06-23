package com.wingsweaver.kuja.common.utils.support.tostring;

/**
 * 默认的 {@link ToStringConfigCustomizer} 实现。
 *
 * @author wingsweaver
 */
final class DefaultToStringConfigCustomizer implements ToStringConfigCustomizer {
    public static final DefaultToStringConfigCustomizer INSTANCE = new DefaultToStringConfigCustomizer();

    private DefaultToStringConfigCustomizer() {
        // 禁止外部实例化
    }

    @Override
    public void customize(ToStringConfig.Builder builder) {
        builder.setPublicOnly(true);
        builder.setIncludeTypeName(false);
        builder.setIncludeTransient(false);
        builder.setReflectFields(true);
        builder.setReflectMethods(true);
        builder.setDateTimeFormat(ToStringConfig.DEFAULT_DATETIME_FORMAT);
        builder.setDateFormat(ToStringConfig.DEFAULT_DATE_FORMAT);
        builder.setTimeFormat(ToStringConfig.DEFAULT_TIME_FORMAT);
    }
}
