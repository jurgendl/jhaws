package org.jhaws.common.io.filter;

import org.jhaws.common.io.IOFile;

/**
 * accepts all real files but only directories
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class OnlyFilesFilter extends FileFilter {
    /**
     * Creates a new OnlyFilesFilter object.
     */
    public OnlyFilesFilter() {
        this("all files"); //$NON-NLS-1$
    }

    /**
     * Creates a new OnlyFilesFilter object.
     *
     * @param description description
     */
    public OnlyFilesFilter(String description) {
        super(description);
    }

    /**
     *
     * @see org.jhaws.common.io.filter.FileFilter#acceptRealFile(util.io.IOFile)
     */
    @Override
    public final boolean acceptRealFile(IOFile f) {
        return true;
    }
}
