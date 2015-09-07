package org.jhaws.common.net.client.tmp;

import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;

public class PostParams extends PutParams {
    private static final long serialVersionUID = -3939699621850878105L;

    private HashMap<String, Path> attachments = new HashMap<String, Path>();

    private String name;

    public PostParams() {
        super();
    }

    public PostParams(URI uri) {
        super(uri);
    }

    public PostParams(String uri) {
        super(uri);
    }

    public PostParams(String uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PostParams(URI uri, String body, String mime) {
        super(uri, body, mime);
    }

    public HashMap<String, Path> getAttachments() {
        return attachments;
    }

    public void setAttachments(HashMap<String, Path> attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
