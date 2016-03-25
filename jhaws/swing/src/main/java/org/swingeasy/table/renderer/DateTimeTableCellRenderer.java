package org.swingeasy.table.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTable;

import org.swingeasy.DateTimeType;

// javax.swing.text.DateFormatter
/**
 * @author Jurgen
 */
public class DateTimeTableCellRenderer extends ETableCellRenderer<Date> {
    private static final long serialVersionUID = -8217402048878663776L;

    protected DateFormat formatter;

    protected DateTimeType type;

    public DateTimeTableCellRenderer() {
        this(DateTimeType.DATE_TIME);
    }

    public DateTimeTableCellRenderer(DateTimeType type) {
        this.type = type;
        this.newFormatter();
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
     * @see org.swingeasy.table.renderer.ETableCellRenderer#render(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component render(JTable table, Date value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.super_getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
    public void setValue(Object value) {
        String text = (value == null) ? "" : this.formatter.format(value);
        this.setText(text);
        this.setToolTipText(text);
    }
}
