package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathNode extends MutableNode<Path> {
	private static final long serialVersionUID = -1692719839334996632L;

	protected PathNode parent;

	public PathNode(File file) {
		this(Paths.get(file.getAbsolutePath()));
	}

	public PathNode(Path path) {
		super(path);
	}

	public PathNode(String path) {
		this(Paths.get(path));
	}

	public PathNode(URI uri) {
		this(Paths.get(uri));
	}

	@Override
	public void add(Node<Path> node) {
		super.add(node);
		PathNode.class.cast(node).parent = this;
	}

	@Override
	public String name() {
		return get().getFileName().toString();
	}

	public PathNode parent() {
		return parent;
	}

	@Override
	public void remove(Node<Path> node) {
		super.remove(node);
		PathNode.class.cast(node).parent = null;
	}

	@Override
	public String toString() {
		return value() + (parent == null ? "" : " <- " + parent.value());
	}

	@Override
	public String value() {
		return get().toString();
	}
}
