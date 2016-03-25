package org.swingeasy.table.renderer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.swingeasy.EComponentI;
import org.swingeasy.EComponentRenderer;

/**
 * @author Jurgen
 */
public class ETableCellRenderer<T> extends DefaultTableCellRenderer.UIResource implements EComponentI {
    private static final long serialVersionUID = 2588181759941994912L;

    protected EComponentRenderer backgroundRenderer;

    public EComponentRenderer getBackgroundRenderer() {
        return this.backgroundRenderer;
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @SuppressWarnings("unchecked")
    @Override
    final public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.render(table, (T) value, isSelected, hasFocus, row, column);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        this.renderBackground(this, g);
        super.paintComponent(g);
    }

    public Component render(JTable table, T value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.super_getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    protected void renderBackground(JComponent c, Graphics g) {
        if (this.backgroundRenderer != null) {
            this.backgroundRenderer.render(c, g);
        }
    }

    public void setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        this.backgroundRenderer = backgroundRenderer;
    }

    protected Component super_getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
