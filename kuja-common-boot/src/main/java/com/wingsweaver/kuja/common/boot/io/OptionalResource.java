package com.wingsweaver.kuja.common.boot.io;

import com.wingsweaver.kuja.common.utils.support.Optional;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

/**
 * 可选（非必须） Resource 的定义。
 *
 * @author wingsweaver
 */
public class OptionalResource implements Resource, Optional {
    public static final String PREFIX_OPTIONAL = "optional:";

    private final Resource resource;

    public OptionalResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return PREFIX_OPTIONAL + this.resource.toString();
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public boolean exists() {
        return this.resource.exists();
    }

    @Override
    public boolean isReadable() {
        return this.resource.isReadable();
    }

    @Override
    public boolean isOpen() {
        return this.resource.isOpen();
    }

    @Override
    public boolean isFile() {
        return this.resource.isFile();
    }

    @SuppressWarnings({"NullableProblems", "PMD.LowerCamelCaseVariableNamingRule"})
    @Override
    public URL getURL() throws IOException {
        return this.resource.getURL();
    }

    @SuppressWarnings({"NullableProblems", "PMD.LowerCamelCaseVariableNamingRule"})
    @Override
    public URI getURI() throws IOException {
        return this.resource.getURI();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public File getFile() throws IOException {
        return this.resource.getFile();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        return this.resource.readableChannel();
    }

    @Override
    public long contentLength() throws IOException {
        return this.resource.contentLength();
    }

    @Override
    public long lastModified() throws IOException {
        return this.resource.lastModified();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return this.resource.createRelative(relativePath);
    }

    @Override
    public String getFilename() {
        return this.resource.getFilename();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String getDescription() {
        return this.resource.getDescription();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public InputStream getInputStream() throws IOException {
        return this.resource.getInputStream();
    }

    public Resource getResource() {
        return resource;
    }
}
