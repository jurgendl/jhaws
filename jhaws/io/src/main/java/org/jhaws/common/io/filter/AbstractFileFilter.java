package org.jhaws.common.io.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * General abstract file filter.<br>
 * Extends, implements javax.swing.filechooser.FileFilter, java.io.FileFilter and java.io.FilenameFilter. Supports chaining with AND, OR, NOT.<br>
 * <br>
 * <br>
 * <b><u>version history</u></b><br>
 * 2.0.0:<br>
 * - moved all filter classes to same jar<br>
 * - moved filter to other packages<br>
 * - reimplemented case sensitivity and filter chaining (and/or/not)<br>
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see java.io.FileFilter
 * @see java.io.FilenameFilter
 * @see javax.swing.filechooser.FileFilter
 */
@Deprecated
@SuppressWarnings("deprecation")
public abstract class AbstractFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter, FilenameFilter {
	/**
	 * na
	 *
	 * @param filter
	 *            na
	 *
	 * @return
	 */
	public static AbstractFileFilter convert(final java.io.FileFilter filter) {
		return new AbstractFileFilter("") { //$NON-NLS-1$
			@Override
			public boolean acceptFile(File f) {
				return filter.accept(f);
			}
		};
	}

	/**
	 * na
	 *
	 * @param filter
	 *            na
	 *
	 * @return
	 */
	public static AbstractFileFilter convert(final java.io.FilenameFilter filter) {
		return new AbstractFileFilter("") { //$NON-NLS-1$
			@Override
			public boolean acceptFile(File f) {
				return filter.accept(f.getParentFile(), f.getName());
			}
		};
	}

	/**
	 * na
	 *
	 * @param filter
	 *            na
	 *
	 * @return
	 */
	public static AbstractFileFilter convert(final javax.swing.filechooser.FileFilter filter) {
		return new AbstractFileFilter(filter.getDescription()) {
			@Override
			public boolean acceptFile(File f) {
				return filter.accept(f);
			}
		};
	}

	/** description */
	private String description = ""; //$NON-NLS-1$

	/** NOT */
	private boolean not;

	/**
	 * Creates a new AbstractFileFilter object.
	 *
	 * @param description
	 *            description
	 */
	protected AbstractFileFilter(String description) {
		this.description = description;
	}

	/**
	 *
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public final boolean accept(File fileOrDirectory) {
		return (this.not ^ this.acceptFile(fileOrDirectory));
	}

	/**
	 *
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public final boolean accept(File parentDirectory, String fileOrDirectoryName) {
		return (this.not ^ this.acceptFile(new File(parentDirectory, fileOrDirectoryName)));
	}

	/**
	 * accept file?
	 *
	 * @param f
	 *            File
	 *
	 * @return boolean
	 */
	public abstract boolean acceptFile(File f);

	/**
	 * casts to java.io.FileFilter
	 *
	 * @return java.io.FileFilter
	 */
	public java.io.FileFilter castFileFilter() {
		return this;
	}

	/**
	 * casts to java.io.FilenameFilter
	 *
	 * @return java.io.FilenameFilter
	 */
	public java.io.FilenameFilter castFilenameFilter() {
		return this;
	}

	/**
	 * casts to javax.swing.filechooser.FileFilter
	 *
	 * @return javax.swing.filechooser.FileFilter
	 */
	public javax.swing.filechooser.FileFilter castSwingFilter() {
		return this;
	}

	/**
	 *
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public final String getDescription() {
		return this.description;
	}

	/**
	 * sets opposite NOT/invers
	 *
	 * @return
	 */
	public final AbstractFileFilter invert() {
		this.not = !this.not;

		return this;
	}

	/**
	 * gets NOT/invers
	 *
	 * @return Returns the not.
	 */
	public final boolean isNot() {
		return this.not;
	}

	/**
	 * sets description
	 *
	 * @param description
	 *            The description to set.
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * sets normal instead of NOT/invers
	 *
	 * @return
	 */
	public final AbstractFileFilter setNormal() {
		this.not = false;

		return this;
	}

	/**
	 * sets NOT/invers
	 *
	 * @return
	 */
	public final AbstractFileFilter setNot() {
		this.not = true;

		return this;
	}

	/**
	 * sets NOT/invers
	 *
	 * @param not
	 *            The not to set.
	 *
	 * @return
	 */
	public final AbstractFileFilter setNot(boolean not) {
		this.not = not;

		return this;
	}

	/**
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.not) {
			return "NOT '" + this.getDescription() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return "'" + this.getDescription() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
