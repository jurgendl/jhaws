package org.jhaws.common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 28 February 2005
 */
public class SystemIcon implements ExtensionIconFinder {
    /** field */
    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    /** field */
    private static HashMap<String, Icon> iconMap = new HashMap<String, Icon>();

    /**
     * Creates a new SystemIcon object.
     * 
     * @param f na
     * 
     * @return
     */
    public static Icon getSystemIcon(final File f) {
        if (f == null) {
            return null;
        }

        String ext = IOGeneralFile.getExtension(f.getName());

        if (!f.isDirectory() && SystemIcon.iconMap.containsKey(ext)) {
            return SystemIcon.iconMap.get(ext);
        }

        try {
            Icon icon = SystemIcon.fsv.getSystemIcon(f);

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

    /**
     * 
     * @see util.io.ExtensionIconFinder#getLargeIcon(util.io.IOFile)
     */
    @Override
    public Icon getLargeIcon(IOFile file) {
        return SystemIcon.getSystemIcon(file);
    }

    /**
     * 
     * @see util.io.ExtensionIconFinder#getSmallIcon(util.io.IOFile)
     */
    @SuppressWarnings("restriction")
    @Override
    public Icon getSmallIcon(IOFile file) {
        try {
            sun.awt.shell.ShellFolder sf;

            try {
                sf = sun.awt.shell.ShellFolder.getShellFolder(file);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            return new ImageIcon(sf.getIcon(true));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
