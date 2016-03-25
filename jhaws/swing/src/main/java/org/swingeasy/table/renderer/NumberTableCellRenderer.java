package org.swingeasy.table.renderer;

import java.text.NumberFormat;
import java.util.Locale;

// javax.swing.text.NumberFormatter
/**
 * @author Jurgen
 */
public class NumberTableCellRenderer extends ETableCellRenderer<Number> {
    private static final long serialVersionUID = 5169127745067354714L;

    protected NumberFormat formatter;

    public NumberTableCellRenderer() {
        this.newFormatter();
    }

    protected void newFormatter() {
        Locale locale = this.getLocale();
        this.formatter = NumberFormat.getInstance(locale);
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

    /**
     * 
     * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    @Override
    protected void setValue(Object value) {
        this.setText((value == null) ? "" : this.formatter.format(value)); //$NON-NLS-1$
    }
}