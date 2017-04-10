package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node<T> extends Value<T> implements Iterable<Node<T>> {
    private static final long serialVersionUID = -970998500165039417L;

    protected Node<T> parent;

    protected final List<Node<T>> children = new ArrayList<>();

    public Node() {
        super();
    }

    public Node(T value) {
        super(value);
    }

    public Node(Node<T> parent, T value) {
        super(value);
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Node<T> child) {
        children.add(child);
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return children.iterator();
    }

    public List<Node<T>> getChildren() {
        return this.children;
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return super.toString();
        // if (children.isEmpty()) return super.toString();
        // return super.toString() + "\n\t" + children.stream().map(Node::getValue).map(Object::toString).collect(Collectors.joining("\n\t"));
    }
}