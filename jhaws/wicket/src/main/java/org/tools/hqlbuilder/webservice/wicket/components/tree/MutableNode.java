package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class MutableNode<T> implements DataNode<T> {
    private static final long serialVersionUID = -8078160385496330700L;

    protected T object;

    protected List<Node<T>> children = new ArrayList<>();

    public MutableNode() {
        super();
    }

    public MutableNode(T object) {
        this();
        this.object = object;
    }

    public void add(Node<T> node) {
        this.children.add(node);
    }

    @Override
    public Boolean checked() {
        return false;
    }

    @Override
    public List<Node<T>> children() {
        return this.children;
    }

    @Override
    public int compareTo(DataNode<T> o) {
        return new CompareToBuilder().append(this.name(), o.name()).toComparison();
    }

    @Override
    public T get() {
        return this.object;
    }

    @Override
    public String name() {
        return String.valueOf(this.get());
    }

    public void remove(Node<T> node) {
        this.children.remove(node);
    }

    public void set(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return this.name();
    }

    @Override
    public String value() {
        return String.valueOf(this.get());
    }

    @Override
    public Node<T> getChild(int i) {
        return this.children().get(i);
    }

    @Override
    public int getChildCount() {
        return this.children().size();
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return this.children().iterator();
    }
}
