package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.AnnotatedElement;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * {@link Hashed} 注解相应的 {@link ToStringWithConverter} 实现类。
 *
 * @author wingsweaver
 */
public class HashedConverter extends AbstractToStringWithConverter<Hashed>
        implements ConfigurableToStringConverter<HashedConfig> {

    public HashedConverter() {
        super(Hashed.class);
    }

    @Override
    protected void toStringInternal(Object value, AnnotatedElement annotatedElement, Hashed annotation,
                                    StringBuilder builder, ToStringConfig config) {
        // 计算转换设置
        HashedConfig customConfig = new HashedConfig();
        customConfig.setAlgorithm(StringUtil.notEmptyOr(annotation.value(), HashedConfig.DEFAULT_ALGORITHM));
        customConfig.setCodec(StringUtil.notEmptyOr(annotation.codec(), HashedConfig.DEFAULT_CODEC));

        // 进行转换
        this.toString(value, builder, config, customConfig);
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config, HashedConfig customConfig) {
        // 计算哈希值
        String text = String.valueOf(object);
        MessageDigest digest = DigestUtils.getDigest(customConfig.getAlgorithm());
        byte[] bytes = DigestUtils.digest(digest, text.getBytes(StandardCharsets.UTF_8));

        // 转换成字符串，输出到 builder 中
        String encodedText = BinaryCodec.encodeAsNamed(bytes, customConfig.getCodec());
        builder.append(encodedText);

        // 返回
        return true;
    }
}
