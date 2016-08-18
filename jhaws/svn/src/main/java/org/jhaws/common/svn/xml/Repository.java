package org.jhaws.common.svn.xml;

public class Repository {
	private String root;

	private String uuid;

	public String getRoot() {
		return root;
	}

	public String getUuid() {
		return uuid;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Repository [" + (root != null ? "root=" + root + ", " : "") + (uuid != null ? "uuid=" + uuid : "") + "]";
	}
}
