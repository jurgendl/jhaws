package org.swingeasy.list.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JList;

import org.swingeasy.DateTimeType;

// javax.swing.text.DateFormatter
/**
 * @author Jurgen
 */
public class DateTimeListCellRenderer extends EListCellRenderer<Date> {
    private static final long serialVersionUID = -1237135359072682865L;

    protected DateFormat formatter;

    protected DateTimeType type;

    public DateTimeListCellRenderer() {
        this(DateTimeType.DATE_TIME);
    }

    public DateTimeListCellRenderer(DateTimeType type) {
        this.type = type;
        this.newFormatter();
    }

    protected String getValue(Date value) {
        return ((value == null) ? "" : this.formatter.format(value)); //$NON-NLS-1$
    }

    protected void newFormatter() {
        switch (this.type) {
            case DATE:
                this.formatter = DateFormat.getDateInstance(DateFormat.LONG, this.getLocale());
                break;
            case DATE_TIME:
                this.formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, this.getLocale());
                break;
            case TIME:
                this.formatter = DateFormat.getTimeInstance(DateFormat.LONG, this.getLocale());
                break;
        }
    }

    /**
     * @see org.swingeasy.list.renderer.EListCellRenderer#render(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    protected Component render(JList<?> list, Date value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = this.getValue(value);
        Component tmp = this.super_getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        this.setToolTipText(text);
        return tmp;
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
