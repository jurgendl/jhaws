package org.swingeasy.formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.util.Locale;


/**
 * @author Jurgen
 */
public class DecimalFormatBuilder implements EFormatBuilder {
    protected String pattern;

    public DecimalFormatBuilder(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 
     * @see org.swingeasy.formatters.EFormatBuilder#build(java.util.Locale)
     */
    @Override
    public Format build(Locale locale) {
        return new DecimalFormat(this.pattern, new DecimalFormatSymbols(locale));
    }
}
