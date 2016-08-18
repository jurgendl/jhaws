package org.jhaws.common.io.filter;

import org.jhaws.common.io.IOFile;

/**
 * filters files on extensions
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
@SuppressWarnings("deprecation")
public class FileExtensionFilter extends FileFilter {
	/** extensions */
	private String[] exts;

	/**
	 * Creates a new FileExtensionFilter object.
	 *
	 * @param description
	 *            description
	 * @param ext
	 *            one extension
	 */
	public FileExtensionFilter(String description, String ext) {
		this(description, new String[] { ext });
	}

	/**
	 * Creates a new FileExtensionFilter object.
	 *
	 * @param description
	 *            description
	 * @param exts
	 *            list of extensions
	 */
	public FileExtensionFilter(String description, String... exts) {
		super(description);
		this.exts = new String[exts.length];

		for (int i = 0; i < exts.length; i++) {
			this.exts[i] = exts[i].toLowerCase();
		}
	}

	/**
	 * Creates a new FileExtensionFilter object.
	 *
	 *
	 * @param ext
	 *            one extension
	 */
	public FileExtensionFilter(String ext) {
		this(new String[] { ext });
	}

	/**
	 * Creates a new FileExtensionFilter object.
	 *
	 *
	 * @param exts
	 *            list of extensions
	 */
	public FileExtensionFilter(String... exts) {
		this("extension filter " + getDescription(exts), exts); //$NON-NLS-1$
	}

	/**
	 * na
	 *
	 * @param exts
	 *            na
	 *
	 * @return
	 */
	private static String getDescription(String[] exts) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < exts.length; i++) {
			sb.append(exts[i]);

			if (i < (exts.length - 1)) {
				sb.append(", "); //$NON-NLS-1$
			}
		}

		return sb.toString();
	}

	/**
	 *
	 * @see org.jhaws.common.io.filter.FileFilter#acceptRealFile(util.io.IOFile)
	 */
	@Override
	public final boolean acceptRealFile(IOFile f) {
		String e = f.getExtension().toLowerCase();

		for (String ext : exts) {
			if (e.equals(ext.toLowerCase())) {
				return true;
			}
		}

		return false;
	}
}
