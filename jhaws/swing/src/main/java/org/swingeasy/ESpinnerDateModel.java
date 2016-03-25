package org.swingeasy;

import java.util.Calendar;
import java.util.Date;

import javax.swing.SpinnerDateModel;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @author Jurgen
 */
public class ESpinnerDateModel extends SpinnerDateModel {
    private static final long serialVersionUID = 3829866433193892549L;

    protected static final String VALUE = "value";

    protected final ObjectWrapper ow;

    public ESpinnerDateModel() {
        this.ow = new ObjectWrapper(this);
    }

    /**
     *
     * @see javax.swing.SpinnerDateModel#getNextValue()
     */
    @Override
    public Object getNextValue() {
        try {
            return super.getNextValue();
        } catch (NullPointerException ex) {
            return new Date();
        }
    }

    /**
     *
     * @see javax.swing.SpinnerDateModel#getPreviousValue()
     */
    @Override
    public Object getPreviousValue() {
        try {
            return super.getPreviousValue();
        } catch (NullPointerException ex) {
            return new Date();
        }
    }

    /**
     *
     * @see javax.swing.SpinnerDateModel#getValue()
     */
    @Override
    public Object getValue() {
        Calendar calendarValue = this.ow.get(ESpinnerDateModel.VALUE, Calendar.class);
        return calendarValue == null ? null : super.getValue();
    }

    /**
     *
     * @see javax.swing.SpinnerDateModel#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        Calendar calendarValue = this.ow.get(ESpinnerDateModel.VALUE, Calendar.class);
        if (!new EqualsBuilder().append(value, calendarValue == null ? null : calendarValue.getTime()).isEquals()) {
            if (value == null) {
                this.ow.unset(ESpinnerDateModel.VALUE);
            } else {
                if (calendarValue == null) {
                    calendarValue = Calendar.getInstance();
                    this.ow.set(ESpinnerDateModel.VALUE, calendarValue);
                }
                calendarValue.setTime((Date) value);
            }
            this.fireStateChanged();
        }
    }
}