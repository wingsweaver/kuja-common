package com.wingsweaver.kuja.common.utils.support.codec.binary;

import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 二进制编解码器。
 *
 * @author wingsweaver
 */
public final class BinaryCodec {
    private BinaryCodec() {
        // 禁止实例化
    }

    private static final Map<String, BinaryCodecProvider> PROVIDER_MAP;

    private static final AtomicReference<BinaryCodecProvider> DEFAULT_PROVIDER;

    static {
        // 获取所有的解码器的集合
        List<BinaryCodecProvider> providers = new LinkedList<>();
        SpringFactoriesLoader.loadFactories(BinaryCodecRegister.class, null)
                .forEach(register -> register.register(providers));
        new DefaultBinaryCodecRegister().register(providers);
        providers.sort(Orders::compareReversed);

        // 将解码器的集合转换成字典
        int size = MapUtil.hashInitCapacity(providers.size());
        Map<String, BinaryCodecProvider> providerMap = new HashMap<>(size);
        providers.forEach(provider -> providerMap.put(normalizeProviderName(provider.getName()), provider));
        PROVIDER_MAP = providerMap;

        // 设置默认的解码器
        BinaryCodecProvider defaultProvider = getProvider("base64");
        DEFAULT_PROVIDER = new AtomicReference<>(defaultProvider);
    }

    public static BinaryCodecProvider getDefaultProvider() {
        return DEFAULT_PROVIDER.get();
    }

    public static void setDefaultProvider(BinaryCodecProvider provider) {
        Objects.requireNonNull(provider, "[provider] is null");
        DEFAULT_PROVIDER.set(provider);
    }

    static String normalizeProviderName(String providerName) {
        return providerName == null ? null : providerName.toLowerCase();
    }

    public static BinaryCodecProvider getProvider(String providerName) {
        Objects.requireNonNull(providerName, "[providerName] is null");
        return PROVIDER_MAP.get(normalizeProviderName(providerName));
    }

