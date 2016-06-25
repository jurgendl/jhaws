package org.jhaws.common.io.filter;

import org.jhaws.common.io.IODirectory;

/**
 * accepts all directories but only directories
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class OnlyDirectoriesFileFilter extends DirectoryFilter {
	/**
	 * Creates a new OnlyDirectoriesFileFilter object.
	 */
	public OnlyDirectoriesFileFilter() {
		this("all directories"); //$NON-NLS-1$
	}

	/**
	 * Creates a new OnlyDirectoriesFileFilter object.
	 * 
	 * @param description
	 *            description
	 */
	public OnlyDirectoriesFileFilter(String description) {
		super(description);
	}

	/**
	 * 
	 * @see org.jhaws.common.io.filter.DirectoryFilter#acceptDirectory(util.io.IODirectory)
	 */
	@Override
	public final boolean acceptDirectory(IODirectory f) {
		return true;
	}
}
