package org.swingeasy.list.renderer;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JList;

// javax.swing.text.NumberFormatter
/**
 * @author Jurgen
 */
public class NumberListCellRenderer extends EListCellRenderer<Number> {
    private static final long serialVersionUID = 5169127745067354714L;

    protected NumberFormat formatter;

    public NumberListCellRenderer() {
        this.newFormatter();
    }

    protected String getValue(Number value) {
        return ((value == null) ? "" : this.formatter.format(value)); //$NON-NLS-1$
    }

    protected void newFormatter() {
        this.formatter = NumberFormat.getInstance(this.getLocale());
    }

    /**
     * @see org.swingeasy.list.renderer.EListCellRenderer#render(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component render(JList<?> list, Number value, int index, boolean isSelected, boolean cellHasFocus) {
        return this.super_getListCellRendererComponent(list, this.getValue(value), index, isSelected, cellHasFocus);
    }

    /**
     *
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.newFormatter();
    }
}