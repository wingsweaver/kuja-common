package com.wingsweaver.kuja.common.utils.support;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 模块的版本信息。
 *
 * @author wingsweaver
 */
public final class ModuleVersion extends AbstractPojo {
    private static final ModuleVersion EMPTY = new ModuleVersion(null, null, null);

    private final String artifactId;

    private final String groupId;

    private final String version;

    private ModuleVersion(String artifactId, String groupId, String version) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getVersion() {
        return version;
    }

    public static Builder builder(Class<?> clazz) {
        return new Builder(clazz);
    }

    public static Builder builder(URL jarFileUrl) {
        return new Builder(jarFileUrl);
    }

    /**
     * 构建工具类。
     */
    public static class Builder {
        private final Supplier<ModuleVersion> supplier;

        private volatile ModuleVersion moduleVersion;

        public Builder(Class<?> clazz) {
            this.supplier = () -> of(clazz);
        }

        public Builder(URL jarFileUrl) {
            this.supplier = () -> of(jarFileUrl);
        }

        /**
         * 生成 ProjectVersion 实例。
         *
         * @return ProjectVersion 实例
         */
        public ModuleVersion build() {
            if (moduleVersion == null) {
                synchronized (this) {
                    if (moduleVersion == null) {
                        moduleVersion = supplier.get();
                    }
                }
            }
            return moduleVersion;
        }
    }

    /**
     * 从类中获取项目版本信息。
     *
     * @param clazz 类
     * @return 项目版本信息
     */
    @SuppressFBWarnings({"URLCONNECTION_SSRF_FD"})
    public static ModuleVersion of(Class<?> clazz) {
        return of(clazz.getProtectionDomain().getCodeSource().getLocation());
    }

    /**
     * 从 jar 文件中获取项目版本信息。
     *
     * @param jarFileUrl jar 文件的 URL
     * @return 项目版本信息
     */
    @SuppressFBWarnings({"URLCONNECTION_SSRF_FD"})
    public static ModuleVersion of(URL jarFileUrl) {
        ModuleVersion moduleVersion = EMPTY;
        try (InputStream inputStream = jarFileUrl.openStream()) {
            try (JarInputStream jarInputStream = new JarInputStream(inputStream)) {
                moduleVersion = of(jarInputStream.getManifest());
            }
        } catch (IOException ignored) {
            // 忽略错误
        }
        return moduleVersion;
    }

    /**
     * 从 Manifest 中获取项目版本信息。
     *
     * @param manifest Manifest
     * @return 项目版本信息
     */
    public static ModuleVersion of(Manifest manifest) {
        if (manifest == null) {
            return EMPTY;
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        String artifactId = mainAttributes.getValue("Project-ArtifactId");
        String groupId = mainAttributes.getValue("Project-GroupId");
        String version = mainAttributes.getValue("Project-Version");
        return new ModuleVersion(artifactId, groupId, version);
    }
}
