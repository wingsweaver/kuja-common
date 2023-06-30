package com.wingsweaver.kuja.common.utils.support.json;

import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;

/**
 * 默认的 {@link JsonCodecFactory} 实现。
 *
 * @author wingsweaver
 */
class DefaultJsonCodecFactory implements JsonCodecFactory {
    private static final String COM_FASTERXML_JACKSON_DATABIND_OBJECT_MAPPER = "com.fasterxml.jackson.databind.ObjectMapper";

    private static final String COM_GOOGLE_GSON_GSON = "com.google.gson.Gson";

    private static final String COM_ALIBABA_FASTJSON_JSON = "com.alibaba.fastjson.JSON";

    private static final String COM_ALIBABA_FASTJSON_2_JSON = "com.alibaba.fastjson2.JSON";

    @Override
    public JsonCodec createJsonCodec() {
        if (ClassUtil.exists(COM_FASTERXML_JACKSON_DATABIND_OBJECT_MAPPER)) {
            return new JacksonCodec();
        }

        if (ClassUtil.exists(COM_GOOGLE_GSON_GSON)) {
            return new GsonCodec();
        }

        if (ClassUtil.exists(COM_ALIBABA_FASTJSON_JSON)) {
            return new FastJsonCodec();
        }

        if (ClassUtil.exists(COM_ALIBABA_FASTJSON_2_JSON)) {
            return new FastJson2Codec();
        }

        // 都不存在的话，返回 null
        return null;
    }

}
