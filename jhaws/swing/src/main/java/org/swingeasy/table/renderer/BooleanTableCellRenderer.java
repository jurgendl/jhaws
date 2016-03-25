package org.swingeasy.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524
 */
public class BooleanTableCellRenderer extends ETableCellRenderer<Boolean> {
    private static final long serialVersionUID = 2577869717107398445L;

    private JCheckBox renderer;

    public BooleanTableCellRenderer() {
        this.renderer = new JCheckBox() {
            private static final long serialVersionUID = 2759192766733886746L;

            @Override
            protected void paintComponent(Graphics g) {
                BooleanTableCellRenderer.this.renderBackground(this, g);
                super.paintComponent(g);
            }
        };
        this.renderer.setHorizontalAlignment(SwingConstants.CENTER);
        this.renderer.setBorderPaintedFlat(true);
    }

    /**
     * @see org.swingeasy.table.renderer.ETableCellRenderer#render(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component render(JTable table, Boolean b, boolean isSelected, boolean hasFocus, int row, int column) {
        this.super_getTableCellRendererComponent(table, b, isSelected, hasFocus, row, column);
        if (b != null) {
            this.renderer.setSelected(b);
        } else {
            this.renderer.setSelected(false);
        }
        if (isSelected) {
            this.renderer.setForeground(table.getSelectionForeground());
            this.renderer.setBackground(table.getSelectionBackground());
        } else {
            Color bg = this.getBackground();
            this.renderer.setForeground(this.getForeground());
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure
            // why.
            this.renderer.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }
        this.renderer.setOpaque(false);
        return this.renderer;
    }
}