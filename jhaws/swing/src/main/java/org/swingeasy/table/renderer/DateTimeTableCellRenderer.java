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
		newFormatter();
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
	 * @see org.swingeasy.table.renderer.ETableCellRenderer#render(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component render(JTable table, Date value, boolean isSelected, boolean hasFocus, int row, int column) {
		return super_getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	/**
	 * 
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		newFormatter();
	}

	/**
	 * 
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		String text = value == null ? "" : formatter.format(value);
		setText(text);
		setToolTipText(text);
	}
}
