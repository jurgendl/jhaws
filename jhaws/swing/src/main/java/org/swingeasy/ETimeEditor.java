package org.swingeasy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JSpinner;

/**
 * @author Jurgen
 */
public class ETimeEditor extends EDateEditor {
    private static final long serialVersionUID = 75826218735013158L;

    /**
     * @see org.swingeasy.EDateEditor#createDateChooser()
     */
    @Override
    protected EDateTimeChooser createDateChooser() {
        return new EDateTimeChooser(DateTimeType.TIME);
    }

    /**
     * @see org.swingeasy.EDateEditor#getAction()
     */
    @Override
    protected String getAction() {
        return "pick-time";
    }

    /**
     * @see org.swingeasy.EDateEditor#getFormatter()
     */
    @Override
    public SimpleDateFormat getFormatter() {
        if (this.formatter == null) {
            this.formatter = SimpleDateFormat.class.cast(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()));
        }
        return this.formatter;
    }

    /**
     * @see org.swingeasy.EDateEditor#getInput()
     */
    @Override
    public ESpinner<Date> getInput() {
        if (this.input == null) {
            ESpinnerDateModel model = new ESpinnerDateModel();
            model.setValue(new Date());
            this.input = new ESpinner<Date>(model);
            this.input.setEditor(new JSpinner.DateEditor(this.input, this.getFormatter().toPattern()));
            this.input.setBorder(null);
        }
        return this.input;
    }

    /**
     * @see org.swingeasy.EDateEditor#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getLabel().setText(Messages.getString(l, "ETimeEditor.label.text") + ": ");
    }
}
