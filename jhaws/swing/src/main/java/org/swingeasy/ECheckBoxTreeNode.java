package org.swingeasy;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Jurgen
 */
public class ECheckBoxTreeNode<T> extends DefaultMutableTreeNode {
    private static final long serialVersionUID = -7007258738961493810L;

    protected boolean selected;

    public ECheckBoxTreeNode(T userObject) {
        this(userObject, false);
    }

    public ECheckBoxTreeNode(T userObject, boolean selected) {
        super(userObject);
        this.setSelected(selected);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) this.getUserObject();
    }

    public String getStringValue() {
        return String.valueOf(this.getUserObject());
    }

    public String getTooltip() {
        return this.getStringValue();
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void set(T value) {
        this.setUserObject(value);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        return this.getUserObject() == null ? "" : (this.getUserObject().toString() + "=" + this.selected); //$NON-NLS-1$
    }
}
