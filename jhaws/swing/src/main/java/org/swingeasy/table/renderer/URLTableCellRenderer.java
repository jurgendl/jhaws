package org.swingeasy.table.renderer;

import java.awt.Component;
import java.net.URL;

import javax.swing.JTable;

public class URLTableCellRenderer extends ETableCellRenderer<URL> {
    private static final long serialVersionUID = 5347735590867783815L;

    public URLTableCellRenderer() {
        super();
    }

    /**
     * @see org.swingeasy.table.renderer.ETableCellRenderer#render(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component render(JTable table, URL value, boolean isSelected, boolean hasFocus, int row, int column) {
        String valueHtml = "<html><a href='" + value + "'>" + value + "</a></html>";
        return this.super_getTableCellRendererComponent(table, valueHtml, isSelected, hasFocus, row, column);
    }
}