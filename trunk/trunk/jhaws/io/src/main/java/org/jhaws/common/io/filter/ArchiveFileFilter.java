package org.jhaws.common.io.filter;

/**
 * filters only archive files
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public class ArchiveFileFilter extends FileExtensionFilter {
    private static final String[] ext = new String[0];

    private static String[] extensions() {
        return ArchiveFileFilter.ext;
    }

    /**
     * Creates a new ArchiveFileFilter object.
     */
    public ArchiveFileFilter() {
        this("archive files"); //$NON-NLS-1$
    }

    /**
     * Creates a new ArchiveFileFilter object.
     * 
     * @param description description
     */
    public ArchiveFileFilter(String description) {
        super(description, ArchiveFileFilter.extensions());
    }
}
