package org.jhaws.common.io.filter;

import java.io.File;

/**
 * accepts all files and directories
 * 
 * @author Jurgen De Landsheer
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public class AllFilesFilter extends AbstractFileFilter {
    /**
     * Creates a new AllFilesFilter object.
     * 
     * @param description description
     */
    public AllFilesFilter(String description) {
        super(description);
    }

    /**
     * Creates a new AllFilesFilter object.
     */
    public AllFilesFilter() {
        this("all files and directories"); //$NON-NLS-1$
    }

    /**
     * 
     * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
     */
    @Override
    public final boolean acceptFile(File f) {
        return true;
    }
}
