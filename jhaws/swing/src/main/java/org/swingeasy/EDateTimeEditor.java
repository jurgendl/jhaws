package org.swingeasy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JSpinner;

/**
 * @author Jurgen
 */
public class EDateTimeEditor extends EDateEditor {
	private static final long serialVersionUID = 1682632931152108808L;

	/**
	 * @see org.swingeasy.EDateEditor#createDateChooser()
	 */
	@Override
	protected EDateTimeChooser createDateChooser() {
		return new EDateTimeChooser(DateTimeType.DATE_TIME);
	}

	/**
	 * @see org.swingeasy.EDateEditor#getAction()
	 */
	@Override
	protected String getAction() {
		return "pick-date-time";//$NON-NLS-1$
	}

	/**
	 * @see org.swingeasy.EDateEditor#getFormatter()
	 */
	@Override
	public SimpleDateFormat getFormatter() {
		if (formatter == null) {
			formatter = SimpleDateFormat.class.cast(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()));
		}
		return formatter;
	}

	/**
	 * @see org.swingeasy.EDateEditor#getInput()
	 */
	@Override
	public ESpinner<Date> getInput() {
		if (input == null) {
			ESpinnerDateModel model = new ESpinnerDateModel();
			model.setValue(new Date());
			input = new ESpinner<>(model);
			input.setEditor(new JSpinner.DateEditor(input, getFormatter().toPattern()));
			input.setBorder(null);
		}
		return input;
	}

	/**
	 * @see org.swingeasy.EDateEditor#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		getLabel().setText(Messages.getString(l, "EDateTimeEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
	}
}
