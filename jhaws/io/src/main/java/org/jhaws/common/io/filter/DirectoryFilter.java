package org.jhaws.common.io.filter;

import java.io.File;

import org.jhaws.common.io.IODirectory;

/**
 * abstract, accepts only directories but needs inheritant function
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public abstract class DirectoryFilter extends AbstractFileFilter {
    /**
     * Creates a new DirectoryFilter object.
     *
     * @param description description
     */
    protected DirectoryFilter(String description) {
        super(description);
    }

    /**
     *
     * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
     */
    @Override
    public final boolean acceptFile(File f) {
        return f.isDirectory() && acceptDirectory(new IODirectory(f));
    }

    /**
     * accepts this directory?
     *
     * @param f IODirectory
     *
     * @return boolean
     */
    public abstract boolean acceptDirectory(IODirectory f);
}
