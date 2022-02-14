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
        return (T) getUserObject();
    }

    public String getStringValue() {
        return String.valueOf(getUserObject());
    }

    public String getTooltip() {
        return this.getStringValue();
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void set(T value) {
        setUserObject(value);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        return getUserObject() == null ? "" : getUserObject().toString() + "=" + this.selected; //$NON-NLS-1$
    }
}
