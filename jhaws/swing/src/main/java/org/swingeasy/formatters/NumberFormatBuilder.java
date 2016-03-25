package org.swingeasy.formatters;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * @author Jurgen
 */
public class NumberFormatBuilder implements EFormatBuilder {
    public static enum Type {
        Default, Currency, Integer, Number, Percentage;
    }

    protected Type type;

    public NumberFormatBuilder(Type type) {
        this.type = type;
    }

    /**
     * 
     * @see org.swingeasy.formatters.EFormatBuilder#build(java.util.Locale)
     */
    @Override
    public Format build(Locale locale) {
        switch (this.type) {
            case Currency:
                return NumberFormat.getCurrencyInstance(locale);
            case Integer:
                return NumberFormat.getIntegerInstance(locale);
            case Number:
                return NumberFormat.getNumberInstance(locale);
            case Percentage:
                return NumberFormat.getPercentInstance(locale);
            case Default:
            default:
                return NumberFormat.getInstance(locale);
        }
    }
}
