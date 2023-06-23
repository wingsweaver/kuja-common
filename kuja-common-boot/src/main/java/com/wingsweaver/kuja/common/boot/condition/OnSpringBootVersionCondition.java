package com.wingsweaver.kuja.common.boot.condition;

import com.vdurmont.semver4j.Semver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * {@link ConditionalOnSpringBootVersion} 的条件判定处理。
 *
 * @author wingsweaver
 */
class OnSpringBootVersionCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnSpringBootVersion.class.getName());
        String versionExpected = (String) attributes.getOrDefault("value", "");
        Semver.SemverType semverType = (Semver.SemverType) attributes.getOrDefault("type", Semver.SemverType.STRICT);
        return getMatchOutcomeInternal(versionExpected, semverType);
    }

    private ConditionOutcome getMatchOutcomeInternal(String versionExpected, Semver.SemverType semverType) {
        boolean matches = StringUtils.isEmpty(versionExpected) || SpringBootVersionUtil.matches(versionExpected);
        ConditionMessage message = ConditionMessage
                .forCondition(ConditionalOnSpringBootVersion.class, SpringBootVersionUtil.getSpringBootVersion())
                .foundExactly(versionExpected);
        return new ConditionOutcome(matches, message);
    }
}
