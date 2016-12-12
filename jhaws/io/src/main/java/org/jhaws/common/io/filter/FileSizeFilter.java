package org.jhaws.common.io.filter;

import org.jhaws.common.io.IOFile;

/**
 * compairs sizes fo files for filtering
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class FileSizeFilter extends FileFilter {
	/** Modifier */
	private Modifier mod;

	/** size */
	private long size;

	/**
	 * Creates a new FileSizeFilter object.
	 *
	 * @param description
	 *            description
	 * @param mod
	 *            Modifier
	 * @param size
	 *            size
	 */
	public FileSizeFilter(String description, Modifier mod, long size) {
		super(description);
		this.mod = mod;
		this.size = size;
	}

	/**
	 * Creates a new FileSizeFilter object.
	 *
	 * @param mod
	 *            Modifier
	 * @param size
	 *            size
	 */
	public FileSizeFilter(Modifier mod, long size) {
		this(mod.getDescription() + " " + size, mod, size); //$NON-NLS-1$
	}

	/**
	 *
	 * @see org.jhaws.common.io.filter.FileFilter#acceptRealFile(util.io.IOFile)
	 */
	@Override
	public final boolean acceptRealFile(IOFile f) {
		long compareTo = size;
		long len = f.length();

		if (Modifier.EQUALS.equals(mod)) {
			return len == compareTo;
		}

		if (Modifier.LARGER_OR_EQUAL.equals(mod)) {
			return len >= compareTo;
		}

		if (Modifier.LARGER_THAN.equals(mod)) {
			return len > compareTo;
		}

		if (Modifier.SMALLER_OR_EQUAL.equals(mod)) {
			return len <= compareTo;
		}

		if (Modifier.SMALLER_THAN.equals(mod)) {
			return len < compareTo;
		}

		// Modifier.DIFFERS.equals(mod)
		return len != compareTo;
	}
}
