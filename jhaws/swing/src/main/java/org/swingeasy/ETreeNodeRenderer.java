package org.swingeasy;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Jurgen
 */
public class ETreeNodeRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 157224120324383687L;

    protected boolean focused = false;

    protected int lastSelected = -1;

    protected Color oldBackgroundNonSelectionColor;

    protected Color focusColor;

    public Color getFocusColor() {
        return focusColor;
    }

    public int getLastSelected() {
        return lastSelected;
    }

    /**
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focusOverridden) {
        focused = row == lastSelected;
        init();
        if (focused && focusColor != null) {
            setBackgroundNonSelectionColor(focusColor);
        } else {
            setBackgroundNonSelectionColor(oldBackgroundNonSelectionColor);
        }
        ETreeNode<?> node = ETreeNode.class.cast(value);
        super.getTreeCellRendererComponent(tree, node == null ? null : node.getStringValue(), sel, expanded, leaf, row, false);
        setToolTipText(node == null ? null : node.getTooltip());
        return this;
    }

    protected void init() {
        if (focusColor != null && oldBackgroundNonSelectionColor != null) {
            return;
        }
        oldBackgroundNonSelectionColor = getBackgroundNonSelectionColor();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
    }

    public void setLastSelected(int lastSelected) {
        this.lastSelected = lastSelected;
    }
}
