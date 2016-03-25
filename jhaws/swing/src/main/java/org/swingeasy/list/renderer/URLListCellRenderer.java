package org.swingeasy.list.renderer;

import java.awt.Component;
import java.net.URL;

import javax.swing.JList;

public class URLListCellRenderer extends EListCellRenderer<URL> {
    private static final long serialVersionUID = -6503504630206227959L;

    public URLListCellRenderer() {
        super();
    }

    @Override
    protected Component render(JList<?> list, URL value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueHtml = "<html><a href='" + value + "'>" + value + "</a></html>";
        return this.super_getListCellRendererComponent(list, valueHtml, index, isSelected, cellHasFocus);
    }
}