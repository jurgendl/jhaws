package org.swingeasy;

import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.swing.SpinnerDateModel;
import java.util.Date;

/**
 * @author Jurgen
 */
public class ESpinnerDateModel extends SpinnerDateModel {
    private static final long serialVersionUID = 3829866433193892549L;

    /**
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
     * @see javax.swing.SpinnerDateModel#getValue()
     */
    @Override
    public Object getValue() {
        return super.getValue();
    }

    /**
     * @see javax.swing.SpinnerDateModel#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        if (!new EqualsBuilder().append(value, getDate()).isEquals()) {
            super.setValue(value);
            fireStateChanged();
        }
    }
}