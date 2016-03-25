package org.swingeasy.formatters;

import java.text.DateFormat;
import java.text.Format;
import java.util.Locale;


/**
 * @author Jurgen
 */
public class DateFormatBuilder implements EFormatBuilder {
    public static enum Length {
        Short, Long, Default;
    }

    public static enum Type {
        Date, Time, Both;
    }

    protected Type type;

    protected Length length;

    public DateFormatBuilder(Type type, Length length) {
        this.type = type;
        this.length = length;
    }

    /**
     * 
     * @see org.swingeasy.formatters.EFormatBuilder#build(java.util.Locale)
     */
    @Override
    public Format build(Locale locale) {
        switch (this.type) {
            case Date:
                switch (this.length) {
                    case Long:
                        return DateFormat.getDateInstance(DateFormat.LONG, locale);
                    case Short:
                        return DateFormat.getDateInstance(DateFormat.SHORT, locale);
                    case Default:
                    default:
                        return DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                }
            case Time:
                switch (this.length) {
                    case Long:
                        return DateFormat.getTimeInstance(DateFormat.LONG, locale);
                    case Short:
                        return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
                    case Default:
                    default:
                        return DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
                }
            case Both:
            default:
                switch (this.length) {
                    case Long:
                        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
                    case Short:
                        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
                    case Default:
                    default:
                        return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
                }
        }
    }
}