    /**
     * 确保指定的编码服务者存在。<br>
     * 不存在的话，抛出异常。
     *
     * @param providerName 服务名称
     * @return 服务者
     */
    static BinaryCodecProvider ensureProvider(String providerName) {
        if (!StringUtils.hasText(providerName)) {
            throw new IllegalArgumentException("Provider name must not be empty");
        }
        BinaryCodecProvider provider = PROVIDER_MAP.get(normalizeProviderName(providerName));
        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerName);
        }
        return provider;
    }

    /**
     * 确保默认的编码服务者存在。<br>
     * 不存在的话，抛出异常。
     *
     * @return 服务者
     */
    static BinaryCodecProvider ensureDefaultProvider() {
        BinaryCodecProvider binaryCodecProvider = DEFAULT_PROVIDER.get();
        Objects.requireNonNull(binaryCodecProvider, "Default provider is null");
        return binaryCodecProvider;
    }

    /**
     * 使用指定的编码服务，将字节数据编码为字符串。
     *
     * @param bytes        字节数据
     * @param providerName 编码服务名称
     * @return 字符串
     */
    public static String encode(byte[] bytes, String providerName) {
        return ensureProvider(providerName).encode(bytes);
    }

    /**
     * 使用默认的编码服务，将字节数据编码为字符串。
     *
     * @param bytes 字节数据
     * @return 字符串
     */
    public static String encode(byte[] bytes) {
        return ensureDefaultProvider().encode(bytes);
    }

    /**
     * 将服务名称和编码结果合并成一个字符串的处理接口定义。
     */
    public interface ProviderResultCombiner {
        /**
         * 将服务名称和编码结果合并成一个字符串。
         *
         * @param provider 编码服务
         * @param text     编码结果
         * @return 合并后的字符串
         */
        String combine(BinaryCodecProvider provider, String text);
    }

    /**
     * 使用指定的编码服务，将字符串解码为字节数据。
     *
     * @param bytes        字符串
     * @param providerName 编码服务名称
     * @param combiner     合并服务名称和编码结果的处理
     * @return 字节数据
     */
    public static String encodeAsNamed(byte[] bytes, String providerName, ProviderResultCombiner combiner) {
        return encodeWithNameInternal(bytes, ensureProvider(providerName), combiner);
    }

    /**
     * 使用指定的编码服务，将字符串解码为字节数据。
     *
     * @param bytes        字符串
     * @param providerName 编码服务名称
     * @return 字节数据
     */
    public static String encodeAsNamed(byte[] bytes, String providerName) {
        return encodeWithNameInternal(bytes, ensureProvider(providerName), DEFAULT_COMBINER);
    }

    public static String encodeAsNamed(byte[] bytes, ProviderResultCombiner combiner) {
        return encodeWithNameInternal(bytes, ensureDefaultProvider(), combiner);
    }

    public static String encodeAsNamed(byte[] bytes) {
        return encodeWithNameInternal(bytes, ensureDefaultProvider(), DEFAULT_COMBINER);
    }

    /**
     * 使用指定的编码服务，将字符串解码为字节数据。
     *
     * @param bytes    字符串
     * @param provider 编码服务
     * @param combiner 合并服务名称和编码结果的处理
     * @return 字节数据
     */
    public static String encodeAsNamed(byte[] bytes, BinaryCodecProvider provider, ProviderResultCombiner combiner) {
        return encodeWithNameInternal(bytes, provider, combiner);
    }

    /**
     * 使用指定的编码服务，将字符串解码为字节数据。
     *
     * @param bytes    字符串
     * @param provider 编码服务
     * @return 字节数据
     */
    public static String encodeAsNamed(byte[] bytes, BinaryCodecProvider provider) {
        return encodeWithNameInternal(bytes, provider, DEFAULT_COMBINER);
    }

    /**
     * 使用指定的编码服务，将字符串解码为字节数据。
     *
     * @param bytes    字符串
     * @param provider 编码服务
     * @param combiner 合并服务名称和编码结果的处理
     * @return 字节数据
     */
    static String encodeWithNameInternal(byte[] bytes, BinaryCodecProvider provider, ProviderResultCombiner combiner) {
        Objects.requireNonNull(provider, "[provider] is null");
        Objects.requireNonNull(combiner, "[combiner] is null");
        String text = provider.encode(bytes);
        return combiner.combine(provider, text);
    }

    /**
     * 默认的 {@link ProviderResultCombiner} 实现。
     */
    public static final ProviderResultCombiner DEFAULT_COMBINER =
            (provider, text) -> "{" + provider.getName() + "}" + text;

    /**
     * 使用指定的编码服务，将字符串解码为字节数组。
     *
     * @param text         字符串
     * @param providerName 编码服务名称
     * @return 字节数组
     */
    public static byte[] decode(String text, String providerName) {
        return ensureProvider(providerName).decode(text);
    }

    /**
     * 使用默认的编码服务，将字符串解码为字节数组。
     *
     * @param text 字符串
     * @return 字节数组
     */
    public static byte[] decode(String text) {
        return ensureDefaultProvider().decode(text);
    }

    /**
     * 从指定字符串中解码出编码服务名称和编码结果的处理器的接口定义。
     */
    public interface ProviderResultParser {
        /**
         * 从指定字符串中解码出编码服务名称和编码结果。
         *
         * @param text 字符串
         * @return 编码服务名称 ({@link Tuple2#getT1()}) 和编码结果 ({@link Tuple2#getT2()})
         */
        Tuple2<String, String> parse(String text);
    }

    /**
     * 解析字符串中的编码服务名称和编码结果。
     *
     * @param text 字符串
     * @return 编码服务名称 ({@link Tuple2#getT1()}) 和编码结果 ({@link Tuple2#getT2()})
     */
    public static Tuple2<String, String> parseCodecAndEncodedText(String text) {
        // 检查参数
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("[text] is null or empty");
        }

        // 解析 { 和 } 的位置
        int pos = text.indexOf('{');
        int pos2 = text.indexOf('}');
        if (pos < 0 || pos2 < 0 || pos2 < pos) {
            throw new IllegalArgumentException("Cannot find provider name token from text: " + text);
        }

        String providerName = text.substring(pos + 1, pos2).trim();
        String encodedText = text.substring(pos2 + 1);
        return Tuple2.of(providerName, encodedText);
    }

    public static byte[] parseAndDecode(String text, ProviderResultParser parser) {
        return parseAndDecodeInternal(text, parser);
    }

    public static byte[] parseAndDecode(String text) {
        return parseAndDecodeInternal(text, DEFAULT_PARSER);
    }

    static byte[] parseAndDecodeInternal(String text, ProviderResultParser parser) {
        Objects.requireNonNull(parser, "[parser] is null");
        Tuple2<String, String> tuple2 = parser.parse(text);
        if (tuple2 == null) {
            throw new IllegalArgumentException("Cannot parse provider and encoded text from text: " + text);
        }
        String providerName = tuple2.getT1();
        if (!StringUtils.hasText(providerName)) {
            throw new IllegalArgumentException("Cannot resolve provider name from text: " + text);
        }
        return ensureProvider(providerName).decode(tuple2.getT2());
    }

    public static final ProviderResultParser DEFAULT_PARSER = BinaryCodec::parseCodecAndEncodedText;
}
