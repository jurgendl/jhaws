package org.swingeasy.list.renderer;

import java.awt.Component;
import java.net.URI;

import javax.swing.JList;

public class URIListCellRenderer extends EListCellRenderer<URI> {
    private static final long serialVersionUID = -6503504630206227959L;

    public URIListCellRenderer() {
        super();
    }

    @Override
    protected Component render(JList<?> list, URI value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueHtml = "<html><a href='" + value + "'>" + value + "</a></html>";
        return this.super_getListCellRendererComponent(list, valueHtml, index, isSelected, cellHasFocus);
    }
}