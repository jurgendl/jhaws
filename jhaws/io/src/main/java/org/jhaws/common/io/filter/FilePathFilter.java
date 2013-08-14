package org.jhaws.common.io.filter;

import java.io.File;

/**
 * regular expression filter against parent full directory name
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public class FilePathFilter extends RegexFileFilter {
    /**
     * Creates a new FilePathFilter object.
     * 
     * @param description description
     * @param regex regular expression
     * @param caseSensitive Case
     */
    public FilePathFilter(String description, String regex, Case caseSensitive) {
        super(description, regex, caseSensitive);
    }

    /**
     * Creates a new FilePathFilter object.
     * 
     * @param description description
     * @param regex regular expression
     */
    public FilePathFilter(String description, String regex) {
        super(description, regex, Case.INSENSITIVE);
    }

    /**
     * Creates a new FilePathFilter object.
     * 
     * @param regex regular expression
     * @param caseSensitive Case
     */
    public FilePathFilter(String regex, Case caseSensitive) {
        super(regex, caseSensitive);
    }

    /**
     * Creates a new FilePathFilter object.
     * 
     * @param regex regular expression
     */
    public FilePathFilter(String regex) {
        super(regex, Case.INSENSITIVE);
    }

    /**
     * 
     * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
     */
    @Override
    public final boolean acceptFile(File f) {
        return acceptFile(f.getParentFile().getAbsolutePath());
    }
}
