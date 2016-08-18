package org.jhaws.common.io;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

/**
 * na
 *
 * @author Jurgen
 * @version 1.0.0 - 28 February 2005
 */
public class SystemIcon implements ExtensionIconFinder {
	/**
	 * Creates a new SystemIcon object.
	 *
	 * @param f
	 *            na
	 *
	 * @return
	 */
	public static Icon getSystemIcon(final FilePath f) {
		if (f == null) {
			return null;
		}

		String ext = f.getExtension();

		if (!f.isDirectory() && SystemIcon.iconMap.containsKey(ext)) {
			return SystemIcon.iconMap.get(ext);
		}

		try {
			Icon icon = SystemIcon.fsv.getSystemIcon(f.toFile());

			if (!f.isDirectory()) {
				SystemIcon.iconMap.put(ext, icon);
			}

			return icon;
		} catch (final NullPointerException ex) {
			if (f.isDirectory()) {
				return UIManager.getIcon("FileView.directoryIcon"); //$NON-NLS-1$
			}

			return UIManager.getIcon("FileView.fileIcon"); //$NON-NLS-1$
		}
	}

	/** field */
	private static FileSystemView fsv = FileSystemView.getFileSystemView();

	/** field */
	private static HashMap<String, Icon> iconMap = new HashMap<>();

	/**
	 *
	 * @see util.io.ExtensionIconFinder#getLargeIcon(util.io.IOFile)
	 */
	@Override
	public Icon getLargeIcon(FilePath file) {
		return SystemIcon.getSystemIcon(file);
	}

	/**
	 *
	 * @see org.jhaws.common.io.ExtensionIconFinder#getSmallIcon(org.jhaws.common.io.FilePath)
	 */
	@SuppressWarnings("restriction")
	@Override
	public Icon getSmallIcon(FilePath file) {
		try {
			sun.awt.shell.ShellFolder sf;

			try {
				sf = sun.awt.shell.ShellFolder.getShellFolder(file.toFile());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

			return new ImageIcon(sf.getIcon(true));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
