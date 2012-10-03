package org.jhaws.common.io.filter;

import java.util.regex.Pattern;

/**
 * case sensitivity
 * 
 * @version 2.0.0 - 27 June 2006
 * 
 * @author Jurgen De Landsheer
 */
public enum Case {
    /** case sensitive */
    SENSITIVE,
    /** case insensitive */
    INSENSITIVE;
    /**
     * na
     * 
     * @return
     * 
     * @throws IllegalArgumentException na
     */
    public int getPattern() {
        switch (this) {
            case SENSITIVE:
                return Pattern.CANON_EQ;

            case INSENSITIVE:
                return Pattern.CASE_INSENSITIVE;

            default:
                throw new IllegalArgumentException();
        }
    }
}
