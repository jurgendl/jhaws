package org.jhaws.common.io;

import javax.swing.Icon;

public class SystemIcon implements ExtensionIconFinder {
	public static Icon getSystemIcon(final FilePath f) {
		return FilePath.getSystemIcon(f);
	}

	/**
	 *
	 * @see util.io.ExtensionIconFinder#getLargeIcon(util.io.IOFile)
	 */
	@Override
	public Icon getLargeIcon(FilePath file) {
		return file.getLargeIcon();
	}

	/**
	 *
	 * @see org.jhaws.common.io.ExtensionIconFinder#getSmallIcon(org.jhaws.common.io.FilePath)
	 */
	@Override
	public Icon getSmallIcon(FilePath file) {
		return null;// file.getSmallIcon();
	}
}
