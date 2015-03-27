package org.jhaws.common.lang;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;

public class DateTime8 {
    public static char decimalSeperator;

    static {
        // http://stackoverflow.com/questions/4713166/decimal-separator-in-numberformat
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        decimalSeperator = symbols.getDecimalSeparator();
    }

    public static char getDecimalSeperator() {
        return decimalSeperator;
    }

    public static void setDecimalSeperator(char decimalSeperator) {
        DateTime8.decimalSeperator = decimalSeperator;
    }

    public static String print(Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.toMillis() / 1000;
        duration = duration.minusSeconds(seconds);
        long millis = duration.toMillis();
        StringBuilder formatted = new StringBuilder();
        if ((days != 0) || (formatted.length() > 0)) {
            formatted.append(days).append("d");
        }
        if ((hours != 0) || (formatted.length() > 0)) {
            formatted.append(hours).append("h");
        }
        if ((minutes != 0) || (formatted.length() > 0)) {
            formatted.append(minutes).append("m");
        }
        if ((seconds != 0) || (formatted.length() > 0)) {
            if (millis > 0) {
                formatted.append(seconds).append(getDecimalSeperator()).append(millis).append("s");
            } else {
                formatted.append(seconds).append("s");
            }
        } else {
            formatted.append(millis).append("ms");
        }
        return formatted.toString();
    }
}
