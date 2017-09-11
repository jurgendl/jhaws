package org.jhaws.common.math;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TreeModel<T> implements Iterable<TreeNode<T>>, Serializable {
    private static final long serialVersionUID = -2347798526303375015L;

    protected Set<TreeNode<T>> nodes = new HashSet<>();

    protected TreeNode<T> root;

    public TreeNode<T> root() {
        return root;
    }

    public Set<TreeNode<T>> nodes() {
        return nodes;
    }

    public TreeNode<T> parent(TreeNode<T> node) {
        return node.parent;
    }

    public List<TreeNode<T>> children(TreeNode<T> node) {
        return node.children;
    }

    public boolean isBranch(TreeNode<T> node) {
        return node.children != null && !node.children.isEmpty();
    }

    public boolean isLeaf(TreeNode<T> node) {
        return node.children == null && node.children.isEmpty();
    }

    public boolean isRoot(TreeNode<T> node) {
        return node == root;
    }

    public Set<TreeNode<T>> getNodes() {
        return this.nodes;
    }

    public void setNodes(Set<TreeNode<T>> nodes) {
        this.nodes = nodes;
    }

    public TreeNode<T> getRoot() {
        return this.root;
    }

    public void setRoot(TreeNode<T> root) {
        this.root = root;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return nodes.iterator();
    }
}
