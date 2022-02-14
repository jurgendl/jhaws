package org.jhaws.common.io.filter;

import java.io.File;

/**
 * regular expression filter against simple file name
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class FileNameFilter extends RegexFileFilter {
    /**
     * Creates a new FileNameFilter object.
     *
     * @param description description
     * @param regex regular expression
     * @param caseSensitive Case
     */
    public FileNameFilter(String description, String regex, Case caseSensitive) {
        super(description, regex, caseSensitive);
    }

    /**
     * Creates a new FileNameFilter object.
     *
     * @param description description
     * @param regex regular expression
     */
    public FileNameFilter(String description, String regex) {
        super(description, regex, Case.INSENSITIVE);
    }

    /**
     * Creates a new FileNameFilter object.
     *
     * @param regex regular expression
     * @param caseSensitive Case
     */
    public FileNameFilter(String regex, Case caseSensitive) {
        super(regex, caseSensitive);
    }

    /**
     * Creates a new FileNameFilter object.
     *
     * @param regex regular expression
     */
    public FileNameFilter(String regex) {
        super(regex, Case.INSENSITIVE);
    }

    /**
     * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
     */
    @Override
    public final boolean acceptFile(File f) {
        return acceptFile(f.getName());
    }
}
