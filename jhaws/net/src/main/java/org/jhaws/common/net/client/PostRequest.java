package org.jhaws.common.net.client;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class PostRequest extends AbstractPutRequest<PostRequest> {
    private static final long serialVersionUID = -3939699621850878105L;

    protected Map<String, Path> attachments = new LinkedHashMap<>();

    protected String name;

    protected Supplier<InputStream> stream;

    public PostRequest() {
        super();
    }

    public PostRequest(URI uri) {
        super(uri);
    }

    public PostRequest(String uri) {
        super(uri);
    }

    public PostRequest(String uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PostRequest(URI uri, String body, String mime) {
        super(uri, body, mime);
    }

    public Map<String, Path> getAttachments() {
        return Collections.unmodifiableMap(attachments);
    }

    public PostRequest setAttachments(Map<String, Path> attachments) {
        this.attachments = attachments;
        return cast();
    }

    public String getName() {
        return name;
    }

    public PostRequest setName(String name) {
        this.name = name;
        return cast();
    }

    public PostRequest addAttachment(String filename, Path path) {
        if (attachments.containsKey(filename) && !new EqualsBuilder().append(attachments.get(filename), path).isEquals()) {
            throw new IllegalArgumentException(filename);
        }
        this.attachments.put(filename, path);
        return cast();
    }

    public Supplier<InputStream> getStream() {
        return this.stream;
    }

    public PostRequest setStream(Supplier<InputStream> stream) {
        this.stream = stream;
        return cast();
    }
}
