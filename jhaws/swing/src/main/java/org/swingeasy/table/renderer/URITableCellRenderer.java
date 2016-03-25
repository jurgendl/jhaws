package org.swingeasy.table.renderer;

import java.awt.Component;
import java.net.URI;

import javax.swing.JTable;

public class URITableCellRenderer extends ETableCellRenderer<URI> {
    private static final long serialVersionUID = 5347735590867783815L;

    public URITableCellRenderer() {
        super();
    }

    /**
     * @see org.swingeasy.table.renderer.ETableCellRenderer#render(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component render(JTable table, URI value, boolean isSelected, boolean hasFocus, int row, int column) {
        String valueHtml = "<html><a href='" + value + "'>" + value + "</a></html>";
        return this.super_getTableCellRendererComponent(table, valueHtml, isSelected, hasFocus, row, column);
    }
}