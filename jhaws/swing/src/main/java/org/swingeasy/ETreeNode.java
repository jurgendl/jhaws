package org.swingeasy;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author Jurgen
 */
public class ETreeNode<T> extends DefaultMutableTreeNode {
    private static final long serialVersionUID = -437035560334777359L;

    /**
     * treenode with lazy init of children
     */
    public ETreeNode(T userObject) {
        super(userObject);
        children = null; // not initialized (lazy)
    }

    /**
     * treenode with easger init of children, give an empty collection to make a childless eager treenode
     */
    public ETreeNode(T userObject, Collection<ETreeNode<T>> children) {
        super(userObject);
        this.children = new Vector<>(children); // initialized (eager)
    }

    // @Override// public Enumeration<ETreeNode<T>> children() {// return
    // this.getChildren().elements();// }
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<TreeNode> children() { // XXX JAVA 9
        return Enumeration.class.cast(this.getChildren().elements()); // XXX JAVA 9
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) getUserObject();
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#getChildAt(int)
     */
    @Override
    public TreeNode getChildAt(int index) {
        return this.getChildren().elementAt(index);
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#getChildCount()
     */
    @Override
    public int getChildCount() {
        return this.getChildren().size();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Vector<ETreeNode<T>> getChildren() {
        if (getAllowsChildren() && children == null) {
            children = new Vector<>();
            this.initChildren((List) children); // XXX JAVA 9
        }

        return ((Vector) children); // XXX JAVA 9
    }

    public String getStringValue() {
        return String.valueOf(getUserObject());
    }

    public String getTooltip() {
        return this.getStringValue();
    }

    /**
     * called when children is not initialized an {@link #getAllowsChildren()} is true (default)
     * 
     * @param list
     */
    protected void initChildren(List<ETreeNode<T>> list) {
        // override
    }

    public boolean isInitialized() {
        return children != null;
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return !getAllowsChildren();
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
     */
    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
        children = null;
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(getUserObject()) + "=" + this.isInitialized(); //$NON-NLS-1$
    }
}
