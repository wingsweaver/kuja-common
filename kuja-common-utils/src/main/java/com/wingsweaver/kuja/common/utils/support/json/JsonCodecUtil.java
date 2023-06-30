package com.wingsweaver.kuja.common.utils.support.json;

import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link JsonCodec} 工具类。
 *
 * @author wingsweaver
 */
public final class JsonCodecUtil {
    private JsonCodecUtil() {
        // 禁止实例化
    }

    private static final AtomicReference<JsonCodec> JSON_CODEC = new AtomicReference<>();

    static {
        List<JsonCodecFactory> jsonWriterFactories = SpringFactoriesLoader.loadFactories(JsonCodecFactory.class, null);
        jsonWriterFactories.sort(Orders::compare);
        jsonWriterFactories.add(new DefaultJsonCodecFactory());
        for (JsonCodecFactory jsonWriterFactory : jsonWriterFactories) {
            JsonCodec jsonWriter = jsonWriterFactory.createJsonCodec();
            if (jsonWriter != null) {
                JSON_CODEC.set(jsonWriter);
                break;
            }
        }
    }

    /**
     * 获取 JSON 编码解码器。
     *
     * @return JSON 编码解码器
     */
    public static JsonCodec getJsonCodec() {
        return JSON_CODEC.get();
    }

    /**
     * 获取并校验 JSON 编码解码器。
     *
     * @return JSON 编码解码器
     * @throws IllegalStateException 当不存在 JSON 编码解码器时
     */
    public static JsonCodec ensureJsonCodec() throws IllegalStateException {
        JsonCodec codec = JSON_CODEC.get();
        AssertState.notNull(codec, "No JsonCodec available");
        return codec;
    }

    /**
     * 设置 JSON 编码解码器。
     *
     * @param jsonCodec JSON 编码解码器
     */
    public static void setJsonCodec(JsonCodec jsonCodec) {
        AssertArgs.Named.notNull("jsonCodec", jsonCodec);
        JSON_CODEC.set(jsonCodec);
    }
}
