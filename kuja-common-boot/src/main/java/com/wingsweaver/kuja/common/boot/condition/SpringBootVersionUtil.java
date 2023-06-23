package com.wingsweaver.kuja.common.boot.condition;

import com.vdurmont.semver4j.Semver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootVersion;

import java.util.EnumMap;

/**
 * Spring Boot 版本的工具类。
 *
 * @author wingsweaver
 */
public final class SpringBootVersionUtil {
    private SpringBootVersionUtil() {
        // 禁止实例化
    }

    public static final String V2 = "2.x";

    public static final String V3 = "3.x";

    private static final String SPRING_BOOT_VERSION = StringUtils.trimToEmpty(SpringBootVersion.getVersion());

    private static final EnumMap<Semver.SemverType, Semver> SEMANTIC_SPRING_BOOT_VERSION_MAP;

    static {
        SEMANTIC_SPRING_BOOT_VERSION_MAP = new EnumMap<>(Semver.SemverType.class);
        for (Semver.SemverType semverType : Semver.SemverType.class.getEnumConstants()) {
            SEMANTIC_SPRING_BOOT_VERSION_MAP.put(semverType, new Semver(SPRING_BOOT_VERSION, semverType));
        }
    }

    public static String getSpringBootVersion() {
        return SPRING_BOOT_VERSION;
    }

    public static boolean matches(String requirement, Semver.SemverType semverType) {
        Semver semver = SEMANTIC_SPRING_BOOT_VERSION_MAP.get(semverType);
        return semver.satisfies(requirement);
    }

    public static boolean matches(String requirement) {
        return matches(requirement, Semver.SemverType.NPM);
    }
}
