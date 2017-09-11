package org.jhaws.common.math;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>>, Serializable {
    private static final long serialVersionUID = -9031743102377745141L;

    protected TreeNode<T> parent;

    protected T item;

    protected List<TreeNode<T>> children;

    public TreeNode(T item) {
        this.item = item;
    }

    public TreeNode(TreeNode<T> previous, T item) {
        this(item);
        if (previous != null) {
            parent = previous;
            previous.children.add(this);
        }
    }

    public TreeNode(T item, TreeNode<T> next) {
        this(item);
        if (next != null) {
            next.parent = this;
            children.add(next);
        }
    }

    public TreeNode<T> parent() {
        return this.parent;
    }

    public void parent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T item() {
        return this.item;
    }

    public void item(T item) {
        this.item = item;
    }

    public List<TreeNode<T>> children() {
        return this.children;
    }

    public TreeNode<T> getParent() {
        return this.parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<TreeNode<T>> getChildren() {
        return this.children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return children.iterator();
    }
}
