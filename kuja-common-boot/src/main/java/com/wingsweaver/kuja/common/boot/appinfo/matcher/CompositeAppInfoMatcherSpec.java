package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import com.wingsweaver.kuja.common.utils.support.json.JsonCodecUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 由多个 {@link AppInfoMatcherSpec} 组合而成的 {@link AppInfoMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class CompositeAppInfoMatcherSpec extends AbstractAppInfoMatcherSpec {
    public static final String SPEC_TYPE = "composite";

    /**
     * 匹配模式。
     */
    private CompositeMatchMode mode = CompositeMatchMode.ALL;

    /**
     * 具体的匹配设置的列表。
     */
    private List<AppInfoMatcherSpec> matchers;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CompositeAppInfoMatcherSpec that = (CompositeAppInfoMatcherSpec) o;
        return mode == that.mode && Objects.equals(matchers, that.matchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mode, matchers);
    }

    /**
     * 构造函数。
     *
     * @param mode     匹配模式
     * @param matchers 具体的匹配设置的列表
     */
    public CompositeAppInfoMatcherSpec(CompositeMatchMode mode, AppInfoMatcherSpec... matchers) {
        this.mode = mode;
        this.matchers = CollectionUtil.listOf(matchers);
    }

    /**
     * 构造函数。
     *
     * @param matchers 具体的匹配设置的列表
     */
    public CompositeAppInfoMatcherSpec(AppInfoMatcherSpec... matchers) {
        this(CompositeMatchMode.ALL, matchers);
    }

    /**
     * 构造函数。
     */
    public CompositeAppInfoMatcherSpec() {
        // 什么都不做
    }

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        super.exportConfig(map);

        // 导出 mode 设置
        this.exportMode(map);

        // 导出 matchers 设置
        this.exportMatchers(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        super.loadConfig(map);

        // 导入 mode 设置
        this.loadMode(map);

        // 导入 matchers 设置
        this.loadMatchers(map);
    }

    /**
     * {@link #mode} 的 TagKey。
     */
    public static final TagKey.EnumKey<CompositeMatchMode> TAG_KEY_MODE = TagKeys.ofEnum("mode", CompositeMatchMode.class);

    /**
     * 导出 mode 设置。
     *
     * @param map 保存配置的字典
     */
    protected void exportMode(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_MODE, this.getMode());
    }

    /**
     * 导入 mode 设置。
     *
     * @param map 保存配置的字典
     */
    protected void loadMode(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_MODE, this::setMode);
    }

    /**
     * {@link #matchers} 的 Key。
     */
    public static final String KEY_MATCHERS = "matchers";

    /**
     * 导出 matchers 设置。
     *
     * @param map 保存配置的字典
     */
    protected void exportMatchers(Map<String, String> map) {
        if (CollectionUtils.isEmpty(this.matchers)) {
            return;
        }

        List<String> matcherJsonTexts = this.matchers.stream()
                .map(AppInfoMatcherSpecUtil::toJsonString)
                .collect(Collectors.toList());
        String jsonText = JsonCodecUtil.ensureJsonCodec().toJsonString(matcherJsonTexts);
        map.put(KEY_MATCHERS, jsonText);
    }

    /**
     * 导入 matchers 设置。
     *
     * @param map 保存配置的字典
     */
    protected void loadMatchers(Map<String, String> map) {
        String jsonText = map.get(KEY_MATCHERS);
        if (StringUtil.isEmpty(jsonText)) {
            return;
        }

        List<String> matcherJsonTexts = JsonCodecUtil.ensureJsonCodec().parseList(jsonText, String.class);
        this.matchers = matcherJsonTexts.stream()
                .map(AppInfoMatcherSpecUtil::fromJsonString)
                .collect(Collectors.toList());
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        CompositeAppInfoMatcher matcher = new CompositeAppInfoMatcher();
        matcher.initialize(this);
        matcher.validate();
        return matcher;
    }
}
