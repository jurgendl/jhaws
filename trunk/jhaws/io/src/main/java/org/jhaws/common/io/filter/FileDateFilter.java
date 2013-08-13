package org.jhaws.common.io.filter;

import java.io.File;
import java.util.Date;

/**
 * compairs the date of file for filtering
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public class FileDateFilter extends AbstractFileFilter {
    /** date */
    private Date date;

    /** modifier */
    private Modifier mod;

    /**
     * Creates a new FileDateFilter object.
     * 
     * @param description description
     * @param mod Modifier
     * @param date date
     */
    public FileDateFilter(String description, Modifier mod, Date date) {
        super(description);
        this.mod = mod;
        this.date = date;
    }

    /**
     * Creates a new FileDateFilter object.
     * 
     * @param mod Modifier
     * @param date Date
     */
    public FileDateFilter(Modifier mod, Date date) {
        super(mod.getDescription() + " " + date.getTime()); //$NON-NLS-1$
        this.mod = mod;
        this.date = date;
    }

    /**
     * 
     * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
     */
    @Override
    public final boolean acceptFile(File f) {
        long compareTo = date.getTime();
        long lastMod = f.lastModified();

        if (Modifier.EQUALS.equals(mod)) {
            return lastMod == compareTo;
        }

        if (Modifier.LARGER_OR_EQUAL.equals(mod)) {
            return lastMod >= compareTo;
        }

        if (Modifier.LARGER_THAN.equals(mod)) {
            return lastMod > compareTo;
        }

        if (Modifier.SMALLER_OR_EQUAL.equals(mod)) {
            return lastMod <= compareTo;
        }

        if (Modifier.SMALLER_THAN.equals(mod)) {
            return lastMod < compareTo;
        }

        // Modifier.DIFFERS.equals(mod)
        return lastMod != compareTo;
    }
}
