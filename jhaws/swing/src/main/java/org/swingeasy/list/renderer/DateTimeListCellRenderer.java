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
        newFormatter();
    }

    protected String getValue(Date value) {
        return value == null ? "" : formatter.format(value); //$NON-NLS-1$
    }

    protected void newFormatter() {
        switch (type) {
            case DATE:
                formatter = DateFormat.getDateInstance(DateFormat.LONG, getLocale());
                break;
            case DATE_TIME:
                formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, getLocale());
                break;
            case TIME:
                formatter = DateFormat.getTimeInstance(DateFormat.LONG, getLocale());
                break;
        }
    }

    /**
     * @see org.swingeasy.list.renderer.EListCellRenderer#render(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    protected Component render(JList<?> list, Date value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = getValue(value);
        Component tmp = super_getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        setToolTipText(text);
        return tmp;
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        newFormatter();
    }
}
