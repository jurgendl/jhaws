package org.swingeasy;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author Jurgen
 */
public class ECheckBoxTreeNodeRenderer implements TreeCellRenderer {
    private JCheckBox delegate;

    public ECheckBoxTreeNodeRenderer() {
        this(new JCheckBox());
    }

    public ECheckBoxTreeNodeRenderer(JCheckBox delegate) {
        super();
        this.delegate = delegate;
        this.delegate.setOpaque(false);
    }

    /**
     * 
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     *      boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        @SuppressWarnings("rawtypes")
        ECheckBoxTreeNode node = ECheckBoxTreeNode.class.cast(value);

        this.delegate.setToolTipText(node.getTooltip());
        this.delegate.setText(node.getStringValue());
        this.delegate.setSelected(node.isSelected());
        this.delegate.setEnabled(tree.isEnabled());
        this.delegate.setComponentOrientation(tree.getComponentOrientation());

        return this.delegate;
    }
}
