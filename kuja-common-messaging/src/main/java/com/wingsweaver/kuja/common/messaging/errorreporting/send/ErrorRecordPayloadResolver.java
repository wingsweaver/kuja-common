package com.wingsweaver.kuja.common.messaging.errorreporting.send;

import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.errorreporting.ErrorInfoErrorRecordCustomizer;
import com.wingsweaver.kuja.common.boot.errorreporting.ErrorRecord;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.boot.include.IncludeCheckerFactory;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.messaging.common.AbstractSenderInfoOwner;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.messaging.core.send.common.PayloadResolver;
import com.wingsweaver.kuja.common.messaging.errorreporting.common.ErrorRecordPayload;
import com.wingsweaver.kuja.common.utils.exception.ErrorInfoExportUtil;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于将 {@link ErrorRecord} 转换成 {@link ErrorRecordPayload} 的 {@link PayloadResolver} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ErrorRecordPayloadResolver extends AbstractSenderInfoOwner implements PayloadResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorRecordPayloadResolver.class);

    /**
     * 默认的导出设置。
     */
    public static final IncludeSettings DEFAULT_INCLUDE_SETTINGS = new IncludeSettings(IncludeAttribute.ALWAYS);

    /**
     * 消息 ID 的生成器。
     */
    private StringIdGenerator idGenerator;

    /**
     * 错误信息的导出设置。
     */
    private IncludeSettings errorInfoInclude;

    /**
     * 错误信息的导出设置。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private IncludeCheckerFactory errorInfoIncludeCheckerFactory;

    /**
     * 错误附加信息的导出设置。
     */
    private IncludeSettings errorTagsInclude;

    /**
     * 错误附加信息的导出设置。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private IncludeCheckerFactory errorTagsIncludeCheckerFactory;

    @Override
    public Tuple2<Boolean, Object> resolvePayload(MessageSendContext context) {
        ErrorRecord errorRecord = ObjectUtil.cast(context.getOriginalMessage(), ErrorRecord.class);
        if (errorRecord == null) {
            return UNHANDLED;
        }

        ErrorRecordPayload payload = this.resolvePayload(context, errorRecord);
        return Tuple2.of(true, payload);
    }

    /**
     * 生成需要发送的消息内容 ({@link ErrorRecordPayload})。
     *
     * @param context     消息发送上下文
     * @param errorRecord 错误记录
     * @return 消息内容
     */
    protected ErrorRecordPayload resolvePayload(MessageSendContext context, ErrorRecord errorRecord) {
        // 生成 Payload
        ErrorRecordPayload payload = new ErrorRecordPayload();
        payload.setId(errorRecord.getId());
        payload.setCreationTime(errorRecord.getCreationTime());

        // 生成发送者的信息
        Map<String, String> sender = this.resolveSender(context);
        payload.setSender(sender);

        // 生成错误信息
        Map<String, Object> errorInfo = this.resolveErrorInfo(context, errorRecord);
        payload.setError(errorInfo);

        // 生成附加数据
        Map<String, Object> tags = this.resolveErrorTags(context, errorRecord);
        payload.setErrorTags(tags);

        // 返回
        return payload;
    }

    /**
     * 生成错误的附加信息。
     *
     * @param context     消息发送上下文
     * @param errorRecord 错误记录
     * @return 错误信息
     */
    protected Map<String, Object> resolveErrorTags(MessageSendContext context, ErrorRecord errorRecord) {
        Map<String, Object> map = errorRecord.getTags();
        if (CollectionUtils.isEmpty(map)) {
            return map;
        }

        // 删除可能存在的错误信息，以避免重复
        Map<String, Object> tags = new HashMap<>(map);
        tags.remove(ErrorInfoErrorRecordCustomizer.KEY);

        // 删除不需要导出的附加信息
        IncludeChecker includeChecker = this.errorTagsIncludeCheckerFactory.build(BusinessContextHolder.getCurrent(), null);
        List<String> tagKeys = new ArrayList<>(tags.keySet());
        for (String tagKey : tagKeys) {
            if (!includeChecker.includes(tagKey)) {
                tags.remove(tagKey);
            }
        }

        // 返回
        return tags;
    }

    /**
     * 生成错误信息。
     *
     * @param context     消息发送上下文
     * @param errorRecord 错误记录
     * @return 错误信息
     */
    protected Map<String, Object> resolveErrorInfo(MessageSendContext context, ErrorRecord errorRecord) {
        IncludeChecker includeChecker = this.errorInfoIncludeCheckerFactory.build(BusinessContextHolder.getCurrent(), null);
        return ErrorInfoExportUtil.export(errorRecord.getError(), includeChecker::includes);
    }

    /**
     * 生成发送者的信息。
     *
     * @param context 消息发送上下文
     * @return 发送者的信息
     */
    protected Map<String, String> resolveSender(MessageSendContext context) {
        return this.ensureSenderInfo(false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 idGenerator
        this.initIdGenerator();

        // 初始化 errorInfoInclude
        this.initErrorInfoInclude();

        // 初始化 errorTagsInclude
        this.initErrorTagsInclude();
    }

    /**
     * 初始化 errorTagsInclude。
     */
    protected void initErrorTagsInclude() {
        if (this.errorTagsInclude == null) {
            this.errorTagsInclude = DEFAULT_INCLUDE_SETTINGS;
        }
        this.errorTagsIncludeCheckerFactory = new IncludeCheckerFactory(this.errorTagsInclude);
    }

    /**
     * 初始化 errorInfoInclude。
     */
    protected void initErrorInfoInclude() {
        if (this.errorInfoInclude == null) {
            this.errorInfoInclude = DEFAULT_INCLUDE_SETTINGS;
        }
        this.errorInfoIncludeCheckerFactory = new IncludeCheckerFactory(this.errorInfoInclude);
    }

    /**
     * 初始化 idGenerator。
     */
    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.getBean(StringIdGenerator.class);
        }
    }
}
