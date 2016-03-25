package org.swingeasy;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;

/**
 * @author Jurgen
 */
public class ETreeNodeEditor extends AbstractCellEditor implements TreeCellEditor {
    private static final long serialVersionUID = 6632821822151837356L;

    protected JTextField delegate = new JTextField();

    protected ETreeNode<?> editingNode;

    /**
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return this.editingNode;
    }

    /**
     * 
     * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
     */
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        this.editingNode = ETreeNode.class.cast(value);
        this.delegate.setText(this.editingNode.getStringValue());
        return this.delegate;
    }

    /**
     * 
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    @Override
    public boolean isCellEditable(EventObject e) {
        return super.isCellEditable(e);
    }
}