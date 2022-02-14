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
        newFormatter();
    }

    protected void newFormatter() {
        Locale locale = getLocale();
        formatter = NumberFormat.getInstance(locale);
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        newFormatter();
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    @Override
    protected void setValue(Object value) {
        setText(value == null ? "" : formatter.format(value)); //$NON-NLS-1$
    }
}