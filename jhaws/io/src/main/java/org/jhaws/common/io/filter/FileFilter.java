package org.jhaws.common.io.filter;

import java.io.File;

import org.jhaws.common.io.IOFile;

/**
 * abstract, accepts only real files but needs inheritant function
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public abstract class FileFilter extends AbstractFileFilter {
	/**
	 * Creates a new FileFilter object.
	 *
	 * @param description
	 *            description
	 */
	protected FileFilter(String description) {
		super(description);
	}

	/**
	 *
	 * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
	 */
	@Override
	public final boolean acceptFile(File f) {
		return !f.isDirectory() && acceptRealFile(new IOFile(f));
	}

	/**
	 * na
	 *
	 * @param f
	 *            na
	 *
	 * @return
	 */
	public abstract boolean acceptRealFile(IOFile f);
}
