package org.jhaws.common.net.client;

import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;

public class PostRequest extends AbstractPutRequest<PostRequest> {
	private static final long serialVersionUID = -3939699621850878105L;

	private HashMap<String, Path> attachments = new HashMap<String, Path>();

	private String name;

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

	public HashMap<String, Path> getAttachments() {
		return attachments;
	}

	public PostRequest setAttachments(HashMap<String, Path> attachments) {
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
}
